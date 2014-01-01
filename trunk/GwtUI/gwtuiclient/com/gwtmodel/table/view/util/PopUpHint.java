/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.view.util;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.injector.LogT;

/**
 * @author hotel
 * 
 */
public class PopUpHint {

    private PopupPanel tup = null;
    /** Widget to be drawn. */
    private Widget message = null;

    private void hideUp() {
        if (tup != null) {
            tup.hide();
            tup = null;
        }
    }

    /**
     * Hide PopUp
     */
    public void delMessage() {
        message = null;
        hideUp();
    }

    /**
     * Set Widget to be displayed
     * 
     * @param message
     *            Widget
     */
    public void setMessage(Widget message) {
        assert message != null : LogT.getT().cannotBeNull();
        this.message = message;
    }

    /**
     * Set message to be displayed
     * 
     * @param message
     *            String
     */
    public void setMessage(String message) {
        if (message == null) {
            this.message = null;
            hideUp();
            return;
        }
        setMessage(new Label(message));
    }

    public void actionOver(final WSize w) {
        if (message != null) {
            tup = PopUpTip.getPopupTip(message);
            PopupUtil.setPos(tup, w);
            tup.show();
        }
    }

    public void actionOut() {
        hideUp();
    }

}
