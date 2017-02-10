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

package com.export.db2.main.util;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class SchemaToList {

	private static Set<String> createSetOfTemporary(Connection con, Properties prop, String schemaName)
			throws SQLException {
		Set<String> temporarySet = new HashSet<String>();
		if (ExportProperties.isOracle(prop)) {
			try (ResultSet res = con.prepareStatement(
					"SELECT * FROM SYS.ALL_TABLES WHERE TEMPORARY = 'Y' AND OWNER = '" + schemaName.toUpperCase() + "'")
					.executeQuery()) {
				while (res.next()) {
					String name = res.getString("TABLE_NAME");
					temporarySet.add(name);
				}
				res.getStatement().close();
			}
		}
		return temporarySet;
	}

	public static void exportList(Connection con, Properties prop, String schemaName, String outputfileName)
			throws IOException, SQLException {
		Set<String> sche = CreateSetOfSchemas.create(con);		
		if (!sche.contains(schemaName))
			throw new IOException("Cannot find schema (case sensitive) " + schemaName);

		Set<String> setT = createSetOfTemporary(con, prop, schemaName);
		DatabaseMetaData mData = con.getMetaData();
		try (ResultSet res = mData.getTables(null, schemaName.toUpperCase(), null, new String[] { "TABLE" });
				OutputTextFile out = new OutputTextFile()) {
			out.open(new File(outputfileName), true);
			// important: append
			while (res.next()) {
				String tableName = res.getString("TABLE_NAME");
				// ignore temporary
				if (setT.contains(tableName))
					continue;
				String tName = res.getString("TABLE_SCHEM") + "." + tableName;
				out.writeline(tName);
			}
		}
	}

}
