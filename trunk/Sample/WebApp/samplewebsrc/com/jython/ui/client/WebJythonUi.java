package com.jython.ui.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.gwtmodel.table.factories.ITableAbstractFactories;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.WebPanelHolder;
import com.gwtmodel.table.view.webpanel.IWebPanel;
import com.gwtmodel.table.view.webpanel.WebPanelFactory;
import com.jythonui.client.IJythonUIClient;
import com.jythonui.client.IJythonUIClientResources;
import com.jythonui.client.JythonUIClientFactory;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WebJythonUi implements EntryPoint {

    /**
     * Create a remote service proxy to talk to the server-side Greeting
     * service.
     */
    private final GreetingServiceAsync greetingService = GWT
            .create(GreetingService.class);

    private class JResource implements IJythonUIClientResources {

        @Override
        public void getDialogFormat(String name,
                AsyncCallback<DialogFormat> callback) {
            greetingService.getDialogFormat(name, callback);

        }

        @Override
        public void runAction(DialogVariables v, String name, String actionId,
                AsyncCallback<DialogVariables> callback) {
            greetingService.runAction(v, name, actionId, callback);
        }

    }

    private final IJythonUIClient iClient;
    private static final String START = "start.xml";

    public WebJythonUi() {
        iClient = JythonUIClientFactory.construct(new JResource());
    }

    public void onModuleLoad() {

        ITableAbstractFactories tFactories = GwtGiniInjector.getI()
                .getITableAbstractFactories();
        tFactories.registerGetCustomValues(new CustomFactory());
        tFactories
                .registerDataFormConstructorAbstractFactory(new FormFactory());

        startW();
        iClient.start(START);
    }

    private void startW() {
        WebPanelFactory wFactory = GwtGiniInjector.getI().getWebPanelFactory();
        IWebPanel wPan = wFactory.construct(new WebPanelResources(), null);
        WebPanelHolder.setWebPanel(wPan);
        RootPanel.get().add(wPan.getWidget());
    }
}
