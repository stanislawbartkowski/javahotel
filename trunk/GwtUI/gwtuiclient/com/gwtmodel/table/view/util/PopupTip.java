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

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.WSize;

/**
 * Simple class for implementing PopUp widget while is moving on
 * 
 * @author hotel
 * 
 */
public abstract class PopupTip extends Composite {

    private final PopUpHint pHint = new PopUpHint();

    protected PopupTip() {
        setMouse();
    }

    public void delMessage() {
        pHint.delMessage();
    }

    public void setMessage(Widget message) {
        pHint.setMessage(message);
    }

    public void setMessage(String message) {
        pHint.setMessage(message);
    }

    private class MouseO implements MouseOverHandler, MouseOutHandler {

        @Override
        public void onMouseOver(MouseOverEvent event) {
            pHint.actionOver(new WSize(PopupTip.this));
        }

        @Override
        public void onMouseOut(MouseOutEvent event) {
            pHint.actionOut();
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
