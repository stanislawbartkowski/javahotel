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

import com.gwtmodel.table.PersistTypeEnum;
import com.javahotel.client.GWTGetService;
import com.javahotel.client.types.DataUtil;
import com.javahotel.client.types.HModelData;
import com.javahotel.common.toobject.HotelP;

class PersistHotel extends APersonHotelPersist {

    private final boolean validate;

    PersistHotel(boolean validate) {
        this.validate = validate;
    }

    @Override
    public void persist(PersistTypeEnum persistTypeEnum, HModelData h,
            IPersistResult iRes) {
        HotelP ho = (HotelP) h.getA();
        CallBack ca = new CallBack(iRes, persistTypeEnum, ho);
        if (validate) {
            GWTGetService.getService().validatePersistHotel(
                    DataUtil.persistTo(persistTypeEnum), ho, ca);
            return;
        }
        switch (persistTypeEnum) {
        case ADD:
        case MODIF:
            GWTGetService.getService().addHotel(ho, ca);
            break;
        case REMOVE:
            GWTGetService.getService().removeHotel(ho, ca);
            break;
        }

    }

}
