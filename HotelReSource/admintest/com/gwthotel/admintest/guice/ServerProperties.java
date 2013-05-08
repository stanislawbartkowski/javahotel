package com.gwthotel.admintest.guice;

import java.net.URL;

import com.gwthotel.admintest.suite.TestHelper;
import com.jythonui.server.IJythonUIServerProperties;
import com.jythonui.server.JythonUiServerProvider;

public class ServerProperties implements IJythonUIServerProperties {

    private final String RESOURCES = "resources";
    private final String DIALOGDIR = "dialogs";
    private final String PACKAGEDIR = "packages";
    
    private String getResource(String dir) {
        return JythonUiServerProvider.class.getClassLoader()
                .getResource(RESOURCES + "/" + dir).getPath();
    }

    @Override
    public String getDialogDirectory() {
        return getResource(DIALOGDIR);
    }

    @Override
    public String getPackageDirectory() {
        return getResource(PACKAGEDIR);
    }


    @Override
    public boolean isCached() {
        return true;
    }

    @Override
    public String getBundleBase() {
        return null;
    }
}
