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
package org.ibmstreams.bigsql.mapping;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.ibmstreams.bigsql.transform.ToBIGSQL;
import java.lang.RuntimeException;

/**
 * 
 * @author sbartkowski
 *         <p>
 *         Single value to be put into the HBase cell. Should correspond to
 *         ColumnMapping class.
 *         <p>
 *         Constructor is protected, values should constructed by means of
 *         static method.
 */

public class HVal {

	/** BigSQL columnn type. null means NULL value. */
	private final BIGSQLTYPE t;
	/** Value, appropriate object type. */
	private final Object val;

	HVal(String vals) {
		this.t = BIGSQLTYPE.CHAR;
		val = vals;
	}

	boolean isNull() {
		return t == null;
	}

	HVal(int vali) {
		this.val = new Integer(vali);
		t = BIGSQLTYPE.INT;
	}

	HVal(long vall) {
		this.val = new Long(vall);
		t = BIGSQLTYPE.BIGINT;
	}

	HVal(float valf) {
		t = BIGSQLTYPE.FLOAT;
		val = new Float(valf);
	}

	HVal(double valf) {
		t = BIGSQLTYPE.DOUBLE;
		val = new Double(valf);
	}

	HVal(Timestamp ti) {
		t = BIGSQLTYPE.TIMESTAMP;
		val = ti;
	}

	HVal(Date d) {
		t = BIGSQLTYPE.DATE;
		val = d;
	}

	HVal(Boolean b) {
		this.t = BIGSQLTYPE.BOOLEAN;
		val = b;
	}

	HVal(BigDecimal dec) {
		this.t = BIGSQLTYPE.DECIMAL;
		val = dec;
	}

	HVal() {
		t = null;
		val = null;
	}

	/**
	 * Construct string, CHAR value
	 * 
	 * @param s
	 * @return
	 */
	public static HVal createS(String s) {
		return new HVal(s);
	}

	/**
	 * Construct null value, regardless of the type
	 * 
	 * @return
	 */
	public static HVal createNull() {
		return new HVal();
	}

	/**
	 * Construct integer value (INT, TINYINT).
	 * 
	 * @param i
	 * @return
	 */
	public static HVal createInt(int i) {
		return new HVal(i);
	}

	/**
	 * Construct BIGING value
	 * 
	 * @param l
	 * @return
	 */
	public static HVal createLong(long l) {
		return new HVal(l);
	}

	/**
	 * Construct FLOAT value
	 * 
	 * @param f
	 * @return
	 */
	public static HVal createFloat(float f) {
		return new HVal(f);
	}

	/**
	 * Construct DOUBLE value
	 * 
	 * @param f
	 * @return
	 */
	public static HVal createDouble(double f) {
		return new HVal(f);
	}

	/**
	 * Construct TIMESTAMP value
	 * 
	 * @param t
	 * @return
	 */
	public static HVal createTimestamp(Timestamp t) {
		return new HVal(t);
	}

	/**
	 * Construct DATE value
	 * 
	 * @param d
	 * @return
	 */
	public static HVal createDate(Date d) {
		return new HVal(d);
	}

	/**
	 * Construct BOOLEAN value
	 * 
	 * @param b
	 * @return
	 */
	public static HVal createBool(Boolean b) {
		return new HVal(b);
	}

	/**
	 * Construct DECIMAL value
	 * 
	 * @param dec
	 * @return
	 */

	public static HVal createDecimal(BigDecimal dec) {
		return new HVal(dec);
	}

	/**
	 * Construct appropriate BigSQL value from string.
	 * 
	 * @param t
	 *            BigSQL column type
	 * @param val
	 *            String value, important: cannot be null
	 * @return
	 */
	public static HVal formString(BIGSQLTYPE t, String val) {
		assert val != null;
		switch (t) {
		case CHAR:
			return HVal.createS(val);
		case TINYINT:
		case INT:
			return HVal.createInt(Integer.parseInt(val.trim()));
		case BIGINT:
			return HVal.createLong(Long.parseLong(val));
		case DOUBLE:
			return HVal.createDouble(Double.parseDouble(val));
		case FLOAT:
			return HVal.createFloat(Float.parseFloat(val));
		case TIMESTAMP:
			return HVal.createTimestamp(Timestamp.valueOf(val));
		case DATE:
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			try {
				return HVal.createDate(formatter.parse(val));
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		case BOOLEAN:
			return HVal.createBool(Boolean.valueOf(val));
		case DECIMAL:
			return HVal.createDecimal(ToBIGSQL.tBC(Double.parseDouble(val)));
		}
		return null;
	}

	private double toD() {
		switch (t) {
		case FLOAT:
		case DOUBLE:
			return ((Number) val).doubleValue();
		case CHAR:
			return Double.parseDouble((String) val);
		default:
			assert false;
			return -1;
		}
	}

	private long toL() {
		switch (t) {
		case BIGINT:
		case INT:
			return ((Number) val).longValue();
		case CHAR:
			return Long.parseLong((String) val);
		default:
			assert false;
			return -1;
		}
	}

	/**
	 * Creates byte[] value ready to be put into HBase cell or index.
	 * 
	 * @param t
	 *            BigSQL column type, should correspond to t value from class
	 *            constructor
	 * @return byte[]
	 */
	public byte[] toB(BIGSQLTYPE t) {
		if (isNull())
			return ToBIGSQL.toNull();
		assert val != null;
		switch (t) {
		case TINYINT:
			return ToBIGSQL.tinytoB((byte) toL());
		case INT:
			return ToBIGSQL.inttoB((int) toL());
		case CHAR:
			return ToBIGSQL.sToB((String) val);
		case BIGINT:
			return ToBIGSQL.longtoB(toL());
		case FLOAT:
			return ToBIGSQL.floattoB((float) toD());
		case DOUBLE:
			return ToBIGSQL.doubletoB(toD());
		case TIMESTAMP:
			return ToBIGSQL.timestamptoB((Timestamp) val);
		case DATE:
			return ToBIGSQL.datetoB((Date) val);
		case BOOLEAN:
			return ToBIGSQL.booltoB((Boolean) val);
		case DECIMAL:
			return ToBIGSQL.decimaltoB((BigDecimal) val);
		} // switch
		assert false;
		return null;
	}

}
