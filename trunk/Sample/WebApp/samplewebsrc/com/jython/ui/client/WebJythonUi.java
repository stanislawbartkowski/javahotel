package com.jython.ui.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.gwtmodel.table.factories.ITableAbstractFactories;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.WebPanelHolder;
import com.gwtmodel.table.view.webpanel.IWebPanel;
import com.gwtmodel.table.view.webpanel.WebPanelFactory;
import com.jythonui.client.IJythonUIClient;
import com.jythonui.client.JythonUIClientFactory;
import com.jythonui.client.service.JResource;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WebJythonUi implements EntryPoint {

    private final IJythonUIClient iClient;
    private static final String START = "start.xml";

    public WebJythonUi() {
        iClient = JythonUIClientFactory.construct(new JResource());
    }

    public void onModuleLoad() {

        startW();
        iClient.start(START);
    }

    private void startW() {
        ITableAbstractFactories tFactories = GwtGiniInjector.getI()
                .getITableAbstractFactories();
        tFactories.registerWebPanelResources(new WebPanelResources());

        WebPanelFactory wFactory = GwtGiniInjector.getI().getWebPanelFactory();
        IWebPanel wPan = wFactory.construct(null);
        WebPanelHolder.setWebPanel(wPan);
        RootPanel.get().add(wPan.getWidget());
    }
}
