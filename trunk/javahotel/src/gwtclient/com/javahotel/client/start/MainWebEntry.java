package com.javahotel.client.start;

import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;
import com.javahotel.client.CallBackHotel;
import com.javahotel.client.GWTGetService;
import com.javahotel.client.HoLabel;
import com.javahotel.client.HoMessages;
import com.javahotel.client.IResLocator;
import com.javahotel.client.URights;
import com.javahotel.client.dialog.ICommand;
import com.javahotel.client.dispatcher.IDispatch;
import com.javahotel.client.injector.ResLocatorHolder;
import com.javahotel.client.panel.IWebHotelPanel;
import com.javahotel.client.panel.WebHotelPanelFactory;
import com.javahotel.client.rdata.RData;
import com.javahotel.client.rhtml.IReadRes;
import com.javahotel.client.rhtml.ReadResFactory;
import com.javahotel.view.IViewInterface;
import com.javahotel.view.gwt.GwtGetViewFactory;

public class MainWebEntry implements IWebEntry {

    private IWebHotelPanel wPan;
    private CallBackProgress cProgress;
    private final HoLabel sLab = (HoLabel) GWT.create(HoLabel.class);
    private final HoMessages sMess = (HoMessages) GWT.create(HoMessages.class);
    private RData rD;
    private final IDispatch di;
    private final URights ur = new URights();
    private final IViewInterface[] iView = new IViewInterface[2];
    private Map<String, String> param;
    private boolean googletable;

    public void setGoogletable(boolean googletable) {
        this.googletable = googletable;
    }

    private IReadRes readRes;

    private class BackLogOut extends CallBackHotel<Object> {

        BackLogOut(IResLocator rI) {
            super(rI);
        }

        @Override
        public void onMySuccess(Object arg) {
            RootPanel.get().remove(0);
            start();
        }
    }

    private class LogOut implements ICommand {

        private final IResLocator rI;

        LogOut(IResLocator rI) {
            this.rI = rI;
        }

        public void execute() {
            GWTGetService.getService().logout(new BackLogOut(rI));
        }
    }

    /** Creates a new instance of webEntryPoint */
    @Inject
    public MainWebEntry(IDispatch di) {
        this.di = di;
    }

    private class ResC implements IResLocator {

        public IWebHotelPanel getPanel() {
            return wPan;
        }

        public void IncDecCounter(final boolean inc) {
            cProgress.IncDecL(inc);
        }

        public HoLabel getLabels() {
            return sLab;
        }

        public HoMessages getMessages() {
            return sMess;
        }

        public RData getR() {
            return rD;
        }

        public IDispatch getDi() {
            return di;
        }

        public URights getUR() {
            return ur;
        }

        public IViewInterface getView(int t) {
            return iView[t];
        }

        public IViewInterface getView() {
            // return getView(IViewInterface.EXT);
            return getView(IViewInterface.GWT);
        }

        public String getParam(String key) {
            return param.get(key);
        }

        public boolean withGoogleTable() {
            return googletable;
        }

        public IReadRes readRes() {
            return readRes;
        }
    }

    private class readP extends CallBackHotel<Map<String, String>> {

        readP(IResLocator rI) {
            super(rI);
        }

        @Override
        public void onMySuccess(Map<String, String> arg) {
            param = arg;
        }
    }

    public void start() {
        // String ba = GWT.getModuleBaseURL();
        // String ba1 = GWT.getHostPageBaseURL();
        iView[IViewInterface.GWT] = GwtGetViewFactory.getView();
        IResLocator rI = new ResC();
        ResLocatorHolder.setrI(rI);
        readRes = ReadResFactory.getReadRes(rI);
        cProgress = new CallBackProgress(rI);
        rD = new RData(rI);
        wPan = WebHotelPanelFactory.getPanel(rI, new LogOut(rI));
        RootPanel.get().add(wPan.getWidget());
        di.start(rI, new DecorateAfterLogin(rI));
        GWTGetService.getService().getParam(new readP(rI));
    }

}
