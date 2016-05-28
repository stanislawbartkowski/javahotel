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
package com.gwtmodel.table.injector;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonFactory;
import com.gwtmodel.table.composecontroller.ComposeControllerFactory;
import com.gwtmodel.table.controlbuttonview.StackPanelButtonFactory;
import com.gwtmodel.table.controler.TableDataControlerFactory;
import com.gwtmodel.table.datalisttype.DataListTypeFactory;
import com.gwtmodel.table.datamodelview.DataViewModelFactory;
import com.gwtmodel.table.editc.EditChooseRecordFactory;
import com.gwtmodel.table.factories.IDisclosurePanelFactory;
import com.gwtmodel.table.factories.IGetCustomValues;
import com.gwtmodel.table.factories.ITableAbstractFactories;
import com.gwtmodel.table.factories.ITableCustomFactories;
import com.gwtmodel.table.factories.IWebPanelResources;
import com.gwtmodel.table.htmlview.HtmlPanelFactory;
import com.gwtmodel.table.json.IJsonConvert;
import com.gwtmodel.table.readres.ReadResFactory;
import com.gwtmodel.table.slotmediator.SlotMediatorFactory;
import com.gwtmodel.table.slotmodel.SlotListContainer;
import com.gwtmodel.table.slotmodel.SlotSignalContextFactory;
import com.gwtmodel.table.slotmodel.SlotTypeFactory;
import com.gwtmodel.table.smessage.IGetStandardMessage;
import com.gwtmodel.table.stackpanelcontroller.StackPanelControllerFactory;
import com.gwtmodel.table.tabpanelview.TabPanelViewFactory;
import com.gwtmodel.table.view.controlpanel.IContrButtonViewFactory;
import com.gwtmodel.table.view.daytimetable.IDatePanelScroll;
import com.gwtmodel.table.view.stackpanel.ViewStackPanelFactory;
import com.gwtmodel.table.view.stackvertical.StackPanelFactory;
import com.gwtmodel.table.view.table.GwtTableFactory;
import com.gwtmodel.table.view.webpanel.IWebPanel;
import com.gwtmodel.table.view.webpanel.WebPanelFactory;

@GinModules(GwtTableInjectModule.class)
public interface GwtTableInjector extends Ginjector {

    TablesFactories getTablesFactories();

    SlotTypeFactory getSlotTypeFactory();

    SlotListContainer getSlotListContainer();

    TableDataControlerFactory getTableDataControlerFactory();

    SlotSignalContextFactory getSlotSignalContextFactory();

    ITableAbstractFactories getITableAbstractFactories();

    ComposeControllerFactory getComposeControllerFactory();

    DataViewModelFactory getDataViewModelFactory();

    ReadResFactory getReadResFactory();

    ITableCustomFactories getTableFactoriesContainer();

    GwtTableFactory getGwtTableFactory();

    SlotMediatorFactory getSlotMediatorFactory();

    HtmlPanelFactory getHtmlPanelFactory();

    ControlButtonFactory getControlButtonFactory();

    IContrButtonViewFactory getContrButtonViewFactory();

    WebPanelFactory getWebPanelFactory();

    IWebPanel getWebPanel();

    ICallContext getCallContext();

    StackPanelButtonFactory getStackPanelButtonFactory();

    StackPanelFactory getStackPanelFactory();

    StackPanelControllerFactory getStackPanelControllerFactory();

    EditChooseRecordFactory getEditChooseRecordFactory();

    ViewStackPanelFactory getViewStackPanelFactory();

    DataListTypeFactory getDataListTypeFactory();

    IGetStandardMessage getStandardMessage();

    IGetCustomValues getCustomValues();

    IWebPanelResources getWebPanelResources();

    IJsonConvert getJsonConvert();

    IDatePanelScroll getDatePanelScroll();

    IDisclosurePanelFactory getDisclosurePanelFactory();
}
