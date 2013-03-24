package com.jython.ui.client;

import com.google.gwt.core.client.EntryPoint;
import com.jythonui.client.JythonClientStart;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WebJythonUi implements EntryPoint {

    public void onModuleLoad() {
        JythonClientStart.start(null);
    }

}
