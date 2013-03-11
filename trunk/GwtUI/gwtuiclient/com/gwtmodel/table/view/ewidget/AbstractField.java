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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.AbstractListT;
import com.gwtmodel.table.AbstractListT.IGetList;
import com.gwtmodel.table.IMapEntry;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.factories.IGetCustomValues;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.rdef.IFormChangeListener;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.rdef.ITouchListener;
import com.gwtmodel.table.view.util.PopupTip;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@SuppressWarnings("deprecation")
abstract class AbstractField extends PopupTip implements IFormLineView {

    protected List<ITouchListener> iTouch;
    protected Collection<IFormChangeListener> lC;
    protected final boolean isCheckBox;
    protected final boolean checkBoxVal;
    protected final IGetCustomValues cValues;
    protected final IVField v;
    private final String htmlName;
    protected final AbstractListT listT;

    AbstractField(IGetCustomValues cValues, IVField v, String htmlName) {
        this(cValues, v, true, htmlName);
        iTouch = new ArrayList<ITouchListener>();
    }

    @Override
    public IVField getV() {
        return v;
    }

    AbstractField(IGetCustomValues cValues, final IVField v,
            boolean checkenable, String htmlName) {
        assert v != null : LogT.getT().cannotBeNull();
        this.cValues = cValues;
        iTouch = null;
        lC = null;
        checkBoxVal = checkenable;
        isCheckBox = true;
        this.htmlName = htmlName;
        this.v = v;
        if (v.getType().getLi() == null) {
            listT = null;
        } else {
            IGetList iGet = new IGetList() {
                public List<IMapEntry> getL() {
                    return v.getType().getLi().getList();
                }
            };
            listT = new AbstractListT(iGet) {
            };
        }
    }

    @Override
    public int getChooseResult() {
        return NOCHOOSECHECK;
    }

    public void setChooseCheck(boolean enable) {
    }

    @Override
    public void setGStyleName(final String sName, final boolean set) {
        if (set) {
            getGWidget().setStyleName(sName);
        } else {
            getGWidget().removeStyleName(sName);
        }
    }

    protected class L implements ChangeListener {

        @Override
        public void onChange(Widget sender) {
            runOnChange(AbstractField.this, true);
        }
    }

    @SuppressWarnings("rawtypes")
    protected class ValueChange implements ValueChangeHandler {

        @Override
        public void onValueChange(ValueChangeEvent event) {
            runOnChange(AbstractField.this, true);
        }
    }

    protected class Touch implements KeyboardListener {

        private final ITouchListener iTouch;

        Touch(final ITouchListener iTouch) {
            this.iTouch = iTouch;
        }

        @Override
        public void onKeyDown(Widget sender, char keyCode, int modifiers) {
            if (iTouch != null) {
                iTouch.onTouch();
            }

        }

        @Override
        public void onKeyPress(Widget sender, char keyCode, int modifiers) {
            if (iTouch != null) {
                iTouch.onTouch();
            }
        }

        @Override
        public void onKeyUp(Widget sender, char keyCode, int modifiers) {
            if (iTouch != null) {
                iTouch.onTouch();
            }
        }
    }

    @Override
    public void setOnTouch(final ITouchListener iTouch) {
        this.iTouch.add(iTouch);
    }

    @Override
    public void addChangeListener(final IFormChangeListener l) {
        if (lC == null) {
            lC = new ArrayList<IFormChangeListener>();
        }
        lC.add(l);
    }

    protected void runOnChange(IFormLineView i, boolean afterFocus) {
        if (lC == null) {
            return;
        }
        for (IFormChangeListener c : lC) {
            c.onChange(i, afterFocus);
        }
    }

    @Override
    public void setInvalidMess(String errmess) {
        setMessage(errmess);
    }

    @Override
    public Widget getGWidget() {
        return this;
    }

    @Override
    public void setHidden(boolean hidden) {
        this.getGWidget().setVisible(!hidden);
    }

    @Override
    public boolean isHidden() {
        return !this.getGWidget().isVisible();
    }

    protected void onTouch() {
        for (ITouchListener t : iTouch) {
            t.onTouch();
        }
    }

    @Override
    public String getHtmlName() {
        if (!CUtil.EmptyS(htmlName)) {
            return htmlName;
        }
        return v.getId();
    }
}
