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
package com.javahotel.client.idialog;

import java.util.List;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.IResLocator;
import com.javahotel.client.idialogutil.IValueLB;
import com.javahotel.client.ifield.IChangeListener;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
@SuppressWarnings("deprecation")
class GetValueLB extends ELineDialog implements IValueLB {

    final protected ListBox lB = new ListBox();
    private String beforeVal = null;

    GetValueLB(final IResLocator li) {
        super(li);
        initWidget(lB);
        setMouse();

    }

    public String getVal() {
        int i = lB.getSelectedIndex();
        if (i == -1) {
            return null;
        }
        return lB.getItemText(i);
    }

    private void setV(final String s) {
        if (s == null) {
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
    }

    public void setVal(final String s) {
        setV(s);
        runOnChange(this);
//        if (lC != null) {
//            lC.onChange(this);
//        }
    }

    public void refresh() {
    }

    public void setReadOnly(final boolean readOnly) {
        lB.setEnabled(!readOnly);
    }

    public boolean validateField() {
        return true;
    }

    @Override
    public int getIntVal() {
        return -1;
    }

    public void setChangeListener(final IChangeListener l) {
//        lC = l;
        super.setChangeListener(l);
        ChangeListener le = new ChangeListener() {

            public void onChange(Widget sender) {
//                lC.onChange(GetValueLB.this);
                runOnChange(GetValueLB.this);
            }
        };
        lB.addChangeListener(le);
        //
//        runOnChange(this);
    }

    /**
     * @return the beforeVal
     */
    public String getBeforeVal() {
        return beforeVal;
    }

    public void setList(List<String> li) {
        lB.clear();
        for (String s : li) {
            lB.addItem(s);
        }
    }
}
