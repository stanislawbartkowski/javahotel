/*
 * Copyright 2017 stanislawbartkowski@gmail.com 
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
package com.export.db2.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

import com.export.db2.main.util.ExportProperties;

class JDBCConnection {

	private static Logger log = Logger.getLogger(ExportProperties.class.getName());

	static Connection getConnection(Properties prop) throws ClassNotFoundException, SQLException {

		String URL = prop.getProperty(ExportProp.PARAMSOURCEURL);
		String USER = prop.getProperty(ExportProp.PARAMUSER);
		String PASSWORD = prop.getProperty(ExportProp.PARAMPASSWORD);
		String DRIVERNAME = prop.getProperty(ExportProp.PARAMDRIVERNAME);
		
		log.info("Connecting to " + URL);

		Class.forName(DRIVERNAME);
		
		return DriverManager.getConnection(URL, USER, PASSWORD);

	}

}
