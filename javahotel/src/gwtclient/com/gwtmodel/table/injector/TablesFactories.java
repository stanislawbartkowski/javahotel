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
package com.gwtmodel.table.injector;

import com.google.inject.Inject;
import com.gwtmodel.table.controlbuttonview.ControlButtonViewFactory;
import com.gwtmodel.table.listdataview.ListDataViewFactory;
import com.gwtmodel.table.panelview.PanelViewFactory;
import com.gwtmodel.table.slotmodel.SlotSignalContext;
import com.gwtmodel.table.slotmodel.SlotTypeFactory;

public class TablesFactories {

    private final ControlButtonViewFactory bViewFactory;
    private final PanelViewFactory pViewFactory;
    private final ListDataViewFactory lDataFactory;
    private final SlotTypeFactory slTypeFactory;
    private final SlotSignalContext slSignalContext;

    public ControlButtonViewFactory getbViewFactory() {
        return bViewFactory;
    }

    public PanelViewFactory getpViewFactory() {
        return pViewFactory;
    }

    @Inject
    public TablesFactories(ControlButtonViewFactory bViewFactory,
            PanelViewFactory pViewFactory, ListDataViewFactory lDataFactory,
            SlotTypeFactory slTypeFactory, SlotSignalContext slSignalContext) {
        this.bViewFactory = bViewFactory;
        this.pViewFactory = pViewFactory;
        this.lDataFactory = lDataFactory;
        this.slTypeFactory = slTypeFactory;
        this.slSignalContext = slSignalContext;
    }

    public SlotSignalContext getSlSignalContext() {
        return slSignalContext;
    }

    public SlotTypeFactory getSlTypeFactory() {
        return slTypeFactory;
    }

    public ListDataViewFactory getlDataFactory() {
        return lDataFactory;
    }

}
