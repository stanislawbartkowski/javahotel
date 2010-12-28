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
package com.javahotel.db.copy;

import java.util.List;

import com.javahotel.common.dateutil.DateUtil;
import com.javahotel.common.toobject.AddPaymentP;
import com.javahotel.common.toobject.AdvancePaymentP;
import com.javahotel.common.toobject.BillP;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookRecordP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.BookingStateP;
import com.javahotel.common.toobject.GuestP;
import com.javahotel.common.toobject.PaymentP;
import com.javahotel.common.toobject.PaymentRowP;
import com.javahotel.db.context.ICommandContext;
import com.javahotel.db.copy.CopyBeanToP.ICopyHelper;
import com.javahotel.db.hotelbase.jpa.AddPayment;
import com.javahotel.db.hotelbase.jpa.AdvancePayment;
import com.javahotel.db.hotelbase.jpa.Bill;
import com.javahotel.db.hotelbase.jpa.BookElem;
import com.javahotel.db.hotelbase.jpa.BookRecord;
import com.javahotel.db.hotelbase.jpa.Booking;
import com.javahotel.db.hotelbase.jpa.BookingState;
import com.javahotel.db.hotelbase.jpa.Guest;
import com.javahotel.db.hotelbase.jpa.Payment;
import com.javahotel.db.hotelbase.jpa.PaymentRow;
import com.javahotel.db.hotelbase.jpa.ServiceDictionary;
import com.javahotel.db.jtypes.ToLD;
import com.javahotel.dbjpa.copybean.CopyBean;
import com.javahotel.remoteinterfaces.HotelT;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class CopyBooking {

	private CopyBooking() {
	}

	static void copyBill(final ICommandContext iC, final HotelT ho,
			final String seasonName, final BillP sou1, final Bill dest1) {

		CopyHelper.copyCustomer(iC, sou1, dest1);
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
		final String seasonName = sou.getSeason();
		CopyHelper.copyDict1(iC, sou, dest, FieldList.BookingList);
		CopyHelper.copyCustomer(iC, sou, dest);

		final CopyBeanToP.ICopyHelper copypa = new CopyHelper.INumerableCopyHelper(
				FieldList.PaymentList) {

			public Object getI(final Object se) {
				return new Payment();
			}
		};
		final CopyBeanToP.ICopyHelper copyval = new CopyHelper.INumerableCopyHelper(
				FieldList.ValidationList) {

			public Object getI(final Object se) {
				return new AdvancePayment();
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

		final CopyBeanToP.ICopyHelper copybookre = new CopyHelper.AINumerableCopyHelper(
				FieldList.BookRecordList) {

			public Object getI(final Object se) {
				return new BookRecord();
			}

			@Override
			protected void dcopy(final ICommandContext iC, final Object sou,
					final Object dest) {
				BookRecordP sou1 = (BookRecordP) sou;
				BookRecord dest1 = (BookRecord) dest;
				CopyBeanToP.copyRes1Collection(iC, sou1, dest1, "booklist",
						"bookrecord", BookRecord.class, copybookelem, true);
			}
		};

		final CopyBeanToP.ICopyHelper copybillre = new CopyHelper.AIICopyHelper(
				FieldList.BillList) {

			public Object getI(final Object se) {
				return new Bill();
			}

			@Override
			protected void dcopy(final ICommandContext iC, final Object sou,
					final Object dest) {
				BillP sou1 = (BillP) sou;
				Bill dest1 = (Bill) dest;

				copyBill(iC, ho, seasonName, sou1, dest1);
				CopyBeanToP.copyRes1Collection(iC, sou1, dest1, "payments",
						"bill", Bill.class, copypa, true);
				CopyBeanToP.copyRes1Collection(iC, sou1, dest1, "advancePay",
						"bill", Bill.class, copyval, true);
				CopyBeanToP.copyRes1Collection(iC, sou1, dest1, "addpayments",
						"bill", Bill.class, copyaddpay, true);
			}
		};

		CopyBeanToP.copyRes1Collection(iC, sou, dest, "bill", "booking",
				Booking.class, copybillre, true);
		CopyBeanToP.copyRes1Collection(iC, sou, dest, "state", "booking",
				Booking.class, copystate, true);
		CopyBeanToP.copyRes1Collection(iC, sou, dest, "bookrecords", "booking",
				Booking.class, copybookre, true);
	}

	static void copy2(final ICommandContext iC, final Booking sou,
			final BookingP dest) {
		CopyHelper.copyDict2(iC, sou, dest, FieldList.BookingList);
		dest.setCustomer(ToLD.toLId(sou.getCustomer().getId()));
		CopyHelper.copyRes2Collection(iC, sou, dest, "state",
				BookingStateP.class);
		CopyHelper.copyRes2Collection(iC, sou, dest, "bookrecords",
				BookRecordP.class);
		CopyHelper.copyRes2Collection(iC, sou, dest, "bill", BillP.class);
	}

	static void copy2(final ICommandContext iC, final BookRecord sou,
			final BookRecordP dest) {
		CopyHelper.copyBeanINumerable(iC, sou, dest, FieldList.BookRecordList);
		CopyHelper.copyRes2Collection(iC, sou, dest, "booklist",
				BookElemP.class);
	}

	static void copy2(final ICommandContext iC, final Bill sou, final BillP dest) {
		dest.setCustomer(ToLD.toLId(sou.getCustomer().getId()));

		CopyBean.copyBean(sou, dest, iC.getLog(), FieldList.BillList);
		CopyHelper.copyID(sou, dest);
		CopyHelper
				.copyRes2Collection(iC, sou, dest, "payments", PaymentP.class);
		CopyHelper.copyRes2Collection(iC, sou, dest, "advancePay",
				AdvancePaymentP.class);
		CopyHelper.copyRes2Collection(iC, sou, dest, "addpayments",
				AddPaymentP.class);
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

	static void copyAddPayment(final ICommandContext iC, final Bill dest,
			HotelT ho, List<AddPaymentP> col) {
		BillP sou = new BillP();
		sou.setAddpayments(col);
		final CopyBeanToP.ICopyHelper copylist = getAddPayment(ho);
		CopyBeanToP.copyRes1Collection(iC, sou, dest, "addpayments", "bill",
				Bill.class, copylist, true);
	}
}
