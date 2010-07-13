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
package com.javahotel.nmvc.common;

import java.math.BigDecimal;
import java.util.Date;

import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.rdef.IFormChangeListener;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.rdef.ITouchListener;
import com.javahotel.client.idialog.IKeyboardAction;
import com.javahotel.client.ifield.IChangeListener;
import com.javahotel.client.ifield.ILineField;

public class FormLineDef implements IFormLineView {

    private final ILineField iField;

    public ILineField getiField() {
        return iField;
    }

    private class DefaultListener implements IKeyboardAction {

        private final ITouchListener lTouch;

        DefaultListener(ITouchListener lTouch) {
            this.lTouch = lTouch;
        }

        public KeyboardListener getListener() {
            return new KeyboardListener() {

                public void onKeyDown(final Widget arg0, final char arg1,
                        final int arg2) {
                    delEmptyStyle();
                }

                public void onKeyPress(final Widget arg0, final char arg1,
                        final int arg2) {
                    delEmptyStyle();
                }

                public void onKeyUp(final Widget arg0, final char arg1,
                        final int arg2) {
                    delEmptyStyle();
                }
            };
        }

        public void delEmptyStyle() {
            lTouch.onTouch();
        }

    }

    public FormLineDef(ILineField f) {
        iField = f;
    }

    private class ChangeL implements IChangeListener {

        private final IFormChangeListener cListener;

        ChangeL(IFormChangeListener cListener) {
            this.cListener = cListener;
        }

        public void onChange(ILineField i) {
            cListener.onChange(FormLineDef.this);
        }

    }

    public void addChangeListener(IFormChangeListener cListener) {
        iField.setChangeListener(new ChangeL(cListener));
    }

    public String getVal() {
        return iField.getVal();
    }

    public void setVal(String s) {
        iField.setVal(s);
    }

    public Widget getGWidget() {
        return iField.getMWidget().getWidget();
    }

    public void setReadOnly(boolean readOnly) {
        iField.setReadOnly(readOnly);

    }

    public void setInvalidMess(String errmess) {
        iField.setErrMess(errmess);

    }

    public void setStyleName(String styleMess, boolean set) {
        iField.setStyleName(styleMess, set);

    }

    public void setOnTouch(ITouchListener lTouch) {
        iField.setKLi(new DefaultListener(lTouch));
    }

    public BigDecimal getDecimal() {
        return iField.getDecimal();
    }

    public void setDecimal(BigDecimal b) {
        iField.setDecimal(b);
    }

    public Date getDate() {
        return iField.getDate();
    }

    public int getIntVal() {
        return iField.getIntVal();
    }

    public int getChooseResult() {
        return iField.getChooseResult();
    }

    public boolean isHidden() {
        // TODO Auto-generated method stub
        return false;
    }

    public void setHidden(boolean hidden) {
        // TODO Auto-generated method stub
        
    }

}
