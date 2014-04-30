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
package com.jythonui.client.dialog;

import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.gwtmodel.table.GWidget;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.controlbuttonview.GetButtons;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ISlotCustom;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.slotmodel.SlU;
import com.gwtmodel.table.view.util.CreateFormView;
import com.gwtmodel.table.view.util.CreateFormView.IGetButtons;

class HTMLDisc extends AbstractSlotContainer implements ISlotable {

    private final String html;
    private final String header;

    HTMLDisc(IDataType publishType, String header, String html) {
        this.dType = publishType;
        this.html = html;
        this.header = (header == null) ? "" : header;
    }

    @Override
    public void startPublish(CellId cellId) {
        // HTMLPanel pa = new HTMLPanel(html);
        HTMLPanel pa = new HTMLPanel(html);
        DisclosurePanel di = new DisclosurePanel(header);
        di.setContent(pa);
        FormLineContainer fC = SlU.getFormLineContainer(dType, this);
        if (fC != null)
            CreateFormView.setHtml(pa, fC.getfList());
        ISlotCustom sl = GetButtons.constructSlot(dType);
        ISlotSignalContext i = getSlContainer().getGetterCustom(sl);
        if (i != null) {
            GetButtons g = (GetButtons) i.getCustom();
            CreateFormView.IGetButtons iG = g.getValue();
            CreateFormView.setHtml(pa, iG);
        }
        publish(dType, cellId, new GWidget(di));
    }

}
