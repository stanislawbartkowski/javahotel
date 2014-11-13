/*
 *  Copyright 2014 stanislawbartkowski@gmail.com
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package com.gwtmodel.table.slotmodel;

import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.TablesFactories;
import com.gwtmodel.table.panelview.PanelViewFactory;
import com.gwtmodel.table.slotmediator.ISlotMediator;

/**
 *
 * @author hotel
 */
public abstract class AbstractSlotMediatorContainer extends
        TemplateContainerSlotable<ISlotMediator> {

    protected final TablesFactories tFactories;
    // not final
    protected ISlotMediator slMediator;
    protected final PanelViewFactory pViewFactory;
    protected final SlotSignalContextFactory slContextFactory;
    protected final SlotTypeFactory slTypeFactory;

    protected AbstractSlotMediatorContainer() {
        tFactories = GwtGiniInjector.getI().getTablesFactories();
        slMediator = tFactories.getSlotMediatorFactory().construct();
        iSlot = slMediator;
        pViewFactory = tFactories.getpViewFactory();
        slContextFactory = GwtGiniInjector.getI().getSlotSignalContextFactory();
        slTypeFactory = GwtGiniInjector.getI().getSlotTypeFactory();
    }
}
