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
package com.javahotel.db.copy;

import java.util.List;

import com.gwtmodel.table.common.dateutil.DateUtil;
import com.javahotel.common.toobject.AddPaymentP;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.BookingStateP;
import com.javahotel.common.toobject.GuestP;
import com.javahotel.common.toobject.PaymentP;
import com.javahotel.common.toobject.PaymentRowP;
import com.javahotel.db.context.ICommandContext;
import com.javahotel.db.copy.CopyBeanToP.ICopyHelper;
import com.javahotel.db.hotelbase.jpa.AddPayment;
import com.javahotel.db.hotelbase.jpa.BookElem;
import com.javahotel.db.hotelbase.jpa.Booking;
import com.javahotel.db.hotelbase.jpa.BookingState;
import com.javahotel.db.hotelbase.jpa.Guest;
import com.javahotel.db.hotelbase.jpa.Payment;
import com.javahotel.db.hotelbase.jpa.PaymentRow;
import com.javahotel.db.hotelbase.jpa.ServiceDictionary;
import com.javahotel.db.jtypes.HId;
import com.javahotel.db.jtypes.ToLD;
import com.javahotel.dbjpa.copybean.CopyBean;
import com.javahotel.dbres.messid.IMessId;
import com.javahotel.remoteinterfaces.HotelT;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class CopyBooking {

    private CopyBooking() {
    }

    private static ICopyHelper getAddPayment(final HotelT ho) {
        final CopyBeanToP.ICopyHelper copyaddpay = new CopyHelper.IICopyHelper(
                FieldList.AddPayList, new String[] { "id" }) {

            public Object getI(final Object se) {
                return new AddPayment();
            }

            @Override
            public boolean eq(final Object o1, final Object o2) {
                return false;
            }

            @Override
            protected void dcopy(final ICommandContext iC, final Object sou,
                    final Object dest) {
                AddPaymentP sou1 = (AddPaymentP) sou;
                AddPayment dest1 = (AddPayment) dest;
                ServiceDictionary se = (ServiceDictionary) iC.getC().get(
                        ServiceDictionary.class, sou1.getRService().getName());
                dest1.setRService(se);
                CopyHelper.checkPersonDateOp(iC, dest1);
            }
        };
        return copyaddpay;
    }

    private static ICopyHelper getCopyGuest() {
        final CopyBeanToP.ICopyHelper copyguestlist = new ICopyHelper() {

            public boolean eq(Object o1, Object o2) {
                GuestP g1 = (GuestP) o1;
                Guest g2 = (Guest) o2;
                if (!ToLD.eq(g1.getCustomer(), g2.getCustomer().getId())) {
                    return false;
                }
                if (!DateUtil.eqNDate(g1.getCheckIn(), g2.getCheckIn())) {
                    return false;
                }
                if (!DateUtil.eqNDate(g1.getCheckOut(), g2.getCheckOut())) {
                    return false;
                }
                return true;
            }

            public Object getI(Object se) {
                return new Guest();
            }

            public void copy(ICommandContext iC, Object sou, Object dest) {
                GuestP sou1 = (GuestP) sou;
                Guest dest1 = (Guest) dest;
                CopyHelper.copyCustomer(iC, sou1.getCustomer(), dest1);
                CopyBean.copyBean(sou, dest, iC.getLog(), FieldList.GuestList);
            }
        };
        return copyguestlist;
    }

    static void copy1(final ICommandContext iC, final BookingP sou,
            final Booking dest) {

        final HotelT ho = new HotelT(sou.getHotel());
        CopyHelper.copyDict1(iC, sou, dest, FieldList.BookingList);
        CopyHelper.checkPersonDateOp(iC, dest);
        CopyHelper.copyCustomer(iC, sou, dest);

        final CopyBeanToP.ICopyHelper copypa = new CopyHelper.INumerableCopyHelper(
                FieldList.PaymentList) {

            public Object getI(final Object se) {
                return new Payment();
            }
        };
        final CopyBeanToP.ICopyHelper copystate = new CopyHelper.INumerableCopyHelper(
                FieldList.StateList) {

            public Object getI(final Object se) {
                return new BookingState();
            }
        };

        final CopyBeanToP.ICopyHelper copypayrow = new CopyHelper.IICopyHelper(
                FieldList.PaymentRowList, new String[] { "id" }) {

            public Object getI(final Object se) {
                return new PaymentRow();
            }
        };

        final CopyBeanToP.ICopyHelper copyaddpay = getAddPayment(ho);

        final CopyBeanToP.ICopyHelper copyguestlist = getCopyGuest();

        final CopyBeanToP.ICopyHelper copybookelem = new CopyHelper.AIICopyHelper(
                FieldList.BookElemList) {

            @Override
            protected void dcopy(final ICommandContext iC, final Object sou,
                    final Object dest) {
                BookElemP sou1 = (BookElemP) sou;
                if (sou1.getPaymentrows() == null
                        || sou1.getPaymentrows().isEmpty()) {
                    iC.logFatal(IMessId.NOPAYMENTROWS, sou1.getResObject());
                }
                BookElem dest1 = (BookElem) dest;
                CopyBeanToP.copyRes1Collection(iC, sou1, dest1, "paymentrows",
                        "bookelem", BookElem.class, copypayrow, true);
                CopyBeanToP.copyRes1Collection(iC, sou1, dest1, "guests",
                        "bookelem", BookElem.class, copyguestlist, true);
            }

            public Object getI(Object se) {
                return new BookElem();
            }
        };

        CopyBeanToP.copyRes1Collection(iC, sou, dest, "payments", "booking",
                Booking.class, copypa, true);
        CopyBeanToP.copyRes1Collection(iC, sou, dest, "addpayments", "booking",
                Booking.class, copyaddpay, true);

        CopyBeanToP.copyRes1Collection(iC, sou, dest, "state", "booking",
                Booking.class, copystate, true);
        CopyBeanToP.copyRes1Collection(iC, sou, dest, "booklist", "booking",
                Booking.class, copybookelem, true);
    }

    static void copy2(final ICommandContext iC, final Booking sou,
            final BookingP dest) {

        CopyHelper.copyDict2(iC, sou, dest, FieldList.BookingList);
        HId id = sou.getId();
        dest.setId(ToLD.toLId(id));

        dest.setCustomer(ToLD.toLId(sou.getCustomer().getId()));
        CopyHelper.copyRes2Collection(iC, sou, dest, "state",
                BookingStateP.class);
        CopyHelper
                .copyRes2Collection(iC, sou, dest, "payments", PaymentP.class);
        CopyHelper.copyRes2Collection(iC, sou, dest, "addpayments",
                AddPaymentP.class);
        CopyHelper.copyRes2Collection(iC, sou, dest, "booklist",
                BookElemP.class);

    }

    static void copy2(final ICommandContext iC, final Guest sou,
            final GuestP dest) {
        dest.setCustomer(ToLD.toLId(sou.getCustomer().getId()));
        CopyBean.copyBean(sou, dest, iC.getLog(), FieldList.GuestList);
    }

    static void copy2(final ICommandContext iC, final BookElem sou,
            final BookElemP dest) {
        CopyBean.copyBean(sou, dest, iC.getLog(), FieldList.BookElemList);
        CopyHelper.copyRes2Collection(iC, sou, dest, "paymentrows",
                PaymentRowP.class);
        CopyHelper.copyRes2Collection(iC, sou, dest, "guests", GuestP.class);
    }

    static void copyGuests(final ICommandContext iC, final BookElem dest,
            List<GuestP> guests) {

        BookElemP sou = new BookElemP();
        sou.setGuests(guests);
        final CopyBeanToP.ICopyHelper copyguestlist = getCopyGuest();

        CopyBeanToP.copyRes1Collection(iC, sou, dest, "guests", "bookelem",
                BookElem.class, copyguestlist, true);
    }

    static void copyAddPayment(final ICommandContext iC, final Booking dest,
            HotelT ho, List<AddPaymentP> col) {
        BookingP sou = new BookingP();
        sou.setAddpayments(col);
        final CopyBeanToP.ICopyHelper copylist = getAddPayment(ho);
        CopyBeanToP.copyRes1Collection(iC, sou, dest, "addpayments", "booking",
                Booking.class, copylist, true);
    }
}
