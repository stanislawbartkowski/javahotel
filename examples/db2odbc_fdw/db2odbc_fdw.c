/*-------------------------------------------------------------------------
 *
 * db2odbc_fdw.c - foreign data wrapper for odbc
 * 
 * Author: stanislawbartkowski@gmail.com
 * 
 * Copyright (c) 2010-2012, PostgreSQL Global Development Group
 *
 * IDENTIFICATION
 *		  db2odbc_fdw/db2odbc_fdw
 *
 *-------------------------------------------------------------------------
 */
#include "postgres.h"

#include <stdio.h>
#include <sys/stat.h>
#include <unistd.h>

#include "access/reloptions.h"
#include "catalog/pg_foreign_server.h"
#include "catalog/pg_foreign_table.h"
#include "catalog/pg_user_mapping.h"
#include "catalog/pg_type.h"
#include "commands/defrem.h"
#include "commands/explain.h"
#include "foreign/fdwapi.h"
#include "foreign/foreign.h"
#include "miscadmin.h"
#include "optimizer/cost.h"
#include "mb/pg_wchar.h"
#include "storage/fd.h"
#include "utils/array.h"
#include "utils/builtins.h"
#include "funcapi.h"

#include <sql.h>
#include <sqlext.h>


PG_MODULE_MAGIC;

typedef struct db2ColumnDesc {
    char *buf;
    SQLULEN columnsize;
    bool isNumber;
} db2ColumnDesc;

typedef struct db2PrivateData {
    AttInMetadata *attinmeta;
    SQLHENV env;
    SQLHDBC dbc;
    SQLHSTMT stmt;
    SQLSMALLINT no_columns;
    db2ColumnDesc *columnsbuf;
    char **values;
} db2PrivateData;

/*
 * Describes the valid options for objects that use this wrapper.
 */
typedef struct db2FdwOption {
    const char *optname;
    Oid optcontext; /* Oid of catalog in which option may appear */
    bool required;
} db2FdwOption;

#define DSN "dsn"
#define USERNAME "username"
#define PASSWORD "password"
#define QUERY "sql_query" 

/*
 * Array of valid options
 *
 */
static struct db2FdwOption valid_options[] ={
    /* Foreign server options */
    { DSN, ForeignServerRelationId, true},

    /* Foreign table options */
    { QUERY, ForeignTableRelationId, true},

    /* User mapping options */
    { USERNAME, UserMappingRelationId, true},
    { PASSWORD, UserMappingRelationId, true},

    /* Sentinel */
    { NULL, InvalidOid}
};

static void lognotice(const char *s) {
    elog(NOTICE, s);
}

// uncomment to detailed debug
#define N DEBUG5
// #define N NOTICE

static void logdebug(const char *s) {
    elog(N, s);
}

static void logdebugi(const char *s, int p) {
    char temp[255];
    sprintf(temp, s, p);
    elog(N, temp);
}

static void logdebug1i(const char *s, const char *p1, int p2) {
    char temp[255];
    sprintf(temp, s, p1, p2);
    elog(N, temp);
}

static void logdebug1(const char *s, const char *par) {
    elog(N, s, par);
}

static void logdebug2(const char *s, const char *par1, const char *par2) {
    elog(N, s, par1, par2);
}

/*
 * SQL functions*/
extern Datum db2odbc_fdw_handler(PG_FUNCTION_ARGS);
extern Datum db2odbc_fdw_validator(PG_FUNCTION_ARGS);

PG_FUNCTION_INFO_V1(db2odbc_fdw_handler);
PG_FUNCTION_INFO_V1(db2odbc_fdw_validator);

/*
 * FDW callback routines
 */
static FdwPlan *db2_PlanForeignScan(Oid foreigntableid,
        PlannerInfo *root,
        RelOptInfo *baserel);
static void db2_ExplainForeignScan(ForeignScanState *node,
        ExplainState *es);
static void db2_BeginForeignScan(ForeignScanState *node,
        int eflags);
static TupleTableSlot *db2_IterateForeignScan(
        ForeignScanState *node);
static void db2_ReScanForeignScan(ForeignScanState *node);
static void db2_EndForeignScan(ForeignScanState *node);

/*
 * Foreign-data wrapper handler function: return a struct with pointers
 * to my callback routines.
 */
Datum
db2odbc_fdw_handler(PG_FUNCTION_ARGS) {
    logdebug("db2_fdw_handler");
    FdwRoutine *fdwroutine = makeNode(FdwRoutine);

    fdwroutine->PlanForeignScan = db2_PlanForeignScan;
    fdwroutine->ExplainForeignScan = db2_ExplainForeignScan;
    fdwroutine->BeginForeignScan = db2_BeginForeignScan;
    fdwroutine->IterateForeignScan = db2_IterateForeignScan;
    fdwroutine->ReScanForeignScan = db2_ReScanForeignScan;
    fdwroutine->EndForeignScan = db2_EndForeignScan;

    PG_RETURN_POINTER(fdwroutine);
}

static const char * get_name(Oid catalog) {
    switch (catalog) {
        case ForeignTableRelationId: return "foreign table";
        case ForeignServerRelationId: return "foreign data server";
        case UserMappingRelationId: return "foreing user mapping";
    }
    return "unrecognized";
}

static void createValidOptions(StringInfoData *buf, Oid context) {
    initStringInfo(buf);
    db2FdwOption *opt1;
    for (opt1 = valid_options; opt1->optname; opt1++) {
        if (context == opt1->optcontext)
            appendStringInfo(buf, "%s%s", (buf->len > 0) ? ", " : "",
                opt1->optname);
    }
}

/*
 * Validate the generic options given to a FOREIGN DATA WRAPPER, SERVER,
 * USER MAPPING or FOREIGN TABLE that uses file_fdw.
 *
 * Raise an ERROR if the option or its value is considered invalid.
 */
Datum
db2odbc_fdw_validator(PG_FUNCTION_ARGS) {
    List *options_list = untransformRelOptions(PG_GETARG_DATUM(0));
    Oid context = PG_GETARG_OID(1);
    ListCell *cell;
    StringInfoData buf;

    logdebug1("db2_fdw_validator : %s ", get_name(context));

    foreach(cell, options_list) {
        DefElem *def = (DefElem *) lfirst(cell);
        const char *option = defGetString(def);
        logdebug2("%s : %s", def->defname, option);
        db2FdwOption *opt;
        bool valid = false;
        for (opt = valid_options; opt->optname; opt++) {
            logdebug(opt->optname);
            if (strcmp(opt->optname, def->defname) == 0) {
                logdebug("recognized");
                if (context == opt->optcontext) {
                    valid = true;
                }
                break;
            }
        }
        if (!valid) {
            createValidOptions(&buf, context);
            ereport(ERROR,
                    (errcode(ERRCODE_FDW_INVALID_OPTION_NAME),
                    errmsg("invalid option \"%s\" %s", def->defname,
                    opt->optname == NULL ? "" : "(option name is recognized but is invalid in this context)"),
                    errhint("Valid options in this context are: %s", buf.len ? buf.data : "<none>")
                    ));
        }

    }

    // second phase, check required
    db2FdwOption *opt1;
    for (opt1 = valid_options; opt1->optname; opt1++) {
        if (!opt1->required) {
            continue;
        }
        if (context != opt1->optcontext) {
            continue;
        }
        logdebug1("validate required option %s", opt1->optname);
        bool found = false;

        foreach(cell, options_list) {
            DefElem *def = (DefElem *) lfirst(cell);
            const char *option = defGetString(def);
            if (strcmp(opt1->optname, def->defname) == 0) {
                found = true;
                break;
            }
        }
        if (!found) {
            createValidOptions(&buf, context);
            ereport(ERROR,
                    (errcode(ERRCODE_FDW_OPTION_NAME_NOT_FOUND),
                    errmsg("option is required: %s", opt1->optname),
                    errhint("Valid options in this context are: %s", buf.len ? buf.data : "<none>")
                    ));
        }
    }

    PG_RETURN_VOID();
}

static void list_drivers() {
    SQLRETURN ret;
    SQLHENV env;
    char dsn[256];
    char desc[256];
    SQLSMALLINT dsn_ret;
    SQLSMALLINT desc_ret;
    SQLUSMALLINT direction;

    logdebug("list drivers");
    ret = SQLAllocHandle(SQL_HANDLE_ENV, SQL_NULL_HANDLE, &env);
    if (!SQL_SUCCEEDED(ret)) {
        lognotice("Cannot allocate SQL_HANDLE_ENV");
    }
    SQLSetEnvAttr(env, SQL_ATTR_ODBC_VERSION, (void *) SQL_OV_ODBC3, 0);
    direction = SQL_FETCH_FIRST;
    while (SQL_SUCCEEDED(ret = SQLDataSources(env, direction,
            (SQLCHAR *) dsn, sizeof (dsn), &dsn_ret,
            (SQLCHAR *) desc, sizeof (desc), &desc_ret))) {
        direction = SQL_FETCH_NEXT;
        logdebug2("%s - %s", dsn, desc);
    }
    SQLFreeHandle(SQL_HANDLE_ENV, env);
    logdebug("end of list drivers");
}

static void extract_error(char *fn,
        SQLHANDLE handle,
        SQLSMALLINT type) {
    SQLINTEGER i = 0;
    SQLINTEGER native;
    SQLCHAR state[ 7 ];
    SQLCHAR text[256];
    SQLSMALLINT len;
    SQLRETURN ret;

    elog(NOTICE,
            "\n"
            "The driver reported the following diagnostics while running "
            "%s\n",
            fn);

    do {
        ret = SQLGetDiagRec(type, handle, ++i, state, &native, text,
                sizeof (text), &len);
        if (SQL_SUCCEEDED(ret))
            elog(NOTICE, "%s:%ld:%ld:%s\n", state, (long int) i, (long int) native, text);
    }    while (ret == SQL_SUCCESS);
}

static void getConnection(db2PrivateData *data, Oid foreigntableid, char **query) {
    ForeignTable *table;
    ForeignServer *server;
    UserMapping *mapping;
    List *options;
    ListCell *lc;
    StringInfoData conn_str;
    SQLRETURN ret;

    char *dsn;
    char *username;
    char *password;


    table = GetForeignTable(foreigntableid);
    server = GetForeignServer(table->serverid);
    mapping = GetUserMapping(GetUserId(), table->serverid);

    options = NIL;
    options = list_concat(options, table->options);
    options = list_concat(options, server->options);
    options = list_concat(options, mapping->options);

    foreach(lc, options) {
        DefElem *def = (DefElem *) lfirst(lc);
        if (strcmp(def->defname, DSN) == 0) {
            dsn = defGetString(def);
            logdebug1("DSN: %s", dsn);
            continue;
        }
        if (strcmp(def->defname, PASSWORD) == 0) {
            password = defGetString(def);
            logdebug1("PASSWORD: %s", password);
            continue;
        }
        if (strcmp(def->defname, USERNAME) == 0) {
            username = defGetString(def);
            logdebug1("USERNAME: %s", username);
            continue;
        }
        if (strcmp(def->defname, QUERY) == 0) {
            *query = defGetString(def);
            logdebug1("QUERY: %s", *query);
            continue;
        }
    }
    /* Allocate an environment handle */
    ret = SQLAllocHandle(SQL_HANDLE_ENV, SQL_NULL_HANDLE, &data->env);
    if (!SQL_SUCCEEDED(ret)) {
        lognotice("Cannot allocate SQL_HANDLE_ENV");
    }
    ret = SQLSetEnvAttr(data->env, SQL_ATTR_ODBC_VERSION, (void *) SQL_OV_ODBC3, 0);
    if (!SQL_SUCCEEDED(ret)) {
        lognotice("Cannot set SQL_ATTR_ODBC_VERSION");
        extract_error("SQL_ATTR_ODBC_VERSION", data->env, SQL_HANDLE_ENV);
    }
    ret = SQLAllocHandle(SQL_HANDLE_DBC, data->env, &data->dbc);
    if (!SQL_SUCCEEDED(ret)) {
        lognotice("Cannot allocate SQL_HANDLE_DBC");
        extract_error("SQLAllocHandle SQL_HANDLE_DBC", data->env, SQL_HANDLE_ENV);
    }

    ret = SQLConnect(data->dbc, dsn, SQL_NTS, username, SQL_NTS, password, SQL_NTS);
    if (SQL_SUCCEEDED(ret)) {
        logdebug("Successfully connected to driver");
    }
    else {
        extract_error("SQLDriverConnect", data->dbc, SQL_HANDLE_DBC);
        ereport(ERROR,
                (errcode(ERRCODE_FDW_UNABLE_TO_ESTABLISH_CONNECTION),
                errmsg("cannot connect to odbc dsn %s", conn_str.data),
                errhint("Check connection data or make sure that target database is online")
                ));
    }
}

static void closeConnection(db2PrivateData *data) {
    SQLDisconnect(data->dbc);
    SQLFreeHandle(SQL_HANDLE_DBC, data->dbc);
    SQLFreeHandle(SQL_HANDLE_ENV, data->env);
}

/*
 * filePlanForeignScan
 *		Create a FdwPlan for a scan on the foreign table
 */
static FdwPlan *
db2_PlanForeignScan(Oid foreigntableid,
        PlannerInfo *root,
        RelOptInfo *baserel) {
    logdebug("db2_PlanForeignScan");
    FdwPlan *fdwplan;
    fdwplan = makeNode(FdwPlan);
    fdwplan->startup_cost = 1;
    fdwplan->total_cost = 10;
    fdwplan->fdw_private = NULL;
    return fdwplan;
}

/*
 * fileExplainForeignScan
 *		Produce extra output for EXPLAIN
 */
static void
db2_ExplainForeignScan(ForeignScanState *node, ExplainState *es) {
    logdebug("db2_ExplainForeignScan");
}

/*
 * file_fixed_lengthBeginForeignScan
 *		Initiate access to the file
 */
static void
db2_BeginForeignScan(ForeignScanState *node, int eflags) {
    logdebug("db2_BeginForeignScan");
    list_drivers();
    db2PrivateData *data;
    char *query;
    data = (db2PrivateData *) palloc(sizeof (db2PrivateData));
    getConnection(data, RelationGetRelid(node->ss.ss_currentRelation), &query);
    SQLAllocHandle(SQL_HANDLE_STMT, data->dbc, &data->stmt);
    /* Retrieve a list of rows */
    SQLRETURN ret = SQLExecDirect(data->stmt, (SQLCHAR *) query, SQL_NTS);
    if (SQL_SUCCEEDED(ret)) {
        logdebug("SQLExecDirect");
    } else {
        extract_error("Error while executing query", data->stmt, SQL_HANDLE_STMT);
        ereport(ERROR,
                (errcode(ERRCODE_FDW_ERROR),
                errmsg("Cannot execute query %s", query),
                errhint("Check query syntax")
                ));
    }

    ret = SQLNumResultCols(data->stmt, &data->no_columns);
    if (!SQL_SUCCEEDED(ret)) {
        extract_error("SQLNumResultCols", data->stmt, SQL_HANDLE_STMT);
        ereport(ERROR,
                (errcode(ERRCODE_FDW_ERROR),
                errmsg("Cannot retrieve number of columns %s", query),
                errhint("Check query syntax")
                ));
    }
    logdebugi("Number of columns: %u", data->no_columns);
    data->columnsbuf = palloc(data->no_columns * sizeof (db2ColumnDesc));
    logdebug("Memory for buffor allocated");
    int i;
    data->values = palloc(sizeof (char *) * data->no_columns);
    for (i = 0; i < data->no_columns; i++) {
        SQLCHAR name[255];
        SQLSMALLINT NameLengthPtr;
        SQLSMALLINT DataTypePtr;
        SQLSMALLINT DecimalDigitsPtr;
        SQLSMALLINT NullablePtr;
        ret = SQLDescribeCol(data->stmt,
                i + 1,
                name,
                sizeof (SQLCHAR) * sizeof (name),
                &NameLengthPtr,
                &DataTypePtr,
                &data->columnsbuf[i].columnsize,
                &DecimalDigitsPtr,
                &NullablePtr);
        if (!SQL_SUCCEEDED(ret)) {
            extract_error("SQLDescribeCol", data->stmt, SQL_HANDLE_STMT);
            ereport(ERROR,
                    (errcode(ERRCODE_FDW_ERROR),
                    errmsg("Cannot retrieve column description for query %s", query),
                    errhint("Check query syntax")
                    ));
        }
        logdebug1i("Number of bytes for column %s : %u", name, data->columnsbuf[i].columnsize);
        // important : for some reason it cause crash with declaration Size
        // or putting expression directly in palloc invocation
        int size = sizeof (char) * (Size) data->columnsbuf[i].columnsize;
        data->columnsbuf[i].buf = palloc(size);
        logdebug("Memory for column buffer allocated");
        data->values[i] = data->columnsbuf[i].buf;
        data->columnsbuf[i].isNumber = false;
        if ((DataTypePtr == SQL_DECIMAL) || (DataTypePtr == SQL_NUMERIC) || (DataTypePtr == SQL_REAL) ||
                (DataTypePtr == SQL_DOUBLE) || (DataTypePtr == SQL_FLOAT)) {
            logdebug("Decimal type");
            data->columnsbuf[i].isNumber = true;
        }
    }
    data->attinmeta = TupleDescGetAttInMetadata(node->ss.ss_currentRelation->rd_att);

    node->fdw_state = (void *) data;

}

/*
 * fileIterateForeignScan
 *		Read next record from the data file and store it into the
 *		ScanTupleSlot as a virtual tuple
 */
static TupleTableSlot *
db2_IterateForeignScan(ForeignScanState *node) {
    logdebug("db2_IterateForeignScan");
    db2PrivateData *data;
    char *query;
    data = (db2PrivateData *) node->fdw_state;
    SQLRETURN ret = SQLFetch(data->stmt);
    logdebugi("SQLFetch %u", ret);
    if (ret == SQL_NO_DATA_FOUND) {
        return NULL;
    }
    if (!SQL_SUCCEEDED(ret)) {
        extract_error("SQLFetch", data->stmt, SQL_HANDLE_STMT);
        ereport(ERROR,
                (errcode(ERRCODE_FDW_ERROR),
                errmsg("Cannot fetch next row"),
                errhint("Check query syntax")
                ));
    }
    TupleTableSlot *slot = node->ss.ss_ScanTupleSlot;
    ExecClearTuple(slot);
    SQLSMALLINT i;
    for (i = 0; i < data->no_columns; i++) {
        SQLLEN indicator;
        ret = SQLGetData(data->stmt, i + 1, SQL_C_CHAR,
                data->columnsbuf[i].buf, data->columnsbuf[i].columnsize, &indicator);
        logdebug1i("GetData %s %u", data->columnsbuf[i].buf, i);
        logdebugi("Indicator %i", indicator);
        // for some reason indicator should be casted to int to have comparison correct
        if ((int) indicator == SQL_NULL_DATA) {
            data->values[i] = NULL;
        } else {
            data->values[i] = data->columnsbuf[i].buf;
            if (data->columnsbuf[i].isNumber) {
                logdebug("Decimal type, replace , with .");
                char *p;
                while (p = strrchr(data->values[i], ',')) {
                    *p = '.';
                }
            }
        }
        if (!SQL_SUCCEEDED(ret)) {
            extract_error("SQLGetData", data->stmt, SQL_HANDLE_STMT);
            ereport(ERROR,
                    (errcode(ERRCODE_FDW_ERROR),
                    errmsg("Cannot get data for next column"),
                    errhint("Check query syntax")
                    ));
        }
    }
    HeapTuple tuple = BuildTupleFromCStrings(data->attinmeta, data->values);
    ExecStoreTuple(tuple, slot, InvalidBuffer, false);

    return slot;
}

/*
 * fileEndForeignScan
 *		Finish scanning foreign table and dispose objects used for this scan
 */
static void
db2_EndForeignScan(ForeignScanState *node) {
    logdebug("db2_EndForeignScan");
    db2PrivateData *data = (db2PrivateData *) node->fdw_state;
    SQLFreeHandle(SQL_HANDLE_STMT, data->stmt);
    closeConnection(data);
}

/*
 * fileReScanForeignScan
 *		Rescan table, possibly with new parameters
 */
static void
db2_ReScanForeignScan(ForeignScanState *node) {
    logdebug("db2_ReScanForeignScan");
}