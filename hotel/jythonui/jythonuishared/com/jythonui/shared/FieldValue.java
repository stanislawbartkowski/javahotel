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
package com.jythonui.shared;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import com.gwtmodel.table.common.TT;
import com.gwtmodel.table.shared.JythonUIFatal;

/**
 * @author hotel
 * 
 */
public class FieldValue implements Serializable {

	private static final long serialVersionUID = 1L;
	private TT type;
	private byte afterdot;

	transient private Object o;

	/**
	 * @return the valueS
	 */
	public String getValueS() {
		return (String) o;
	}

	/**
	 * @return the valueB
	 */
	public Boolean getValueB() {
		return (Boolean) o;
	}

	public Date getValueD() {
		return (Date) o;
	}

	public Timestamp getValueT() {
		return (Timestamp) o;
	}

	public Long getValueL() {
		return (Long) o;
	}

	public Integer getValueI() {
		return (Integer) o;
	}

	public BigDecimal getValueBD() {
		return (BigDecimal) o;
	}

	public void setValue(String valueS) {
		this.o = valueS;
		this.type = TT.STRING;
	}

	public void setValue(Boolean b) {
		this.o = b;
		type = TT.BOOLEAN;
	}

	public void setValue(Date d) {
		this.o = d;
		this.type = TT.DATE;
	}

	public void setValue(Timestamp t) {
		this.o = t;
		this.type = TT.DATETIME;
	}

	public void setValue(Long l) {
		this.o = l;
		type = TT.LONG;
	}

	public void setValue(Integer i) {
		this.o = i;
		type = TT.INT;
	}

	public void setValue(BigDecimal b, int afterdot) {
		this.o = b;
		type = TT.BIGDECIMAL;
		this.afterdot = (byte) afterdot;
	}

	/**
	 * @return the type
	 */
	public TT getType() {
		return type;
	}

	public int getAfterdot() {
		return afterdot;
	}

	public void setValue(TT type, Object val, int afterdot) {
		this.type = type;
		this.afterdot = (byte) afterdot;
		// it can simplified by o = val
		// but casting has been added to force type checking
		switch (type) {
		case STRING:
			setValue((String) val);
			break;
		case BOOLEAN:
			setValue((Boolean) val);
			break;
		case DATE:
			setValue((Date) val);
			break;
		case DATETIME:
			setValue((Timestamp) val);
			break;
		case LONG:
			setValue((Long) val);
			break;
		case INT:
			setValue((Integer) val);
			break;
		case BIGDECIMAL:
			o = (BigDecimal) val;
			break;
		default:
			throw new JythonUIFatal(type.toString() + " setValue, not implemented yet");
		}
	}

	public Object getValue() {
		// it can be simplified by return o
		// but casting has been added to force type checking
		switch (type) {
		case STRING:
			return (String) o;
		case BOOLEAN:
			return (Boolean) o;
		case DATE:
			return getValueD();
		case DATETIME:
			return getValueT();
		case LONG:
			return (Long) o;
		case INT:
			return (Integer) o;
		case BIGDECIMAL:
			return (BigDecimal) o;
		default:
			throw new JythonUIFatal(type.toString() + " getValue, not implemented yet");
		}
	}

}
