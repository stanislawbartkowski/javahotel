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

import com.gwtmodel.table.IVField;
import com.gwtmodel.table.editc.IRequestForGWidget;
import com.gwtmodel.table.factories.IGetCustomValues;
import com.gwtmodel.table.injector.GwtGiniInjector;

class EditTextFieldWithHelper extends ExtendTextBox {

    private final WidgetWithPopUpTemplate wHelp;

    EditTextFieldWithHelper(IGetCustomValues cValues, IVField v,
            ExtendTextBox.EParam p, IRequestForGWidget i,
            boolean refreshAlways, String htmlName) {
        super(cValues, v, p, htmlName);
        IGetCustomValues c = GwtGiniInjector.getI().getCustomValues();

        wHelp = new WidgetWithPopUpTemplate(v, hPanel,
                c.getCustomValue(IGetCustomValues.IMAGEFORLISTHELP), i,
                refreshAlways, getHtmlName());
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        wHelp.setReadOnly(readOnly);
    }

}
