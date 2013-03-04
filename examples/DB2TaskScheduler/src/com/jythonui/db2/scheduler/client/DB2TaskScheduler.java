/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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
package com.jythonui.db2.scheduler.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.gwtmodel.table.factories.ITableAbstractFactories;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.WebPanelHolder;
import com.gwtmodel.table.view.webpanel.IWebPanel;
import com.gwtmodel.table.view.webpanel.WebPanelFactory;
import com.jythonui.client.IJythonUIClient;
import com.jythonui.client.JythonUIClientFactory;
import com.jythonui.client.service.JResource;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class DB2TaskScheduler implements EntryPoint {

    private final IJythonUIClient iClient;
    private static final String START = "start.xml";

    public DB2TaskScheduler() {
        iClient = JythonUIClientFactory.construct(new JResource());
    }

    public void onModuleLoad() {

        ITableAbstractFactories tFactories = GwtGiniInjector.getI()
                .getITableAbstractFactories();
        tFactories.registerWebPanelResources(new WebPanelResources());

        startW();
        iClient.start(START);
    }

    private void startW() {
        WebPanelFactory wFactory = GwtGiniInjector.getI().getWebPanelFactory();
        IWebPanel wPan = wFactory.construct(null);
        WebPanelHolder.setWebPanel(wPan);
        RootPanel.get().add(wPan.getWidget());
    }
}
