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
package com.javahotel.client.mvc.dict.validator;

import com.google.inject.Inject;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.mvc.dict.validator.price.PriceListValidatorService;
import com.javahotel.client.mvc.validator.IRecordValidator;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class DictValidatorFactory {

    private final IResLocator rI;

    @Inject
    public DictValidatorFactory(IResLocator rI) {
        this.rI = rI;
    }

    public IRecordValidator getValidator(final DictData da,
            boolean omitTestEmpty) {

        if (da.getRt() != null) {
            switch (da.getRt()) {
            case AllPersons:
                return new PersonValidator(rI, da);
            case AllHotels:
                return new HotelValidator(rI, da);
            }
        }

        if (da.isSe()) {
            switch (da.getSE()) {
            case ResGuestList:
                return new ResGuestValidator(rI, da);
            case LoginUser:
            case LoginAdmin:
                return new LoginValidator(rI, da);
            default:
                break;
            }
        }

        return new DictValidator(rI, da, omitTestEmpty);
    }

    public IRecordValidator getValidator(final DictData da) {
        return getValidator(da, false);
    }

    public IRecordValidator getPriceValidator() {
        PriceListValidatorService p = HInjector.getI()
                .getPriceListValidatorService();
        return p;
    }
}
