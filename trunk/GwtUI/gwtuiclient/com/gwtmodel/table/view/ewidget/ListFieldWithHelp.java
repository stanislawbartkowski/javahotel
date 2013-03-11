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
package com.gwtmodel.table.view.ewidget;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.factories.IGetCustomValues;

class ListFieldWithHelp extends ExtendTextBox {

    private final WidgetWithPopUpTemplate wHelp;
    private final ChooseListHelper cHelper;

    private class RHelp extends ChooseListHelper {

        RHelp(IDataType dType) {
            super(dType);
        }

        @Override
        void asetValue(String sy) {
            setValObj(sy);
        }

        @Override
        void hide() {
            wHelp.hide();
        }
    }

    ListFieldWithHelp(IGetCustomValues cValues, IVField v, IDataType dType,
            ExtendTextBox.EParam p, boolean refreshAlways, String htmlName) {
        super(cValues, v, p, htmlName);
        cHelper = new RHelp(dType);

        wHelp = new WidgetWithPopUpTemplate(v, hPanel,
                cValues.getCustomValue(IGetCustomValues.IMAGEFORLISTHELP),
                cHelper.getI(), refreshAlways);
    }
}
