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
import com.gwtmodel.table.factories.ITableCustomFactories;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author perseus
 */
class ComboBoxField extends GetValueLB {

    private final List<ComboVal> wy;

    ComboBoxField(ITableCustomFactories tFactories, List<ComboVal> wy) {
        super(tFactories);
        this.wy = wy;
        List<String> val = new ArrayList<String>();
        for (ComboVal v : wy) {
            val.add(v.getDispVal());
        }
        setList(val);
    }

    @Override
    public void setVal(String val) {
        String di = null;
        for (ComboVal v : wy) {
            if (v.eqS(val)) {
                di = v.getDispVal();
            }
        }
        super.setVal(di);

    }

    @Override
    public String getVal() {
        String vv = super.getVal();
        for (ComboVal v : wy) {
            if (CUtil.EqNS(vv, v.getDispVal())) {
                return v.getVal();
            }
        }
        return null;
    }
}
