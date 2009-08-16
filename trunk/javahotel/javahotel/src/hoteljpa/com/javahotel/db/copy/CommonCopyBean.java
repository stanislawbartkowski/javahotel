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
package com.javahotel.db.copy;

import java.util.ArrayList;
import java.util.Collection;

import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.AddPaymentP;
import com.javahotel.common.toobject.AdvancePaymentP;
import com.javahotel.common.toobject.BankAccountP;
import com.javahotel.common.toobject.BillP;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookRecordP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.BookingStateP;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.GuestP;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.OfferSeasonP;
import com.javahotel.common.toobject.OfferSeasonPeriodP;
import com.javahotel.common.toobject.OfferServicePriceP;
import com.javahotel.common.toobject.PaymentP;
import com.javahotel.common.toobject.PaymentRowP;
import com.javahotel.common.toobject.PhoneNumberP;
import com.javahotel.common.toobject.RemarkP;
import com.javahotel.common.toobject.ResObjectP;
import com.javahotel.common.toobject.RoomStandardP;
import com.javahotel.common.toobject.ServiceDictionaryP;
import com.javahotel.common.toobject.VatDictionaryP;
import com.javahotel.db.context.ICommandContext;
import com.javahotel.db.hotelbase.jpa.AddPayment;
import com.javahotel.db.hotelbase.jpa.AdvancePayment;
import com.javahotel.db.hotelbase.jpa.Bill;
import com.javahotel.db.hotelbase.jpa.BookElem;
import com.javahotel.db.hotelbase.jpa.BookRecord;
import com.javahotel.db.hotelbase.jpa.Booking;
import com.javahotel.db.hotelbase.jpa.BookingState;
import com.javahotel.db.hotelbase.jpa.Customer;
import com.javahotel.db.hotelbase.jpa.CustomerBankAccount;
import com.javahotel.db.hotelbase.jpa.CustomerPhoneNumber;
import com.javahotel.db.hotelbase.jpa.CustomerRemark;
import com.javahotel.db.hotelbase.jpa.Guest;
import com.javahotel.db.hotelbase.jpa.OfferPrice;
import com.javahotel.db.hotelbase.jpa.OfferSeason;
import com.javahotel.db.hotelbase.jpa.OfferSeasonPeriod;
import com.javahotel.db.hotelbase.jpa.OfferServicePrice;
import com.javahotel.db.hotelbase.jpa.OfferSpecialPrice;
import com.javahotel.db.hotelbase.jpa.Payment;
import com.javahotel.db.hotelbase.jpa.PaymentRow;
import com.javahotel.db.hotelbase.jpa.ResObject;
import com.javahotel.db.hotelbase.jpa.RoomFacilities;
import com.javahotel.db.hotelbase.jpa.RoomStandard;
import com.javahotel.db.hotelbase.jpa.ServiceDictionary;
import com.javahotel.db.hotelbase.jpa.VatDictionary;
import com.javahotel.db.hotelbase.types.IHotelDictionary;
import com.javahotel.db.jtypes.ToLD;
import com.javahotel.dbjpa.copybean.CopyBean;
import com.javahotel.dbres.messid.IMessId;
import com.javahotel.types.LId;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class CommonCopyBean {

	private interface IAddFields {

		void addFields(boolean add, final Object sou, final Object dest);
	}

	private static <T> T persistObject(final ICommandContext iC,
			final Class<?> cla, final DictionaryP sou, final String[] fields,
			final IAddFields i) {
		String na = sou.getName();
		IHotelDictionary rs = (IHotelDictionary) iC.getC().get(cla, na);
		if (rs == null) {
			rs = (IHotelDictionary) CopyBean.createI(cla, iC.getLog());
			CopyHelper.copyDict1(iC, sou, rs, fields);
			if (i != null) {
				i.addFields(true, sou, rs);
			}
			// iC.getJpa().addRecord(rs);
			iC.getC().put(cla, na, rs, true);

			String logs = iC.getRecordDescr(null, rs);
			String logm = iC.logEvent(IMessId.ADDDICTRECORD, logs);
			iC.getLog().getL().info(logm);

		} else {
			if (i != null) {
				i.addFields(false, sou, rs);
			}
		}
		return (T) rs;
	}

	static private void copyRes1(final ICommandContext iC,
			final ResObjectP sou, final ResObject dest) {
		CopyBeanToP.ICopyHelper eq = new CopyBeanToP.ICopyDicHelper() {

			public Object getI(final Object o) {
				DictionaryP se = (DictionaryP) o;
				se.setHotel(sou.getHotel());
				RoomFacilities afa = persistObject(iC, RoomFacilities.class,
						se, FieldList.DictList, null);
				return afa;
			}

			public void copy(final ICommandContext iC, final Object sou,
					final Object dest) {
			}
		};
		CopyBeanToP.copyRes1(iC, sou, dest, FieldList.ObjectList, "facilities",
				null, null, eq, false);

		DictionaryP r = sou.getRStandard();
		if (r != null) {
			r.setHotel(sou.getHotel());
			RoomStandard rs = persistObject(iC, RoomStandard.class, r,
					FieldList.DictList, null);
			dest.setRStandard(rs);
		}

	}

	static private class AddServI implements IAddFields {

		private final ICommandContext iC;

		AddServI(final ICommandContext iC) {
			this.iC = iC;
		}

		public void addFields(final boolean add, final Object sou,
				final Object dest) {
			ServiceDictionaryP s = (ServiceDictionaryP) sou;
			ServiceDictionary d = (ServiceDictionary) dest;
			VatDictionaryP v = s.getVat();
			if (v == null) {
				d.setVat(null);
				iC.logFatal(IMessId.VATNOTEFINED);
				return;
			}
			v.setHotel(s.getHotel());
			VatDictionary rs = persistObject(iC, VatDictionary.class, s
					.getVat(), FieldList.DictVatList, null);
			d.setVat(rs);
		}
	}

	static private void copyRes1(final ICommandContext iC,
			final RoomStandardP sou, final RoomStandard dest) {
		final IAddFields ia = new AddServI(iC);
		CopyBeanToP.ICopyHelper eq = new CopyBeanToP.ICopyDicHelper() {

			public Object getI(final Object o) {
				ServiceDictionaryP se = (ServiceDictionaryP) o;
				ServiceDictionaryP se1 = (ServiceDictionaryP) iC.getC().get(
						ServiceDictionaryP.class, se.getName());
				if (se1 != null) {
					LId id = se1.getId();
					se.setId(id);
				}
				se.setHotel(sou.getHotel());
				ServiceDictionary sed = persistObject(iC,
						ServiceDictionary.class, se, FieldList.DictServiceList,
						ia);
				String logs = iC.getRecordDescr(DictType.ServiceDict, sed);
				String logm = iC.logEvent(IMessId.ADDDICTRECORD, logs);
				iC.getLog().getL().info(logm);
				return sed;
			}

			public void copy(final ICommandContext iC, final Object sou,
					final Object dest) {
			}
		};
		CopyBeanToP.copyRes1(iC, sou, dest, FieldList.DictList, "services",
				null, null, eq, false);
	}

	static private void copyRes1(final ICommandContext iC,
			final ServiceDictionaryP sou, final ServiceDictionary dest) {
		IAddFields ia = new AddServI(iC);
		CopyHelper.copyDict1(iC, sou, dest, FieldList.DictServiceList);
		ia.addFields(true, sou, dest);
	}

	static private void copyRes1(final ICommandContext iC,
			final OfferSeasonP sou, final OfferSeason dest) {
		CopyBeanToP.ICopyHelper eq = new CopyBeanToP.ICopyHelper() {

			public boolean eq(final Object o1, final Object o2) {
				OfferSeasonPeriodP oo1 = (OfferSeasonPeriodP) o1;
				OfferSeasonPeriod oo2 = (OfferSeasonPeriod) o2;
				long l1 = oo1.getPId().longValue();
				long l2 = oo2.getPId().longValue();
				return (l1 == l2);
			}

			public Object getI(final Object o) {
				return new OfferSeasonPeriod();
			}

			public void copy(final ICommandContext iC, final Object sou,
					final Object dest) {
				CopyBean.copyBean(sou, dest, iC.getLog(),
						FieldList.DictSeasonOfferPeriodList);
			}
		};
		CopyBeanToP.copyRes1(iC, sou, dest, FieldList.DictSeasonOfferList,
				"periods", "offerid", OfferSeason.class, eq, true);
	}

	static private void copyRes1(final ICommandContext iC,
			final OfferPriceP sou, final OfferPrice dest) {
		CopyPriceList.copyRes1(iC, sou, dest);
	}

	static private void copyRes2(final ICommandContext iC,
			final OfferPrice sou, final OfferPriceP dest) {
		CopyPriceList.copyRes2(iC, sou, dest);

	}

	static private void copyRes2(final ICommandContext iC, final ResObject sou,
			final ResObjectP dest) {
		CopyBean.copyBean(sou, dest, iC.getLog(), FieldList.ObjectList);
		RoomStandard rs = sou.getRStandard();
		DictionaryP r = new DictionaryP();
		if (rs != null) {
			CopyHelper.copyDict2(iC, rs, r, FieldList.DictList);
			dest.setRStandard(r);
		}

		Collection<RoomFacilities> fa = sou.getFacilities();
		if (fa == null) {
			return;
		}

		Collection<DictionaryP> fac = new ArrayList<DictionaryP>();
		for (RoomFacilities s : fa) {
			DictionaryP afa = new DictionaryP();
			CopyHelper.copyDict2(iC, s, afa, FieldList.DictList);
			fac.add(afa);
		}

		dest.setFacilities(fac);
		CopyHelper.copyHotel(sou, dest);
	}

	static private void copyRes2(final ICommandContext iC,
			final VatDictionary sou, final VatDictionaryP dest) {
		CopyHelper.copyDict2(iC, sou, dest, FieldList.DictVatList);
	}

	static private void copyRes2(final ICommandContext iC,
			final ServiceDictionary sou, final ServiceDictionaryP dest) {
		CopyHelper.copyDict2(iC, sou, dest, FieldList.DictServiceList);
		CopyHelper.copyHotel(sou, dest);
		if (sou.getVat() == null) {
			dest.setVat(null);
			return;
		}

		VatDictionaryP v = new VatDictionaryP();
		copyRes2(iC, sou.getVat(), v);
		dest.setVat(v);
	}

	static private void copyRes2(final ICommandContext iC,
			final RoomStandard sou, final RoomStandardP dest) {
		CopyHelper.copyRes2(iC, sou, dest, FieldList.DictList, "services",
				ServiceDictionaryP.class);
	}

	static private void copyRes2(final ICommandContext iC,
			final OfferSeason sou, final OfferSeasonP dest) {
		CopyHelper.copyRes2(iC, sou, dest, FieldList.DictSeasonOfferList,
				"periods", OfferSeasonPeriodP.class);
	}

	static private void copyRes2(final ICommandContext iC,
			final OfferSeasonPeriodP sou, final OfferSeasonPeriod dest) {
		CopyBean.copyBean(sou, dest, iC.getLog(),
				FieldList.DictSeasonOfferPeriodList);
	}

	static private void copyRes2(final ICommandContext iC,
			final OfferServicePriceP sou, final OfferServicePrice dest) {
		CopyBean.copyBean(sou, dest, iC.getLog(),
				FieldList.DictSeasonOfferPeriodList);
	}

	static private void copyRes2(final ICommandContext iC, PaymentRow sou,
			PaymentRowP dest) {
		CopyBean.copyBean(sou, dest, iC.getLog(), FieldList.PaymentRowList);
		dest.setId(ToLD.toLId(sou.getId()));
	}

	public static void copyB(final ICommandContext iC, final Object sou,
			final Object dest) {

		if (sou instanceof BookingStateP) {
			BookingStateP sou1 = (BookingStateP) sou;
			BookingState dest1 = (BookingState) dest;
			CopyHelper.copyBeanINumerable(iC, sou1, dest1, FieldList.StateList);
			return;
		}

		if (sou instanceof PaymentP) {
			PaymentP sou1 = (PaymentP) sou;
			Payment dest1 = (Payment) dest;
			CopyHelper.copyBeanINumerable(iC, sou1, dest1,
					FieldList.PaymentList);
			return;
		}

		if (sou instanceof CustomerP) {
			CopyCustomer.copy1(iC, (CustomerP) sou, (Customer) dest);
			return;
		}

		if (sou instanceof BookingP) {
			CopyBooking.copy1(iC, (BookingP) sou, (Booking) dest);
			return;
		}

		if (sou instanceof OfferPriceP) {
			copyRes1(iC, (OfferPriceP) sou, (OfferPrice) dest);
			return;
		}

		if (sou instanceof ResObjectP) {
			copyRes1(iC, (ResObjectP) sou, (ResObject) dest);
			return;
		}

		if (sou instanceof RoomStandardP) {
			copyRes1(iC, (RoomStandardP) sou, (RoomStandard) dest);
			return;
		}

		if (sou instanceof ServiceDictionaryP) {
			copyRes1(iC, (ServiceDictionaryP) sou, (ServiceDictionary) dest);
			return;
		}

		if (sou instanceof VatDictionaryP) {
			CopyHelper.copyDict1(iC, (DictionaryP) sou,
					(IHotelDictionary) dest, FieldList.DictVatList);
			return;
		}

		if (sou instanceof OfferSeasonP) {
			copyRes1(iC, (OfferSeasonP) sou, (OfferSeason) dest);
			return;
		}

		if (sou instanceof OfferServicePriceP) {
			copyRes2(iC, (OfferServicePriceP) sou, (OfferServicePrice) dest);
			return;
		}

		if (sou instanceof OfferSeasonPeriodP) {
			copyRes2(iC, (OfferSeasonPeriodP) sou, (OfferSeasonPeriod) dest);
			return;
		}

		if (sou instanceof DictionaryP) {
			CopyHelper.copyDict1(iC, (DictionaryP) sou,
					(IHotelDictionary) dest, FieldList.DictList);
			return;
		}

		if (sou instanceof Booking) {
			CopyBooking.copy2(iC, (Booking) sou, (BookingP) dest);
			return;
		}

		if (sou instanceof OfferPrice) {
			copyRes2(iC, (OfferPrice) sou, (OfferPriceP) dest);
			return;
		}

		if (sou instanceof OfferSeason) {
			copyRes2(iC, (OfferSeason) sou, (OfferSeasonP) dest);
			return;
		}

		if (sou instanceof ResObject) {
			copyRes2(iC, (ResObject) sou, (ResObjectP) dest);
			return;
		}

		if (sou instanceof RoomStandard) {
			copyRes2(iC, (RoomStandard) sou, (RoomStandardP) dest);
			return;
		}

		if (sou instanceof ServiceDictionary) {
			copyRes2(iC, (ServiceDictionary) sou, (ServiceDictionaryP) dest);
			return;
		}

		if (sou instanceof VatDictionary) {
			copyRes2(iC, (VatDictionary) sou, (VatDictionaryP) dest);
			return;
		}

		if (sou instanceof OfferSeasonPeriod) {
			CopyBean.copyBean(sou, dest, iC.getLog(),
					FieldList.DictSeasonOfferPeriodList);
			return;
		}

		if (sou instanceof Customer) {
			CopyCustomer.copy2(iC, (Customer) sou, (CustomerP) dest);
			return;
		}

		if (sou instanceof OfferSpecialPrice) {
			CopyBean.copyBean(sou, dest, iC.getLog(),
					FieldList.PriceListSpecialOfferList);
			return;
		}

		if (sou instanceof OfferServicePrice) {
			CopyPriceList.copyRes2(iC, (OfferServicePrice) sou,
					(OfferServicePriceP) dest);
			return;
		}

		if (sou instanceof CustomerRemark) {
			CustomerRemark sou1 = (CustomerRemark) sou;
			RemarkP dest1 = (RemarkP) dest;
			CopyHelper.copyBeanIId(iC, sou1, dest1, FieldList.RemarkList);
			return;
		}

		if (sou instanceof CustomerPhoneNumber) {
			CustomerPhoneNumber sou1 = (CustomerPhoneNumber) sou;
			PhoneNumberP dest1 = (PhoneNumberP) dest;
			CopyHelper.copyBeanIId(iC, sou1, dest1, FieldList.PhoneList);
			return;
		}

		if (sou instanceof CustomerBankAccount) {
			CustomerBankAccount sou1 = (CustomerBankAccount) sou;
			BankAccountP dest1 = (BankAccountP) dest;
			CopyHelper.copyBeanIId(iC, sou1, dest1, FieldList.BankList);
			return;
		}

		if (sou instanceof Payment) {
			Payment sou1 = (Payment) sou;
			PaymentP dest1 = (PaymentP) dest;
			CopyHelper.copyBeanINumerable(iC, sou1, dest1,
					FieldList.PaymentList);
			return;
		}

		if (sou instanceof AdvancePayment) {
			AdvancePayment sou1 = (AdvancePayment) sou;
			AdvancePaymentP dest1 = (AdvancePaymentP) dest;
			CopyHelper.copyBeanINumerable(iC, sou1, dest1,
					FieldList.ValidationList);
			return;
		}

		if (sou instanceof BookingState) {
			BookingState sou1 = (BookingState) sou;
			BookingStateP dest1 = (BookingStateP) dest;
			CopyHelper.copyBeanINumerable(iC, sou1, dest1, FieldList.StateList);
			return;
		}

		if (sou instanceof Bill) {
			CopyBooking.copy2(iC, (Bill) sou, (BillP) dest);
			return;
		}

		if (sou instanceof BookRecord) {
			CopyBooking.copy2(iC, (BookRecord) sou, (BookRecordP) dest);
			return;
		}

		if (sou instanceof BookElem) {
			CopyBooking.copy2(iC, (BookElem) sou, (BookElemP) dest);
			return;
		}

		if (sou instanceof PaymentRow) {
			// CopyBean.copyBean(sou, dest, iC.getLog(),
			// FieldList.PaymentRowList);
			copyRes2(iC, (PaymentRow) sou, (PaymentRowP) dest);
			return;
		}

		if (sou instanceof Guest) {
			CopyBooking.copy2(iC, (Guest) sou, (GuestP) dest);
			return;
		}

		if (sou instanceof AddPayment) {
			AddPayment sou1 = (AddPayment) sou;
			AddPaymentP dest1 = (AddPaymentP) dest;
			CopyBean.copyBean(sou, dest, iC.getLog(), FieldList.AddPayList);
			dest1.setRService(new ServiceDictionaryP());
			copyB(iC, sou1.getRService(), dest1.getRService());
			return;
		}

		if (sou instanceof IHotelDictionary) {
			CopyHelper.copyDict2(iC, (IHotelDictionary) sou,
					(DictionaryP) dest, FieldList.DictList);
			return;
		}
		// exception !!!
	}
}
