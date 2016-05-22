/*
 * Copyright 2016 stanislawbartkowski@gmail.com 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package com.extract.extractemp;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExtractEmpDB2  extends AbstractJDBC  {
	
	
	private static final String DRIVERNAME = "com.ibm.db2.jcc.DB2Driver";
	private static final String SOURCEURL = "jdbc:db2://re64:50040/TESTDB";
	private static final String USER = "db2inst1";
	private static final String PASSWORD = "db2inst1";

    ExtractEmpDB2() {
    	super(DRIVERNAME,SOURCEURL,USER,PASSWORD);
    }
	
	@Override
	public IResultSet getEmp(String empName, String mgmName, String depName) throws SQLException {
		CallableStatement stmt = connection.prepareCall("{ call db2inst1.EMP_SEARCH ( ?, ?, ?) } ");
		int i = 0;
		stmt.setString(++i, empName);
		stmt.setString(++i, depName);
		stmt.setString(++i, mgmName);
		ResultSet rset = stmt.executeQuery();
		return getResultSet(rset);
	}


}
