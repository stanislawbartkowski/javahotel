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
package com.extract.test;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.extract.extractemp.ExtractFactory;
import com.extract.extractemp.IExtractEmp;

public class Test1 {

//	private static final ExtractFactory.dbtype dType = ExtractFactory.dbtype.OracleJDBC;
	private static final ExtractFactory.dbtype dType = ExtractFactory.dbtype.OracleHibernate;
//	private static final ExtractFactory.dbtype dType = ExtractFactory.dbtype.DB2Hibernate;
//	private static final ExtractFactory.dbtype dType = ExtractFactory.dbtype.DB2JDBC;
	private IExtractEmp iEmp;

	@Before
	public void before() throws ClassNotFoundException, SQLException {
		iEmp = ExtractFactory.construct(dType);
		iEmp.connect();
	}

	@After
	public void after() throws SQLException {
		iEmp.close();
	}

	private void assertRow(IExtractEmp.IResultSet res, String empname, String job, String depname) throws SQLException {
		assertEquals(empname, res.getString(1));
		assertEquals(job, res.getString(2));
		assertEquals(depname, res.getString(3));
	}

	@Test
	public void test1() throws SQLException {
		IExtractEmp.IResultSet res = iEmp.getEmp(null, null, null);
		int no = 0;
		while (res.next())
			no++;
		res.close();
		assertEquals(14, no);
	}

	@Test
	public void test2() throws SQLException {
		IExtractEmp.IResultSet  res = iEmp.getEmp("SCOTT", null, null);
		int no = 0;
		while (res.next()) {
			no++;
			assertRow(res, "SCOTT", "ANALYST", "RESEARCH");
		}
		res.close();
		assertEquals(1, no);
	}

	@Test
	public void test3() throws SQLException {
		IExtractEmp.IResultSet res = iEmp.getEmp(null, null, "RESEARCH");
		int no = 0;
		while (res.next()) {
			no++;
			switch (no) {
			case 1:
				assertRow(res, "ADAMS", "CLERK", "RESEARCH");
				break;
			case 2:
				assertRow(res, "FORD", "ANALYST", "RESEARCH");
				break;
			case 3:
				assertRow(res, "JONES", "MANAGER", "RESEARCH");
				break;
			case 4:
				assertRow(res, "SCOTT", "ANALYST", "RESEARCH");
				break;
			case 5:
				assertRow(res, "SMITH", "CLERK", "RESEARCH");
				break;
			}
		}
		res.close();
		assertEquals(5, no);
	}

	@Test
	public void test4() throws SQLException {
		IExtractEmp.IResultSet res = iEmp.getEmp(null, "JONES", null);
		int no = 0;
		while (res.next()) {
			no++;
			switch (no) {
			case 1:
				assertRow(res, "FORD", "ANALYST", "RESEARCH");
				break;
			case 2:
				assertRow(res, "SCOTT", "ANALYST", "RESEARCH");
				break;
			}
		}
		res.close();
		assertEquals(2, no);
	}

}
