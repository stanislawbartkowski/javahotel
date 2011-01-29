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
package com.javahotel.nmvc.factories;

import java.util.HashMap;
import java.util.Map;

import com.gwtmodel.table.IVField;
import com.gwtmodel.table.factories.IGetCustomValues;
import com.javahotel.client.IResLocator;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.nmvc.common.VField;

class CustomFactory implements IGetCustomValues {

    private Map<String, String> ma = new HashMap<String, String>();

    CustomFactory(IResLocator sI) {
        ma.put(IGetCustomValues.IMAGEFOLDER, "img");
        ma.put(IGetCustomValues.RESOURCEFOLDER, "res");
        ma.put(IGetCustomValues.COMMERROR, sI.getLabels().commError());
        ma.put(IGetCustomValues.QUESTION, sI.getLabels().Question());
        ma.put(IGetCustomValues.LOGINBUTTON, sI.getLabels().LoginButton());
        ma.put(IGetCustomValues.LOGINMAME, sI.getLabels().LoginName());
        ma.put(IGetCustomValues.PASSWORD, sI.getLabels().Password());
        ma.put(IGetCustomValues.ADDITEM, sI.getLabels().DodajButton());
        ma.put(IGetCustomValues.REMOVEITEM, sI.getLabels().UsunButton());
//        ma.put(IGetCustomValues.EMPTYFIELDERRORDEFAULT, sI.getLabels().EmptyField());
        ma.put(IGetCustomValues.MODIFITEM, sI.getLabels().ModifItem());
        ma.put(IGetCustomValues.SHOWITEM, sI.getLabels().ShowItem());
        ma.put(IGetCustomValues.YESVALUE, sI.getLabels().Yes());
        ma.put(IGetCustomValues.NOVALUE, sI.getLabels().No());
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
}
