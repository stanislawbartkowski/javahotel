/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
package com.javahotel.view.gwt.panel.view;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.CommonUtil;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.IClickNextYesNo;
import com.javahotel.client.dialog.ICommand;
import com.javahotel.client.mvc.util.YesNoDialog;
import com.javahotel.client.panel.IWebHotelPanel;
import com.javahotel.client.widgets.popup.PopupUtil;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class WebHotelPanel implements IWebHotelPanel {

    private final DockPanel dPanel = new DockPanel();
    private final IResLocator resI;
    private final Label hotelName = new Label();
    private final HorizontalPanel uPanel;
    private boolean isReplyL;
    private final Label replyL = new Label();
    private final Label userName = new Label();
    private Widget wCenter = null;
    private Widget wWest = null;
    private Widget wWest1 = null;
    private final static String LOGOUTIMAGE = "up_one_level.gif";
    private final HTML ha = new HTML();
    private final ICommand logOut;

    private void toReplayL() {
        if (isReplyL) {
            return;
        }
        uPanel.setCellWidth(replyL, "10%");
        isReplyL = true;
    }

    private void toErrorL() {
        if (isReplyL) {
            uPanel.setCellWidth(replyL, "20%");
        }
        isReplyL = true;
    }

    public Label getReplyL() {
        toReplayL();
        return replyL;
    }

    public Label getErrorL() {
        toErrorL();
        return replyL;
    }

    public void setDCenter(final Widget w) {
        if (wCenter != null) {
            dPanel.remove(wCenter);
        }
        wCenter = w;
        if (w != null) {
            dPanel.add(w, DockPanel.CENTER);
            dPanel.setCellWidth(w, "100%");
        }
    }

    public void setMenuPanel(Panel pa) {
        uPanel.insert(pa, 3);
    }

    public void setWest1(Widget w) {
        if (wWest1 != null) {
            dPanel.remove(wWest1);
        }
        if (w != null) {
            dPanel.add(w, DockPanel.WEST);
        }
        wWest1 = w;
    }

    private class ClickLogOut implements MouseDownHandler {

        public void onMouseDown(MouseDownEvent event) {
            IClickNextYesNo yes = new IClickNextYesNo() {

                public void click(boolean yes) {
                    if (!yes) {
                        return;
                    }
                    logOut.execute();
                }
            };
            YesNoDialog yesD = new YesNoDialog(resI, resI.getLabels().LogoutQuestion(), yes);
            PopupUtil.setPos(yesD.getW(), ha);
            yesD.show();
        }
    }

    WebHotelPanel(final IResLocator rI, ICommand logOut) {
        Window.setTitle("Hotel na Javie");
        resI = rI;
        this.logOut = logOut;
        uPanel = new HorizontalPanel();
        uPanel.setStyleName("header-panel");
        Label tL = new Label(resI.getLabels().productName());
        uPanel.add(tL);
        tL.addStyleName("product-name");
        uPanel.setCellWidth(tL, "15%");

        uPanel.add(hotelName);
        hotelName.addStyleName("hotel-name");
        uPanel.setCellWidth(hotelName, "15%");

        uPanel.add(userName);
        hotelName.addStyleName("hotel-name");
        uPanel.setCellWidth(userName, "15%");

        uPanel.add(ha);
        ha.addMouseDownHandler(new ClickLogOut());


        uPanel.add(replyL);
        isReplyL = false;
        toReplayL();
//        uPanel.setCellWidth(replyL, "10%");
        uPanel.setCellHorizontalAlignment(replyL,
                HasHorizontalAlignment.ALIGN_RIGHT);
        uPanel.setWidth("100%");
        dPanel.add(uPanel, DockPanel.NORTH);
        dPanel.setWidth("100%");
    }

    public Widget getWidget() {
        return dPanel;
    }

    public void setWest(Widget w) {
        if (wWest != null) {
            dPanel.remove(wWest);
        }
        if (w != null) {
            dPanel.add(w, DockPanel.WEST);
        }
        wWest = w;
    }

    public void setUserHotel(String user, String hotel) {
        userName.setText(user);
        hotelName.setText(hotel);
        String h = CommonUtil.getImageHTML(LOGOUTIMAGE);
        ha.setHTML(h);
    }
}
