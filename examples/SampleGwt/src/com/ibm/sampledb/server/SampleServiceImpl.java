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

import java.io.IOException;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.springframework.jdbc.core.JdbcTemplate;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ibm.sampledb.client.SampleService;
import com.ibm.sampledb.shared.GetField;
import com.ibm.sampledb.shared.GetField.FieldValue;
import com.ibm.sampledb.shared.GetRowsInfo;
import com.ibm.sampledb.shared.IResourceType;
import com.ibm.sampledb.shared.OneRecord;
import com.ibm.sampledb.shared.ResourceInfo;
import com.ibm.sampledb.shared.RowFieldInfo;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class SampleServiceImpl extends RemoteServiceServlet implements
		SampleService {

	private String getResourceName(String name) throws NamingException {
		InitialContext ctx = new InitialContext();
		try {
			String res = (String) ctx.lookup("java:comp/env/" + name);
			return res;
		} catch (javax.naming.NameNotFoundException e) {
			return null;
		}
	}

	private boolean getResourceBool(String name) throws NamingException {
		InitialContext ctx = new InitialContext();
		try {
			Boolean res = (Boolean) ctx.lookup("java:comp/env/" + name);
			return res.booleanValue();
		} catch (javax.naming.NameNotFoundException e) {
			return false;
		}
	}

	@Override
	public List<OneRecord> getList(String orderBy) throws Exception {
		JdbcTemplate jdbc = GetResourceFile.getJDBC();
		String query = GetResourceFile.getSql("employeequery.sql");
		if (orderBy != null) {
			query += " ORDER BY " + orderBy;
		}
		List<OneRecord> mList = jdbc.query(query, new RecordMapper(
				FieldFactory.getFlist()));
		return mList;
	}

	private String getCustom(String name) throws NamingException, IOException {
		String fName = getResourceName(name);
		return GetResourceFile.getFile(fName);
	}

	private ResourceInfo getInfo() throws Exception {
		boolean customRow = getResourceBool("customRow");
		String jScriptRes = getCustom("jsFile");
		String cssRes = getCustom("cssFile");
		String jsAddRowFunc = getResourceName("customAddStyle");
		String birtURL = getResourceName("ReportBirtURL");
		String reportE = getResourceName("ReportEmployee");
		ResourceInfo info = new ResourceInfo();
		info.setCssS(cssRes);
		info.setCustomRow(customRow);
		info.setJavaS(jScriptRes);
		info.setJsAddRowFunc(jsAddRowFunc);
		info.setBirtURL(birtURL);
		info.setEmployeeReport(reportE);
		info.setTableWidth("97%");
		return info;
	}

	@Override
	public String printList(List<OneRecord> li) throws Exception {
		CreateXML xml = new CreateXML("employees", "employee");
		xml.startXML();
		for (OneRecord i : li) {
			xml.startElem();
			for (RowFieldInfo f : FieldFactory.getFlist()) {
				FieldValue val = GetField.getValue(f, i);
				xml.drawElem(f.getfId(), val.getString(f));
			}
			xml.endElem();
		}
		xml.endXML();
		return xml.getFileName();
	}

	@Override
	public GetRowsInfo getInfo(String resType) throws Exception {
		GetRowsInfo info = new GetRowsInfo();
		if (resType.equals(IResourceType.EMPLOYEE)) {
			info.setResource(getInfo());
			info.setfList(FieldFactory.getFlist());
		}
		if (resType.equals(IResourceType.ATTACHMENTS)) {
			ResourceInfo r = new ResourceInfo();
			r.setTableWidth("97%");
			info.setResource(r);
			info.setfList(FieldFactory.getAlist());
		}
		return info;
	}

	@Override
	public List<OneRecord> getAttachmentList(String empNo) throws Exception {
		JdbcTemplate jdbc = GetResourceFile.getJDBC();
		String query = GetResourceFile.getSql("selectattachment.sql");
		List<OneRecord> mList = jdbc.query(query, new RecordMapper(
				FieldFactory.getAlist()), new Object[] { empNo });
		return mList;
	}

	@Override
	public void removeAttachment(Integer Id) throws Exception {
		JdbcTemplate jdbc = GetResourceFile.getJDBC();
		String query = GetResourceFile.getSql("removeattachment.sql");
		jdbc.update(query, new Object[] { Id });
	}
}
