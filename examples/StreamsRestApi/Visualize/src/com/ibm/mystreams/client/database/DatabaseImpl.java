/*
 * Copyright 2014 stanislawbartkowski@gmail.com
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
package com.ibm.mystreams.client.database;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;
import com.ibm.mystreams.client.util.Util;
import com.ibm.mystreams.shared.ConnectionData;

/**
 * 
 * @author sbartkowski
 * Implementation (based on cookies) for IDatabase
 */

class DatabaseImpl implements IDatabase {

	private static final String COOKIENAME = "Connections";

	@Override
	public List<ConnectionData> getList() {
		String val = Util.getCookie(COOKIENAME);
		List<ConnectionData> cList = new ArrayList<ConnectionData>();
		if (val == null)
			return cList;
		String[] names = val.split(",");
		int id = 0;
		for (String s : names) {
			String vals[] = s.split(":");
			ConnectionData con = new ConnectionData();
			con.setHost(vals[0]);
			con.setPort(vals[1]);
			con.setUser(vals[2]);
			con.setPassword(vals[3]);
			con.setId(id++);
			cList.add(con);
		}
		return cList;
	}

	private void toLineS(StringBuffer outString, ConnectionData data) {
		if (outString.length() > 0)
			outString.append(',');
		outString.append(Joiner.on(":").join(data.getHost(), data.getPort(),
				data.getUser(), data.getPassword()));
	}

	private boolean theSame(ConnectionData data1, ConnectionData data2) {
		if (!data1.getHost().equals(data2.getHost()))
			return false;
		if (!data1.getPort().equals(data2.getPort()))
			return false;
		if (!data1.getUser().equals(data2.getUser()))
			return false;
		return true;
	}

	@Override
	public void databaseOp(OP op, ConnectionData data) {
		StringBuffer outString = new StringBuffer();
		boolean wasAdded = false;
		for (ConnectionData c : getList()) {
			ConnectionData addC = c;
			if (theSame(c, data)) {
				if (op == OP.REMOVE)
					continue;
				addC = data;
				wasAdded = true;
			}
			toLineS(outString, addC);
		}
		if (op != OP.REMOVE && !wasAdded)
			toLineS(outString, data);
		if (outString.length() == 0)
			Util.removeCookie(COOKIENAME);
		else
			Util.setCookie(COOKIENAME, outString.toString());

	}

	@Override
	public boolean connectionExists(ConnectionData data) {
		for (ConnectionData c : getList())
			if (theSame(c, data))
				if (c.getId() != data.getId())
					return true;
		return false;
	}

	@Override
	public String toS(ConnectionData data) {
		return Joiner.on(":").join(data.getHost(), data.getPort(),
				data.getUser());
	}

	@Override
	public ConnectionData findS(String s) {
		String vals[] = s.split(":");
		ConnectionData con = new ConnectionData();
		con.setHost(vals[0]);
		con.setPort(vals[1]);
		con.setUser(vals[2]);
		for (ConnectionData c : getList())
			if (theSame(c, con))
				return c;
		return null;
	}

}
