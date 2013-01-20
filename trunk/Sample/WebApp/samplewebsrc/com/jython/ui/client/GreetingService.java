package com.jython.ui.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
    DialogFormat getDialogFormat(String name);

    DialogVariables runAction(DialogVariables v, String name, String actionId);

}
