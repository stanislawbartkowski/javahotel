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
package com.javahotel.view.gwt.record.view;

import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.idialog.IKeyboardAction;

class DefaultListener implements IKeyboardAction {

    private final ELineStore eStore;

    DefaultListener(ELineStore eStore) {
        this.eStore = eStore;
    }

    public KeyboardListener getListener() {
        return new KeyboardListener() {

            public void onKeyDown(final Widget arg0, final char arg1,
                    final int arg2) {
                delEmptyStyle();
            }

            public void onKeyPress(final Widget arg0, final char arg1,
                    final int arg2) {
            }

            public void onKeyUp(final Widget arg0, final char arg1,
                    final int arg2) {
            }
        };
    }

    public void delEmptyStyle() {
        eStore.clearE();
    }
}
