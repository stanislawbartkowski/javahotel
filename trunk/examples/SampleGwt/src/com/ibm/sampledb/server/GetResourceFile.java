/*
 * Copyright 2011 stanislawbartkowski@gmail.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.ibm.sampledb.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

class GetResourceFile {

	static String getFile(String name) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(name));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");
		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
			stringBuilder.append(ls);
		}
		return stringBuilder.toString();
	}

	static String getSql(String name) throws IOException {
		URL u = SampleServiceImpl.class.getResource("resource/" + name);
		return getFile(u.getFile());
	}

	private static JdbcTemplate setDataSourceName(String name) throws NamingException,
			SQLException {
		InitialContext ctx = new InitialContext();
		DataSource ds = (DataSource) ctx.lookup(name);
		Connection con = ds.getConnection();
		con.close();
		return new JdbcTemplate(ds);
	}

   static JdbcTemplate getJDBC() throws NamingException, SQLException {
		JdbcTemplate jdbc = setDataSourceName("java:comp/env/jdbc/SAMPLE");
		return jdbc;
	}

}
