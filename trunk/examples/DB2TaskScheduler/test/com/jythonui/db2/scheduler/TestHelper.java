package com.jythonui.db2.scheduler;

import com.jythonui.db2.scheduler.injector.ServiceInjector;
import com.jythonui.server.IJythonUIServer;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogInfo;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.RequestContext;

abstract class TestHelper {

    protected IJythonUIServer iJ = ServiceInjector.contructJythonUiServer();

    protected final static String D = "test/test.xml";

    TestHelper() {
        ServiceInjector.setPropertyFileName("/tmp/test.properties");
    }

    protected DialogFormat getD(String dName) {
        DialogInfo dI = iJ.findDialog(new RequestContext(), dName);
        if (dI == null)
            return null;
        return dI.getDialog();

    }

    protected void runAction(DialogVariables v, String dialogName,
            String actionId) {
        iJ.runAction(new RequestContext(), v, dialogName, actionId);
    }

}
