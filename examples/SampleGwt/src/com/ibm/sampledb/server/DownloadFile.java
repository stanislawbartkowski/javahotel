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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ibm.sampledb.shared.IResourceType;
import com.ibm.sampledb.shared.OneRecord;

@SuppressWarnings("serial")
public class DownloadFile extends RemoteServiceServlet {

	class RecordMapper implements RowMapper<OneRecord> {

		private LobHandler lobHandler = new DefaultLobHandler();
		private final HttpServletResponse response;

		RecordMapper(HttpServletResponse response) {
			this.response = response;
		}

		@Override
		public OneRecord mapRow(ResultSet arg0, int arg1) throws SQLException {
			String filename = arg0.getString(1);
			MimetypesFileTypeMap mim = new MimetypesFileTypeMap();
			String e = mim.getContentType(new File(filename));
			response.setContentType(e);
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ filename + '\"');
			InputStream input = lobHandler.getBlobAsBinaryStream(arg0, 2);
			byte[] buffer = new byte[8192];
			int size = 0;
			int len;
			try {
				BufferedOutputStream output = new BufferedOutputStream(
						response.getOutputStream());
				while ((len = input.read(buffer)) != -1) {
					size += len;
					output.write(buffer, 0, len);
				}
				output.close();
				input.close();
			} catch (IOException e1) {
				e1.printStackTrace();
				throw new SQLException();
			}
			response.setHeader("Content-Length", "" + size);
			return null;
		}
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// String filename = URLDecoder.decode(request.getPathInfo(), "UTF-8");
		try {
			String fileid = request.getParameter(IResourceType.ATTACHID);
			Integer id = new Integer(fileid);
			JdbcTemplate jdbc = GetResourceFile.getJDBC();
			String query = GetResourceFile.getSql("getblob.sql");
			@SuppressWarnings("unused")
			OneRecord o = jdbc.queryForObject(query,
					new RecordMapper(response), new Object[] { id });
		} catch (Exception e) {
			e.printStackTrace();
			PrintWriter out = response.getWriter();
			out.println(IResourceType.ERRORSUBMIT);
			out.close();
		}
	}
}