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
package com.javahotel.client.panelcommand;

// TODO: not in use

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.IParamKey;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.IMvcWidget;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class ReportsTest {

    private final VerticalPanel vp = new VerticalPanel();
    private final IResLocator rI;

    ReportsTest(IResLocator rI) {
        this.rI = rI;

    }

    public void beforeDrawAction() {
        Button bu = new Button("Raport");
        ClickListener cl = new ClickListener() {

            public void onClick(Widget sender) {
                String urlPref = rI.getParam(IParamKey.REPORTURL);
//                        String url = "http://localhost:8143/birt/frameset?__report=test.rptdesign&sample=my+parameter";
                String rep = "/home/hotel/workspace/hotelbirt/hotelrep1/report1.rptdesign";
//                        String url = urlPref +
//                                "/frameset?__report=test.rptdesign&sample=my+parameter";
                String url = urlPref +
                        "/frameset?__report=" + rep;
                Window.open(url, "", "");
            }
        };
        bu.addClickListener(cl);
        vp.add(bu);
    }

    public void drawAction() {
    }

    public IMvcWidget getMWidget() {
        // TODO Auto-generated method stub
        return null;
    }
}
