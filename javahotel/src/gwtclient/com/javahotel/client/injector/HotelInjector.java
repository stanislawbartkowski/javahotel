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
package com.javahotel.client.injector;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.javahotel.client.IResLocator;
import com.javahotel.client.abstractto.AbstractToFactory;
import com.javahotel.client.mvc.checktable.view.ICheckTableView;
import com.javahotel.client.mvc.checktable.view.IDecimalTableView;
import com.javahotel.client.mvc.controller.onerecordmodif.OneRecordModifWidgetFactory;
import com.javahotel.client.mvc.crudtable.controler.CrudTableControlerFactory;
import com.javahotel.client.mvc.dict.validator.DictValidatorFactory;
import com.javahotel.client.mvc.dict.validator.price.PriceListValidatorService;
import com.javahotel.client.mvc.dictcrud.controler.DictCrudControlerFactory;
import com.javahotel.client.mvc.dictcrud.controler.DictCrudIOneRecordFactory;
import com.javahotel.client.mvc.dictcrud.controler.bookelemlist.BookRowList;
import com.javahotel.client.mvc.dictcrud.controler.booking.BookResRoom;
import com.javahotel.client.mvc.dictcrud.controler.bookroom.BookRoom;
import com.javahotel.client.mvc.dictcrud.controler.priceoffer.ExtractOfferPriceService;
import com.javahotel.client.mvc.dictcrud.controler.priceoffer.GetSeasonSpecial;
import com.javahotel.client.mvc.dictcrud.controler.priceoffer.PriceListAuxView;
import com.javahotel.client.mvc.dictcrud.controler.priceoffer.SetPriceForOffer;
import com.javahotel.client.mvc.dictcrud.read.CrudReadModelFactory;
import com.javahotel.client.mvc.gridmodel.model.GridModelViewFactory;
import com.javahotel.client.mvc.persistrecord.PersistRecordFactory;
import com.javahotel.client.mvc.recordviewdef.ColListFactory;
import com.javahotel.client.mvc.recordviewdef.DictButtonFactory;
import com.javahotel.client.mvc.recordviewdef.DictEmptyFactory;
import com.javahotel.client.mvc.recordviewdef.GetRecordDefFactory;
import com.javahotel.client.mvc.table.model.TableModelFactory;
import com.javahotel.client.mvc.table.view.IGetTableViewFactory;
import com.javahotel.client.start.IWebEntry;
import com.javahotel.nmvc.ewidget.EWidgetFactory;
import com.javahotel.nmvc.factories.RegisterFactories;
import com.javahotel.nmvc.pricemodel.PriceSeasonModelFactory;

@GinModules(GiniClientModule.class)
public interface HotelInjector extends Ginjector {

    IResLocator getI();

    IWebEntry getW();

    IDispatchCommand getDI();

    CrudReadModelFactory getCrudReadModelFactory();

    IGetTableViewFactory getViewTableFactory();

    GridModelViewFactory getGridViewFactory();

    BookRoom getBookRoom();

    DictCrudControlerFactory getDictCrudControlerFactory();

    PriceListAuxView getPriceListAuxView();

    TableModelFactory getTableModelFactory();

    ColListFactory getColListFactory();

    BookResRoom getBookResRoom();

    ICheckTableView getCheckTableView();

    IDecimalTableView getDecimaleTableView();

    GetSeasonSpecial getGetSeasonSpecial();

    SetPriceForOffer getSetPriceForOffer();

    DictValidatorFactory getDictValidFactory();

    DictButtonFactory getDictButtonFactory();

    BookRowList getBookRowList();

    CrudTableControlerFactory getCrudTableFactory();

    OneRecordModifWidgetFactory getOneRecordModifWidgetFactory();

    DictCrudIOneRecordFactory getDictCrudIOneRecordFactory();

    ExtractOfferPriceService getExtractOfferPriceService();

    GetRecordDefFactory getGetRecordDefFactory();

    DictEmptyFactory getDictEmptyFactory();

    PriceListValidatorService getPriceListValidatorService();

    RegisterFactories getRegisterFactories();

    AbstractToFactory getAbstractToFactory();

    PersistRecordFactory getPersistRecordFactory();

    PriceSeasonModelFactory getPriceSeasonModelFactory();

    EWidgetFactory getEWidgetFactory();
}
