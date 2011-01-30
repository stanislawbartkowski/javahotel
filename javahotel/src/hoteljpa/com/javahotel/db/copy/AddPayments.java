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
package com.javahotel.db.copy;

import com.javahotel.common.toobject.AddPaymentP;
import com.javahotel.common.toobject.BillP;
import com.javahotel.db.context.ICommandContext;
import com.javahotel.db.hotelbase.jpa.Bill;
import com.javahotel.db.hotelbase.jpa.Booking;
import com.javahotel.db.util.CommonHelper;
import com.javahotel.dbjpa.copybean.CopyBean;
import com.javahotel.remoteinterfaces.HotelT;
import java.util.List;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class AddPayments {

	private AddPayments() {
	}

	public static String addPayment(final ICommandContext iC, final Booking b,
			final BillP bill, final List<AddPaymentP> col) {
		Bill bi = CommonHelper.getName(b.getBill(), bill.getName());
		HotelT ho = new HotelT(iC.getHotel());
		if (bi == null) {
			bi = new Bill();
			CopyBean.copyBean(bill, bi, iC.getLog(), FieldList.BillList,
					new String[] { "id" });
			bi.setBooking(b);
			CopyBooking.copyBill(iC, ho, b.getSeason(), bill, bi);
			b.getBill().add(bi);
		} else {
			CopyBean.copyBean(bill, bi, iC.getLog(), FieldList.BillList,
					new String[] { "id" });
		}
		CopyBooking.copyAddPayment(iC, bi, ho, col);
		return bi.getName();
	}
}
