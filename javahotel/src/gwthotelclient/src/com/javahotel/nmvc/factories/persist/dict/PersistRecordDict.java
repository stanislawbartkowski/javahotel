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
package com.javahotel.nmvc.factories.persist.dict;

import com.gwtmodel.table.view.callback.CommonCallBack;
import com.javahotel.client.GWTGetService;
import com.javahotel.client.IResLocator;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.DictionaryP;

/**
 * Class for persisting 'dictionary' object
 * 
 * @author stanislawbartkowski@gmail.com
 */
class PersistRecordDict extends APersistRecordDict {

    PersistRecordDict(final IResLocator rI, final DictType d, boolean validate) {
        super(rI, d, validate);
    }

    @Override
    protected void persistDict(DictType d, DictionaryP dP,
            CommonCallBack<ReturnPersist> b) {
        if (d == DictType.BookingList) {
            // in case of booking use different service
            BookingP boo = (BookingP) dP;
            GWTGetService.getService().persistResBookingReturn(boo, b);
        } else {
            GWTGetService.getService().persistDict(d, dP, b);
        }
    }

}