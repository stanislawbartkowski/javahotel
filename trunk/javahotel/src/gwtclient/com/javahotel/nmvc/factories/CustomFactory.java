/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
package com.javahotel.nmvc.factories;

import java.util.HashMap;
import java.util.Map;

import com.gwtmodel.table.IVField;
import com.gwtmodel.table.factories.IGetCustomValues;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.nmvc.common.VField;

class CustomFactory implements IGetCustomValues {
    
    private Map<String,String> ma = new HashMap<String,String>();
    
    CustomFactory() {
        ma.put(IGetCustomValues.IMAGEFOLDER,"img");
        ma.put(IGetCustomValues.RESOURCEFOLDER,"res");
        
    }

    public IVField getSymForCombo() {
        return new VField(DictionaryP.F.name);
    }

    public boolean compareComboByInt() {
        return false;
    }

    public String getCustomValue(String key) {
        return ma.get(key);
    }

}
