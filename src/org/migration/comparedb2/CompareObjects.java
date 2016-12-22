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
package org.migration.comparedb2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.migration.extractor.ObjectExtractor;
import org.migration.extractor.ObjectExtractor.OBJECT;
import org.migration.tasks.ExtractContainer;

public class CompareObjects {

	private final static Logger log = Logger.getLogger(CompareObjects.class.getName());

	private static Connection connect(String url, String user, String password)
			throws ClassNotFoundException, SQLException {
		Class.forName("com.ibm.db2.jcc.DB2Driver");
		return DriverManager.getConnection(url, user, password);
	}

	private static void compare(ObjectExtractor.OBJECT oType, List<ObjectExtractor.IObjectExtracted> li, Connection con,
			List<ObjectsNofFound> outList) throws SQLException {
		// put all objects into set for fast looking up
		// map value is original name, map key is upper case to match the names
		// retrieves from quert
		Map<String, String> oSet = li.stream()
				.collect(Collectors.toMap(l -> l.getName().toUpperCase(), l -> l.getName()));
		char tQualif = 0;
		char routQualif = 0;
		switch (oType) {
		case TABLE:
			tQualif = 'T';
			break;
		case GLOBALTEMP:
			tQualif = 'G';
			break;
		case VIEW:
			tQualif = 'V';
			break;
		case FUNCTION:
			routQualif = 'F';
			break;
		case PROCEDURE:
			routQualif = 'P';
			break;
		}
		if (tQualif != 0) {
			// V, G or T
			ResultSet res = con.createStatement()
					.executeQuery("SELECT * FROM SYSCAT.TABLES WHERE TYPE='" + tQualif + "'");
			while (res.next()) {
				String schemaname = res.getString("TABSCHEMA").trim();
				String tabName = res.getString("TABNAME").trim();
				String oName = schemaname.toUpperCase() + "." + tabName.toUpperCase();
				// if found, remove from the set
				if (oSet.get(oName) != null)
					oSet.remove(oName);
			} // while(res)
		}
		if (oType == ObjectExtractor.OBJECT.SEQUENCE) {
			ResultSet res = con.createStatement().executeQuery("SELECT * FROM SYSCAT.SEQUENCES");
			while (res.next()) {
				String schemaname = res.getString("SEQSCHEMA").trim();
				String tabName = res.getString("SEQNAME").trim();
				String oName = schemaname.toUpperCase() + "." + tabName.toUpperCase();
				// if found, remove from the set
				if (oSet.get(oName) != null)
					oSet.remove(oName);
			} // while(res)
		}
		if (oType == ObjectExtractor.OBJECT.TYPE) {
			ResultSet res = con.createStatement().executeQuery("SELECT * FROM SYSCAT.DATATYPES");
			while (res.next()) {
				String schemaname = res.getString("TYPESCHEMA").trim();
				String tabName = res.getString("TYPENAME").trim();
				String oName = schemaname.toUpperCase() + "." + tabName.toUpperCase();
				// if found, remove from the set
				if (oSet.get(oName) != null)
					oSet.remove(oName);
			} // while(res)
		}
		if (oType == ObjectExtractor.OBJECT.PACKAGE || oType == ObjectExtractor.OBJECT.BODY) {
			// distinguish between package and package body is tricky
			// only by REMARKS
			
			String sql = "SELECT * FROM SYSCAT.MODULES";
			if (oType == ObjectExtractor.OBJECT.BODY)
				// learn by experience
				sql = "SELECT * FROM SYSCAT.MODULES WHERE REMARKS = 'PL/SQL Package Body'";
			ResultSet res = con.createStatement().executeQuery(sql);
			while (res.next()) {
				String schemaname = res.getString("MODULESCHEMA").trim();
				String tabName = res.getString("MODULENAME").trim();
				String oName = schemaname.toUpperCase() + "." + tabName.toUpperCase();
				// if found, remove from the set
				if (oSet.get(oName) != null)
					oSet.remove(oName);
			} // while(res)
		}
		if (routQualif != 0) {
			ResultSet res = con.createStatement().executeQuery("SELECT * FROM SYSCAT.ROUTINES");
			while (res.next()) {
				String schemaname = res.getString("ROUTINESCHEMA").trim();
				String tabName = res.getString("ROUTINENAME").trim();
				String oName = schemaname.toUpperCase() + "." + tabName.toUpperCase();
				// if found, remove from the set
				if (oSet.get(oName) != null)
					oSet.remove(oName);
			} // while(res)
		}
		
		if (oType == ObjectExtractor.OBJECT.TRIGGER) {
			ResultSet res = con.createStatement().executeQuery("SELECT * FROM SYSCAT.TRIGGERS");
			while (res.next()) {
				String schemaname = res.getString("TRIGSCHEMA").trim();
				String tabName = res.getString("TRIGNAME").trim();
				String oName = schemaname.toUpperCase() + "." + tabName.toUpperCase();
				// if found, remove from the set
				if (oSet.get(oName) != null)
					oSet.remove(oName);
			} // while(res)
		}
		
		outList.add(new ObjectsNofFound() {

			@Override
			public OBJECT objectType() {
				return oType;
			}

			@Override
			public List<String> list() {
				// return remaining objects
				return oSet.values().stream().collect(Collectors.toList());
			}
		});

	}

	public static List<ObjectsNofFound> analyze(String inputName, String url, String user, String password,
			Set<ObjectExtractor.OBJECT> lType) throws Exception {
		List<ObjectsNofFound> outList = new ArrayList<ObjectsNofFound>();
		Connection con = connect(url, user, password);
		File fn = new File(inputName);
		ExtractContainer.run(new BufferedReader(new FileReader(fn)),
				(ObjectExtractor.OBJECT oType, List<ObjectExtractor.IObjectExtracted> li,
						Map<ObjectExtractor.OBJECT, List<ObjectExtractor.IObjectExtracted>> ma) -> {
					if (lType.contains(oType) && li != null)
						try {
							compare(oType, li, con, outList);
						} catch (SQLException e) {
							log.log(Level.SEVERE, e.getMessage(), e);
							throw new RuntimeException(e);
						}
				});
		return outList;
	}

}
