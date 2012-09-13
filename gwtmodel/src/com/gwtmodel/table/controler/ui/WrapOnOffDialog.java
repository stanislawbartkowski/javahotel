/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.controler.ui;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.view.util.PopupUtil;

/**
 * @author hotel
 * 
 */
public class WrapOnOffDialog {

    private WrapOnOffDialog() {

    }

    public interface IChangeWrap {
        void action(boolean Wrap);
    }

    private static class BChange implements
            ValueChangeHandler<java.lang.Boolean> {

        private final IChangeWrap l;

        BChange(final IChangeWrap l) {
            this.l = l;
        }

        @Override
        public void onValueChange(ValueChangeEvent<Boolean> event) {
            l.action(event.getValue().booleanValue());
        }
    }

    static public void doDialog(WSize w, boolean wrap, IChangeWrap iwrap) {

        WrapOnOff pUp = new WrapOnOff();
        pUp.checkWrap.setChecked(wrap);
        pUp.checkWrap.addValueChangeHandler(new BChange(iwrap));
        PopupUtil.setPos(pUp, w);
        pUp.show();
    }

}
