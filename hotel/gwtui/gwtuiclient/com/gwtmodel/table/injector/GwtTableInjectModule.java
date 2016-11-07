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

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.gwtmodel.table.IConsts;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonFactory;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonImages;
import com.gwtmodel.table.chooselist.ChooseListFactory;
import com.gwtmodel.table.composecontroller.ComposeControllerFactory;
import com.gwtmodel.table.controlbuttonview.ControlButtonViewFactory;
import com.gwtmodel.table.controlbuttonview.StackPanelButtonFactory;
import com.gwtmodel.table.controler.TableDataControlerFactory;
import com.gwtmodel.table.datalisttype.DataListTypeFactory;
import com.gwtmodel.table.datamodelview.DataViewModelFactory;
import com.gwtmodel.table.disclosure.DisclosurePanelFactory;
import com.gwtmodel.table.editc.EditChooseRecordFactory;
import com.gwtmodel.table.factories.IDisclosurePanelFactory;
import com.gwtmodel.table.factories.IGetCustomValues;
import com.gwtmodel.table.factories.ITableAbstractFactories;
import com.gwtmodel.table.factories.ITableCustomFactories;
import com.gwtmodel.table.factories.IWebPanelResources;
import com.gwtmodel.table.factories.customvalues.CustomValuesProvider;
import com.gwtmodel.table.factories.customvalues.WebPanelResources;
import com.gwtmodel.table.htmlview.HtmlPanelFactory;
import com.gwtmodel.table.json.CreateJSonForIVData;
import com.gwtmodel.table.json.IJsonConvert;
import com.gwtmodel.table.listdataview.ListDataViewFactory;
import com.gwtmodel.table.login.LoginViewFactory;
import com.gwtmodel.table.panelview.PanelViewFactory;
import com.gwtmodel.table.readres.ReadResFactory;
import com.gwtmodel.table.slotmediator.ISlotMediator;
import com.gwtmodel.table.slotmediator.SlotMediatorFactory;
import com.gwtmodel.table.slotmodel.SlU;
import com.gwtmodel.table.slotmodel.SlotListContainer;
import com.gwtmodel.table.slotmodel.SlotSignalContextFactory;
import com.gwtmodel.table.slotmodel.SlotTypeFactory;
import com.gwtmodel.table.smessage.GetStandardMessage;
import com.gwtmodel.table.smessage.IGetStandardMessage;
import com.gwtmodel.table.stackpanelcontroller.StackPanelControllerFactory;
import com.gwtmodel.table.tabpanelview.TabPanelViewFactory;
import com.gwtmodel.table.view.binder.ICreateBinderWidget;
import com.gwtmodel.table.view.binder.ISetWidgetAttribute;
import com.gwtmodel.table.view.binder.impl.CreateBinderWidget;
import com.gwtmodel.table.view.binder.impl.SetWidgetAttribute;
import com.gwtmodel.table.view.button.IImgButton;
import com.gwtmodel.table.view.button.ImgButtonFactory;
import com.gwtmodel.table.view.button.gwt.ImgButtonGwtImpl;
import com.gwtmodel.table.view.button.polymer.ImgButtonPolymerImpl;
import com.gwtmodel.table.view.controlpanel.ContrButtonViewFactory;
import com.gwtmodel.table.view.controlpanel.IContrButtonViewFactory;
import com.gwtmodel.table.view.daytimetable.IDatePanelScroll;
import com.gwtmodel.table.view.daytimetable.impl.WidgetScrollSeasonFactory;
import com.gwtmodel.table.view.ewidget.EditWidgetFactory;
import com.gwtmodel.table.view.ewidget.IEditWidget;
import com.gwtmodel.table.view.ewidget.gwt.GwtWidgetImpl;
import com.gwtmodel.table.view.ewidget.polymer.EditWidgetPolymer;
import com.gwtmodel.table.view.form.GwtFormViewFactory;
import com.gwtmodel.table.view.grid.GridViewFactory;
import com.gwtmodel.table.view.panel.GwtPanelViewFactory;
import com.gwtmodel.table.view.pullmenu.PullMenuFactory;
import com.gwtmodel.table.view.stackpanel.ViewStackPanelFactory;
import com.gwtmodel.table.view.stackvertical.StackPanelFactory;
import com.gwtmodel.table.view.util.polymer.PolymerUtil;
import com.gwtmodel.table.view.webpanel.IWebPanel;
import com.gwtmodel.table.view.webpanel.WebPanelFactory;

public class GwtTableInjectModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(TablesFactories.class).in(Singleton.class);
		bind(ListDataViewFactory.class).in(Singleton.class);
		bind(SlotTypeFactory.class).in(Singleton.class);
		bind(SlotSignalContextFactory.class).in(Singleton.class);
		bind(SlotListContainer.class);
		bind(TableDataControlerFactory.class).in(Singleton.class);
		bind(GwtFormViewFactory.class).in(Singleton.class);
		bind(ITableAbstractFactories.class).toProvider(TableAbstractFactoriesProvider.class);
		bind(ITableCustomFactories.class).toProvider(TablesFactoriesContainerProvider.class);
		bind(DataViewModelFactory.class).in(Singleton.class);
		bind(ControlButtonFactory.class).in(Singleton.class);
		bind(ComposeControllerFactory.class).in(Singleton.class);
		bind(IEditWidget.class).annotatedWith(Names.named(IConsts.GWT)).to(GwtWidgetImpl.class).in(Singleton.class);
		bind(IEditWidget.class).annotatedWith(Names.named(IConsts.POLYMER)).to(EditWidgetPolymer.class)
				.in(Singleton.class);
		bind(IImgButton.class).annotatedWith(Names.named(IConsts.GWT)).to(ImgButtonGwtImpl.class).in(Singleton.class);
		bind(IImgButton.class).annotatedWith(Names.named(IConsts.POLYMER)).to(ImgButtonPolymerImpl.class)
				.in(Singleton.class);
		bind(ReadResFactory.class).in(Singleton.class);
		bind(SlotMediatorFactory.class).in(Singleton.class);
		bind(HtmlPanelFactory.class).in(Singleton.class);
		bind(IContrButtonViewFactory.class).to(ContrButtonViewFactory.class).in(Singleton.class);
		bind(WebPanelFactory.class).in(Singleton.class);
		bind(IWebPanel.class).toProvider(WebPanelProvider.class);
		bind(StackPanelFactory.class).in(Singleton.class);
		bind(ISlotMediator.class).toProvider(SlotMediatorFactory.class);
		bind(ICallContext.class).to(CallContext.class);
		bind(StackPanelButtonFactory.class).in(Singleton.class);
		bind(StackPanelControllerFactory.class).in(Singleton.class);
		bind(EditChooseRecordFactory.class).in(Singleton.class);
		bind(WidgetScrollSeasonFactory.class).in(Singleton.class);
		bind(ViewStackPanelFactory.class).in(Singleton.class);
		bind(PullMenuFactory.class).in(Singleton.class);
		bind(DataListTypeFactory.class).in(Singleton.class);
		bind(IGetStandardMessage.class).to(GetStandardMessage.class).in(Singleton.class);
		bind(IGetCustomValues.class).to(CustomValuesProvider.class).in(Singleton.class);
		bind(IWebPanelResources.class).to(WebPanelResources.class).in(Singleton.class);
		bind(IJsonConvert.class).to(CreateJSonForIVData.class).in(Singleton.class);
		bind(IDatePanelScroll.class).to(WidgetScrollSeasonFactory.class).in(Singleton.class);
		bind(IDisclosurePanelFactory.class).to(DisclosurePanelFactory.class).in(Singleton.class);
		bind(ICreateBinderWidget.class).to(CreateBinderWidget.class).in(Singleton.class);
		bind(ISetWidgetAttribute.class).to(SetWidgetAttribute.class).in(Singleton.class);
		requestStaticInjection(ImgButtonFactory.class, ControlButtonViewFactory.class, EditWidgetFactory.class,
				GridViewFactory.class, GwtPanelViewFactory.class, ChooseListFactory.class, PanelViewFactory.class,
				LoginViewFactory.class, TabPanelViewFactory.class, PolymerUtil.class, ControlButtonImages.class,
				SlU.class, Utils.class);
	}
}
