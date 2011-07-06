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

public class GetField {

	public enum FieldType {
		INTEGER, DATE, STRING, NUMBER
	}

	/**
	 * Field, column value (C union structure would be the best) Only one
	 * attribute should be set
	 * 
	 * @author sbartkowski
	 * 
	 */
	public static class FieldValue  {
		/** Timestamp. date column. */
		private final Timestamp dField;
		/** char, carchar2 column. */
		private final String sField;
		/** integer column. */
		private final Integer iField;
		/** decimal column. */
		private final BigDecimal nField;

		public FieldValue(Timestamp dField, String sField, Integer iField,
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

		public String getString(RowFieldInfo f) {
			String val = null;
			switch (f.getfType()) {
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


	public static FieldValue getValue(RowFieldInfo f, OneRecord i) {
		Object o = i.getField(f.getfId());
		String val = null;
		Integer iVal = null;
		Timestamp dVal = null;
		BigDecimal bVal = null;
		switch (f.getfType()) {
		case INTEGER:
			iVal = (Integer) o;
			break;
		case DATE:
			dVal = (Timestamp) o;
			break;
		case STRING:
			val = (String) o;
			break;
		case NUMBER:
			bVal = (BigDecimal) o;
			break;
		}
		return new FieldValue(dVal, val, iVal, bVal);
	}

	public static FieldValue getValue(String field, GetRowsInfo rInfo,
			OneRecord i) {
		RowFieldInfo f = rInfo.getFieldInfo(field);
		return getValue(f, i);
	}

}
