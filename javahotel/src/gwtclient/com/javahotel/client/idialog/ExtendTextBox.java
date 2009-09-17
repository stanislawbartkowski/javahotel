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
package com.javahotel.client.idialog;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.IResLocator;
import com.javahotel.client.ifield.IChangeListener;
import com.javahotel.client.ifield.ISetWidget;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@SuppressWarnings("deprecation")
abstract class ExtendTextBox extends ELineDialog {

    protected final TextBox tBox;
    protected final HorizontalPanel hPanel = new HorizontalPanel();
    protected final CheckBox check;

    protected ExtendTextBox(final IResLocator pLoc, final boolean password) {
        super(pLoc);
        if (password) {
            tBox = new PasswordTextBox();
        } else {
            tBox = new TextBox();
        }
        check = null;
        hPanel.add(tBox);
        initWidget(hPanel);
        setMouse();
    }

    protected ExtendTextBox(final IResLocator pLoc, final boolean password,
            boolean checkEnable) {
        super(pLoc, checkEnable);
        if (password) {
            tBox = new PasswordTextBox();
        } else {
            tBox = new TextBox();
        }
        check = new CheckBox("Auto");
        check.setChecked(checkEnable);
        check.addClickListener(new ChangeC());
        hPanel.add(check);
        hPanel.add(tBox);
        changeS();
        initWidget(hPanel);
        setMouse();
    }

    private void changeS() {
        if (check.isChecked()) {
            tBox.setEnabled(false);
        } else {
            tBox.setEnabled(true);
        }
    }

    private class ChangeC implements ClickListener {

        public void onClick(Widget sender) {
            if (check.isChecked()) {
                tBox.setText("");
            }
            changeS();
        }
    }

    @Override
    public int getChooseResult() {
        if (check == null) {
            return NOCHOOSECHECK;
        }
        if (check.isChecked()) {
            return CHOOSECHECKTRUE;
        } else {
            return CHOOSECHECKFALSE;
        }
    }

    @Override
    public void setKLi(final IKeyboardAction kLi) {
        super.setKLi(kLi);
        tBox.addKeyboardListener(kLi.getListener());
    }

    public void setVal(final String v) {
        if (kLi != null) {
            kLi.delEmptyStyle();
        }
        tBox.setText(v);
        if (lC != null) {
            lC.onChange(null);
        }
    }

    protected void setValAndFireChange(final String v) {
        setVal(v);
        if (lC != null) {
            lC.onChange(this);
        }
    }

    public void refresh() {
    }

    public String getVal() {
        return tBox.getText();
    }

    public void setReadOnly(final boolean r) {
        tBox.setReadOnly(r);
        if (check != null) {
            check.setEnabled(!r);
        }
    }

    public boolean validateField() {
        return true;
    }

    @Override
    public void setWidget(final ISetWidget i) {
        i.setW(tBox);
    }

    private class L implements ChangeListener {

        public void onChange(Widget sender) {
            lC.onChange(ExtendTextBox.this);
        }
    }

    public void setChangeListener(final IChangeListener l) {
        lC = l;
        tBox.addChangeListener(new L());
    }
}
