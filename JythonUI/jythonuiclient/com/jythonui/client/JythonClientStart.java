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

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.gwtmodel.table.ICommand;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.factories.ITableAbstractFactories;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.WebPanelHolder;
import com.gwtmodel.table.view.webpanel.IWebPanel;
import com.gwtmodel.table.view.webpanel.WebPanelFactory;
import com.jythonui.client.login.LoginPage;
import com.jythonui.shared.ClientProp;
import com.jythonui.shared.ICommonConsts;

public class JythonClientStart {
    private static final String START = "start.xml";

    private static final IJythonUIClient iClient = JythonUIClientFactory
            .construct();

    private static class ResBack implements AsyncCallback<ClientProp> {

        private String startX;
        private String shiroRealm;

        private class AfterLogin implements ICommand {

            @Override
            public void execute() {
                iClient.start(startX);
            }

        }

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

        private class LogOut implements ICommand {

            private final boolean auth;

            LogOut(boolean auth) {
                this.auth = auth;
            }

            @Override
            public void execute() {
                IWebPanel wPanel = GwtGiniInjector.getI().getWebPanel();
                wPanel.setWest1(null);
                wPanel.setWest(null);
                wPanel.setUserData(null, null);
                startBegin(auth);
            }

        }

        private void startBegin(boolean auth) {
            ICommand co = new AfterLogin();
            if (auth) {
                LoginPage.login(shiroRealm, co);
            } else
                co.execute();
        }

        @Override
        public void onSuccess(ClientProp result) {
            if (result == null) {
                drawError();
                return;
            }
            // resolve root dialog
            if (CUtil.EmptyS(startX)) {
                startX = Utils.getURLParam(ICommonConsts.STARTPAGE);
            }
            if (CUtil.EmptyS(startX)) {
                startX = START;
            }
            shiroRealm = result.getAttr(ICommonConsts.SHIROREALM);
            // resolve if authentication is required
            boolean auth = result.isAuthenticate();
            if (!auth) {
                // not global authentication
                String authPages = result.getLoginPage();
                if (!CUtil.EmptyS(authPages)) {
                    // verify if starting page on the list of pages requiring
                    // authentication
                    String[] pList = authPages
                            .split(ICommonConsts.LOGINDELIMITER);
                    for (String s : pList) {
                        if (s.equals(startX)) {
                            auth = true;
                        }
                    }
                }
            }

            // initialize several factories
            ITableAbstractFactories tFactories = GwtGiniInjector.getI()
                    .getITableAbstractFactories();
            tFactories.registerWebPanelResources(new JythonWebPanelResources(
                    result));

            // construct WebPanel handler
            WebPanelFactory wFactory = GwtGiniInjector.getI()
                    .getWebPanelFactory();
            IWebPanel wPan = wFactory.construct(new LogOut(auth));
            WebPanelHolder.setWebPanel(wPan);
            RootPanel.get().add(wPan.getWidget());
            // start running
            startBegin(auth);
        }

    }

    public static void start(String startXML) {
        M.JR().getClientRes(new ResBack(startXML));
    }

}
