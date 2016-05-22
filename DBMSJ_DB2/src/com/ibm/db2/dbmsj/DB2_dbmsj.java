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
package com.ibm.db2.dbmsj;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DB2_dbmsj {

	private static void echo(String mess) throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:default:connection");
		CallableStatement cstmt;

		cstmt = con.prepareCall("{ CALL DBMS_OUTPUT.PUT_LINE(?) }");
		cstmt.setString(1, mess);
		cstmt.executeUpdate();
		cstmt.close();
	}

	private static int no = 0;

	static class CursorData {
		String query = null;
		boolean afterwhere = true;
		// set is necessary to keep nulls
		Set<String> markers = new HashSet<String>();
		Map<String, String> bindV = new HashMap<String, String>();
	}

	private static Map<Integer, CursorData> stor = new ConcurrentHashMap<Integer, CursorData>();

	private static CursorData getData(int id, boolean query) throws SQLException {
		CursorData my = stor.get(id);
		if (my == null)
			throw new SQLException("SQL identifier " + id + " not found");
		if (query && my.query == null)
			throw new SQLException("SQL identifier " + id + " parseQuery not provided");
		return my;
	}

	private static synchronized int getNum() {
		return no++;
	}

	public static int openCursor() {
		int no = getNum();
		stor.put(no, new CursorData());
		return no;
	}

	public static void parseQuery(int id, String query) throws SQLException {
		// getData(id, false).query = query.replaceAll("\\r|\\n|\\t", " ");
		getData(id, false).query = query;
	}

	public static void parseQueryI(int id, String query) throws SQLException {
		getData(id, false).query = query;
		getData(id, false).afterwhere = false;
	}

	public static void bindV(int id, String marker, String val) throws SQLException {
		CursorData da = getData(id, true);
		if (!da.query.contains(marker))
			throw new SQLException(id + "," + marker + "  SQL, variable marker not found");
		da.markers.add(marker);
		// can be null
		da.bindV.put(marker, val);
	}

	public static void prepareP(int id, String[] q, java.sql.Array[] outval) throws SQLException {
		CursorData da = getData(id, true);
		// parse query
		StringBuffer bu = new StringBuffer(da.query);
		int i = -1;
		if (da.afterwhere) {
			i = bu.indexOf(" WHERE ");
			if (i == -1) {
				q[0] = bu.toString();
				return;
			}
		}
		List<String> a = new ArrayList<String>();
		while (++i < bu.length()) {
			if (bu.charAt(i) != ':')
				continue;
			int j = i;
			StringBuffer marker = new StringBuffer(":");
			while (++j < bu.length() && (Character.isLetterOrDigit(bu.charAt(j)) || bu.charAt(j) == '_'))
				marker.append(bu.charAt(j));
			String val = da.bindV.get(marker.toString());
			if (val == null && !da.markers.contains(marker.toString()))
				throw new SQLException(id + " cannot find marker " + marker.toString());

			bu.replace(i, i + marker.length(), "?");
			a.add(val);
		}
		String[] outs = new String[a.size()];
		for (int inde = 0; inde < a.size(); inde++)
			outs[inde] = a.get(inde);
		Connection con = DriverManager.getConnection("jdbc:default:connection");
		outval[0] = con.createArrayOf(outval[0].getBaseTypeName(), outs);
		q[0] = bu.toString();
	}

}
