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

import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.OfferServicePriceP;
import com.javahotel.common.toobject.OfferSpecialPriceP;
import com.javahotel.db.context.ICommandContext;
import com.javahotel.db.hotelbase.jpa.OfferPrice;
import com.javahotel.db.hotelbase.jpa.OfferServicePrice;
import com.javahotel.db.hotelbase.jpa.OfferSpecialPrice;
import com.javahotel.db.hotelbase.jpa.ServiceDictionary;
import com.javahotel.dbjpa.copybean.CopyBean;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class CopyPriceList {


	static void copyRes1(final ICommandContext iC, final OfferPriceP sou,
			final OfferPrice dest) {
		CopyBean.copyBean(sou, dest, iC.getLog(), FieldList.PriceListOfferList);
		dest.setHotel(iC.getRHotel());

		final CopyBeanToP.ICopyHelper eqspec = new CopyBeanToP.ICopyHelper() {

			public boolean eq(Object o1, Object o2) {
				OfferSpecialPriceP oo1 = (OfferSpecialPriceP) o1;
				OfferSpecialPrice oo2 = (OfferSpecialPrice) o2;
				long l1 = oo1.getSpecialperiod();
				long l2 = oo2.getSpecialperiod();
				return (l1 == l2);
			}

			public Object getI(Object o) {
				return new OfferSpecialPrice();
			}

			public void copy(final ICommandContext iC, final Object sou,
					final Object dest) {
				CopyBean.copyBean(sou, dest, iC.getLog(),
						FieldList.PriceListSpecialOfferList);
			}
		};

		CopyBeanToP.ICopyHelper eqservice = new CopyBeanToP.ICopyHelper() {

			public boolean eq(Object o1, Object o2) {
				OfferServicePriceP oo1 = (OfferServicePriceP) o1;
				OfferServicePrice oo2 = (OfferServicePrice) o2;
				String serv1 = oo1.getService();
				String serv2 = oo2.getService().getName();
				return serv1.equals(serv2);
			}

			public Object getI(Object se) {
				return new OfferServicePrice();
			}

			public void copy(final ICommandContext iC, final Object sou,
					final Object dest) {
				OfferServicePriceP sou1 = (OfferServicePriceP) sou;
				OfferServicePrice dest1 = (OfferServicePrice) dest;
				CopyBean.copyBean(sou1, dest1, iC.getLog(),
						FieldList.PriceServiceListOffer);
				CopyBeanToP.copyRes1Collection(iC, sou1, dest1, "specialprice",
						"priceid", OfferServicePrice.class, eqspec, false);
				ServiceDictionary se1 = (ServiceDictionary) iC.getC().get(
						ServiceDictionary.class, sou1.getService());
				dest1.setService(se1);
			}
		};

		CopyBeanToP.copyRes1Collection(iC, sou, dest, "serviceprice",
				"offerid", OfferPrice.class, eqservice, false);
	}

	static void copyRes2(final ICommandContext iC, final OfferPrice sou,
			final OfferPriceP dest) {
		CopyBean.copyBean(sou, dest, iC.getLog(), FieldList.PriceListOfferList);
		CopyHelper.copyRes2Collection(iC, sou, dest, "serviceprice",
				OfferServicePriceP.class);
		dest.setHotel(iC.getHotel());
	}

	static void copyRes2(final ICommandContext iC, final OfferServicePrice sou,
			final OfferServicePriceP dest) {
		CopyBean.copyBean(sou, dest, iC.getLog(),
				FieldList.PriceServiceListOffer);
		CopyHelper.copyRes2Collection(iC, sou, dest, "specialprice",
				OfferSpecialPriceP.class);
		dest.setService(sou.getService().getName());
	}
}
