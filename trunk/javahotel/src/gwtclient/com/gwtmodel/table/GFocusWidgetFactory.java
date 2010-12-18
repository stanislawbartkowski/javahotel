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
import com.google.gwt.user.client.ui.FocusWidget;

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
    }

    public static IGFocusWidget construct(FocusWidget w) {
        return new F(w);
    }
}
