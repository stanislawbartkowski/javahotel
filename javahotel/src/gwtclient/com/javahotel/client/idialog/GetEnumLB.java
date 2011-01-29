/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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

import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.IResLocator;
import com.javahotel.client.ifield.IChangeListener;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@SuppressWarnings("deprecation")
class GetEnumLB extends ELineDialog {

    final protected ListBox lB = new ListBox();
    final private Map<String, String> ma;

    GetEnumLB(final IResLocator li, final Map<String, String> ma) {
        super(li);
        this.ma = ma;
        Set<String> se = ma.keySet();
        for (String s : se) {
            String val = ma.get(s);
            lB.addItem(val);
        }
        initWidget(lB);
        setMouse();
    }

    public String getVal() {
        int i = lB.getSelectedIndex();
        if (i == -1) {
            return null;
        }
        String val = lB.getItemText(i);
        Set<String> se = ma.keySet();
        String e = null;
        for (String s : se) {
            String v = ma.get(s);
            if (v.equals(val)) {
                e = s;
                break;
            }
        }
        return e;
    }

    public void setVal(final String s) {
        if (s == null) {
            return;
        }
        String val = (String) ma.get(s);
        for (int i = 0; i < lB.getItemCount(); i++) {
            String v = lB.getItemText(i);
            if (v.equals(val)) {
                lB.setSelectedIndex(i);
//                if (lC != null) {
//                    lC.onChange(this);
//                }
                runOnChange(this);
                return;
            }
        }
    }

    public void refresh() {
    }

    public void setReadOnly(boolean readOnly) {
        lB.setEnabled(!readOnly);
    }

    public boolean validateField() {
        return true;
    }

    private class L implements ChangeListener {

        public void onChange(Widget sender) {
//            lC.onChange(GetEnumLB.this);
            runOnChange(GetEnumLB.this);
        }
    }

    public void setChangeListener(IChangeListener l) {
        super.setChangeListener(l);
        lB.addChangeListener(new L());

    }
}
