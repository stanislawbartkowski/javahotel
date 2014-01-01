/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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

import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.IConsts;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.factories.IGetCustomValues;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class NumberCalculator extends ExtendTextBox {

    NumberCalculator(IGetCustomValues cValues, IVField v,
            ExtendTextBox.EParam p, String htmlName) {
        super(cValues, v, p, htmlName);
        wW.addStyleName(IConsts.numberStyle);
    }

    @Override
    public void setValObj(Object o) {
        String s = FUtils.getValueOS(o, v);
        super.setValObj(s);
    }

    @Override
    public Object getValObj() {
        String s = (String) super.getValObj();
        return FUtils.getValue(v, s);
    }
}
