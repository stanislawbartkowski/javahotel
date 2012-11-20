/*
    Copyright 2012 <copyright holder> <email>

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

#include <string.h>

#include <iostream>

#include <sql.h>
#include <sqlext.h>

#include "testopt.h"

#define CONNECTSTRING "DSN=SAMPLET;UID=db2inst1;PWD=db2inst1;CURRENTOPTIMIZATIONPROFILE=SELECT_SIMPLE_OPTIMIZER;DB2Explain=2"

void testopt()
{
    // Declare The Local Memory Variables
    SQLHANDLE  EnvHandle = 0;
    SQLHANDLE  ConHandle = 0;
    SQLHANDLE  StmtHandle = 0;
    SQLRETURN  RetCode = SQL_SUCCESS;
    
    SQLWCHAR OutConnStr[255];
    SQLSMALLINT OutConnStrLen;

    SQLINTEGER ide;
    SQLCHAR name[200];

    /*-----------------------------------------------------*/
    /* INITIALIZATION                                      */
    /*-----------------------------------------------------*/

    // Allocate An Environment Handle
    SQLAllocHandle(SQL_HANDLE_ENV, SQL_NULL_HANDLE,
        &EnvHandle);

    // Set The ODBC Application Version To 3.x
    if (EnvHandle != 0)
        SQLSetEnvAttr(EnvHandle, SQL_ATTR_ODBC_VERSION, 
            (SQLPOINTER) SQL_OV_ODBC3, SQL_IS_UINTEGER);

    // Allocate A Connection Handle
    if (EnvHandle != 0)
        SQLAllocHandle(SQL_HANDLE_DBC, EnvHandle,
            &ConHandle);

    // Connect To The Appropriate Data Source 
    if (ConHandle != 0)
        RetCode = SQLConnect(ConHandle, (SQLCHAR *) "SAMPLET",
                      SQL_NTS, (SQLCHAR *) "db2inst1",
                      SQL_NTS, (SQLCHAR *) "db2inst1",
                      SQL_NTS);
	
/*	
    if (ConHandle != 0)
      RetCode = SQLDriverConnect(ConHandle,NULL,(SQLCHAR *)CONNECTSTRING,SQL_NTS,(SQLCHAR*)OutConnStr,255,&OutConnStrLen,SQL_DRIVER_NOPROMPT);
*/    

    /*-----------------------------------------------------*/
    /* TRANSACTION PROCESSING                              */
    /*-----------------------------------------------------*/

    // Allocate An SQL Statement Handle
    if (ConHandle != 0 && RetCode == SQL_SUCCESS)
        SQLAllocHandle(SQL_HANDLE_STMT, ConHandle,
           &StmtHandle);
	
    if (RetCode != SQL_SUCCESS) {
      std::cout << "Cannot connect !" << std::endl;
      return;
    }
    
    RetCode = SQLExecDirect(StmtHandle, (SQLCHAR *)"select * from testts where ide = 10", SQL_NTS);
    if (RetCode != SQL_SUCCESS) {
        std::cout << "Failure, cannot execute statement !" << std::endl;
    }

    if (RetCode == SQL_SUCCESS)
    {
        // Bind The Columns In The Result Data Set Returned
        // To Application Variables
        SQLBindCol(StmtHandle, 1, SQL_C_LONG, (SQLPOINTER) 
            &ide, sizeof(ide), NULL);

        SQLBindCol(StmtHandle, 2, SQL_C_CHAR, (SQLPOINTER) 
            name, sizeof(name), NULL);

        // While There Are Records In The Result Data Set
        // Produced, Retrieve And Display Them
        while (RetCode != SQL_NO_DATA)
        {
            RetCode = SQLFetch(StmtHandle);
            if (RetCode != SQL_NO_DATA)
               std::cout << ide << " " << name << std::endl;
        }
    }

    // Commit The Transaction
    RetCode = SQLEndTran(SQL_HANDLE_DBC, ConHandle, SQL_COMMIT);


    // Free The SQL Statement Handle
    if (StmtHandle != 0)
        SQLFreeHandle(SQL_HANDLE_STMT, StmtHandle);

    /*-----------------------------------------------------*/
    /* TERMINATION                                         */
    /*-----------------------------------------------------*/

    // Terminate The Data Source Connection
    if (ConHandle != 0)
        RetCode = SQLDisconnect(ConHandle);

    // Free The Connection Handle
    if (ConHandle != 0)
        SQLFreeHandle(SQL_HANDLE_DBC, ConHandle);

    // Free The Environment Handle
    if (EnvHandle != 0)
        SQLFreeHandle(SQL_HANDLE_ENV, EnvHandle);

}   
