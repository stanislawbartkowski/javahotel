/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.panelcommand;

import com.gwtmodel.table.controler.IDataControler;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DefaultMvcWidget;
import com.javahotel.client.dialog.ISetGwtWidget;
import com.javahotel.client.injector.HInjector;
import com.javahotel.common.command.DictType;
import com.javahotel.nmvc.common.DataType;
import com.javahotel.nmvc.controler.DataControlerEnum;
import com.javahotel.nmvc.controler.DataControlerFactory;
import com.javahotel.nmvc.factories.RegisterFactories;

class NewMvcPanel extends AbstractPanelCommand {

    private ISetGwtWidget iS;
    private final IResLocator rI;
    private final DictType da;
    private final RegisterFactories rFactories;

    NewMvcPanel(IResLocator rI, DictType da) {
        this.rI = rI;
        this.da = da;
        rFactories = HInjector.getI().getRegisterFactories();
        rFactories.register();
    }

    private class SetGwt implements ISlotSignaller {

        public void signal(ISlotSignalContext slContext) {
            iS.setGwtWidget(new DefaultMvcWidget(slContext.getGwtWidget().getWidget()));
        }

    }

    public void beforeDrawAction(ISetGwtWidget iSet) {
        iS = iSet;
        IDataControler iControler = DataControlerFactory
                .constructDataControler(rI, DataControlerEnum.DisplayList,
                        new DataType(da), 0, 1);
        iControler.getSlContainer().registerSubscriber(0, new SetGwt());
        iControler.startPublish();

    }

    public void drawAction() {
    }

}
