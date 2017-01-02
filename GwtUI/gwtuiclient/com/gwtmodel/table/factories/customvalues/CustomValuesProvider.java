/*
 * Copyright 2017 stanislawbartkowski@gmail.com  
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
package com.gwtmodel.table.factories.customvalues;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.gwtmodel.table.IVField;
import com.gwtmodel.table.factories.IGetCustomValues;
import com.gwtmodel.table.factories.ITableCustomFactories;

public class CustomValuesProvider implements IGetCustomValues {

    private final ITableCustomFactories iFactories;
    private final Map<String, String> cMap = new HashMap<String, String>();

    @Inject
    public CustomValuesProvider(ITableCustomFactories iFactories) {
        this.iFactories = iFactories;
        cMap.put(IGetCustomValues.FILTRIMAGE, "default_filtericon.png");
        cMap.put(IGetCustomValues.FINDIMAGE, "default_findicon.png");
        cMap.put(IGetCustomValues.ADDITEMIMAGE, "default_addicon.png");
        cMap.put(IGetCustomValues.MODIFITEMIMAGE, "default_modificon.png");
        cMap.put(IGetCustomValues.REMOVEITEMIMAGE, "default_removeicon.png");
        cMap.put(IGetCustomValues.SHOWITEMIMAGE, "default_showicon.png");
        cMap.put(IGetCustomValues.PROPIMAGE, "default_toolicon.png");
        cMap.put(IGetCustomValues.DATEFORMAT, "yyyy/MM/dd");
        cMap.put(IGetCustomValues.DATETIMEFORMAT, "yyyy/MM/dd HH:mm:ss");
        cMap.put(IGetCustomValues.IMAGEFOLDER, "img");
        cMap.put(IGetCustomValues.RESOURCEFOLDER, "res");
        cMap.put(IGetCustomValues.IMAGEFORLISTHELP, "default_helpicon");
        cMap.put(IGetCustomValues.CLOSEIMAGE, "default_closeicon");
        cMap.put(IGetCustomValues.EXPANDIMAGE, "default_expandicon");
        cMap.put(IGetCustomValues.YESVALUE, "Y");
        cMap.put(IGetCustomValues.NOVALUE, "N");
        cMap.put(IGetCustomValues.JCOOKIEPREFIX, "JYTHONCOOKIE-");
        cMap.put(IGetCustomValues.HTMLPANELADDID, IGetCustomValues.VALUEYES);
    }

    @Override
    public IVField getSymForCombo() {
        IGetCustomValues c = iFactories.getGetCustomValuesNotDefault();
        if (c == null)
            return null;
        return c.getSymForCombo();
    }

    @Override
    public String getCustomValue(String key) {
        String val = null;
        IGetCustomValues c = iFactories.getGetCustomValuesNotDefault();
        if (c != null)
            val = c.getCustomValue(key);
        if (val != null)
            return val;
        // default values
        return cMap.get(key);
    }

    @Override
    public boolean compareComboByInt() {
        IGetCustomValues c = iFactories.getGetCustomValuesNotDefault();
        if (c == null)
            return false;
        return c.addEmptyAsDefault();
    }

    @Override
    public boolean addEmptyAsDefault() {
        IGetCustomValues c = iFactories.getGetCustomValuesNotDefault();
        if (c == null) {
            return false;
        }
        return c.addEmptyAsDefault();
    }

    @Override
    public String getStandMessage(String key) {
        IGetCustomValues c = iFactories.getGetCustomValuesNotDefault();
        if (c == null)
            return null;
        return c.getStandMessage(key);
    }

}
