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
package com.export.db2.main.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Logger;

public class NumberOfRecords {

	private static Logger log = Logger.getLogger(NumberOfRecords.class.getName());

	private NumberOfRecords() {

	}

	static private void numberTable(Connection con, String tableName, OutputTextFile out) throws SQLException {
		ResultSet res = con.prepareStatement("SELECT COUNT(*) FROM " + tableName).executeQuery();
		res.next();
		int num = res.getInt(1);
		out.writeline(tableName + "," + num);
		res.getStatement().close();
		res.close();
		log.info(tableName + " : " + num);
	}

	static public void exportNumbers(Connection con, Properties prop, String listOfTables, String outputFile)
			throws IOException, SQLException {
		Scanner s = new Scanner(new File(listOfTables));
		OutputTextFile out = new OutputTextFile();
		out.open(new File(outputFile), false);
		while (s.hasNext()) {
			String tableName = s.next();
			numberTable(con, tableName, out);
		}
		s.close();
		out.close();
	}

}
