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
package com.jython.ui.client;

import com.gwtmodel.table.IVField;
import com.gwtmodel.table.factories.IGetCustomValues;

/**
 * @author hotel
 * 
 */
class CustomFactory implements IGetCustomValues {

    @Override
    public IVField getSymForCombo() {
        return null;
    }

    @Override
    public boolean compareComboByInt() {
        return false;
    }

    @Override
    public String getCustomValue(String key) {
        if (key.equals(IGetCustomValues.DATEFORMAT)) {
            return "yyyy/MM/dd";
        }
        if (key.equals(IGetCustomValues.RESOURCEFOLDER)) {
            return "res";
        }
        if (key.equals(IGetCustomValues.IMAGEFOLDER)) {
            return "img";
        }
        if (key.equals(IGetCustomValues.IMAGEFORLISTHELP)) {
            return "arrow-down-default.gif";
        }
        return null;
    }

    @Override
    public boolean addEmptyAsDefault() {
        return false;
    }
}
