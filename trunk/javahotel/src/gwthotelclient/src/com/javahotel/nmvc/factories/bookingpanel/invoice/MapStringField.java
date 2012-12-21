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
package com.javahotel.nmvc.factories.bookingpanel.invoice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gwtmodel.table.FieldDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.VSField;
import com.javahotel.client.abstractto.IAbstractType;
import com.javahotel.client.injector.HInjector;

/**
 * @author hotel
 * 
 */
abstract class MapStringField {

    enum SType {
        STRING, INT, DATE, PAYMENTMETHOD, AMOUNT, TITLE;
    }

    abstract SType GetType(String s);

    private final Map<String, IVField> mapFie = new HashMap<String, IVField>();
    private final IAbstractType factoryType;

    MapStringField() {
        factoryType = HInjector.getI().getAbstractType();
    }

    void createMap(List<String> fList) {
        for (String f : fList) {
            IVField v;
            switch (GetType(f)) {
            case DATE:
                v = VSField.createVDate(f);
                break;
            case INT:
                v = VSField.createVInteger(f);
                break;
            case PAYMENTMETHOD:
                v = VSField.createVSField(f,
                        factoryType.contructPaymentMethod());
                break;
            case AMOUNT:
                v = VSField.createVSField(f,
                        FieldDataType.constructBigDecimal());
                break;
            case TITLE:
                v = VSField.createVSField(f, factoryType.contructPersonTitle());
                break;

            default:
                v = VSField.createVString(f);
                break;
            }
            mapFie.put(f, v);
        }
    }

    IVField get(String s) {
        return mapFie.get(s);
    }

}
