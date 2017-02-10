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

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.export.db2.main.util.ExportProperties;
import com.export.db2.main.util.SchemaToList;

public class ExportTest {

	private static Logger log = Logger.getLogger(ExportTest.class.getName());

	// private static String TABLE="ho.bsl_dms_file_lob";
	private static String TABLE = "HO.LAP_DN_ARCHIVE";
	// private static String TABLEH = "HumanResources.Department";
	private static String TABLEH = "Sales.CountryRegionCurrency";

	public static void main(String[] args) {
		log.info("Connect to Oracle");
		Connection conn = null;
		Properties prop = null;
		try {
			prop = ExportProperties.readProp(TestProp.getTestName());
			conn = JDBCConnection.getConnection(prop);
		} catch (ClassNotFoundException | SQLException | IOException e) {
			log.log(Level.SEVERE, "Cannot connect", e);
			System.exit(10);
		}
		try {
			// TableToCSV.exportTable(conn, prop, TABLE, "/tmp/data/");
			SchemaToList.exportList(conn, prop, "dbo", "output.list");
			// SchemaToList.exportList(conn, prop, "Sales", "output.list");
			//ExtractSchemas.exportList(conn, prop, "output.schema");
			// CreateHiveTable.create(conn, prop, TABLEH,"output.hive");
			//NumberOfRecords.exportNumbers(conn, prop, "output.list", "out.number");
		} catch (SQLException | IOException e) {
			log.log(Level.SEVERE, "Error while exporting file", e);
			System.exit(10);
		}
		try {
			conn.close();
		} catch (SQLException e) {
			log.log(Level.SEVERE, "Cannot close", e);
			System.exit(10);
		}

	}

}
