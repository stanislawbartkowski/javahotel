/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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

import com.javahotel.common.command.CommandUtil;
import com.javahotel.types.INumerable;
import com.javahotel.types.LId;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
@SuppressWarnings("serial")
public class ResDayObjectStateP extends AbstractTo implements INumerable {

    private Date d;
    private String resObject;
    private Integer lp;
    private String bookName;
    private LId paymentRowId;
    private BookingStateP lState;

    public BookingStateP getLState() {
        return lState;
    }

    public void setLState(BookingStateP lState) {
        this.lState = lState;
    }

    public enum F implements IField {

        d, resObject, bookName,
    };

    @Override
    public Class getT(final IField f) {
        Class cla = String.class;
        F fi = (F) f;
        switch (fi) {
            case d:
                cla = Date.class;
                break;
        }
        return cla;
    }

    @Override
    public IField[] getT() {
        return CommandUtil.addT(F.values(), BookingStateP.F.values());
    }

    private BookingStateP.F dF(IField f) {
        if (f instanceof BookingStateP.F) {
            return (BookingStateP.F) f;
        }
        return null;
    }

    @Override
    public Object getF(IField f) {
        BookingStateP.F fd = dF(f);
        if (fd != null) {
            return getLState().getF(fd);
        }
        F fi = (F) f;
        switch (fi) {
            case d:
                return getD();
            case resObject:
                return getResObject();
            case bookName:
                return getBookName();
        }
        return null;

    }

    @Override
    public void setF(IField f, Object o) {
        // do nothing, never set
    }

    public Date getD() {
        return d;
    }

    public void setD(final Date d) {
        this.d = d;
    }

    public String getResObject() {
        return resObject;
    }

    public void setResObject(final String resObject) {
        this.resObject = resObject;
    }

    public Integer getLp() {
        return lp;
    }

    public void setLp(Integer lp) {
        this.lp = lp;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(final String bookName) {
        this.bookName = bookName;
    }

    public LId getPaymentRowId() {
        return paymentRowId;
    }

    public void setPaymentRowId(LId paymentRowId) {
        this.paymentRowId = paymentRowId;
    }

    public void setFree() {
        setLState(null);
    }
}
