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
package com.javahotel.client.types;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gwtmodel.table.FieldDataType;
import com.gwtmodel.table.IVField;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.IField;

/**
 * @author hotel
 *
 */
public class CreateList {
    
    private CreateList() {        
    }
    
    public static List<IVField> getF(AbstractTo a) {
        List<IVField> eList = new ArrayList<IVField>();
        for (int i = 0; i < a.getT().length; i++) {
            IField fie = a.getT()[i];
            Class<?> c = a.getT(fie);
            FieldDataType t;
            if (c == Date.class) {
                t = FieldDataType.constructDate();
            } else {
                t = FieldDataType.constructString();
            }
            eList.add(new VField(fie, t));
        }
        return eList;
    }



}
