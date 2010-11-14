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
package com.gwtmodel.table.controlbuttonview;

import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.GWidget;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.view.controlpanel.ContrButtonViewFactory;
import com.gwtmodel.table.view.controlpanel.IContrButtonView;
import com.gwtmodel.table.view.controlpanel.IControlClick;

class ControlButtonView extends AbstractSlotContainer implements
        IControlButtonView {

    private final IContrButtonView vButton;
//    private final IDataType dType;

    private class Click implements IControlClick {

        @Override
        public void click(ControlButtonDesc co, Widget w) {
            publish(co.getActionId(), new GWidget(w));
        }
    }

    ControlButtonView(ContrButtonViewFactory vFactory,
            ListOfControlDesc listButton, IDataType dType) {
        this.dType = dType;
        vButton = vFactory.getView(listButton, new Click());
    }

    @Override
    public void startPublish(CellId cellId) {
        IGWidget w = getHtmlWidget(cellId);
        if (w == null) {
            publish(dType, cellId, vButton);
        } else {
            vButton.fillHtml(w);
        }
    }
}
