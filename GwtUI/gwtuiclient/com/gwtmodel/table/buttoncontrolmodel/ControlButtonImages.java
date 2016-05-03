/*
 * Copyright 2016 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.buttoncontrolmodel;

import com.gwtmodel.table.factories.IGetCustomValues;
import com.gwtmodel.table.slotmodel.ClickButtonType;

class ControlButtonImages {

    static String getImageName(IGetCustomValues cValues,
            ClickButtonType.StandClickEnum actionId) {
        switch (actionId) {
        case FIND:
            return cValues.getCustomValue(IGetCustomValues.FINDIMAGE);
        case FILTRLIST:
            return cValues.getCustomValue(IGetCustomValues.FILTRIMAGE);
        case ADDITEM:
            return cValues.getCustomValue(IGetCustomValues.ADDITEMIMAGE);
        case MODIFITEM:
            return cValues.getCustomValue(IGetCustomValues.MODIFITEMIMAGE);
        case REMOVEITEM:
            return cValues.getCustomValue(IGetCustomValues.REMOVEITEMIMAGE);
        case SHOWITEM:
            return cValues.getCustomValue(IGetCustomValues.SHOWITEMIMAGE);
        case TABLEDEFAULTMENU:
            return cValues.getCustomValue(IGetCustomValues.PROPIMAGE);
        default:
            break;
        }
        return null;
    }
}
