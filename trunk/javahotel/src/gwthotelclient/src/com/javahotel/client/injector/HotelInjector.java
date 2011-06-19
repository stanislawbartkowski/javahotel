/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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
import com.javahotel.client.abstractto.IAbstractFactory;
import com.javahotel.client.abstractto.IAbstractType;
import com.javahotel.client.calculateprice.IPaymentData;
import com.javahotel.client.gename.IGetFieldName;
import com.javahotel.client.start.IWebEntry;
import com.javahotel.client.start.action.ILoginDialog;
import com.javahotel.client.start.action.IStartHotelAction;
import com.javahotel.client.start.panel.IPanelCommandFactory;
import com.javahotel.nmvc.ewidget.EWidgetFactory;
import com.javahotel.nmvc.factories.RegisterFactories;
import com.javahotel.nmvc.factories.persist.dict.IHotelPersistFactory;

@GinModules(GiniClientModule.class)
public interface HotelInjector extends Ginjector {

    IResLocator getI();

    IWebEntry getW();

    IStartHotelAction getStartHotelAction();

    RegisterFactories getRegisterFactories();

    EWidgetFactory getEWidgetFactory();

    ILoginDialog getLoginDialog();

    IPanelCommandFactory getPanelCommandFactory();

    IAbstractFactory getAbstractFactory();

    IAbstractType getAbstractType();

    IGetFieldName getGetFieldName();

    IHotelPersistFactory getHotelPersistFactory();

    IPaymentData getPaymentData();

}
