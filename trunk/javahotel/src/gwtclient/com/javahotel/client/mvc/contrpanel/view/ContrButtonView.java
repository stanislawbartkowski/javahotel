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
package com.javahotel.client.mvc.contrpanel.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IGFocusWidget;
import com.gwtmodel.table.view.button.ImgButtonFactory;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DefaultMvcWidget;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.mvc.contrpanel.model.ContrButton;
import com.javahotel.client.mvc.contrpanel.model.IContrPanel;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class ContrButtonView implements IContrButtonView {

    private final IResLocator rI;
    private final IContrPanel model;
    private final Panel hP;
    private final IControlClick co;
    private final Map<Integer, IGFocusWidget> iBut = new HashMap<Integer, IGFocusWidget>();
    private final static String BUTTON_CUST = "CUSTOM-BUTTON";

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void setEnable(int id, boolean enable) {
        IGFocusWidget b = iBut.get(id);
        if (b == null) {
            return;
        }
        b.setEnabled(enable);
    }

    @Override
    public IMvcWidget getMWidget() {
        return new DefaultMvcWidget(hP);
    }

    private class Click implements ClickHandler {

        private final ContrButton c;

        Click(final ContrButton c) {
            this.c = c;
        }

        @Override
        public void onClick(ClickEvent event) {
            if (co != null) {
                co.click(c, (Widget) event.getSource());
            }
        }
    }

    ContrButtonView(final IResLocator rI, final IContrPanel model,
            final IControlClick co, final boolean hori) {
        this.rI = rI;
        this.model = model;
        this.co = co;
        if (hori) {
            hP = new HorizontalPanel();
        } else {
            hP = new VerticalPanel();
        }
        List<ContrButton> bu = model.getContr();
        for (ContrButton b : bu) {
            IGFocusWidget but;
            if (b.isTextimage()) {
                but = ImgButtonFactory.getButtonTextImage(BUTTON_CUST, b.getContrName(), b.getImageHtml());
            } else {
                but = ImgButtonFactory.getButton(BUTTON_CUST, b.getContrName(),
                        b.getImageHtml());
            }
            but.addClickHandler(new Click(b));
            hP.add(but.getGWidget());
            iBut.put(b.getActionId(), but);
        }
    }
}
