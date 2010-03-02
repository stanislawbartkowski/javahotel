/***************************************************************************
 *   Copyright (C) 2010 by Stanislaw Bartkowski   *
 *   sbartkowski@sbartkowski-linux.krakow.pl.ibm.com   *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU General Public License for more details.                          *
 *                                                                         *
 *   You should have received a copy of the GNU General Public License     *
 *   along with this program; if not, write to the                         *
 *   Free Software Foundation, Inc.,                                       *
 *   59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.             *
 ***************************************************************************/

#ifdef WIN32
#include <windows.h>
#endif

#include <sql.h>
#include <sqlext.h>
#include <locale>

#include "ODBCDrivers.h"

#ifdef WIN32
typedef SQLWCHAR MYSQLCHAR;
#else
typedef SQLCHAR MYSQLCHAR;
#endif


namespace {

#define TEXT_SIZE 1024

void copystring(char *dest, const MYSQLCHAR *sou, bool toupper=false) {
	char *pdest = dest;
#ifdef WIN32
	wcstombs(dest,sou,TEXT_SIZE);
#else
    for (; *sou; sou++,dest++) *dest = *sou;
    *dest = 0;
#endif
	if (toupper) {
		std::locale loc;
		for (; *pdest; pdest++) {
			*pdest = std::toupper(*pdest, loc);
		}
	}	 	 
}

const ODBCInfo construct(const MYSQLCHAR *name,const MYSQLCHAR *descr) {
  char cname[TEXT_SIZE] ,cdescr[TEXT_SIZE];
  copystring(cname,name,false);
  copystring(cdescr,descr,false);
  return ODBCInfo(cname,cdescr);
}
  
  void copystring(MYSQLCHAR *dest, const char *sou) {
    for (; *sou; sou++,dest++) *dest = *sou;
    *dest = 0;
}

  const std::string readSqlError(std::string pwhere,SQLSMALLINT type,SQLHANDLE handle) {

   SQLINTEGER NativeError;
   SQLSMALLINT MsgLen;
   MYSQLCHAR Msg[SQL_MAX_MESSAGE_LENGTH],SqlState[6];
   SQLRETURN ret;
   std::string errs;

    errs = pwhere;
    errs += " ";
    ret = SQLGetDiagRec(type, handle, 1, SqlState, &NativeError,
            Msg, SQL_MAX_MESSAGE_LENGTH-1, &MsgLen);
     if (SQL_SUCCEEDED(ret)) {
       char s1[TEXT_SIZE+1],s2[TEXT_SIZE+1];
       copystring(s1,SqlState);
       copystring(s2,Msg);
       errs += "\n";
       errs += s1;
       errs += " ";
       errs += s2;
     }
   return errs;
}

}


std::vector<ODBCInfo> getODBCDrivers() throw() {
  SQLHENV env;
  MYSQLCHAR driver[TEXT_SIZE];
  MYSQLCHAR attr[TEXT_SIZE];
  SQLSMALLINT driver_ret;
  SQLSMALLINT attr_ret;
  SQLUSMALLINT direction;
  SQLRETURN ret;

  std::vector<ODBCInfo> driverList;

  SQLAllocHandle(SQL_HANDLE_ENV, SQL_NULL_HANDLE, &env);
  SQLSetEnvAttr(env, SQL_ATTR_ODBC_VERSION, (void *) SQL_OV_ODBC3, 0);

  direction = SQL_FETCH_FIRST;
  while(SQL_SUCCEEDED(ret = SQLDrivers(env, direction,
				       driver, sizeof(driver), &driver_ret,
				       attr, sizeof(attr), &attr_ret))) {
    const ODBCInfo &odriver = construct(driver,attr);
    driverList.push_back(odriver);
    direction = SQL_FETCH_NEXT;
  }
  return driverList;
}

std::vector<ODBCInfo> getODBCDataSources() throw() {
 SQLHENV env;
  MYSQLCHAR dsn[256];
  MYSQLCHAR desc[256];
  SQLSMALLINT dsn_ret;
  SQLSMALLINT desc_ret;
  SQLUSMALLINT direction;
  SQLRETURN ret;
  std::vector<ODBCInfo> datasourceList;

  SQLAllocHandle(SQL_HANDLE_ENV, SQL_NULL_HANDLE, &env);
  SQLSetEnvAttr(env, SQL_ATTR_ODBC_VERSION, (void *) SQL_OV_ODBC3, 0);

  direction = SQL_FETCH_FIRST;
  while(SQL_SUCCEEDED(ret = SQLDataSources(env, direction,
					   dsn, sizeof(dsn), &dsn_ret,
					   desc, sizeof(desc), &desc_ret))) {
    ODBCInfo odatasource = construct(dsn,desc);
    datasourceList.push_back(odatasource);
    direction = SQL_FETCH_NEXT;
  }
 return datasourceList;
}



void ODBCConnection::retrieveError(std::string pwhere,SQLSMALLINT type,SQLHANDLE handle) {
	errs = readSqlError(pwhere,type,handle);
}

bool ODBCConnection::open(const char *connectionString) {
  SQLRETURN ret; /* ODBC API return status */
  MYSQLCHAR outstr[TEXT_SIZE];
  SQLSMALLINT outstrlen;

  /* Allocate an environment handle */
  ret = SQLAllocHandle(SQL_HANDLE_ENV, SQL_NULL_HANDLE, &env);
  if (!SQL_SUCCEEDED(ret)) {
     errs = "Error while SQLAllocHandle";
     return false; 
  }

  /* We want ODBC 3 support */
  ret = SQLSetEnvAttr(env, SQL_ATTR_ODBC_VERSION, (void *) SQL_OV_ODBC3, 0);
  if (!SQL_SUCCEEDED(ret)) {
     errs = "Error while SQLSetEnvAttr";
     return false; 
  }
  /* Allocate a connection handle */
  ret = SQLAllocHandle(SQL_HANDLE_DBC, env, &dbc);
  if (!SQL_SUCCEEDED(ret)) {
     errs = "Error while SQLAllocHandle";
     return false; 
  }
  /* Connect to the DSN mydsn */
  MYSQLCHAR dname[1024];
  copystring(dname,connectionString);
  ret = SQLDriverConnect(dbc, NULL, dname, SQL_NTS,
			 outstr, sizeof(outstr), &outstrlen,
			 SQL_DRIVER_COMPLETE);
  if (SQL_SUCCEEDED(ret)) {
//    printf("Connected\n");
//    printf("Returned connection string was:\n\t%s\n", outstr);
    if (ret == SQL_SUCCESS_WITH_INFO) {
    }
  } else {
     std::string errx = "Cannot connect";
     switch (ret) {
        case  SQL_NO_DATA: errs = "No data"; break;
        case  SQL_ERROR: errs = "Sql error"; break;
        case SQL_INVALID_HANDLE: errs = "Sql invalid handle"; break;
        case SQL_STILL_EXECUTING: errs = "Still executing"; break;  
     }

    retrieveError(errx,SQL_HANDLE_DBC,dbc);
    release();
    return false;
  }
  return true;
}

void ODBCConnection::close() {
    SQLDisconnect(dbc);
    release();
}

bool ODBCConnection::executeSelect(std::string sqlstatement,std::vector<SelectValue> &res) {
   SQLHSTMT stmt;
   SQLRETURN ret = SQLAllocHandle(SQL_HANDLE_STMT, dbc, &stmt);
   if (!SQL_SUCCEEDED(ret)) {
     errs = "Error while AllocHandle";
     return false; 
   }
   MYSQLCHAR stat[TEXT_SIZE];
   copystring(stat,sqlstatement.c_str());
   ret = SQLExecDirect(stmt,stat,SQL_NTS);
   if (!SQL_SUCCEEDED(ret)) {
     retrieveError(sqlstatement.c_str(),SQL_HANDLE_STMT,stmt);
     SQLFreeHandle(SQL_HANDLE_STMT, stmt);
     return false; 
   }
   SQLSMALLINT columns;
   MYSQLCHAR colname[TEXT_SIZE];
   SQLSMALLINT colnamelen;
   SQLSMALLINT coltype;
//   SQLUINTEGER  precision;
   SQLULEN  precision;
   SQLSMALLINT scale;

   ret = SQLNumResultCols(stmt, &columns);
   if (!SQL_SUCCEEDED(ret)) {
     SQLFreeHandle(SQL_HANDLE_STMT, stmt);
     errs = "Error while SQLNumResultCols";
     return false; 
   }

   while (SQL_SUCCEEDED(ret = SQLFetch(stmt))) {
      SelectValue sel;
      SQLUSMALLINT i;
    
    /* Loop through the columns */
    for (i = 1; i <= columns; i++) {
        ret = SQLDescribeCol(stmt, i, colname, sizeof(colname),
                       &colnamelen, &coltype, &precision, &scale, NULL);
       if (!SQL_SUCCEEDED(ret)) {
        SQLFreeHandle(SQL_HANDLE_STMT, stmt);
        errs = "Error while SQLNumResultCols";
        return false; 
       }

//        SQLINTEGER indicator;
        SQLLEN indicator;
        char buf[TEXT_SIZE];
        /* retrieve column data as a string */
	    ret = SQLGetData(stmt, i, SQL_C_CHAR, buf, sizeof(buf), &indicator);
        if (SQL_SUCCEEDED(ret)) {
            /* Handle null columns */
            if (indicator == SQL_NULL_DATA) strcpy(buf, "NULL");
        }
        else {
          errs = "Error while SQLGetData";
          SQLFreeHandle(SQL_HANDLE_STMT, stmt);
          return false;
        }
        char colnames[TEXT_SIZE];
        copystring(colnames,colname,true);
        sel[colnames] = buf;
     } // for
     res.push_back(sel);
    } // while
    SQLFreeHandle(SQL_HANDLE_STMT, stmt);
    return true;
}


void ODBCConnection::release() {
  SQLFreeHandle(SQL_HANDLE_DBC, dbc);
  SQLFreeHandle(SQL_HANDLE_ENV, env);
}  
