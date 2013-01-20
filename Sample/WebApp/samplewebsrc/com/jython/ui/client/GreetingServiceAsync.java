package com.jython.ui.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {

    void getDialogFormat(String name, AsyncCallback<DialogFormat> callback);

    void runAction(DialogVariables v, String name, String actionId,
            AsyncCallback<DialogVariables> callback);
}
