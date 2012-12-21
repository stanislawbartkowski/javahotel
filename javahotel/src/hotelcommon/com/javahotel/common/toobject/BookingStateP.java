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
package com.javahotel.common.toobject;

import java.util.Date;

import com.javahotel.common.command.BookingStateType;
import com.javahotel.types.INumerable;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
@SuppressWarnings("serial")
public class BookingStateP extends AbstractTo implements INumerable {

    private Long id;
    private Integer lp;
    private BookingStateType bState;
    private OperationData op;

    public enum F implements IField {

        id, dateOp, lp, bState, remarks, personOp
    };

    @Override
    public Class<?> getT(IField f) {
        F fi = (F) f;
        Class<?> cla = String.class;
        switch (fi) {
            case id:
                cla = Long.class;
                break;
            case lp:
                cla = Integer.class;
                break;
            case dateOp:
                cla = Date.class;
                break;
            case bState:
                cla = BookingStateType.class;
                break;
        }
        return cla;
    }

    @Override
    public IField[] getT() {
        return F.values();
    }

    @Override
    public Object getF(IField f) {
        F fi = (F) f;
        switch (fi) {
            case id:
                return getId();
            case dateOp:
                return getDateOp();
            case lp:
                return getLp();
            case bState:
                return getBState();
            case remarks:
                return getRemarks();
            case personOp:
                return getPersonOp();
        }
        return null;
    }

    @Override
    public void setF(IField f, Object o) {
        F fi = (F) f;
        switch (fi) {
            case id:
                setId((Long) o);
                break;
            case dateOp:
                setDateOp((Date) o);
                break;
            case lp:
                setLp((Integer) o);
                break;
            case bState:
                setBState((BookingStateType) o);
                break;
            case remarks:
                setRemarks((String) o);
                break;
            case personOp:
                setPersonOp((String) o);
                break;
        }
    }

    public BookingStateP() {
        op = new OperationData();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateOp() {
        return op.getDateOp();
    }

    public void setDateOp(final Date dateOp) {
        op.setDateOp(dateOp);
    }

    public Integer getLp() {
        return lp;
    }

    public void setLp(Integer lp) {
        this.lp = lp;
    }

    public BookingStateType getBState() {
        return bState;
    }

    public void setBState(BookingStateType bState) {
        this.bState = bState;
    }

    public String getRemarks() {
        return op.getRemarks();
    }

    public void setRemarks(final String remarks) {
        op.setRemarks(remarks);
    }
    
    /**
     * @return the personOp
     */
    public String getPersonOp() {
        return op.getPersonOp();
    }

    /**
     * @param personOp
     *            the personOp to set
     */
    public void setPersonOp(String personOp) {
        op.setPersonOp(personOp);
    }

}
