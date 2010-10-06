/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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

import java.util.List;
import java.util.Date;

import com.javahotel.common.command.BookingEnumTypes;
import com.javahotel.common.command.CommandUtil;
import com.javahotel.types.DateP;
import com.javahotel.types.LId;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class BookingP extends DictionaryP {

    private DateP checkIn;
    private DateP checkOut;
    private LId customer;
    private Integer noPersons;
    private String season;
    private List<BookRecordP> bookrecords;
    private List<BookingStateP> state;
    private List<BillP> bill;
    private BookingEnumTypes bookingType;
    private String resName;

    public List<BillP> getBill() {
        return bill;
    }

    public void setBill(List<BillP> bill) {
        this.bill = bill;
    }

    public BookingEnumTypes getBookingType() {
        return bookingType;
    }

    public void setBookingType(BookingEnumTypes bookingType) {
        this.bookingType = bookingType;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public enum F implements IField {

        checkIn, checkOut, customer, noPersons, season, resName, bookingType
    };

    @Override
    public void copyFrom(final AbstractTo from) {
        super.copyFrom(from);
        BookingP f = (BookingP) from;
        bookrecords = f.getBookrecords();
        state = f.getState();
        bill = f.getBill();
    }


    @Override
    protected boolean emptySpecialTrue(final IField f) {
        if (f instanceof BookingP.F) {
            F fie = (F) f;
            switch (fie) {
                case noPersons:
                    if (noPersons == null) {
                        break;
                    }
                    if ((noPersons.intValue() == 0) ||
                            (noPersons.intValue() == -1)) {
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

    @Override
    public Class getT(final IField f) {
        Class cla = super.getT(f);
        if (cla != null) {
            return cla;
        }
        F fie = (F) f;
        switch (fie) {
            case checkIn:
            case checkOut:
                return Date.class;
            case noPersons:
                return Integer.class;
            case resName:
            case season:
                return String.class;
        }
        return null;
    }

    @Override
    public IField[] getT() {
        return CommandUtil.addTD(F.values());
    }

    @Override
    public Object getF(IField f) {
        DictionaryP.F pfi = CommandUtil.dF(f);
        if (pfi != null) {
            return super.getF(pfi);
        }

        F fi = (F) f;
        switch (fi) {
            case checkIn:
                return getCheckIn();
            case checkOut:
                return getCheckOut();
            case customer:
                return getCustomer();
            case noPersons:
                return getNoPersons();
            case season:
                return getSeason();
            case resName:
                return getResName();
            case bookingType:
                return getBookingType();

        }
        return null;
    }

    @Override
    public void setF(IField f, Object o) {
        DictionaryP.F pfi = CommandUtil.dF(f);
        if (pfi != null) {
            super.setF(pfi, o);
            return;
        }

        F fi = (F) f;
        switch (fi) {
            case checkIn:
                setCheckIn((Date) o);
                break;
            case checkOut:
                setCheckOut((Date) o);
                break;
            case customer:
                setCustomer((LId) o);
                break;
            case noPersons:
                setNoPersons((Integer) o);
                break;
            case season:
                setSeason((String) o);
                break;
            case resName:
                setResName((String) o);
                break;
            case bookingType:
                setBookingType((BookingEnumTypes) o);
                break;
        }
    }

    public BookingP() {
        checkIn = new DateP();
        checkOut = new DateP();
    }

    public Date getCheckIn() {
        return checkIn.getD();
    }

    public void setCheckIn(final Date checkIn) {
        this.checkIn.setD(checkIn);
    }

    public Date getCheckOut() {
        return checkOut.getD();
    }

    public void setCheckOut(final Date checkOut) {
        this.checkOut.setD(checkOut);
    }

    public LId getCustomer() {
        return customer;
    }

    public void setCustomer(LId customer) {
        this.customer = customer;
    }

    public Integer getNoPersons() {
        return noPersons;
    }

    public void setNoPersons(Integer noPersons) {
        this.noPersons = noPersons;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(final String season) {
        this.season = season;
    }

    public List<BookRecordP> getBookrecords() {
        return bookrecords;
    }

    public void setBookrecords(final List<BookRecordP> bookrecords) {
        this.bookrecords = bookrecords;
    }

    public List<BookingStateP> getState() {
        return state;
    }

    public void setState(final List<BookingStateP> state) {
        this.state = state;
    }
}
