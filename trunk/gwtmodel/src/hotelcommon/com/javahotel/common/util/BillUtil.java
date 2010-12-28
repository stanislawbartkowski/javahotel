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
package com.javahotel.common.util;

import com.javahotel.common.command.BillEnumTypes;
import com.javahotel.common.toobject.BillP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.PaymentP;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class BillUtil {

    public static final String DOWNSYMBOL = "DownPaymentBill";

    public static BillP createPaymentBill() {
        BillP p = new BillP();
        p.setName(DOWNSYMBOL);
        p.setBillType(BillEnumTypes.MainBill);
        return p;
    }

    public static <T extends DictionaryP> T getName(
            final List<? extends DictionaryP> col, final String name) {
        for (DictionaryP t : col) {
            if (t.getName().equals(name)) {
                return (T) t;
            }
        }
        return null;
    }

    public static BillP getBill(final BookingP p, final String bName) {
        return getName(p.getBill(), bName);
    }

    public static BillP getBill(final BookingP p) {
        return getBill(p, BillUtil.DOWNSYMBOL);
    }
    
    public static List<PaymentP> getPayment(final BookingP p,
            final String bName) {
        return getBill(p,bName).getPayments();        
    }
    
    public static List<PaymentP> getPayment(final BookingP p) {
        return getBill(p).getPayments();        
    }
    
    public static <T> List<T> getEmptyCol(T elem) {
        List<T> col = new ArrayList<T>();
        col.add(elem);
        return col;
    }

}
