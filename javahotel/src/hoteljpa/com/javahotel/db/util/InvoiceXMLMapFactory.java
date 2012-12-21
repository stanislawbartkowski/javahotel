/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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
package com.javahotel.db.util;

import com.gwtmodel.mapxml.SimpleXMLTypeFactory;
import com.gwtmodel.table.common.CUtil;
import com.javahotel.common.command.PaymentMethod;
import com.javahotel.common.command.PersonTitle;

/**
 * @author hotel
 * 
 */
public class InvoiceXMLMapFactory extends SimpleXMLTypeFactory {

    public final static String PAYMENT = "pay";
    public final static String TITLE = "title";

    @Override
    public Object contruct(String xType, String s) {
        if (!CUtil.EmptyS(xType)) {
            if (xType.equals(PAYMENT)) {
                return PaymentMethod.valueOf(s);
            }
            if (xType.equals(TITLE)) {
                return PersonTitle.valueOf(s);
            }
        }
        return super.contruct(xType, s);
    }

    @Override
    public String toS(String xType, Object o) {
        if (!CUtil.EmptyS(xType)) {
            if (xType.equals(PAYMENT)) {
                PaymentMethod pa = (PaymentMethod) o;
                return pa.toString();
            }
            if (xType.equals(TITLE)) {
                PersonTitle pa = (PersonTitle) o;
                return pa.toString();
            }
        }
        return super.toS(xType, o);
    }

}
