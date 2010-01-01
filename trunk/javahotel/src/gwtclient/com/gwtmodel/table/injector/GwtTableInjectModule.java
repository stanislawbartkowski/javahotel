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

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonFactory;
import com.gwtmodel.table.composecontroller.ComposeControllerFactory;
import com.gwtmodel.table.controlbuttonview.ControlButtonViewFactory;
import com.gwtmodel.table.controler.TableDataControlerFactory;
import com.gwtmodel.table.datamodelview.DataViewModelFactory;
import com.gwtmodel.table.factories.ITableAbstractFactories;
import com.gwtmodel.table.listdataview.ListDataViewFactory;
import com.gwtmodel.table.panelview.PanelViewFactory;
import com.gwtmodel.table.slotmodel.SlotListContainer;
import com.gwtmodel.table.slotmodel.SlotSignalContextFactory;
import com.gwtmodel.table.slotmodel.SlotTypeFactory;
import com.gwtmodel.table.view.form.GwtFormViewFactory;
import com.gwtmodel.table.view.panel.GwtPanelViewFactory;
import com.gwtmodel.table.view.table.GwtTableFactory;

public class GwtTableInjectModule extends AbstractGinModule {

    protected void configure() {
        bind(ControlButtonViewFactory.class).in(Singleton.class);
        bind(TablesFactories.class).in(Singleton.class);
        bind(PanelViewFactory.class).in(Singleton.class);
        bind(ListDataViewFactory.class).in(Singleton.class);
        bind(GwtPanelViewFactory.class).in(Singleton.class);
        bind(GwtTableFactory.class).in(Singleton.class);
        bind(SlotTypeFactory.class).in(Singleton.class);
        bind(SlotSignalContextFactory.class).in(Singleton.class);
        bind(SlotListContainer.class);
        bind(TableDataControlerFactory.class).in(Singleton.class);
        bind(GwtFormViewFactory.class).in(Singleton.class);
        bind(TableFactoriesContainer.class).in(Singleton.class);
        bind(ITableAbstractFactories.class).to(TableFactoriesContainer.class).in(Singleton.class);
        bind(DataViewModelFactory.class).in(Singleton.class);     
        bind(ControlButtonFactory.class).in(Singleton.class);
        bind(ComposeControllerFactory.class).in(Singleton.class);
    }

}
