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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.common.DateFormatUtil;
import com.gwtmodel.table.factories.ITableCustomFactories;
import com.gwtmodel.table.rdef.IFormChangeListener;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.rdef.ITouchListener;
import com.gwtmodel.table.view.util.PopupTip;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
abstract class AbstractField extends PopupTip implements IFormLineView {

    protected ITouchListener iTouch;
    protected Collection<IFormChangeListener> lC;
    protected final boolean isCheckBox;
    protected final boolean checkBoxVal;
    protected final ITableCustomFactories tFactories;

    AbstractField(ITableCustomFactories tFactories) {
        this.tFactories = tFactories;
        iTouch = null;
        lC = null;
        checkBoxVal = false;
        isCheckBox = true;
    }

    AbstractField(ITableCustomFactories tFactories, boolean checkenable) {
        this.tFactories = tFactories;
        iTouch = null;
        lC = null;
        checkBoxVal = checkenable;
        isCheckBox = true;
    }

    public int getChooseResult() {
        return NOCHOOSECHECK;
    }

    public void setChooseCheck(boolean enable) {
    }

    public boolean emptyF() {
        String val = getVal();
        return ((val == null) || val.equals(""));
    }

    public void setStyleName(final String sName, final boolean set) {
        if (set) {
            getGWidget().setStyleName(sName);
        } else {
            getGWidget().removeStyleName(sName);
        }
    }

    protected class L implements ChangeListener {

        public void onChange(Widget sender) {
            runOnChange(AbstractField.this);
        }
    }

    protected class ValueChange implements ValueChangeHandler {

        public void onValueChange(ValueChangeEvent event) {
            runOnChange(AbstractField.this);
        }
    }

    protected class Touch implements KeyboardListener {

        private final ITouchListener iTouch;

        Touch(final ITouchListener iTouch) {
            this.iTouch = iTouch;
        }

        public void onKeyDown(Widget sender, char keyCode, int modifiers) {
            iTouch.onTouch();

        }

        public void onKeyPress(Widget sender, char keyCode, int modifiers) {
            iTouch.onTouch();
        }

        public void onKeyUp(Widget sender, char keyCode, int modifiers) {
            iTouch.onTouch();
        }
    }

    public void setOnTouch(final ITouchListener iTouch) {
        this.iTouch = iTouch;
    }

    public int getIntVal() {
        String n = getVal();
        int no = Utils.getNum(n);
        return no;
    }

    public BigDecimal getDecimal() {
        return Utils.toBig(getVal());
    }

    public Date getDate() {
        String n = getVal();
        if (n == null) {
            return null;
        }
        return DateFormatUtil.toD(n);
    }

    public void setDecimal(BigDecimal b) {
        if (b == null) {
            setVal("");
            return;
        }
        setVal(Utils.DecimalToS(b));
    }

    public void addChangeListener(final IFormChangeListener l) {
        if (lC == null) {
            lC = new ArrayList<IFormChangeListener>();
        }
        lC.add(l);
    }

    protected void runOnChange(IFormLineView i) {
        if (lC == null) {
            return;
        }
        for (IFormChangeListener c : lC) {
            c.onChange(i);
        }
    }

    public void setInvalidMess(String errmess) {
        setMessage(errmess);
    }

    public Widget getGWidget() {
        return this;
    }
}
