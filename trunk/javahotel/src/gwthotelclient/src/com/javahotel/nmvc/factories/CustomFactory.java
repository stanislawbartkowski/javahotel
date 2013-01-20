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
package com.javahotel.nmvc.factories;

import java.util.HashMap;
import java.util.Map;

import com.gwtmodel.table.IVField;
import com.gwtmodel.table.factories.IGetCustomValues;
import com.javahotel.client.IResLocator;
import com.javahotel.client.M;
import com.javahotel.client.types.VField;
import com.javahotel.common.toobject.DictionaryP;

class CustomFactory implements IGetCustomValues {

    private Map<String, String> ma = new HashMap<String, String>();

    CustomFactory() {
        ma.put(IGetCustomValues.IMAGEFOLDER, "img");
        ma.put(IGetCustomValues.RESOURCEFOLDER, "res");
        ma.put(IGetCustomValues.COMMERROR, M.L().commError());
        ma.put(IGetCustomValues.QUESTION, M.L().Question());
        ma.put(IGetCustomValues.LOGINBUTTON, M.L().LoginButton());
        ma.put(IGetCustomValues.LOGINMAME, M.L().LoginName());
        ma.put(IGetCustomValues.PASSWORD, M.L().Password());
        ma.put(IGetCustomValues.ADDITEM, M.L().DodajButton());
        ma.put(IGetCustomValues.REMOVEITEM, M.L().UsunButton());
        ma.put(IGetCustomValues.MODIFITEM, M.L().ModifItem());
        ma.put(IGetCustomValues.SHOWITEM, M.L().ShowItem());
        ma.put(IGetCustomValues.YESVALUE, M.L().Yes());
        ma.put(IGetCustomValues.NOVALUE, M.L().No());
        ma.put(IGetCustomValues.DATEFORMAT,M.L().DataFormat());
    }

    @Override
    public IVField getSymForCombo() {
        return new VField(DictionaryP.F.name);
    }

    @Override
    public boolean compareComboByInt() {
        return false;
    }

    @Override
    public String getCustomValue(String key) {
        return ma.get(key);
    }

    @Override
    public boolean addEmptyAsDefault() {
        return false;
    }
}