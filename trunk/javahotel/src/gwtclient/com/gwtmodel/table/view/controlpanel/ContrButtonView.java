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
package com.gwtmodel.table.view.controlpanel;

import com.gwtmodel.table.IGWidget;
import java.util.List;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.view.button.ImgButtonFactory;
import com.gwtmodel.table.view.util.CreateFormView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@SuppressWarnings("deprecation")
class ContrButtonView implements IContrButtonView {

    private final Panel hP;
    private final IControlClick co;
    private final Map<ClickButtonType, Button> iBut =
            new HashMap<ClickButtonType, Button>();

    @Override
    public void setEnable(ClickButtonType id, boolean enable) {
        Button b = iBut.get(id);
        if (b == null) {
            return;
        }
        b.setEnabled(enable);
    }

    @Override
    public void fillHtml(IGWidget gw) {
        List<ClickButtonType> cList = new ArrayList<ClickButtonType>();
        List<Button> bList = new ArrayList<Button>();
        for (Entry<ClickButtonType, Button> e : iBut.entrySet()) {
            cList.add(e.getKey());
            bList.add(e.getValue());
        }
        Widget w = gw.getGWidget();
        HTMLPanel pa = (HTMLPanel) w;
        CreateFormView.setHtml(pa, cList, bList);
    }

    private class Click implements ClickListener {

        private final ControlButtonDesc c;

        Click(final ControlButtonDesc c) {
            this.c = c;
        }

        @Override
        public void onClick(Widget sender) {
            if (co != null) {
                co.click(c, sender);
            }
        }
    }

    ContrButtonView(final ListOfControlDesc model,
            final IControlClick co, final boolean hori) {
        this.co = co;
        if (hori) {
            hP = new HorizontalPanel();
        } else {
            hP = new VerticalPanel();
        }
        List<ControlButtonDesc> bu = model.getcList();
        for (ControlButtonDesc b : bu) {
            Button but;
            String bName = b.getActionId().getCustomButt();
            if (bName == null) {
                bName = b.getActionId().getClickEnum().toString();
            }
            if (b.isTextimage()) {
                but = ImgButtonFactory.getButtonTextImage(bName,
                        b.getContrName(), b.getImageHtml());
            } else {
                but = ImgButtonFactory.getButton(bName, b.getContrName(),
                        b.getImageHtml());
            }
            but.addClickListener(new Click(b));
            hP.add(but);
            iBut.put(b.getActionId(), but);
        }
    }

    @Override
    public Widget getGWidget() {
        return hP;
    }
}
