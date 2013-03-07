package com.jythonui.db2.scheduler;

import com.jythonui.db2.scheduler.injector.ServiceInjector;
import com.jythonui.server.IJythonUIServer;

abstract class TestHelper {

    protected IJythonUIServer iJ = ServiceInjector.contructJythonUiServer();
    
    protected final static String D = "test/test.xml";


    TestHelper() {
        ServiceInjector.setPropertyFileName("/tmp/test.properties");
    }

}
