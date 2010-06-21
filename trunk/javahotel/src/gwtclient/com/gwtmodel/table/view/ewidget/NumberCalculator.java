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
package com.gwtmodel.table.view.ewidget;

import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.factories.ITableCustomFactories;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class NumberCalculator extends ExtendTextBox {

    private final int afterdot;

    NumberCalculator(ITableCustomFactories tFactories, int afterdot) {
        super(tFactories, false, false);
        this.afterdot = afterdot;
        hPanel.addStyleName("Number");
    }

    @Override
    public void setVal(String v) {
        if (CUtil.EmptyS(v)) {
            return;
        }
        if (!CUtil.OkNumber(v)) {
            return;
        }
        String s = CUtil.toAfterS(v, afterdot);
        super.setVal(s);
    }

    @Override
    public String getVal() {
        String s = super.getVal();
        if (!CUtil.OkNumber(s)) {
            return "";
        }
        return CUtil.toAfterS(s, afterdot);
    }
}
