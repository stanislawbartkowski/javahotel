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
package com.ibm.sampledb.shared;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class GetField {

    public enum FieldType {
        INTEGER, DATE, STRING, NUMBER
    }

    public static class FieldValue {
        private final Timestamp dField;
        private final String sField;
        private final int iField;
        private final BigDecimal nField;

        public FieldValue(Timestamp dField, String sField, int iField,
                BigDecimal nField) {
            super();
            this.dField = dField;
            this.sField = sField;
            this.iField = iField;
            this.nField = nField;
        }

        public Timestamp getdField() {
            return dField;
        }

        public String getsField() {
            return sField;
        }

        public int getiField() {
            return iField;
        }

        public BigDecimal getnField() {
            return nField;
        }

        public String getString(FieldInfo f) {
            String val = null;
            switch (f.fType) {
            case STRING:
                val = sField;
                break;
            case DATE:
                if (dField != null) {
                    val = dField.toString();
                }
                break;
            case INTEGER:
                val = new Integer(iField).toString();
                break;
            case NUMBER:
                if (nField != null) {
                    val = nField.toPlainString();
                }
                break;
            }
            return val;
        }
    }

    public static class FieldInfo {
        private final String fId;
        private final FieldType fType;

        public int getcSize() {
            return cSize;
        }

        private final String fDescr;
        private final int cSize;

        public FieldInfo(String fId, FieldType fType, String fDescr, int cSize) {
            this.fId = fId;
            this.fType = fType;
            this.fDescr = fDescr;
            this.cSize = cSize;
        }

        public String getfId() {
            return fId;
        }

        public FieldType getfType() {
            return fType;
        }

        public String getfDescr() {
            return fDescr;
        }

    }

    private static List<FieldInfo> fList = new ArrayList<FieldInfo>();

    static {
        fList.add(new FieldInfo("EMPNO", FieldType.STRING, "Employee", 10));
        fList.add(new FieldInfo("FIRSTNME", FieldType.STRING, "First name", 10));
        fList.add(new FieldInfo("MIDINIT", FieldType.STRING, "Mid", 5));
        fList.add(new FieldInfo("LASTNAME", FieldType.STRING, "Last name", 10));
        fList.add(new FieldInfo("WORKDEPT", FieldType.STRING, "Dept", 5));
        fList.add(new FieldInfo("PHONENO", FieldType.STRING, "Phone", 5));
        fList.add(new FieldInfo("HIREDATE", FieldType.DATE, "Hire date", 10));
        fList.add(new FieldInfo("JOB", FieldType.STRING, "Job", 8));
        fList.add(new FieldInfo("EDLEVEL", FieldType.INTEGER, "Ed level", 2));
        fList.add(new FieldInfo("SEX", FieldType.STRING, "Sex", 2));
        fList.add(new FieldInfo("BIRTHDATE", FieldType.DATE, "Birth day", 10));
        fList.add(new FieldInfo("SALARY", FieldType.NUMBER, "Salary", 6));
        fList.add(new FieldInfo("BONUS", FieldType.NUMBER, "Bonus", 6));
        fList.add(new FieldInfo("COMM", FieldType.NUMBER, "Comm", 6));
    }

    public static List<FieldInfo> getfList() {
        return fList;
    }

    public static FieldValue getValue(String field, IRecord i) {

        EmployeeRecord e = (EmployeeRecord) i;

        String val = null;
        int iVal = -1;
        Timestamp dVal = null;
        BigDecimal bVal = null;

        if (field.equals("EMPNO")) {
            val = e.getEmpno();
        } else if (field.equals("FIRSTNME")) {
            val = e.getFirstname();
        } else if (field.equals("LASTNAME")) {
            val = e.getLastname();
        } else if (field.equals("WORKDEPT")) {
            val = e.getWorkdept();
        } else if (field.equals("PHONENO")) {
            val = e.getPhoneno();
        } else if (field.equals("SEX")) {
            val = e.getSex();
        } else if (field.equals("MIDINIT")) {
            val = e.getMidinit();
        } else if (field.equals("JOB")) {
            val = e.getJob();
        } else if (field.equals("EDLEVEL")) {
            iVal = e.getEdlevel();
        } else if (field.equals("SALARY")) {
            bVal = e.getSalary();
        } else if (field.equals("BONUS")) {
            bVal = e.getBonus();
        } else if (field.equals("COMM")) {
            bVal = e.getComm();
        } else if (field.equals("HIREDATE")) {
            dVal = e.getHiredate();
        } else if (field.equals("BIRTHDATE")) {
            dVal = e.getBirthdate();
        } else {
            // internal error - field name does not match
        }
        return new FieldValue(dVal, val, iVal, bVal);
    }

}
