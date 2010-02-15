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
import com.gwtmodel.table.htmlview.HtmlPanelFactory;
import com.gwtmodel.table.listdataview.ListDataViewFactory;
import com.gwtmodel.table.panelview.PanelViewFactory;
import com.gwtmodel.table.readres.ReadResFactory;
import com.gwtmodel.table.slotmediator.SlotMediatorFactory;
import com.gwtmodel.table.slotmodel.SlotListContainer;
import com.gwtmodel.table.slotmodel.SlotSignalContextFactory;
import com.gwtmodel.table.slotmodel.SlotTypeFactory;
import com.gwtmodel.table.stringlist.MemoryStringTableFactory;
import com.gwtmodel.table.view.checkstring.CheckDictModelFactory;
import com.gwtmodel.table.view.controlpanel.ContrButtonViewFactory;
import com.gwtmodel.table.view.ewidget.EditWidgetFactory;
import com.gwtmodel.table.view.form.GwtFormViewFactory;
import com.gwtmodel.table.view.grid.GridViewFactory;
import com.gwtmodel.table.view.panel.GwtPanelViewFactory;
import com.gwtmodel.table.view.table.GwtTableFactory;
import com.gwtmodel.table.view.webpanel.IWebPanel;
import com.gwtmodel.table.view.webpanel.WebPanelFactory;

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
        bind(EditWidgetFactory.class).in(Singleton.class);
        bind(CheckDictModelFactory.class).in(Singleton.class);
        bind(ReadResFactory.class).in(Singleton.class);
        bind(GridViewFactory.class).in(Singleton.class);
        bind(SlotMediatorFactory.class).in(Singleton.class);
        bind(MemoryStringTableFactory.class).in(Singleton.class);
        bind(HtmlPanelFactory.class).in(Singleton.class);
        bind(ContrButtonViewFactory.class).in(Singleton.class);
        bind(WebPanelFactory.class).in(Singleton.class);
        bind(IWebPanel.class).toProvider(WebPanelProvider.class);
    }
}
