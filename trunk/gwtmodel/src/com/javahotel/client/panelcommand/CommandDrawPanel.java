/*
 * Copyright 2009 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.panelcommand;

import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.view.webpanel.IWebPanel;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.dialog.ISetGwtWidget;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class CommandDrawPanel {

    private CommandDrawPanel() {
    }

    private static void psetW(IWebPanel iW, final Widget w) {
        iW.setDCenter(w);
    }

    public static void setC(final IResLocator rI, final IPanelCommand ic,
            boolean setWest) {

        final IWebPanel iW = GwtGiniInjector.getI().getWebPanel();
        if (setWest) {
            IMvcWidget iw = ic.getWestWidget();
            Widget w = null;
            if (iw != null) {
                w = iw.getWidget();
            }
            iW.setWest1(w);
        }

        ISetGwtWidget i = new ISetGwtWidget() {

            @Override
            public void setGwtWidget(IMvcWidget i) {
                if (i == null) {
                    psetW(iW, null);
                    return;
                }
                psetW(iW, i.getWidget());
                ic.drawAction();
            }
        };
        ic.beforeDrawAction(i);
    }

    public static void setC(final IResLocator rI, final IPanelCommand ic) {
        setC(rI, ic, true);

    }
}
