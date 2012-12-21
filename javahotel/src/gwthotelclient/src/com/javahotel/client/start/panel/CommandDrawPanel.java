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
package com.javahotel.client.start.panel;

import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.ISetGWidget;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.view.webpanel.IWebPanel;
import com.javahotel.client.IResLocator;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class CommandDrawPanel {

    private CommandDrawPanel() {
    }

    private static void psetW(IWebPanel iW, final Widget w) {
        iW.setDCenter(w);
    }

    static void setC(final IResLocator rI, final IPanelCommand ic,
            String menuName) {

        final IWebPanel iW = GwtGiniInjector.getI().getWebPanel();

        ISetGWidget ii = new ISetGWidget() {

            @Override
            public void setW(IGWidget i) {
                if (i == null) {
                    psetW(iW, null);
                    return;
                }
                psetW(iW, i.getGWidget());

            }
        };
        iW.setUpInfo(menuName); // menuName null value is allowed
        ic.beforeDrawAction(ii);
    }

}
