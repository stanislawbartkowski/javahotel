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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

abstract class AbstractJDBC implements IExtractEmp {

	private final String DRIVERNAME;
	private final String SOURCEURL;
	private final String USER;
	private final String PASSWORD;

	protected Connection connection;

	protected AbstractJDBC(String DRIVERNAME, String SOURCEURL, String USER, String PASSWORD) {
		this.DRIVERNAME = DRIVERNAME;
		this.SOURCEURL = SOURCEURL;
		this.USER = USER;
		this.PASSWORD = PASSWORD;
	}

	@Override
	public void connect() throws ClassNotFoundException, SQLException {
		Class.forName(DRIVERNAME);

		connection = DriverManager.getConnection(SOURCEURL, USER, PASSWORD);
	}
	
	@Override
	public void close() throws SQLException {
		connection.close();
	}

	protected IResultSet getResultSet(final ResultSet res) {
		return new IResultSet() {

			@Override
			public boolean next() throws SQLException {
				return res.next();
			}

			@Override
			public String getString(int col) throws SQLException {
				return res.getString(col);
			}

			@Override
			public void close() throws SQLException {
				res.close();

			}

		};
	}

}
