package com.javahotel.client.start;

import java.util.Map;

import com.google.gwt.user.client.ui.RootPanel;
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
import com.javahotel.client.MM;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.injector.ResLocatorHolder;
import com.javahotel.client.rdata.RData;
import com.javahotel.client.start.action.IStartHotelAction;
import com.javahotel.nmvc.factories.RegisterFactories;

public class MainWebEntry implements IWebEntry {

//    private final HoLabel sLab = (HoLabel) GWT.create(HoLabel.class);
//    private final HoMessages sMess = (HoMessages) GWT.create(HoMessages.class);
    private RData rD;
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

    private class ResC implements IResLocator {

        public HoLabel getLabels() {
            return MM.L();
        }

        public HoMessages getMessages() {
            return MM.M();
        }

        public RData getR() {
            return rD;
        }

        public String getParam(String key) {
            return param.get(key);
        }

        public IReadRes readRes() {
            return readRes;
        }

    }

    private void startA() {
        IStartHotelAction i = HInjector.getI().getStartHotelAction();
        i.execute();
    }

    private class readP extends CommonCallBack<Map<String, String>> {

        @Override
        public void onMySuccess(Map<String, String> arg) {
            param = arg;
            startA();
        }
    }

    public void start() {
        IResLocator rI = new ResC();
        ResLocatorHolder.setrI(rI);
        RegisterFactories rFactories = HInjector.getI().getRegisterFactories();
        rFactories.register();
        readRes = GwtGiniInjector.getI().getReadResFactory().getReadRes();
        rD = new RData();
        WebPanelFactory wFactory = GwtGiniInjector.getI().getWebPanelFactory();
        IWebPanel wPan = wFactory.construct(new WebPanelResources(rI),
                new LogOut());
        WebPanelHolder.setWebPanel(wPan);
        RootPanel.get().add(wPan.getWidget());
        GWTGetService.getService().getParam(new readP());
    }

}
