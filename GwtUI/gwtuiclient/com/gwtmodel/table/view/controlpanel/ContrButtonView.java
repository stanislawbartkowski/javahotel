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
package com.gwtmodel.table.view.controlpanel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.gwtmodel.table.IGFocusWidget;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.view.button.ImgButtonFactory;
import com.gwtmodel.table.view.util.CreateFormView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class ContrButtonView implements IContrButtonView {

    private final Panel hP;
    private final IControlClick co;
    private final Map<ClickButtonType, IGFocusWidget> iBut = new HashMap<ClickButtonType, IGFocusWidget>();
    private final Map<ClickButtonType, ControlButtonDesc> cBut = new HashMap<ClickButtonType, ControlButtonDesc>();
    private HTMLPanel pa = null;

    @Override
    public void setEnable(ClickButtonType id, boolean enable) {
        IGFocusWidget b = iBut.get(id);
        if (b == null) {
            return;
        }
        b.setEnabled(enable);
    }

    @Override
    public void setHtml(IGWidget gw) {
        Widget w = gw.getGWidget();
        pa = (HTMLPanel) w;
        fillHtml(pa);
    }

    @Override
    public void fillHtml(HTMLPanel pa) {
        List<ClickButtonType> cList = new ArrayList<ClickButtonType>();
        List<IGFocusWidget> bList = new ArrayList<IGFocusWidget>();
        for (Entry<ClickButtonType, IGFocusWidget> e : iBut.entrySet()) {
            cList.add(e.getKey());
            bList.add(e.getValue());
        }
        CreateFormView.setHtml(pa, cList, bList);
    }

    private class Click implements ClickHandler {

        private final ControlButtonDesc c;

        Click(final ControlButtonDesc c) {
            this.c = c;
        }

        public void onClick(ClickEvent event) {
            if (co != null) {
                co.click(c, (Widget) event.getSource());
            }
        }
    }

    ContrButtonView(final ListOfControlDesc model, final IControlClick co,
            final boolean hori) {
        this.co = co;
        if (hori) {
            hP = new HorizontalPanel();
        } else {
            hP = new VerticalPanel();
            hP.setStyleName("stack-panel");
        }
        List<ControlButtonDesc> bu = model.getcList();
        for (ControlButtonDesc b : bu) {
            IGFocusWidget but;
            String htmlElementName = b.getActionId().getHtmlElementName();
            if (b.isTextimage()) {
                but = ImgButtonFactory.getButtonTextImage(htmlElementName,
                        b.getDisplayName(), b.getImageHtml());
            } else {
                but = ImgButtonFactory.getButton(htmlElementName,
                        b.getDisplayName(), b.getImageHtml());
            }
            if (!hori) {
                but.getGWidget().setWidth("100%");
            }
            if (!b.isEnabled())
                but.setEnabled(false);
            else {
                but.setEnabled(true);
                but.addClickHandler(new Click(b));
            }
            hP.add(but.getGWidget());
            iBut.put(b.getActionId(), but);
            cBut.put(b.getActionId(), b);
        }
    }

    @Override
    public Widget getGWidget() {
        if (pa != null) {
            return pa;
        }
        return hP;
    }

    @Override
    public void emulateClick(ClickButtonType actionId) {
        if (co == null) {
            return;
        }
        IGFocusWidget f = iBut.get(actionId);
        if (f == null) {
            return;
        }
        ControlButtonDesc b = cBut.get(actionId);
        co.click(b, f.getGWidget());
    }
}
