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
package com.javahotel.client.abstractto;

import com.javahotel.client.PUtil;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.IField;

/**
 * @author hotel
 * 
 */
public class BookElemWithPayment extends BookElemP {

    public enum F implements IField {
        offerPrice, customerPrice
    };
        
    @Override
    public Object getF(IField f) {
        if (isField(f)) {
            return super.getF(f);
        }
        PUtil.SumP sumP = PUtil.getPrice(getPaymentrows());
        F fi = (F) f;
        switch (fi) {
        case offerPrice:
            return sumP.sumOffer;
        case customerPrice:
            return sumP.sumCustomer;
        }
        return null;
    }

    @Override
    public void setF(IField f, Object o) {
        if (isField(f)) {
            super.setF(f, o);
        }        
    }

}
