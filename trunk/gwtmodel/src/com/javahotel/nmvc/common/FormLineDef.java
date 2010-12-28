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
package com.javahotel.nmvc.common;


import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IVField;
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

    @Override
    public IVField getV() {
        return null;
    }


    private class DefaultListener implements IKeyboardAction {

        private final ITouchListener lTouch;

        DefaultListener(ITouchListener lTouch) {
            this.lTouch = lTouch;
        }

        @Override
        public KeyboardListener getListener() {
            return new KeyboardListener() {

                @Override
                public void onKeyDown(final Widget arg0, final char arg1,
                        final int arg2) {
                    delEmptyStyle();
                }

                @Override
                public void onKeyPress(final Widget arg0, final char arg1,
                        final int arg2) {
                    delEmptyStyle();
                }

                @Override
                public void onKeyUp(final Widget arg0, final char arg1,
                        final int arg2) {
                    delEmptyStyle();
                }
            };
        }

        @Override
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

        @Override
        public void onChange(ILineField i) {
            cListener.onChange(FormLineDef.this);
        }

    }

    @Override
    public void addChangeListener(IFormChangeListener cListener) {
        iField.setChangeListener(new ChangeL(cListener));
    }

    @Override
    public Object getValObj() {
        return iField.getVal();
    }

    @Override
    public void setValObj(Object s) {
        iField.setVal((String) s);
    }

    @Override
    public Widget getGWidget() {
        return iField.getMWidget().getWidget();
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        iField.setReadOnly(readOnly);

    }

    @Override
    public void setInvalidMess(String errmess) {
        iField.setErrMess(errmess);

    }

    @Override
    public void setGStyleName(String styleMess, boolean set) {
        iField.setGStyleName(styleMess, set);

    }

    @Override
    public void setOnTouch(ITouchListener lTouch) {
        iField.setKLi(new DefaultListener(lTouch));
    }



    @Override
    public int getChooseResult() {
        return iField.getChooseResult();
    }

    @Override
    public boolean isHidden() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setHidden(boolean hidden) {
        // TODO Auto-generated method stub
        
    }

}
