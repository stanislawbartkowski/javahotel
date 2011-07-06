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
 */package com.ibm.sampledb.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.NamingException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;

class InsertDocBlob {

	static void insertDoc(final String empNo, final String comment,
			final String filename, final ByteArrayOutputStream out)
			throws IOException, NamingException, SQLException {
		String sql = GetResourceFile.getSql("insert_doc.sql");
		JdbcTemplate jdbc = GetResourceFile.getJDBC();
		
		// check blob size
		DatabaseMetaData dBase = jdbc.getDataSource().getConnection().getMetaData();
		ResultSet res = dBase.getColumns(null, null, "EMPATTACH", null);
		int maxSize = 0;
		while (res.next()) {
			
			String colName = res.getString("COLUMN_NAME");
			if (colName.equals("ATTACH")) {
				maxSize = res.getInt("COLUMN_SIZE");
			}
		}

		if (maxSize <= out.size()) {
			String message = "Attachment size: " + out.size() + " is to big. (max:" + maxSize + ")";
			throw new SQLException(message);
		}
		LobHandler lobHandler = new DefaultLobHandler();
		jdbc.execute(sql, new AbstractLobCreatingPreparedStatementCallback(
				lobHandler) {
			protected void setValues(PreparedStatement ps, LobCreator lobCreator)
					throws SQLException {
				ps.setString(1, empNo);
				ps.setString(2, filename);
				ps.setString(3, comment);
				lobCreator.setBlobAsBytes(ps, 4, out.toByteArray());
			}
		});

	}

}
