/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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
package com.jythonui.client.injector;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.gwtmodel.table.factories.ITableAbstractFactories;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.login.LoginViewFactory;
import com.gwtmodel.table.smessage.IGetStandardMessage;
import com.jythonui.client.IJythonUIClient;
import com.jythonui.client.charts.ChartManagerFactory;
import com.jythonui.client.dialog.datepanel.DateLineManagerFactory;
import com.jythonui.client.dialog.execute.ExecuteBackAction;
import com.jythonui.client.dialog.impl.DialogContainerFactory;
import com.jythonui.client.formgrid.FormGridManagerFactory;
import com.jythonui.client.impl.JythonUIClientFactory;
import com.jythonui.client.impl.WebPanelResourcesFactory;
import com.jythonui.client.interfaces.IChartManagerFactory;
import com.jythonui.client.interfaces.IDateLineManagerFactory;
import com.jythonui.client.interfaces.IDialogContainerFactory;
import com.jythonui.client.interfaces.IExecuteBackAction;
import com.jythonui.client.interfaces.IExecuteJS;
import com.jythonui.client.interfaces.IFormGridManagerFactory;
import com.jythonui.client.interfaces.ILoginPage;
import com.jythonui.client.interfaces.IRowListDataManagerFactory;
import com.jythonui.client.interfaces.IWebPanelResourcesFactory;
import com.jythonui.client.js.ExecuteJS;
import com.jythonui.client.listmodel.RowListDataManagerFactory;
import com.jythonui.client.login.LoginPage;
import com.jythonui.client.requestcontext.RequestContextFactory;
import com.jythonui.client.start.IJythonClientStart;
import com.jythonui.client.start.impl.JythonClientStart;
import com.jythonui.shared.RequestContext;

public class UIInjectModule extends AbstractGinModule {

    @Override
    protected void configure() {

        bind(IExecuteJS.class).to(ExecuteJS.class).in(Singleton.class);
        bind(IDialogContainerFactory.class).to(DialogContainerFactory.class)
                .in(Singleton.class);
        bind(IDateLineManagerFactory.class).to(DateLineManagerFactory.class)
                .in(Singleton.class);
        bind(IExecuteBackAction.class).to(ExecuteBackAction.class).in(
                Singleton.class);
        bind(IFormGridManagerFactory.class).to(FormGridManagerFactory.class)
                .in(Singleton.class);
        bind(IRowListDataManagerFactory.class).to(
                RowListDataManagerFactory.class).in(Singleton.class);
        bind(RequestContext.class).toProvider(RequestContextFactory.class);
        bind(ILoginPage.class).to(LoginPage.class).in(Singleton.class);
        bind(IWebPanelResourcesFactory.class)
                .to(WebPanelResourcesFactory.class).in(Singleton.class);
        bind(IJythonClientStart.class).to(JythonClientStart.class).in(
                Singleton.class);
        bind(IChartManagerFactory.class).to(ChartManagerFactory.class).in(
                Singleton.class);
    }

    @Provides
    @Singleton
    LoginViewFactory getLoginFactory() {
        return GwtGiniInjector.getI().getLoginViewFactory();
    }

    @Provides
    @Singleton
    IJythonUIClient getJythonUIClient() {
        return JythonUIClientFactory.construct();
    }

    @Provides
    @Singleton
    ITableAbstractFactories getFactories() {
        return GwtGiniInjector.getI().getITableAbstractFactories();
    }

    @Provides
    @Singleton
    IGetStandardMessage getStandardMess() {
        return GwtGiniInjector.getI().getStandardMessage();
    }

}