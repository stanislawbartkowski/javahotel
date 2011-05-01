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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ibm.sampledb.client.SampleService;
import com.ibm.sampledb.shared.EmployeeRecord;
import com.ibm.sampledb.shared.GetField;
import com.ibm.sampledb.shared.GetField.FieldInfo;
import com.ibm.sampledb.shared.GetField.FieldValue;
import com.ibm.sampledb.shared.IRecord;
import com.ibm.sampledb.shared.ResourceInfo;

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

    private String getFile(String name) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(name));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }
        return stringBuilder.toString();
    }

    private String getSql(String name) throws IOException {
        URL u = getClass().getResource("resource/" + name);
        return getFile(u.getFile());
    }

    private JdbcTemplate setDataSourceName(String name) throws NamingException {
        InitialContext ctx = new InitialContext();
        return new JdbcTemplate((DataSource) ctx.lookup(name));
    }

    private JdbcTemplate getJDBC() throws NamingException {
        JdbcTemplate jdbc = setDataSourceName("java:comp/env/jdbc/SAMPLE");
        return jdbc;
    }

    private class EmployeeRecordMapper implements RowMapper<EmployeeRecord> {

        private String getS(ResultSet res, int no) throws SQLException {
            String r = res.getString(no);
            if (r == null) {
                return null;
            }
            return r.trim();
        }

        @Override
        public EmployeeRecord mapRow(ResultSet res, int arg1)
                throws SQLException {

            String empno = getS(res, 1);
            String firstname = getS(res, 2);
            String midinit = getS(res, 3);
            String lastname = getS(res, 4);
            String workdept = getS(res, 5);
            String phoneno = getS(res, 6);
            Timestamp hiredate = res.getTimestamp(7);
            String job = getS(res, 8);
            int edlevel = res.getInt(9);
            String sex = getS(res, 10);
            Timestamp birthdate = res.getTimestamp(11);
            BigDecimal salary = res.getBigDecimal(12);
            BigDecimal bonus = res.getBigDecimal(13);
            BigDecimal comm = res.getBigDecimal(14);

            EmployeeRecord el = new EmployeeRecord();
            el.setEmpno(empno);
            el.setFirstname(firstname);
            el.setMidinit(midinit);
            el.setLastname(lastname);
            el.setWorkdept(workdept);
            el.setPhoneno(phoneno);
            el.setHiredate(hiredate);
            el.setJob(job);
            el.setEdlevel(edlevel);
            el.setSex(sex);
            el.setBirthdate(birthdate);
            el.setSalary(salary);
            el.setBonus(bonus);
            el.setComm(comm);
            return el;
        }

    }

    @Override
    public List<? extends IRecord> getList(String orderBy) throws Exception {
        JdbcTemplate jdbc;
        try {
            jdbc = getJDBC();
        } catch (NamingException e) {
            e.printStackTrace();
            throw e;
        }
        String query;
        try {
            query = getSql("employeequery.sql");
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        if (orderBy != null) {
            query += " ORDER BY " + orderBy;
        }
        List<EmployeeRecord> mList = jdbc.query(query,
                new EmployeeRecordMapper());
        return (List<? extends IRecord>) mList;
    }

    private String getCustom(String name) throws NamingException, IOException {
        String fName = getResourceName(name);
        return getFile(fName);
    }

    @Override
    public ResourceInfo getInfo() throws Exception {
        try {
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
            return info;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public String printList(List<? extends IRecord> li) throws Exception {
        CreateXML xml = new CreateXML("employees", "employee");
        try {
            xml.startXML();
            for (IRecord i : li) {
                xml.startElem();
                for (FieldInfo f : GetField.getfList()) {
                    FieldValue val = GetField.getValue(f.getfId(), i);
                    xml.drawElem(f.getfId(), val.getString(f));
                }
                xml.endElem();
            }
            xml.endXML();
            return xml.getFileName();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
