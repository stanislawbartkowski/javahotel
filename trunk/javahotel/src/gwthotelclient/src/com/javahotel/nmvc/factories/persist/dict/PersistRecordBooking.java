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
package com.javahotel.nmvc.factories.persist.dict;

import com.javahotel.client.IResLocator;
import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.types.LId;

/**
 * @author hotel
 * 
 */
class PersistRecordBooking extends AbstractPersistWithCust {


    PersistRecordBooking(final IResLocator rI, boolean validate) {
        super(rI,DictType.BookingList,validate);
    }


    @Override
    void setCustId(AbstractTo a, LId custId) {
        BookingP p = (BookingP) a;
        p.setCustomer(custId);
        
    }

}
