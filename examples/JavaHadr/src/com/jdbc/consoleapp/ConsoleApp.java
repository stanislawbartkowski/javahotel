/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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
package com.jdbc.consoleapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import com.jdbc.proc.IGetConnection;
import com.jdbc.proc.IJDBCAction;
import com.jdbc.proc.JDBCActionFactory;
import com.jdbc.proc.PersonRecord;

/**
 * Console application built on the top of IJDBCAction interface
 * 
 * @author sbartkowski
 * 
 */
public class ConsoleApp {

	private ConsoleApp() {
	}

	private static void P(String mess) {
		System.out.println(mess);
	}

	private static class GetConn implements IGetConnection {

		private final String user = "db2had1";
		private final String password = "db2had1";
		private final String url = "jdbc:db2://think:50001/sample";

		/** Parameters for HADR stand by, server name and port. */
		private final String serverAlternate = "think";
		private final int portAlternate = 50002;

		@Override
		public Connection getCon() throws SQLException, ClassNotFoundException {
			Class.forName("com.ibm.db2.jcc.DB2Driver");
			// Construct URL referencing HADR environment
			String u = url
					+ ":"
					+ com.ibm.db2.jcc.DB2BaseDataSource.propertyKey_clientRerouteAlternateServerName
					+ "=" + serverAlternate + ";";
			u += com.ibm.db2.jcc.DB2BaseDataSource.propertyKey_clientRerouteAlternatePortNumber
					+ "=" + portAlternate + ";";
			// Connect !
			Connection con = DriverManager.getConnection(u, user, password);
			return con;
		}
	}

	static public void run() throws IOException {
		IJDBCAction ha = JDBCActionFactory.contruct(new GetConn());
		while (true) {
			P("===============================================");
			P("Connection status: " + ha.getStatus());
			P("1) Connect");
			P("2) Disconnect");
			P("3) Read list");
			P("4) Add person");
			P("5) Create PERSON table");
			P("6) Drop PERSON table");
			P("7) Set autocommit on");
			P("8) Set autocommit off");
			P("99) Exit");
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(System.in));
			String in = bufferedReader.readLine();
			String actionS = null;
			if (in.equals("1")) {
				ha.connect();
				actionS = ha.getLastResult();
			}
			if (in.equals("2")) {
				ha.disconnect();
				actionS = ha.getLastResult();
			}
			if (in.equals("3")) {
				List<PersonRecord> list = ha.getList();
				actionS = ha.getLastResult();
				if (list != null) {
					for (PersonRecord re : list) {
						String line = "" + re.getName() + " "
								+ re.getFamilyName();
						P(line);
					}
				}
			}
			if (in.equals("4")) {
				System.out.print("Name:");
				String name = bufferedReader.readLine();
				System.out.print("Family name:");
				String familyName = bufferedReader.readLine();
				PersonRecord pe = new PersonRecord(name, familyName);
				ha.addPerson(pe);
				actionS = ha.getLastResult();
			}
			if (in.equals("5")) {
				ha.createTable();
				actionS = ha.getLastResult();
			}
			if (in.equals("6")) {
				ha.dropTable();
				actionS = ha.getLastResult();
			}
			if (in.equals("7") || in.equals("8")) {
				ha.setAutocommit(in.equals("7"));
				actionS = ha.getLastResult();
			}
			if (in.equals("99")) {
				break;
			}
			if (actionS == null) {
				actionS = "Invalid input";
			}
			P(actionS);
		}
	}

}
