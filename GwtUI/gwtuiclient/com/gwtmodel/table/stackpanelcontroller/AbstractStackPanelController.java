/*
 * Copyright 2016 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.stackpanelcontroller;

import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.GWidget;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.view.controlpanel.IControlClick;

/**
 *
 * @author stanislaw.bartkowski@gmail.com
 */
abstract class AbstractStackPanelController extends AbstractSlotContainer
        implements IStackPanelController {

    protected IGWidget sView;

    protected class CallBack implements IControlClick {

        @Override
        public void click(ControlButtonDesc bu, Widget w) {
            publish(bu.getActionId(), w != null ? new GWidget(w) : sView);
        }
    }

    @Override
    public void startPublish(CellId cellId) {
        publish(dType, cellId, sView);
    }
}
