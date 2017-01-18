/*
 * Copyright 2017 stanislawbartkowski@gmail.com  
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
package com.gwtmodel.table.common;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public class ConvertTT {

	private ConvertTT() {

	}

	public static Object toO(TT t, String s) {
		if (CUtil.EmptyS(s) && t != TT.STRING)
			return null;

		Object o;
		switch (t) {
		case DATE:
			o = DateFormat.toD(s, false);
			break;
		case DATETIME:
			Date d = DateFormat.toD(s, true);
			o = new Timestamp(d.getTime());
			break;
		case BIGDECIMAL:
			o = DecimalUtils.toBig(s);
			break;
		case LONG:
			o = CUtil.toLong(s);
			break;
		case INT:
			o = CUtil.toInteger(s);
			break;
		case BOOLEAN:
			o = Boolean.parseBoolean(s);
			break;
		default:
			o = s;
			break;
		}
		return o;
	}

	private static String getBigDecimalS(Object o, int afterdot) {
		BigDecimal b = (BigDecimal) o;
		if (b == null) {
			return "";
		}
		return DecimalUtils.DecimalToS(b, afterdot);
	}

	private static String getLongS(Object o) {
		Long l = (Long) o;
		if (l == null) {
			return "";
		}
		return l.toString();
	}

	private static String getIntS(Object o) {
		Integer l = (Integer) o;
		if (l == null) {
			return "";
		}
		return l.toString();
	}

	private static String getStringS(Object o) {
		String s = (String) o;
		return s;
	}

	public static String toS(Object o, TT t, int afterdot) {
		if (o == null)
			return null;
		switch (t) {
		case DATE:
			Date d = (Date) o;
			return DateFormat.toS(d, false);
		case BIGDECIMAL:
			return getBigDecimalS(o, afterdot);
		case LONG:
			return getLongS(o);
		case INT:
			return getIntS(o);
		case BOOLEAN:
			Boolean b = (Boolean) o;
			return b.toString();
		case DATETIME:
			Timestamp ti = (Timestamp) o;
			return DateFormat.toS(new Date(ti.getTime()), true);
		default:
			return getStringS(o);
		}
	}

}
