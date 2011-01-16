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

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.factories.ITableCustomFactories;
import com.gwtmodel.table.rdef.IFormChangeListener;
import com.gwtmodel.table.rdef.ITouchListener;
import com.gwtmodel.table.view.ewidget.richtextoolbar.RichTextToolbar;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@SuppressWarnings("deprecation")
class ExtendTextBox extends AbstractField {

    private class EWidget implements IGWidget {

        private final TextBoxBase tBox;
        private final RichTextArea rArea;
        private final VerticalPanel vP;

        EWidget(EParam param) {
            if (param.isPassword()) {
                tBox = new PasswordTextBox();
                rArea = null;
                vP = null;
            } else {
                if (param.isIsRich()) {
                    vP = new VerticalPanel();
                    rArea = new RichTextArea();
                    RichTextToolbar r = new RichTextToolbar(rArea);
                    rArea.setWidth("100%");
                    vP.add(r);
                    vP.add(rArea);
                    tBox = null;
                } else {
                    tBox = param.isArea() ? new TextArea() : new TextBox();
                    rArea = null;
                    vP = null;
                }
            }
        }

        public Widget getGWidget() {
            if (rArea == null) {
                return tBox;
            }
            return vP;
        }

        void setName(String nameId) {
            if (rArea == null) {
                tBox.setName(nameId);
            } else {
                Utils.setId(rArea, nameId);
            }
        }

        void setEnabled(boolean enabled) {
            if (rArea == null) {
                tBox.setEnabled(enabled);
            } else {
                rArea.setEnabled(enabled);
            }
        }

        void setText(String t) {
            if (rArea == null) {
                tBox.setText(t);
            } else {
                rArea.setText(t);
            }
        }

        void addKeyboardListener(KeyboardListener k) {
            if (rArea == null) {
                tBox.addKeyboardListener(k);
            } else {
                rArea.addKeyboardListener(k);
            }
        }

        String getText() {
            if (rArea == null) {
                return tBox.getText();
            } else {
                return rArea.getText();
            }
        }

        void setReadOnly(boolean r) {
            if (rArea == null) {
                tBox.setReadOnly(r);
            } else {
                rArea.setEnabled(!r);
            }
        }

        void addChangeListener(ChangeListener c) {
            if (rArea == null) {
                tBox.addChangeListener(c);
            } else {
//                rArea.a
            }

        }
    }

    static class EParam {

        private final boolean password;
        private final boolean area;
        private final boolean panel;
        private final boolean checkBox;
        private final boolean enable;
        private final boolean isRich;

        EParam(boolean password, boolean area, boolean panel,
                boolean checkBox, boolean enable) {
            this.password = password;
            this.panel = panel;
            this.checkBox = checkBox;
            this.area = area;
            this.enable = enable;
            this.isRich = false;
        }

        EParam(boolean checkBox, boolean enable) {
            this.password = false;
            this.panel = false;
            this.checkBox = checkBox;
            this.area = false;
            this.enable = enable;
            this.isRich = true;
        }

        /**
         * @return the password
         */
        public boolean isPassword() {
            return password;
        }

        /**
         * @return the area
         */
        public boolean isArea() {
            return area;
        }

        /**
         * @return the panel
         */
        public boolean isPanel() {
            return panel || checkBox;
        }

        /**
         * @return the checkBox
         */
        public boolean isCheckBox() {
            return checkBox;
        }

        /**
         * @return the enable
         */
        public boolean isEnable() {
            return enable;
        }

        /**
         * @return the isRich
         */
        public boolean isIsRich() {
            return isRich;
        }
    }
    protected final Widget wW;
//    protected final TextBoxBase tBox;
    private final EWidget eW;
    protected final HorizontalPanel hPanel;
    protected final CheckBox check;
    protected final boolean isArea;

    protected ExtendTextBox(ITableCustomFactories tFactories, IVField v, EParam param) {
        super(tFactories, v);
        this.isArea = param.isArea();
//      if (param.isPassword()) {
//            tBox = new PasswordTextBox();
//        } else {
//            tBox = isArea ? new TextArea() : new TextBox();
//        }
        eW = new EWidget(param);
//      tBox.setName(v.getId());
        eW.setName(v.getId());
        if (param.isPanel()) {
            hPanel = new HorizontalPanel();
            hPanel.add(eW.getGWidget());
            wW = hPanel;
        } else {
            hPanel = null;
            wW = eW.getGWidget();
        }
        if (param.isCheckBox()) {
            check = new CheckBox("Auto");
            check.setChecked(param.isEnable());
            check.addClickListener(new ChangeC());
            changeS();

        } else {
            check = null;
        }
        initWidget(wW);
//        setMouse();
    }

    private void changeS() {
        if (check.isChecked()) {
//            tBox.setEnabled(false);
            eW.setEnabled(false);
        } else {
//            tBox.setEnabled(true);
            eW.setEnabled(true);
        }
    }

    private class ChangeC implements ClickListener {

        @Override
        public void onClick(Widget sender) {
            if (check.isChecked()) {
//                tBox.setText("");
                eW.setText("");
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
    public void setOnTouch(final ITouchListener iTouch) {
        super.setOnTouch(iTouch);
//        tBox.addKeyboardListener(new Touch(iTouch));
        eW.addKeyboardListener(new Touch(iTouch));
    }

    @Override
    public void setValObj(Object o) {
        String va = (String) o;
        if (iTouch != null) {
            iTouch.onTouch();
        }
//        tBox.setText(va);
        eW.setText(va);
        runOnChange(null);
    }

    protected void setValAndFireChange(final String v) {
        setValObj(v);
        runOnChange(this);
    }

    @Override
    public Object getValObj() {
//        return tBox.getText();
        return eW.getText();
    }

    @Override
    public void setReadOnly(final boolean r) {
//        tBox.setReadOnly(r);
        eW.setReadOnly(r);
        if (check != null) {
            check.setEnabled(!r);
        }
    }

    public boolean validateField() {
        return true;
    }

    private class L implements ChangeListener {

        @Override
        public void onChange(Widget sender) {
            runOnChange(ExtendTextBox.this);
        }
    }

    @Override
    public void addChangeListener(final IFormChangeListener l) {
        super.addChangeListener(l);
//        tBox.addChangeListener(new L());
        eW.addChangeListener(new L());
    }

    @Override
    public Widget getGWidget() {
        return this;
    }
}
