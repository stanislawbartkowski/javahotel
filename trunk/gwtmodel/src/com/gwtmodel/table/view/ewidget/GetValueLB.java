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

import java.util.List;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.factories.ITableCustomFactories;
import com.gwtmodel.table.rdef.IFormChangeListener;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@SuppressWarnings("deprecation")
class GetValueLB extends AbstractField implements IValueLB {

    final protected ListBox lB = new ListBox();
    private String beforeVal = null;

    GetValueLB(ITableCustomFactories tFactories, IVField v) {
        super(tFactories,v);
        initWidget(lB);
        setMouse();

    }

    @Override
    public Object getValObj() {
        int i = lB.getSelectedIndex();
        String s;
        if (i == -1) {
// CHANGE: 2010/09/22
//            return null;
            s = beforeVal;
        } else {
          s = lB.getItemText(i);
        }
        return FUtils.getValue(v, s);
    }

    private void setEmpty() {
        if (lB.getItemCount() == 0) {
            return;
        }
        lB.setItemSelected(0, true);
    }

    private void setV(final String s) {
        if (s == null) {
            setEmpty();
            return;
        }
        if (lB.getItemCount() == 0) {
            beforeVal = s;
            return;
        }
        for (int i = 0; i < lB.getItemCount(); i++) {
            String v = lB.getItemText(i);
            if (v.equals(s)) {
                lB.setSelectedIndex(i);
                return;
            }
        }
        setEmpty();
    }

    @Override
    public void setValObj(Object o) {
        setV((String) o);
        runOnChange(this);
    }

    @Override
    public void setReadOnly(final boolean readOnly) {
        lB.setEnabled(!readOnly);
    }

    public boolean validateField() {
        return true;
    }

    @Override
    public void addChangeListener(final IFormChangeListener l) {
        super.addChangeListener(l);
        ChangeListener le = new ChangeListener() {

            @Override
            public void onChange(Widget sender) {
                runOnChange(GetValueLB.this);
            }
        };
        lB.addChangeListener(le);
        //
    }

    /**
     * @return the beforeVal
     */
    @Override
    public String getBeforeVal() {
        return beforeVal;
    }

    @Override
    public void setList(List<String> li) {
        lB.clear();
        for (String s : li) {
            lB.addItem(s);
        }
    }

    @Override
    public Widget getGWidget() {
        return this;
    }
}
