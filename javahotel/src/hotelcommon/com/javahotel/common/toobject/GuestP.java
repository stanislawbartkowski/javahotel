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

import com.gwtmodel.table.common.dateutil.DateP;
import com.javahotel.types.LId;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@SuppressWarnings("serial")
public class GuestP extends AbstractTo {

    /** Customer (guest.) */
    private LId customer;
    /** Check-in date. */
    private DateP checkIn;
    /** Check-out date. */
    private DateP checkOut;

    public enum F implements IField {

        id, checkIn, checkOut
    };
    
    /**
     * @return the customer
     */
    public LId getCustomer() {
        return customer;
    }

    /**
     * @param customer
     *            the customer to set
     */
    public void setCustomer(LId customer) {
        this.customer = customer;
    }

    /**
     * @return the checkIn
     */
    public Date getCheckIn() {
        return checkIn.getD();
    }

    /**
     * @param checkIn
     *            the checkIn to set
     */
    public void setCheckIn(Date checkIn) {
        this.checkIn.setD(checkIn);
    }

    /**
     * @return the checkOut
     */
    public Date getCheckOut() {
        return checkOut.getD();
    }

    /**
     * @param checkOut
     *            the checkOut to set
     */
    public void setCheckOut(Date checkOut) {
        this.checkOut.setD(checkOut);
    }


    @Override
    public IField[] getT() {
        return F.values();
    }


    /**
     * Constructor, creates empty dates
     */
    public GuestP() {
        checkIn = new DateP();
        checkOut = new DateP();
    }

    /**
     * Trasforms IField to local F, does accept any other IField
     * @param f IField
     * @return F (local)
     */
    private F getFie(IField f) {
        return (F) f;
    }

    @Override
    public Object getF(IField f) {
        F fie = getFie(f);
        switch (fie) {
        case id:
            return getCustomer();
        case checkIn:
            return getCheckIn();
        case checkOut:
            return getCheckOut();
        }
        return null;
    }

    @Override
    public Class<?> getT(IField f) {
        F fie = getFie(f);
        switch (fie) {
        case id:
            return Long.class;
        case checkIn:
            return Date.class;
        case checkOut:
            return Date.class;
        }
        return null;
    }

    @Override
    public void setF(IField f, Object o) {
        F fie = (F) f;
        switch (fie) {
        case id:
            setCustomer((LId) o);
            break;
        case checkIn:
            setCheckIn((Date) o);
            break;
        case checkOut:
            setCheckOut((Date) o);
            break;
        }
    }
}