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
package com.gwtmodel.table;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.view.util.PopupTip;

/**
 * 
 * @author perseus
 */
public class GFocusWidgetFactory {

    private GFocusWidgetFactory() {
    }

    private static class F extends GWidget implements IGFocusWidget {

        F(FocusWidget f) {
            super(f);
        }

        @Override
        public void addClickHandler(ClickHandler h) {
            FocusWidget f = (FocusWidget) getGWidget();
            f.addClickHandler(h);
        }

        @Override
        public void setEnabled(boolean enabled) {
            FocusWidget f = (FocusWidget) getGWidget();
            f.setEnabled(enabled);
        }

        @Override
        public boolean isEnabled() {
            FocusWidget f = (FocusWidget) getGWidget();
            return f.isEnabled();
        }
    }

    private static class FTip extends PopupTip implements IGFocusWidget {

        FTip(FocusWidget f, String mess) {
            initWidget(f);
            setMessage(mess);
            // setMouse();
        }

        @Override
        public void addClickHandler(ClickHandler h) {
            FocusWidget b = (FocusWidget) this.getWidget();
            b.addClickHandler(h);
        }

        @Override
        public Widget getGWidget() {
            return this;
        }

        @Override
        public void setEnabled(boolean enabled) {
            FocusWidget b = (FocusWidget) this.getWidget();
            b.setEnabled(enabled);
        }

        @Override
        public boolean isEnabled() {
            Button b = (Button) this.getWidget();
            return b.isEnabled();
        }
    }

    public static IGFocusWidget construct(FocusWidget w, String mess) {
        return new FTip(w, mess);
    }

    public static IGFocusWidget construct(FocusWidget w) {
        return new F(w);
    }
}
