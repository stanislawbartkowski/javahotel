/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
package com.javahotel.loginhelper;

import com.javahotel.remoteinterfaces.HotelServerType;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class HotelGetBeanHelper {

    private HotelGetBeanHelper() {
    }

    private static Object getBeanTomcat(final String bName,
            final String serverHost, final String port) {
        Object o = null;
        try {
            Properties props = new Properties();
            // props.setProperty("org.omg.CORBA.ORBInitialHost", serverHost);
            // props.setProperty("org.omg.CORBA.ORBInitialPort", port);
            // http://127.0.0.1:8080/openejb/ejb"
            props.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                    "org.openejb.client.RemoteInitialContextFactory");
            props.setProperty(Context.PROVIDER_URL, "http://" + serverHost + ":" + port + "/openejb/ejb");

            // String na =
            // "corbaname:iiop:localhost:13384#authenticationEJB#com.javahotel.remoteinterfaces.AuthenticationI";
            // String na1 = "securityEJB";

            InitialContext ic = new InitialContext(props);

            o = ic.lookup(bName);
        } catch (NamingException ex) {
            throw new RuntimeException(ex);
        }
        return o;
    }

    private static Object getBeanJBoss(final String bName,
            final String serverHost, final String port) {
        Object o = null;
        try {
            Properties props = new Properties();
            // props.setProperty("org.omg.CORBA.ORBInitialHost", serverHost);
            // props.setProperty("org.omg.CORBA.ORBInitialPort", port);
            // http://127.0.0.1:8080/openejb/ejb"
            props.put("java.naming.factory.initial",
                    "org.jnp.interfaces.NamingContextFactory");
            props.put("java.naming.factory.url.pkgs",
                    "=org.jboss.naming:org.jnp.interfaces");
            String url = serverHost + ":" + port;
            // props.put("java.naming.provider.url", "localhost:1099");
            props.put("java.naming.provider.url", url);

            InitialContext ic = new InitialContext(props);

//            o = ic.lookup("HotelEAR/" + bName);
            o = ic.lookup(bName);
        } catch (NamingException ex) {
            throw new RuntimeException(ex);
        }
        return o;
    }

    private static Object getBeanGlass(final String bName,
            final String serverHost, final String port) {
        Object o = null;
        try {
            Properties props = new Properties();
            props.setProperty("org.omg.CORBA.ORBInitialHost", serverHost);
            props.setProperty("org.omg.CORBA.ORBInitialPort", port);
            // String na =
            // "corbaname:iiop:localhost:13384#authenticationEJB#com.javahotel.remoteinterfaces.AuthenticationI";
            // String na1 = "securityEJB";

            InitialContext ic = new InitialContext(props);

            o = ic.lookup(bName);
        } catch (NamingException ex) {
            throw new RuntimeException(ex);
        }
        return o;
    }

    public static Object getBean(final HotelServerType t, final String bName,
            final String serverHost, final String port) {
        switch (t) {
            case TOMCAT:
                return getBeanTomcat(bName, serverHost, port);
            case GLASSFISH:
                return getBeanGlass(bName, serverHost, port);
            case JBOSS:
                return getBeanJBoss(bName, serverHost, port);
        }
        return null;
    }
}
