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
package com.javahotel.client.mvc.dictcrud.controler.bookelemlist;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.javahotel.client.mvc.auxabstract.NumAbstractTo;
import com.javahotel.client.mvc.table.model.ITableModel;
import com.javahotel.common.math.MathUtil;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.PaymentRowP;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class SumPayment {

    private SumPayment() {
    }

    static class SumRes {

        BigDecimal sumCustomer;
        BigDecimal sumOffer;

        SumRes() {
            sumCustomer = MathUtil.getO();
            sumOffer = MathUtil.getO();
        }

        void addBig(BigDecimal bC, BigDecimal bO) {
            if (bC != null) {
                sumCustomer = MathUtil.addB(sumCustomer, bC);
            }
            if (bO != null) {
                sumOffer = MathUtil.addB(sumOffer, bO);
            }
        }
    }

    @SuppressWarnings("unchecked")
    static SumRes sum(final ITableModel model) {

        ArrayList<NumAbstractTo> c = (ArrayList<NumAbstractTo>) model.getList();
        SumRes sumR = new SumRes();
        for (NumAbstractTo n : c) {
            BookElemP b = (BookElemP) n.getO();
            List<PaymentRowP> p = b.getPaymentrows();
            for (PaymentRowP ro : p) {
                sumR.addBig(ro.getCustomerPrice(), ro.getOfferPrice());
            }
        }
        return sumR;
    }
}
