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
package com.javahotel.client.abstractto.impl;

import com.javahotel.client.IResLocator;
import com.javahotel.client.abstractto.IAbstractFactory;
import com.javahotel.client.abstractto.LoginRecord;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.types.DataType;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.AdvancePaymentP;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookRecordP;
import com.javahotel.common.toobject.GuestP;
import com.javahotel.common.toobject.HotelP;
import com.javahotel.common.toobject.PaymentRowP;
import com.javahotel.common.util.AbstractObjectFactory;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class AbstractToFactory implements IAbstractFactory {

    public AbstractTo construct(final DataType da) {
        IResLocator rI;
        if (da.isAddType()) {
            switch (da.getAddType()) {
            case BookRecord:
                return new BookRecordP();
            case AdvanceHeader:
                return new AdvancePaymentP();
            case BookElem:
                return new BookElemP();
            case RowPaymentElem:
                return new PaymentRowP();
            case GuestElem:
                return new GuestP();
            }
            rI = HInjector.getI().getI();
            assert false : rI.getMessages().NotSupportedError(
                    da.getAddType().name());
            return null;

        }

        if (da.isDictType()) {
            return AbstractObjectFactory.getAbstract(da.getdType());
        }
        if (da.isRType()) {
            switch (da.getrType()) {
            case AllPersons:
                return new LoginRecord();
            case AllHotels:
                return new HotelP();
            }
            rI = HInjector.getI().getI();
            assert false : rI.getMessages().NotSupportedError(
                    da.getrType().name());
            return null;
        }
        return null;
    }
}
