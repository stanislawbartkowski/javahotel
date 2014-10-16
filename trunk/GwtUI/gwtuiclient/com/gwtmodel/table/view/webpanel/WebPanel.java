/*
 * Copyright 2014 stanislawbartkowski@gmail.com
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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
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
import com.gwtmodel.table.IConsts;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.common.ISignal;
import com.gwtmodel.table.factories.IWebPanelResources;
import com.gwtmodel.table.htmlview.HtmlElemDesc;
import com.gwtmodel.table.htmlview.HtmlPanelFactory;
import com.gwtmodel.table.htmlview.HtmlTypeEnum;
import com.gwtmodel.table.htmlview.IHtmlPanelCallBack;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.injector.MM;
import com.gwtmodel.table.smessage.IGetStandardMessage;
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
    private final Button outButt;
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
    private boolean autologoutmode = true;

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

    public void setPullDownMenu(Widget m) {
        vmenu.clear();
        if (m != null)
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
        if (w == null)
            w = new Label("");
        if (w != null) {
            uPanel.addAndReplaceElement(w, HOTELHEADER_DOWNMENU);
            // important: set id again to enable next replacement by
            // HOTELHEADER_DOWNMENU
            w.getElement().setId(HOTELHEADER_DOWNMENU);
        }
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

    private class ClickLogOut implements ClickHandler {

        @Override
        public void onClick(ClickEvent event) {
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
            yesD.show(new WSize(event.getRelativeElement()));
        }
    }

    private void setOut(boolean visible) {
        outButt.setVisible(visible);
    }

    private class PanelCallback implements IHtmlPanelCallBack {

        @Override
        public void setHtmlPanel(Panel ha) {
            uPanel = (HTMLPanel) ha;
            initStatus();
            dPanel.add(uPanel, DockPanel.NORTH);
            uPanel.setHeight("25px");
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
            String JVersion = pResources.getRes(IWebPanelResources.JUIVERSION);
            vp.add(new Label(LogT.getT().GWTVersion(GWT.getVersion(),JVersion)));
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
                pResources.getRes(IWebPanelResources.IIMAGEPRODUCT),
                IConsts.headerImageWidth, IConsts.headerImageHeight, null);
        logo = new LogoH(h);

        dPanel.add(vp, DockPanel.NORTH);

        dPanel.add(middlePanel, DockPanel.CENTER);
        middlePanel.add(this.vmenu, DockPanel.NORTH);

        List<HtmlElemDesc> hList = new ArrayList<HtmlElemDesc>();
        hList.add(new HtmlElemDesc(tL, "header_appname"));
        hList.add(new HtmlElemDesc(ownerName, "header_ownername"));
        hList.add(new HtmlElemDesc(userName, "header_user"));
        hList.add(new HtmlElemDesc(hotelName, "header_ename"));
        String hout = Utils.getImageHTML(
                pResources.getRes(IWebPanelResources.IMAGELOGOUT),
                IConsts.headerImageWidth, IConsts.headerImageHeight,
                IConsts.LOGOUTHTMLNAME);
        outButt = new Button();
        outButt.setHTML(hout);
        outButt.addClickHandler(new ClickLogOut());
        hList.add(new HtmlElemDesc(outButt, HOTELHEADER_LOGOUT));
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
    public void setPaneText(InfoType t, String te) {
        IGetStandardMessage iMess = GwtGiniInjector.getI().getStandardMessage();
        String text = iMess.getMessage(te);
        switch (t) {
        case USER:
            userName.setText(text);
            if (autologoutmode)
                setOut(text != null ? true : false);
            break;
        case DATA:
            hotelName.setText(text);
            break;
        case OWNER:
            ownerName.setText(text);
            break;
        case TITLE:
            Window.setTitle(text);
            break;
        case PRODUCT:
            tL.setText(text);
            break;
        case UPINFO:
            if (text == null) {
                upInfo.setVisible(false);
            } else {
                upInfo.setVisible(true);
                upInfo.setText(text);
            }
            break;

        }

    }

    @Override
    public void logOut() {
        logOut.execute();
    }

    @Override
    public void setLogOutMode(boolean logout) {
        autologoutmode = logout;
    }
}
