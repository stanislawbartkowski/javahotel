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
package com.javahotel.client.injector;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.javahotel.client.IResLocator;
import com.javahotel.client.abstractto.AbstractToFactory;
import com.javahotel.client.dialog.eadmin.AdminPanelFactory;
import com.javahotel.client.dialog.panel.IUserPanelMenuFactory;
import com.javahotel.client.dialog.user.UserPanelFactory;
import com.javahotel.client.dispatcher.Dispatch;
import com.javahotel.client.dispatcher.IDispatch;
import com.javahotel.client.dispatcher.UICommand;
import com.javahotel.client.dispatcher.command.AdminPanelCommand;
import com.javahotel.client.dispatcher.command.LoginCommand;
import com.javahotel.client.dispatcher.command.UserPanelCommand;
import com.javahotel.client.mvc.checktable.view.CheckTableView;
import com.javahotel.client.mvc.checktable.view.DecimalTableView;
import com.javahotel.client.mvc.checktable.view.ICheckTableView;
import com.javahotel.client.mvc.checktable.view.IDecimalTableView;
import com.javahotel.client.mvc.controller.onerecordmodif.OneRecordModifWidgetFactory;
import com.javahotel.client.mvc.crud.controler.CrudControlerFactory;
import com.javahotel.client.mvc.crudtable.controler.CrudTableControlerFactory;
import com.javahotel.client.mvc.dict.validator.DictValidatorFactory;
import com.javahotel.client.mvc.dict.validator.price.PriceListValidatorService;
import com.javahotel.client.mvc.dictcrud.controler.DictCrudControlerFactory;
import com.javahotel.client.mvc.dictcrud.controler.DictCrudIOneRecordFactory;
import com.javahotel.client.mvc.dictcrud.controler.bookelemlist.BookRowList;
import com.javahotel.client.mvc.dictcrud.controler.bookelemlist.BookingElem;
import com.javahotel.client.mvc.dictcrud.controler.booking.BookResRoom;
import com.javahotel.client.mvc.dictcrud.controler.bookroom.BookRoom;
import com.javahotel.client.mvc.dictcrud.controler.priceoffer.ExtractOfferPriceService;
import com.javahotel.client.mvc.dictcrud.controler.priceoffer.GetSeasonSpecial;
import com.javahotel.client.mvc.dictcrud.controler.priceoffer.PriceListAuxView;
import com.javahotel.client.mvc.dictcrud.controler.priceoffer.SetPriceForOffer;
import com.javahotel.client.mvc.dictcrud.read.CrudReadModelFactory;
import com.javahotel.client.mvc.gridmodel.model.GridModelViewFactory;
import com.javahotel.client.mvc.recordviewdef.ColListFactory;
import com.javahotel.client.mvc.recordviewdef.DictButtonFactory;
import com.javahotel.client.mvc.recordviewdef.DictEmptyFactory;
import com.javahotel.client.mvc.recordviewdef.GetRecordDefFactory;
import com.javahotel.client.mvc.table.model.TableModelFactory;
import com.javahotel.client.mvc.table.view.IGetTableViewFactory;
import com.javahotel.client.paymentdata.PaymentData;
import com.javahotel.client.start.IWebEntry;
import com.javahotel.client.start.MainWebEntry;
import com.javahotel.nmvc.factories.RegisterFactories;
import com.javahotel.view.gwt.grid.view.GridGwtGetViewFactory;
import com.javahotel.view.gwt.recordviewdef.GwtGetRecordDefFactory;
import com.javahotel.view.gwt.table.view.ViewTableViewFactory;

public class GiniClientModule extends AbstractGinModule {

    protected void configure() {
        bind(IResLocator.class).toProvider(ResLocatorProvider.class).asEagerSingleton();
        bind(IDispatch.class).to(Dispatch.class);
        bind(IWebEntry.class).to(MainWebEntry.class);
        bind(UICommand.class).annotatedWith(Names.named("UserLoginCommand")).to(LoginCommand.class);
        bind(UICommand.class).annotatedWith(Names.named("UserPanelCommand")).to(UserPanelCommand.class);
        bind(UICommand.class).annotatedWith(Names.named("AdminPanelCommand")).to(AdminPanelCommand.class);
        bind(IUserPanelMenuFactory.class).annotatedWith(Names.named("UserPanelFactory")).to(UserPanelFactory.class);
        bind(IUserPanelMenuFactory.class).annotatedWith(Names.named("AdminPanelFactory")).to(AdminPanelFactory.class);
        bind(IDispatchCommand.class).to(DispatchCommand.class);
        bind(GridGwtGetViewFactory.class).in(Singleton.class);
        bind(CrudReadModelFactory.class).in(Singleton.class);
        bind(GridModelViewFactory.class).in(Singleton.class);
        bind(IGetTableViewFactory.class).to(ViewTableViewFactory.class).in(Singleton.class);
        bind(BookRoom.class);
        bind(DictCrudControlerFactory.class).in(Singleton.class);
        bind(PriceListAuxView.class);
        bind(TableModelFactory.class).in(Singleton.class);
        bind(ColListFactory.class).in(Singleton.class);
        bind(BookResRoom.class);
        bind(BookingElem.class);
        bind(PaymentData.class);
        bind(ICheckTableView.class).to(CheckTableView.class);
        bind(IDecimalTableView.class).to(DecimalTableView.class);
        bind(GetSeasonSpecial.class);
        bind( SetPriceForOffer.class).in(Singleton.class);
        bind(DictValidatorFactory.class).in(Singleton.class);
        bind(DictButtonFactory.class).in(Singleton.class);
        bind(BookRowList.class);
        bind(CrudTableControlerFactory.class).in(Singleton.class);
        bind(CrudControlerFactory.class).in(Singleton.class);
        bind(OneRecordModifWidgetFactory.class).in(Singleton.class);
        bind(DictCrudIOneRecordFactory.class).in(Singleton.class);
        bind(ExtractOfferPriceService.class).in(Singleton.class);
        bind(GwtGetRecordDefFactory.class).in(Singleton.class);
        bind(GetRecordDefFactory.class).in(Singleton.class);
        bind(DictEmptyFactory.class).in(Singleton.class);
        bind(PriceListValidatorService.class);
        bind(RegisterFactories.class).in(Singleton.class);
        bind(AbstractToFactory.class).in(Singleton.class);
    }
}