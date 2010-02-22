package com.javahotel.client.start;

import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;
import com.gwtmodel.table.ICommand;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.WebPanelHolder;
import com.gwtmodel.table.readres.IReadRes;
import com.gwtmodel.table.view.callback.CommonCallBack;
import com.gwtmodel.table.view.webpanel.IWebPanel;
import com.gwtmodel.table.view.webpanel.WebPanelFactory;
import com.javahotel.client.GWTGetService;
import com.javahotel.client.HoLabel;
import com.javahotel.client.HoMessages;
import com.javahotel.client.IResLocator;
import com.javahotel.client.URights;
import com.javahotel.client.dispatcher.IDispatch;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.injector.ResLocatorHolder;
import com.javahotel.client.rdata.RData;
import com.javahotel.nmvc.factories.RegisterFactories;
import com.javahotel.view.IViewInterface;
import com.javahotel.view.gwt.GwtGetViewFactory;

public class MainWebEntry implements IWebEntry {

    // private IWebPanel wPan;
    // private CallBackProgress cProgress;
    private final HoLabel sLab = (HoLabel) GWT.create(HoLabel.class);
    private final HoMessages sMess = (HoMessages) GWT.create(HoMessages.class);
    private RData rD;
    private final IDispatch di;
    private final URights ur = new URights();
    private final IViewInterface[] iView = new IViewInterface[2];
    private Map<String, String> param;

    private IReadRes readRes;

    private class BackLogOut extends CommonCallBack<Object> {

        @Override
        public void onMySuccess(Object arg) {
            // RootPanel.get().remove(0);
            RootPanel.get().clear();
            start();
        }
    }

    private class LogOut implements ICommand {

        public void execute() {
            GWTGetService.getService().logout(new BackLogOut());
        }
    }

    /** Creates a new instance of webEntryPoint */
    @Inject
    public MainWebEntry(IDispatch di) {
        this.di = di;
    }

    private class ResC implements IResLocator {

        // public IWebPanel getPanel() {
        // return wPan;
        // }

        // public void IncDecCounter(final boolean inc) {
        // cProgress.IncDecL(inc);
        // }

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

        public IReadRes readRes() {
            return readRes;
        }
    }

    private class readP extends CommonCallBack<Map<String, String>> {

        @Override
        public void onMySuccess(Map<String, String> arg) {
            param = arg;
        }
    }

    public void start() {
        iView[IViewInterface.GWT] = GwtGetViewFactory.getView();
        IResLocator rI = new ResC();
        ResLocatorHolder.setrI(rI);
        RegisterFactories rFactories = HInjector.getI().getRegisterFactories();
        rFactories.register();
        readRes = GwtGiniInjector.getI().getReadResFactory().getReadRes();
        // cProgress = new CallBackProgress(rI);
        rD = new RData(rI);
        // wPan = WebHotelPanelFactory.getPanel(rI, new LogOut(rI));
        WebPanelFactory wFactory = GwtGiniInjector.getI().getWebPanelFactory();
        IWebPanel wPan = wFactory.construct(new WebPanelResources(rI),
                new LogOut());
        WebPanelHolder.setWebPanel(wPan);
        RootPanel.get().add(wPan.getWidget());
        di.start(rI, new DecorateAfterLogin(rI));
        GWTGetService.getService().getParam(new readP());
    }

}
