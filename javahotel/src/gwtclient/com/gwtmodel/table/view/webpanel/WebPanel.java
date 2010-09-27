/*
 * Copyright 2010 stanislawbartkowski@gmail.com
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
package com.gwtmodel.table.view.webpanel;

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
import com.gwtmodel.table.IClickNextYesNo;
import com.gwtmodel.table.ICommand;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.htmlview.HtmlElemDesc;
import com.gwtmodel.table.htmlview.HtmlPanelFactory;
import com.gwtmodel.table.htmlview.HtmlTypeEnum;
import com.gwtmodel.table.htmlview.IHtmlPanelCallBack;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.view.util.PopupTip;
import com.gwtmodel.table.view.util.YesNoDialog;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author stanislaw.bartkowski@gmail.com
 */
class WebPanel implements IWebPanel {

    private final DockPanel dPanel = new DockPanel();
    private final IWebPanelResources pResources;
    private final CallBackProgress bCounter;
    private String centerSize = "90%";

    public void setOwnerName(String owner) {
        ownerName.setText(owner);
    }

    public void IncDecCounter(boolean inc) {
        bCounter.IncDecL(inc);
    }

    public void setCenterSize(String size) {
        this.centerSize = size;
        setWidth();
    }

    private enum StatusE {

        NORMAL, REPLYL, ERRORL
    };
    private StatusE sta = StatusE.NORMAL;
    private final StatusL status = new StatusL(null);
    private Widget wCenter = null;
    private Widget wWest = null;
    private Widget wWest1 = null;
    private final static String HOTELHEADER_LOGOUT = "header_logout";
    private final static String HOTELHEADER_LOGO = "header_logo";
    private final static String HOTELHEADER_DOWNMENU = "header_downmenu";
    private final static String HOTELHEADER_STATUSBAR = "header_statusbar";
    private final HTML ha;
    private final LogoH logo;
    private final ICommand logOut;
    private final Label tL; // product name
    private final Label ownerName; // owner name
    private final Label userName = new Label();
    private final Label hotelName = new Label();
    private final VerticalPanel vp = new VerticalPanel();
    private HTMLPanel uPanel = null;

    private void setStatusNormal() {
        status.setVisible(false);
        sta = StatusE.NORMAL;
    }

    private void toReplayL() {
        if (sta == StatusE.REPLYL) {
            return;
        }
        sta = StatusE.REPLYL;
        status.setVisible(true);
        status.l.setText(null);
        status.setStyleName("wait-reply");
    }

    public Label getReplyL() {
        toReplayL();
        return status.l;
    }

    public void clearReply() {
        setStatusNormal();
    }

    private class StatusL extends PopupTip {

        final Label l = new Label("");

        StatusL(String errmess) {
            initWidget(l);
            setMessage(errmess);
            setStyleName("error-reply");
            setMouse();
        }
    }

    public void setErrorL(String errmess) {
        if (sta == StatusE.ERRORL) {
            return;
        }
        sta = StatusE.ERRORL;
        status.setVisible(true);
        status.l.setText(Utils.getEmptytyLabel());
        status.setMessage(errmess);
        status.setStyleName("error-reply");
    }

    public void initStatus() {
        switch (sta) {
            case NORMAL:
                setStatusNormal();
                break;
        }
    }

    private void setWidth() {
        if (wCenter != null) {
            if (wWest != null) {
//                dPanel.setCellWidth(wCenter, "90%");
//                dPanel.setCellWidth(wCenter, "70%");
                dPanel.setCellWidth(wCenter, centerSize);
            } else {
                dPanel.setCellWidth(wCenter, "100%");
            }
        }
    }

    public void setDCenter(final Widget w) {
        if (wCenter != null) {
            dPanel.remove(wCenter);
        }
        wCenter = w;
        if (w != null) {
            dPanel.add(w, DockPanel.CENTER);
            setWidth();
        }
    }

    public void setMenuPanel(Widget w) {
        uPanel.addAndReplaceElement(w, HOTELHEADER_DOWNMENU);
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
            YesNoDialog yesD = new YesNoDialog(pResources.getRes(IWebPanelResources.LOGOUTQUESTION), null, yes);
            yesD.show(new WSize(ha));
        }
    }

    private void setOut(boolean visible) {
        ha.setVisible(visible);
    }

    private class PanelCallback implements IHtmlPanelCallBack {

        public void setHtmlPanel(Panel ha) {
            uPanel = (HTMLPanel) ha;
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
            setMessage(pResources.getRes(IWebPanelResources.WERSJA));
            setMouse();
        }
    }

    WebPanel(IWebPanelResources pResources, ICommand logOut) {
        this.pResources = pResources;
        bCounter = new CallBackProgress(this, pResources);
        Window.setTitle(pResources.getRes(IWebPanelResources.TITLE));
        this.logOut = logOut;
        tL = new Label(pResources.getRes(IWebPanelResources.PRODUCTNAME));
        ownerName = new Label(pResources.getRes(IWebPanelResources.OWNERNAME));

        String h = Utils.getImageHTML(pResources.getRes(IWebPanelResources.IMAGELOGOUT), 20, 20);
        ha = new HTML(h);

        ha.addMouseDownHandler(new ClickLogOut());

        h = Utils.getImageHTML(pResources.getRes(IWebPanelResources.IIMAGEPRODCUT), 20, 20);
        logo = new LogoH(h);

        dPanel.add(vp, DockPanel.NORTH);
        vp.setHeight("25px");

        List<HtmlElemDesc> hList = new ArrayList<HtmlElemDesc>();
        hList.add(new HtmlElemDesc(tL, "header_appname"));
        hList.add(new HtmlElemDesc(ownerName, "header_ownername"));
        hList.add(new HtmlElemDesc(userName, "header_user"));
        hList.add(new HtmlElemDesc(hotelName, "header_ename"));
        hList.add(new HtmlElemDesc(ha, HOTELHEADER_LOGOUT));
        hList.add(new HtmlElemDesc(logo, HOTELHEADER_LOGO));
        hList.add(new HtmlElemDesc(status, HOTELHEADER_STATUSBAR));

        HtmlPanelFactory fa = GwtGiniInjector.getI().getHtmlPanelFactory();
        fa.getHtmlPanel(HtmlTypeEnum.MainStatus,
                new PanelCallback(), hList);

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
        setWidth();
    }

    public void setUserData(String user, String hotel) {
        userName.setText(user);
        if (hotel != null) {
            hotelName.setText(hotel);
        }
        setOut(true);
    }
}
