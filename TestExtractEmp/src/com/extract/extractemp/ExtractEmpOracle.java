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

import oracle.jdbc.OracleTypes;

class ExtractEmpOracle extends AbstractJDBC {
	private static final String DRIVERNAME = "oracle.jdbc.driver.OracleDriver";
	private static final String SOURCEURL = "jdbc:oracle:thin:@think:1521:testdb";
	private static final String USER = "test";
	private static final String PASSWORD = "test123";

	// private static final String SOURCEURL =
	// "jdbc:oracle:thin:@rhelora:1521:hcidb";
	// private static final String USER = "test";
	// private static final String PASSWORD = "secret";

	ExtractEmpOracle() {
		super(DRIVERNAME, SOURCEURL, USER, PASSWORD);
	}

	@Override
	public IResultSet getEmp(String empName, String mgmName, String depName) throws SQLException {
		CallableStatement stmt = connection.prepareCall("{ ? = call EMP_SEARCH ( ?, ?, ?)");
		int i = 0;
		stmt.registerOutParameter(++i, OracleTypes.CURSOR);
		stmt.setString(++i, empName);
		stmt.setString(++i, depName);
		stmt.setString(++i, mgmName);
		stmt.execute();
		ResultSet rset = (ResultSet) stmt.getObject(1);
		return getResultSet(rset);
	}

}
