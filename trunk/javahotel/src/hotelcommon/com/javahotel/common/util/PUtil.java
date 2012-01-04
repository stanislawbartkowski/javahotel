/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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
package com.javahotel.common.util;

import java.math.BigDecimal;
import java.util.List;

import com.javahotel.common.toobject.PaymentRowP;

/**
 * @author hotel
 * 
 */
public class PUtil {

    private PUtil() {

    }

    public static class SumP {
        public BigDecimal sumOffer;
        public BigDecimal sumCustomer;

        private SumP() {
            sumOffer = sumCustomer = null;
        }
    }

    public static void addS(SumP s1, SumP s2) {
        s1.sumCustomer = MUtil.addB(s1.sumCustomer, s2.sumCustomer);
        s1.sumOffer = MUtil.addB(s1.sumOffer, s2.sumOffer);
    }

    public static SumP getPrice(List<PaymentRowP> paymentrows) {
        SumP s = new SumP();
        if (paymentrows == null) {
            return s;
        }
        s.sumCustomer = MUtil.getO();
        s.sumOffer = MUtil.getO();
        for (PaymentRowP p : paymentrows) {
            s.sumCustomer = MUtil.addB(s.sumCustomer, p.getCustomerPrice());
            s.sumOffer = MUtil.addB(s.sumOffer, p.getOfferPrice());
        }
        return s;
    }

}
