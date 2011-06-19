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

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import com.gwtmodel.table.factories.ITableCustomFactories;
import com.gwtmodel.table.injector.GwtTableInjectModule;
import com.gwtmodel.table.injector.TablesFactoriesContainerProvider;
import com.javahotel.client.IResLocator;
import com.javahotel.client.abstractto.IAbstractFactory;
import com.javahotel.client.abstractto.IAbstractType;
import com.javahotel.client.abstractto.impl.AbstractToFactory;
import com.javahotel.client.abstractto.impl.TypeToFactory;
import com.javahotel.client.calculateprice.IPaymentData;
import com.javahotel.client.calculateprice.impl.PaymentData;
import com.javahotel.client.gename.IGetFieldName;
import com.javahotel.client.gename.impl.GetFieldName;
import com.javahotel.client.start.IWebEntry;
import com.javahotel.client.start.MainWebEntry;
import com.javahotel.client.start.action.ILoginDialog;
import com.javahotel.client.start.action.IStartHotelAction;
import com.javahotel.client.start.action.impl.StartHotelAction;
import com.javahotel.client.start.logindialog.ETableLoginDialog;
import com.javahotel.client.start.panel.IPanelCommandFactory;
import com.javahotel.nmvc.ewidget.EWidgetFactory;
import com.javahotel.nmvc.factories.RegisterFactories;
import com.javahotel.nmvc.factories.persist.dict.HotelPersistFactory;
import com.javahotel.nmvc.factories.persist.dict.IHotelPersistFactory;
import com.javahotel.nmvc.panel.PanelCommandFactory;

public class GiniClientModule extends AbstractGinModule {

    @Override
    protected void configure() {
        bind(ITableCustomFactories.class).toProvider(TablesFactoriesContainerProvider.class);
        bind(IResLocator.class).toProvider(ResLocatorProvider.class);
        bind(IWebEntry.class).to(MainWebEntry.class);
        bind(GwtTableInjectModule.class);
        bind(IStartHotelAction.class).to(StartHotelAction.class).in(Singleton.class);
        bind(RegisterFactories.class).in(Singleton.class);
        bind(EWidgetFactory.class).in(Singleton.class);
        bind(ILoginDialog.class).to(ETableLoginDialog.class);
        bind(IPanelCommandFactory.class).to(PanelCommandFactory.class).in(Singleton.class);
        bind(IAbstractFactory.class).to(AbstractToFactory.class).in(Singleton.class);
        bind(IAbstractType.class).to(TypeToFactory.class).in(Singleton.class); 
        bind(IGetFieldName.class).to(GetFieldName.class).in(Singleton.class);
        bind(IHotelPersistFactory.class).to(HotelPersistFactory.class);
        bind(IPaymentData.class).to(PaymentData.class);
    }
}