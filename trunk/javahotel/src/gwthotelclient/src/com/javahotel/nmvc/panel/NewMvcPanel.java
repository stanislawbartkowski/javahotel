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
package com.javahotel.nmvc.panel;

import com.gwtmodel.table.ISetGWidget;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.javahotel.client.start.panel.EPanelCommand;
import com.javahotel.client.start.panel.IPanelCommand;

class NewMvcPanel implements IPanelCommand {

    private final EPanelCommand e;

    NewMvcPanel(EPanelCommand e) {
        this.e = e;
    }

    private class SetGwt implements ISlotSignaller {
        private final ISetGWidget iS;

        SetGwt(ISetGWidget iS) {
            this.iS = iS;
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            iS.setW(slContext.getGwtWidget());
        }
    }

    @Override
    public void beforeDrawAction(ISetGWidget iSet) {
        DataControlerFactory.runDataControler(e, new CellId(0), new SetGwt(
                iSet));
    }
}
