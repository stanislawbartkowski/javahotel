/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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
import com.gwtmodel.table.common.dateutil.DateP;

/**
 * @author hotel
 * 
 */
public class FieldValue implements Serializable {

    // object is immutable but because it is used as a transfer object
    // the attributes cannot be final and default constructor is only used

    private String valueS;
    private Boolean valueB;
    private DateP valueD;
    private TT type;
    private int afterdot;
    private Long l;
    private Integer i;
    private BigDecimal b;

    /**
     * @return the valueS
     */
    public String getValueS() {
        return valueS;
    }

    /**
     * @return the valueB
     */
    public Boolean getValueB() {
        return valueB;
    }

    public Date getValueD() {
        return valueD.getD();
    }

    public Timestamp getValueT() {
        return valueD.getT();
    }

    public Long getValueL() {
        return l;
    }

    public Integer getValueI() {
        return i;
    }

    public BigDecimal getValueBD() {
        return b;
    }

    public void setValue(String valueS) {
        this.valueS = valueS;
        this.type = TT.STRING;
    }

    public void setValue(Boolean b) {
        this.valueB = b;
        type = TT.BOOLEAN;
    }

    public void setValue(Date d) {
        valueD = new DateP();
        valueD.setD(d);
        this.type = TT.DATE;
    }

    public void setValue(Timestamp t) {
        valueD = new DateP();
        valueD.setT(t);
        this.type = TT.DATETIME;
    }

    public void setValue(Long l) {
        this.l = l;
        type = TT.LONG;
    }

    public void setValue(Integer i) {
        this.i = i;
        type = TT.INT;
    }

    public void setValue(BigDecimal b, int afterdot) {
        this.b = b;
        type = TT.BIGDECIMAL;
        this.afterdot = afterdot;
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
        this.afterdot = afterdot;
        switch (type) {
        case STRING:
            valueS = (String) val;
            break;
        case BOOLEAN:
            valueB = (Boolean) val;
            break;
        case DATE:
            setValue((Date) val);
            break;
        case DATETIME:
            setValue((Timestamp) val);
            break;
        case LONG:
            l = (Long) val;
            break;
        case INT:
            i = (Integer) val;
            break;
        case BIGDECIMAL:
            b = (BigDecimal) val;
            break;
        default:
            throw new JythonUIFatal(type.toString()
                    + " setValue, not implemented yet");
        }
    }

    public Object getValue() {
        switch (type) {
        case STRING:
            return valueS;
        case BOOLEAN:
            return valueB;
        case DATE:
            return getValueD();
        case DATETIME:
            return getValueT();
        case LONG:
            return l;
        case INT:
            return i;
        case BIGDECIMAL:
            return b;
        default:
            throw new JythonUIFatal(type.toString()
                    + " getValue, not implemented yet");
        }
    }

}
