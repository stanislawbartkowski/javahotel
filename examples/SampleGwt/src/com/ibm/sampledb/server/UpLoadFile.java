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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.ibm.sampledb.shared.IResourceType;

@SuppressWarnings("serial")
public class UpLoadFile extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletFileUpload upload = new ServletFileUpload();

		try {
			FileItemIterator iter = upload.getItemIterator(request);
			String empNo = null;
			String fileName = null;
			String comment = null;


			while (iter.hasNext()) {
				FileItemStream item = iter.next();

				String name = item.getFieldName();
				String fName = item.getName();
				InputStream stream = item.openStream();
				String value = null;

				// Process the input stream
				boolean character = false;
				if (name.equals(IResourceType.COMMENT)
						|| name.equals(IResourceType.EMPNO)) {
					character = true;
				}

				ByteArrayOutputStream out = new ByteArrayOutputStream();
				int len;
				byte[] buffer = new byte[8192];
				while ((len = stream.read(buffer, 0, buffer.length)) != -1) {
					out.write(buffer, 0, len);
				}
				if (character) {
					Charset csets = Charset.forName("UTF-8");
					ByteBuffer bu = ByteBuffer.wrap(buffer, 0, out.size());
					value = csets.decode(bu).toString();
					if (name.equals(IResourceType.EMPNO)) {
						empNo = value;
					}
					if (name.equals(IResourceType.COMMENT)) {
						comment = value;
					}
				}
				if (name.equals(IResourceType.FILE)) {
					fileName = fName;
					InsertDocBlob.insertDoc(empNo, comment, fileName, out);
					continue;
				}

			} // while

		} catch (Exception e) {
			e.printStackTrace();
			PrintWriter out = response.getWriter();
			out.println(IResourceType.ERRORSUBMIT);
			out.close();
		}

	}
}
