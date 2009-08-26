/*
 * Copyright 2009 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.abstractto;

import com.javahotel.client.dialog.DictData;
import com.javahotel.client.mvc.auxabstract.ABillsCustomer;
import com.javahotel.client.mvc.auxabstract.ANumAbstractTo;
import com.javahotel.client.mvc.auxabstract.BillsCustomer;
import com.javahotel.client.mvc.auxabstract.LoginRecord;
import com.javahotel.client.mvc.auxabstract.NumAddPaymentP;
import com.javahotel.client.mvc.auxabstract.PaymentStateModel;
import com.javahotel.client.mvc.auxabstract.PeOfferNumTo;
import com.javahotel.client.mvc.auxabstract.ResRoomGuest;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.AddPaymentP;
import com.javahotel.common.toobject.AdvancePaymentP;
import com.javahotel.common.toobject.BankAccountP;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookRecordP;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.HotelP;
import com.javahotel.common.toobject.PhoneNumberP;
import com.javahotel.common.util.AbstractObjectFactory;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class AbstractToFactory {

	private AbstractToFactory() {
	}

	public static AbstractTo getA(final DictData da) {
		AbstractTo aa = null;
		if (da.getD() != null) {
			aa = AbstractObjectFactory.getAbstract(da.getD());
		} else if (da.getRt() != null) {
			switch (da.getRt()) {
			case AllPersons:
				aa = new LoginRecord();
				break;
			case AllHotels:
				aa = new HotelP();
				break;
			default:
				assert false : "Unknown RType";
			}
		} else {
			switch (da.getSE()) {
			case SpecialPeriod:
				aa = new PeOfferNumTo();
				break;
			case CustomerPhone:
				PhoneNumberP ph = new PhoneNumberP();
				ANumAbstractTo<?> an = new ANumAbstractTo(ph, ph.getT());
				aa = an;
				break;
			case CustomerAccount:
				BankAccountP ba = new BankAccountP();
				ANumAbstractTo<?> ab = new ANumAbstractTo(ba, ba.getT());
				aa = ab;
				break;
			case BookingElem:
				BookElemP pe = new BookElemP();
				ANumAbstractTo<?> at = new ANumAbstractTo(pe, pe.getT());
				aa = at;
				break;
			case BookingHeader:
				BookRecordP br = new BookRecordP();
				aa = br;
				break;
			case ValidationHeader:
				AdvancePaymentP av = new AdvancePaymentP();
				aa = av;
				break;
			case AddPayment:
				PaymentStateModel ps = new PaymentStateModel();
				aa = ps;
				break;
			case ResGuestList:
				ResRoomGuest rg = new ResRoomGuest();
				aa = rg;
				break;
			case BillsList:
				BillsCustomer bi = new BillsCustomer(new DictionaryP(),
						new CustomerP());
				ABillsCustomer aC = new ABillsCustomer(bi);
				aa = aC;
				break;
			case AddPaymentList:
				AddPaymentP ap = new AddPaymentP();
				NumAddPaymentP att = new NumAddPaymentP(ap);
				aa = att;
				break;
			default:
				assert false : da.getSE() + " unknown new record type";
			}
		}
		return aa;
	}
}