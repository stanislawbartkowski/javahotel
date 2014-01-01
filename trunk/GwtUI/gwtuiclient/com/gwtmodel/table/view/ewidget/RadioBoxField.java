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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.factories.IGetCustomValues;

/**
 * 
 * @author perseus
 */
class RadioBoxField extends AbstractField {

    private final VerticalPanel vP;
    private final List<RadioButton> ra;

    RadioBoxField(IGetCustomValues cValues, IVField v, String htmlName) {
        super(cValues, v, htmlName);
        vP = new VerticalPanel();
        ra = new ArrayList<RadioButton>();
        for (String s : listT.getListVal()) {
            RadioButton r = new RadioButton(getHtmlName(), s);
            r.setName(s) ;
            vP.add(r);
            ra.add(r);
        }
        initWidget(vP);
    }

    @Override
    public void setValObj(Object o) {
        String ke = FUtils.getValueOS(o, v);
        String va = listT.getValueS(ke);
        int no = 0;
        boolean found = false;
        for (String s : listT.getListVal()) {
            if (CUtil.EqNS(s, va)) {
                found = true;
                break;
            }
            no++;
        }
        if (!found) {
            return;
        }
        RadioButton r = ra.get(no);
        r.setValue(true);
    }

    @Override
    public Object getValObj() {
        String s = null;
        int no = 0;
        for (RadioButton r : ra) {
            if (r.getValue()) {
                s = listT.getListVal().get(no);
            }
            no++;
        }
        if (s == null) {
            return null;
        }
        String key = listT.getValue(s);
        return FUtils.getValue(v, key);
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        for (RadioButton r : ra) {
            r.setEnabled(!readOnly);
        }
    }
}
