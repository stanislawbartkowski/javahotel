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

import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.factories.ITableCustomFactories;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author perseus
 */
class ComboBoxField extends GetValueLB {

    private final List<ComboVal> wy;

    private void init() {
        List<String> val = new ArrayList<String>();
        for (ComboVal va : wy) {
            val.add(va.getDispVal());
        }
        setList(val);
    }

    ComboBoxField(ITableCustomFactories tFactories, IVField v, List<ComboVal> wy, boolean addEmpty) {
        super(tFactories, v, addEmpty);
        this.wy = wy;
        init();
    }

    ComboBoxField(ITableCustomFactories tFactories, IVField v, List<ComboVal> wy) {
        super(tFactories, v);
        this.wy = wy;
        init();
    }

    @Override
    public void setValObj(Object o) {
        String val = FUtils.getValueOS(o, v);
        String di = null;
        if (val != null) {
            for (ComboVal vv : wy) {
                if (vv.eqS(val)) {
                    di = vv.getDispVal();
                }
            }
        }
        super.setValObj(di);

    }
}
