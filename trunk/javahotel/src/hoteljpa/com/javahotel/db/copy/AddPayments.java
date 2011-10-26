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

import java.util.List;

import com.javahotel.common.toobject.AddPaymentP;
import com.javahotel.db.context.ICommandContext;
import com.javahotel.db.hotelbase.jpa.Booking;
import com.javahotel.remoteinterfaces.HotelT;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class AddPayments {

	private AddPayments() {
	}

	public static void addPayment(final ICommandContext iC, final Booking b,
			final List<AddPaymentP> col) {
		HotelT ho = new HotelT(iC.getHotel());
		CopyBooking.copyAddPayment(iC, b, ho, col);
	}
}
