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
package com.jythonui.client;

import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.gwtmodel.table.factories.ITableAbstractFactories;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.WebPanelHolder;
import com.gwtmodel.table.view.webpanel.IWebPanel;
import com.gwtmodel.table.view.webpanel.WebPanelFactory;
import com.jythonui.client.service.JResource;

public class JythonClientStart {
    private static final String START = "start.xml";

    private static final IJythonUIClientResources iRes = new JResource();
    private static final IJythonUIClient iClient = JythonUIClientFactory
            .construct(iRes);

    private static class ResBack implements AsyncCallback<Map<String, String>> {

        private final String startX;

        ResBack(String startX) {
            this.startX = startX;
        }

        private void drawError() {
            RootPanel.get().add(
                    new HTML("<H1>" + M.M().cannotLoadClientResource()
                            + "</H1>"));
        }

        @Override
        public void onFailure(Throwable caught) {
            drawError();

        }

        @Override
        public void onSuccess(Map<String, String> result) {
            if (result == null) {
                drawError();
                return;
            }
            ITableAbstractFactories tFactories = GwtGiniInjector.getI()
                    .getITableAbstractFactories();
            tFactories.registerWebPanelResources(new JythonWebPanelResources(
                    result));

            WebPanelFactory wFactory = GwtGiniInjector.getI()
                    .getWebPanelFactory();
            IWebPanel wPan = wFactory.construct(null);
            WebPanelHolder.setWebPanel(wPan);
            RootPanel.get().add(wPan.getWidget());
            iClient.start(startX == null ? START : startX);
        }

    }

    public static void start(String startXML) {
        iRes.getClientRes(new ResBack(startXML));
    }

}
