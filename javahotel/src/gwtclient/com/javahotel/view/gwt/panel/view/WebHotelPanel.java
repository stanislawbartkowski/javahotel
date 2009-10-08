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
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.CommonUtil;
import com.javahotel.client.IImageGallery;
import com.javahotel.client.IResLocator;
import com.javahotel.client.ReadRequestHtml;
import com.javahotel.client.dialog.IClickNextYesNo;
import com.javahotel.client.dialog.ICommand;
import com.javahotel.client.mvc.util.PopupTip;
import com.javahotel.client.mvc.util.YesNoDialog;
import com.javahotel.client.panel.IWebHotelPanel;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class WebHotelPanel implements IWebHotelPanel {

    private final DockPanel dPanel = new DockPanel();
    private final IResLocator resI;

    private enum StatusE {
        NORMAL, REPLYL, ERRORL
    };

    private StatusE sta = StatusE.NORMAL;
    private final Label replyL = new Label();
    private ErrorL errL;

    private Widget wCenter = null;
    private Widget wWest = null;
    private Widget wWest1 = null;
    private final static String HOTELHEADER_LOGOUT = "hotelheader_logout";
    private final static String HOTELHEADER_LOGO = "hotelheader_logo";
    private final static String HOTELHEADER_DOWNMENU = "hotelheader_downmenu";
    private final static String HOTELHEADER_STATUSBAR = "hotelheader_statusbar";
    private final HTML ha;
    private final LogoH logo;
    private final ICommand logOut;

    private final Label tL; // product name
    private final Label ownerName; // owner name
    private final Label userName = new Label();
    private final Label hotelName = new Label();
    private final VerticalPanel vp = new VerticalPanel();
    private HTMLPanel uPanel;

    private void setStatusW(Widget w) {
        if (uPanel == null) {
            return;
        }
        uPanel.addAndReplaceElement(w, HOTELHEADER_STATUSBAR);
    }

    private void setStatusNormal() {
        setStatusW(new Label(""));
        sta = StatusE.NORMAL;
    }

    private void toReplayL() {
        if (sta == StatusE.REPLYL) {
            return;
        }
        replyL.setText("");
        replyL.setStyleName("wait-reply");
        sta = StatusE.REPLYL;
        setStatusW(replyL);
    }

    public Label getReplyL() {
        toReplayL();
        return replyL;
    }

    public void clearReply() {
        setStatusNormal();
    }

    private class ErrorL extends PopupTip {

        ErrorL(String errmess) {
            setMessage(errmess);
            setStyleName("error-reply");
        }

    }

    public void setErrorL(String errmess) {
        if (sta == StatusE.ERRORL) {
            return;
        }
        sta = StatusE.ERRORL;
        errL = new ErrorL(errmess);
        setStatusW(errL);
    }

    public void initStatus() {
        switch (sta) {
        case NORMAL:
            setStatusNormal();
            break;
        case ERRORL:
            setStatusW(errL);
            break;
        case REPLYL:
            setStatusW(replyL);
            break;
        }
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
        uPanel.addAndReplaceElement(pa, HOTELHEADER_DOWNMENU);
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
            YesNoDialog yesD = new YesNoDialog(resI, resI.getLabels()
                    .LogoutQuestion(), yes);
            // PopupUtil.setPos(yesD.getW(), ha);
            yesD.show(ha);
        }
    }

    private void setOut(boolean visible) {
        ha.setVisible(visible);
    }

    private class RequestSet implements ReadRequestHtml.ISetRequestText {

        public void setText(String s) {
            uPanel = new HTMLPanel(s);
            uPanel.addAndReplaceElement(tL, "hotelheader_appname");
            uPanel.addAndReplaceElement(ownerName, "hotelheader_ownername");
            uPanel.addAndReplaceElement(userName, "hotelheader_user");
            uPanel.addAndReplaceElement(hotelName, "hotelheader_hotel");
            uPanel.addAndReplaceElement(ha, HOTELHEADER_LOGOUT);
            uPanel.addAndReplaceElement(logo, HOTELHEADER_LOGO);
            initStatus();
            vp.add(uPanel);
            setOut(false);
        }

    }

    private class LogoH extends PopupTip {
        private final HTML ha;

        LogoH(String h) {
            ha = new HTML(h);
            initWidget(ha);
            setMessage(resI.getLabels().Wersja());
            setMouse();
        }

    }

    WebHotelPanel(final IResLocator rI, ICommand logOut) {
        Window.setTitle("Hotel na Javie");
        resI = rI;
        this.logOut = logOut;
        tL = new Label(resI.getLabels().productName());
        ownerName = new Label("Wersja demonstracyjna");

        String h = CommonUtil.getImageHTML(IImageGallery.LOGOUT, 20, 20);
        ha = new HTML(h);

        ha.addMouseDownHandler(new ClickLogOut());

        h = CommonUtil.getImageHTML(IImageGallery.LOGHOTEL, 20, 20);
        logo = new LogoH(h);

        dPanel.add(vp, DockPanel.NORTH);
        vp.setHeight("25px");
        String res = CommonUtil.getResAdr("header.html");
        ReadRequestHtml.doGet(res, new RequestSet());

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
        setOut(true);
    }

}
