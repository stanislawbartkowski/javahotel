package com.jython.ui.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.jython.ui.client.GreetingService;
import com.jython.ui.server.guice.ServiceInjector;
import com.jythonui.server.IJythonUIServer;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
        GreetingService {

    private final IJythonUIServer iServer;

    public GreetingServiceImpl() {
        iServer = ServiceInjector.contructJythonUiServer();
    }

    @Override
    public DialogFormat getDialogFormat(String name) {
        return iServer.findDialog(name);
    }

    @Override
    public DialogVariables runAction(DialogVariables v, String name,
            String actionId) {
        return iServer.runAction(v, name, actionId);
    }

}
