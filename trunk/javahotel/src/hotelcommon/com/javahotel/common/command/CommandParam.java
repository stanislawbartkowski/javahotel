/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
package com.javahotel.common.command;

import java.io.Serializable;
import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.toobject.AddPaymentP;
import com.javahotel.common.toobject.BillP;
import com.javahotel.common.toobject.BookingStateP;
import com.javahotel.common.toobject.GuestP;
import com.javahotel.common.toobject.PaymentP;
import com.javahotel.types.LId;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@SuppressWarnings("serial")
public class CommandParam implements Serializable {

    private static final transient String PERSON = "Person";
    private static final transient String HOTEL = "Hotel";
    private static final transient String DICT = "Dict";
    private static final transient String RECNAME = "RecName";
    private static final transient String DATEFROM = "DateFrom";
    private static final transient String DATETO = "DateTo";
    private static final transient String RESLIST = "ResList";
    private static final transient String SEASONNAME = "SeasonName";
    private static final transient String RESERVNAME = "ReservName";
    private PaymentP payP;
    private BookingStateP stateP;
    private Map<String, List<GuestP>> guests;
    private List<AddPaymentP> addpayment;
    private BillP bill;
    private LId recId;
    private HotelOpType oP;

    /**
     * @return the guests
     */
    public Map<String, List<GuestP>> getGuests() {
        return guests;
    }

    /**
     * @param guests
     *            the guests to set
     */
    public void setGuests(Map<String, List<GuestP>> guests) {
        this.guests = guests;
    }

    /**
     * @return the addpayment
     */
    public List<AddPaymentP> getAddpayment() {
        return addpayment;
    }

    /**
     * @param addpayment
     *            the addpayment to set
     */
    public void setAddpayment(List<AddPaymentP> addpayment) {
        this.addpayment = addpayment;
    }

    /**
     * @return the bill
     */
    public BillP getBill() {
        return bill;
    }

    /**
     * @param bill
     *            the bill to set
     */
    public void setBill(BillP bill) {
        this.bill = bill;
    }

    /**
     * @return the oP
     */
    public HotelOpType getoP() {
        return oP;
    }

    /**
     * @param oP the oP to set
     */
    public void setoP(HotelOpType oP) {
        this.oP = oP;
    }

    public interface ILog extends Serializable {

        void logEmptyParam(final String pa);
    }
    // cannot be final
    private Map<String, String> params;

    public CommandParam() {
        params = new HashMap<String, String>();
        payP = null;
        stateP = null;
        guests = null;
    }

    private void setS(final String key, final String s) {
        params.put(key, s);
    }

    private String getS(final String key, final ILog iL) {
        String s = params.get(key);
        if (s == null) {
            if (iL != null) {
                iL.logEmptyParam(key);
            }
        }
        return s;
    }

    public void setPerson(final String person) {
        setS(PERSON, person);
    }

    public String getPerson() {
        return getS(PERSON, null);
    }

    public void setHotel(final String hotel) {
        setS(HOTEL, hotel);
    }

    public String getHotel() {
        return getS(HOTEL, null);
    }

    public void setDict(final DictType d) {
        setS(DICT, d.toString());
    }

    public void setRecId(final LId id) {
        recId = id;
    }

    public LId getRecId() {
        return recId;
    }

    public DictType getDict(final ILog iL) {
        String d = getS(DICT, iL);
        if (d == null) {
            return null;
        }
        return DictType.valueOf(d);
    }

    public void setDateFrom(final Date dFrom) {
        String s = DateFormatUtil.toS(dFrom);
        setS(DATEFROM, s);
    }

    public void setDateTo(final Date dTo) {
        String s = DateFormatUtil.toS(dTo);
        setS(DATETO, s);
    }

    public Date getDateFrom() {
        String s = getS(DATEFROM, null);
        if (s == null) {
            return null;
        }
        return DateFormatUtil.toD(s);
    }

    public Date getDateTo() {
        String s = getS(DATETO, null);
        if (s == null) {
            return null;
        }
        return DateFormatUtil.toD(s);
    }

    private static String getResListKey(final int no) {
        return RESLIST + no;
    }

    public void setResListNo(final int no, final String s) {
        String key = getResListKey(no);
        setS(key, s);
    }

    public String getResListNo(final int no) {
        String key = getResListKey(no);
        return getS(key, null);
    }

    public void setRecName(final String s) {
        setS(RECNAME, s);
    }

    public String getRecName() {
        return getS(RECNAME, null);
    }

    public void setSeasonName(final String s) {
        setS(SEASONNAME, s);
    }

    public String getSeasonName() {
        return getS(SEASONNAME, null);
    }

    public void setDownPayment(final PaymentP p) {
        payP = p;
    }

    public PaymentP getDownPayment() {
        return payP;
    }

    public void setStateP(final BookingStateP p) {
        stateP = p;
    }

    public BookingStateP getStateP() {
        return stateP;
    }

    public void setReservName(final String s) {
        setS(RESERVNAME, s);
    }

    public String getReservName() {
        return getS(RESERVNAME, null);
    }

    // default visibility
    String toHashString() {
        String h = "";
        for (final String s : params.keySet()) {
            String val = params.get(s);
            h += s + "=" + val;
        }
        if (recId != null) {
            h += "recId=" + recId.toString();
        }
        return h;
    }
}
