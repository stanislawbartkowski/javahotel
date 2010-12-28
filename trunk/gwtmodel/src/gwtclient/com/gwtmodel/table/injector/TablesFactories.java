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
package com.gwtmodel.table.injector;

import com.google.inject.Inject;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonFactory;
import com.gwtmodel.table.controlbuttonview.ControlButtonViewFactory;
import com.gwtmodel.table.datamodelview.DataViewModelFactory;
import com.gwtmodel.table.listdataview.ListDataViewFactory;
import com.gwtmodel.table.panelview.PanelViewFactory;
import com.gwtmodel.table.slotmediator.SlotMediatorFactory;
import com.gwtmodel.table.slotmodel.SlotTypeFactory;
import com.gwtmodel.table.stackpanelcontroller.StackPanelControllerFactory;

public class TablesFactories {

    private final ControlButtonViewFactory bViewFactory;
    private final PanelViewFactory pViewFactory;
    private final ListDataViewFactory lDataFactory;
    private final SlotTypeFactory slTypeFactory;
    private final DataViewModelFactory dViewFactory;
    private final ControlButtonFactory cButtonFactory;
    private final SlotMediatorFactory meFactory;
    private final StackPanelControllerFactory stFactory;

    public ControlButtonViewFactory getbViewFactory() {
        return bViewFactory;
    }

    public PanelViewFactory getpViewFactory() {
        return pViewFactory;
    }

    @Inject
    public TablesFactories(ControlButtonViewFactory bViewFactory,
            PanelViewFactory pViewFactory, ListDataViewFactory lDataFactory,
            SlotTypeFactory slTypeFactory, DataViewModelFactory dViewFactory,
            ControlButtonFactory cButtonFactory, SlotMediatorFactory meFactory,
            StackPanelControllerFactory stFactory) {
        this.bViewFactory = bViewFactory;
        this.pViewFactory = pViewFactory;
        this.lDataFactory = lDataFactory;
        this.slTypeFactory = slTypeFactory;
        this.dViewFactory = dViewFactory;
        this.cButtonFactory = cButtonFactory;
        this.meFactory = meFactory;
        this.stFactory = stFactory;
    }

    public SlotTypeFactory getSlTypeFactory() {
        return slTypeFactory;
    }

    public ListDataViewFactory getlDataFactory() {
        return lDataFactory;
    }

    public DataViewModelFactory getdViewFactory() {
        return dViewFactory;
    }

    public ControlButtonFactory getControlButtonFactory() {
        return cButtonFactory;
    }

    public SlotMediatorFactory getSlotMediatorFactory() {
        return meFactory;
    }

    public StackPanelControllerFactory getStackPanelControllerFactory() {
        return stFactory;
    }
}
