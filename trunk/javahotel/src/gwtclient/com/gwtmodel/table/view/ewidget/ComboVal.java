/*
 * Copyright 2010 stanislawbartkowski@gmail.com
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
import com.gwtmodel.table.factories.IGetCustomValues;
import com.gwtmodel.table.injector.GwtGiniInjector;

/**
 *
 * @author perseus
 */
public class ComboVal {

    private final String val;
    private final String dispVal;
    private final int no;
    private final boolean compareByNumb;

    public ComboVal(String val, String dispVal) {
        this.val = val;
        this.dispVal = dispVal;
        IGetCustomValues c = GwtGiniInjector.getI().getTableFactoriesContainer().getGetCustomValues();
        compareByNumb = c.compareComboByInt();
        if (compareByNumb) {
            no = CUtil.getNumb(val);
        } else {
            no = -1;
        }

    }

    /**
     * @return the val
     */
    public String getVal() {
        return val;
    }

    /**
     * @return the dispVal
     */
    public String getDispVal() {
        return dispVal;
    }

    boolean eqS(String val) {
        if (!compareByNumb) {
            return CUtil.EqNS(this.val, val);
        }
        String s = CUtil.toAfterS(val, 0);
        int n = CUtil.getNumb(s);
        return n == no;
    }
}
