/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package com.jython.ui.server.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.jython.ui.server.datastore.IDateLineOp;
import com.jython.ui.server.datastore.IPersonOp;
import com.jythonui.server.IJythonUIServer;

/**
 * @author hotel
 * 
 */
public class ServiceInjector {

    private static final Injector injector;

    static {
        injector = Guice.createInjector(new ServerService.ServiceModule());
    }
    
    public static IDateLineOp constructDateOp() {
        return injector.getInstance(IDateLineOp.class);
    }

    public static IPersonOp constructPersonOp() {
        return injector.getInstance(IPersonOp.class);
    }

    public static IJythonUIServer contructJythonUiServer() {
        return injector.getInstance(IJythonUIServer.class);
    }

    public static Injector getInjector() {
        return injector;
    }

}
