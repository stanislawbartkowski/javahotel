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
package com.gwtmodel.table.view.webpanel;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IClickYesNo;
import com.gwtmodel.table.ICommand;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.common.ISignal;
import com.gwtmodel.table.factories.IWebPanelResources;
import com.gwtmodel.table.htmlview.HtmlElemDesc;
import com.gwtmodel.table.htmlview.HtmlPanelFactory;
import com.gwtmodel.table.htmlview.HtmlTypeEnum;
import com.gwtmodel.table.htmlview.IHtmlPanelCallBack;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.injector.MM;
import com.gwtmodel.table.view.util.PopupTip;
import com.gwtmodel.table.view.util.YesNoDialog;

/**
 * 
 * @author stanislaw.bartkowski@gmail.com
 */
class WebPanel implements IWebPanel {

    private final DockPanel dPanel = new DockPanel();
    private final DockPanel middlePanel = new DockPanel();
    private final IWebPanelResources pResources;
    private final CallBackProgress bCounter;
    private String centerSize = "90%";
    private ISignal centreHideSignal = null;

    private final StatusL statusl;
    private Widget wCenter = null;
    private Widget wWest = null;
    private Widget wWest1 = null;
    private final static String HOTELHEADER_LOGOUT = "header_logout";
    private final static String HOTELHEADER_LOGO = "header_logo";
    private final static String HOTELHEADER_DOWNMENU = "header_downmenu";
    private final static String HOTELHEADER_STATUSBAR = "header_statusbar";
    private final static String HEADER_UPINFO = "header_upinfo";
    private final HTML ha;
    private final LogoH logo;
    private final ICommand logOut;
    private final Label tL; // product name
    private final Label ownerName; // owner name
    private final Label userName = new Label();
    private final Label hotelName = new Label();
    private final Label upInfo = new Label();
    private final VerticalPanel vp = new VerticalPanel();
    private final VerticalPanel vmenu = new VerticalPanel();
    private HTMLPanel uPanel = null;

    @Override
    public void setOwnerName(String owner) {
        ownerName.setText(owner);
    }

    @Override
    public void IncDecCounter(boolean inc) {
        bCounter.IncDecL(inc);
    }

    @Override
    public void setCenterSize(String size) {
        this.centerSize = size;
        setWidth();
    }

    @Override
    public void setCentreHideSignal(ISignal iSig) {
        centreHideSignal = iSig;
    }

    public void setUpInfo(String upinfo) {
        if (upinfo == null) {
            upInfo.setVisible(false);
        } else {
            upInfo.setVisible(true);
            upInfo.setText(upinfo);
        }
    }

    public void setTitle(String newTitle) {
        Window.setTitle(newTitle);
    }

    public void setProductName(String productName) {
        tL.setText(productName);
    }

    public void setPullDownMenu(Widget m) {
        vmenu.clear();
        vmenu.add(m);
    }

    @Override
    public void setReplay(int replNo) {
        if (replNo == 1) {
            statusl.setProgres();
            statusl.setStatus(true);
        } else if (replNo == 0)
            statusl.setStatus(false);
    }

    private class StatusL extends PopupTip {

        private final VerticalPanel vp = new VerticalPanel();
        private final Image progress = new Image();
        private final Label l = new Label();

        StatusL() {
            String ima = pResources.getRes(IWebPanelResources.PROGRESSICON);
            String url = Utils.getImageAdr(ima);
            progress.setUrl(url);
            initWidget(vp);
            l.setText(Utils.getEmptytyLabel());
        }

        void setProgres() {
            removeStyleName("error-reply");
            vp.clear();
            vp.add(progress);
        }

        void setError(String errMess) {
            vp.clear();
            vp.add(l);
            setVisible(true);
            setMessage(errMess);
            setStyleName("error-reply");
        }

        void setStatus(boolean visible) {
            setVisible(visible);
        }
    }

    @Override
    public void setErrorL(String errmess) {
        statusl.setError(errmess);
    }

    private void initStatus() {
    }

    private void setWidth() {
        if (wWest != null) {
            dPanel.setCellWidth(middlePanel, centerSize);
        } else {
            dPanel.setCellWidth(middlePanel, "100%");
        }
    }

    @Override
    public void setDCenter(final Widget w) {
        if (wCenter != null) {
            middlePanel.remove(wCenter);
            if (centreHideSignal != null) {
                centreHideSignal.signal();
            }
        }
        wCenter = w;
        if (w != null) {
            middlePanel.add(w, DockPanel.CENTER);
            setWidth();
        }
        centreHideSignal = null;
    }

    @Override
    public void setMenuPanel(Widget w) {
        uPanel.addAndReplaceElement(w, HOTELHEADER_DOWNMENU);
    }

    @Override
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

        @Override
        public void onMouseDown(MouseDownEvent event) {
            IClickYesNo yes = new IClickYesNo() {
                @Override
                public void click(boolean yes) {
                    if (!yes) {
                        return;
                    }
                    logOut.execute();
                }
            };
            String q = pResources.getRes(IWebPanelResources.LOGOUTQUESTION);
            if (q == null)
                q = MM.getL().LogOutQuestion();
            YesNoDialog yesD = new YesNoDialog(q, null, yes);
            yesD.show(ha);
        }
    }

    private void setOut(boolean visible) {
        ha.setVisible(visible);
    }

    private class PanelCallback implements IHtmlPanelCallBack {

        @Override
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
            VerticalPanel vp = new VerticalPanel();
            vp.add(new Label(pResources.getRes(IWebPanelResources.VERSION)));
            vp.add(new Label(LogT.getT().GWTVersion(GWT.getVersion())));
            setMessage(vp);
        }
    }

    WebPanel(IWebPanelResources pResources, ICommand logOut) {
        this.pResources = pResources;
        statusl = new StatusL();
        bCounter = new CallBackProgress(this);
        Window.setTitle(pResources.getRes(IWebPanelResources.TITLE));
        this.logOut = logOut;
        tL = new Label(pResources.getRes(IWebPanelResources.PRODUCTNAME));
        ownerName = new Label(pResources.getRes(IWebPanelResources.OWNERNAME));

        String h = Utils.getImageHTML(
                pResources.getRes(IWebPanelResources.IMAGELOGOUT), 20, 20);
        ha = new HTML(h);

        ha.addMouseDownHandler(new ClickLogOut());

        h = Utils.getImageHTML(
                pResources.getRes(IWebPanelResources.IIMAGEPRODCUT), 20, 20);
        logo = new LogoH(h);

        dPanel.add(vp, DockPanel.NORTH);
        vp.setHeight("25px");

        dPanel.add(middlePanel, DockPanel.CENTER);
        middlePanel.add(this.vmenu, DockPanel.NORTH);

        List<HtmlElemDesc> hList = new ArrayList<HtmlElemDesc>();
        hList.add(new HtmlElemDesc(tL, "header_appname"));
        hList.add(new HtmlElemDesc(ownerName, "header_ownername"));
        hList.add(new HtmlElemDesc(userName, "header_user"));
        hList.add(new HtmlElemDesc(hotelName, "header_ename"));
        hList.add(new HtmlElemDesc(ha, HOTELHEADER_LOGOUT));
        hList.add(new HtmlElemDesc(logo, HOTELHEADER_LOGO));
        hList.add(new HtmlElemDesc(statusl, HOTELHEADER_STATUSBAR));
        hList.add(new HtmlElemDesc(upInfo, HEADER_UPINFO));

        HtmlPanelFactory fa = GwtGiniInjector.getI().getHtmlPanelFactory();
        fa.getHtmlPanel(HtmlTypeEnum.MainStatus, new PanelCallback(), hList);
    }

    @Override
    public Widget getWidget() {
        return dPanel;
    }

    @Override
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

    @Override
    public void setUserData(String user, String hotel) {
        userName.setText(user);
        if (hotel != null) {
            hotelName.setText(hotel);
        }
        setOut(true);
    }
}
