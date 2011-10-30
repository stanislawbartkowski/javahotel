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
package com.gwtmodel.table.view.util;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.injector.LogT;

/**
 * Simple class for implementing PopUp widget while is moving on
 * 
 * @author hotel
 * 
 */
public abstract class PopupTip extends Composite {

    protected PopupTip() {
        setMouse();
    }

    /** Widget to be drawn. */
    private Widget message = null;

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
            return;
        }
        setMessage(new Label(message));
    }

    private PopupPanel tup = null;

    private void hideUp() {
        if (tup != null) {
            tup.hide();
            tup = null;
        }
    }

    private class MouseO implements MouseOverHandler, MouseOutHandler {

        @Override
        public void onMouseOver(MouseOverEvent event) {
            if (message != null) {
                tup = PopUpTip.getPopupTip(message);
                PopupUtil.setPos(tup, PopupTip.this);
                tup.show();
            }
        }

        @Override
        public void onMouseOut(MouseOutEvent event) {
            hideUp();
        }
    }

    public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
        return addDomHandler(handler, MouseOverEvent.getType());
    }

    public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
        return addDomHandler(handler, MouseOutEvent.getType());
    }

    protected void setMouse() {
        this.addMouseOutHandler(new MouseO());
        this.addMouseOverHandler(new MouseO());
    }
}
