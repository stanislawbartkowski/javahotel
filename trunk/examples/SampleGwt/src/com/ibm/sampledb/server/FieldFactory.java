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

import java.util.ArrayList;
import java.util.List;

import com.ibm.sampledb.shared.IResourceType;
import com.ibm.sampledb.shared.RowFieldInfo;
import com.ibm.sampledb.shared.GetField.FieldType;

class FieldFactory {

	// create table empattach (id INTEGER GENERATED ALWAYS AS IDENTITY, empno
	// char(6) NOT NULL, filename varchar2(100) NOT NULL, comment varchar2(100),
	// adddate DATE DEFAULT CURRENT DATE, attach BLOB)
	// alter table empattach FOREIGN KEY FOREIGN_EMPNO (EMPNO) REFERENCES
	// EMPLOYEE ON DELETE NO ACTION

	/*
	 * 
	 * db2 describe table employee EMPNO SYSIBM CHARACTER 6 0 No FIRSTNME SYSIBM
	 * VARCHAR 12 0 No MIDINIT SYSIBM CHARACTER 1 0 Yes LASTNAME SYSIBM VARCHAR
	 * 15 0 No WORKDEPT SYSIBM CHARACTER 3 0 Yes PHONENO SYSIBM CHARACTER 4 0
	 * Yes HIREDATE SYSIBM TIMESTAMP 7 0 Yes JOB SYSIBM CHARACTER 8 0 Yes
	 * EDLEVEL SYSIBM SMALLINT 2 0 No SEX SYSIBM CHARACTER 1 0 Yes BIRTHDATE
	 * SYSIBM TIMESTAMP 7 0 Yes SALARY SYSIBM DECIMAL 9 2 Yes BONUS SYSIBM
	 * DECIMAL 9 2 Yes COMM SYSIBM DECIMAL 9 2 Yes -- additional NOATTACH INT
	 * (number of attachments)
	 */

	private static final List<RowFieldInfo> fList = new ArrayList<RowFieldInfo>();
	private static final List<RowFieldInfo> aList = new ArrayList<RowFieldInfo>();

	static {
		fList.add(new RowFieldInfo(IResourceType.EMPNO, FieldType.STRING,
				"Employee", 8, 1));
		fList.add(new RowFieldInfo(IResourceType.FIRSTNME, FieldType.STRING,
				"First name", 10, 2));
		fList.add(new RowFieldInfo("MIDINIT", FieldType.STRING, "Mid", 5, 3));
		fList.add(new RowFieldInfo(IResourceType.LASTNAME, FieldType.STRING,
				"Last name", 10, 4));
		fList.add(new RowFieldInfo("WORKDEPT", FieldType.STRING, "Dept", 5, 5));
		fList.add(new RowFieldInfo("PHONENO", FieldType.STRING, "Phone", 5, 6));
		fList.add(new RowFieldInfo("HIREDATE", FieldType.DATE, "Hire date", 10,
				7));
		fList.add(new RowFieldInfo("JOB", FieldType.STRING, "Job", 8, 8));
		fList.add(new RowFieldInfo("EDLEVEL", FieldType.INTEGER, "Ed level", 2,
				9));
		fList.add(new RowFieldInfo("SEX", FieldType.STRING, "Sex", 2, 10));
		fList.add(new RowFieldInfo("BIRTHDATE", FieldType.DATE, "Birth day",
				10, 11));
		fList.add(new RowFieldInfo("SALARY", FieldType.NUMBER, "Salary", 6, 12));
		fList.add(new RowFieldInfo("BONUS", FieldType.NUMBER, "Bonus", 6, 13));
		fList.add(new RowFieldInfo("COMM", FieldType.NUMBER, "Comm", 6, 14));
		fList.add(new RowFieldInfo(IResourceType.NOATTACH, FieldType.INTEGER,
				"Att", 3, 15));

		aList.add(new RowFieldInfo(IResourceType.ATTACHID, FieldType.INTEGER,
				null, 3, 1));
		aList.add(new RowFieldInfo(IResourceType.FILENAME, FieldType.STRING,
				"File name", 50, 2));
		aList.add(new RowFieldInfo(IResourceType.COMMENT, FieldType.STRING,
				"Comment", 30, 3));
		aList.add(new RowFieldInfo("ADDDATE", FieldType.DATE, "Date", 10, 4));

	}

	static List<RowFieldInfo> getFlist() {
		return fList;
	}

	static List<RowFieldInfo> getAlist() {
		return aList;
	}

}
