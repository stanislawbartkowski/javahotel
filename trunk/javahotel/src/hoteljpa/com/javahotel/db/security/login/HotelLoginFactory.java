/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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
package com.javahotel.db.security.login;

import java.util.logging.Level;

import com.javahotel.dbres.log.HLog;
import com.javahotel.dbutil.container.ContainerInfo;
import com.javahotel.remoteinterfaces.HotelServerType;
import com.javahotel.security.login.IHotelLogin;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class HotelLoginFactory {

    private static final String GLASSFISHLOGIN =
            "com.javahotel.security.glassfish.login.HotelLoginImpl";
    private static final String TOMCATLOGIN =
            "com.javahotel.security.tomcat.login.HotelLoginImpl";

    private HotelLoginFactory() {
    }

    public static IHotelLogin getHotelLogin() {
        HotelServerType hType = ContainerInfo.getContainerType();
        Object o = null;
        try {
            boolean tomcat = (hType == HotelServerType.TOMCAT);
            boolean jboss = (hType == HotelServerType.JBOSS);
            boolean appengine = (hType == HotelServerType.APPENGINE);
            String c;

            if (tomcat || jboss || appengine) {
                c = TOMCATLOGIN;
            } else {
                c = GLASSFISHLOGIN;
            }
            Class<?> cla = Class.forName(c);
            o = cla.newInstance();
        } catch (InstantiationException ex) {
            HLog.getLo().log(Level.SEVERE,"",ex);
        } catch (IllegalAccessException ex) {
            HLog.getLo().log(Level.SEVERE,"",ex);
        } catch (ClassNotFoundException ex) {
            HLog.getLo().log(Level.SEVERE,"",ex);
        }
        return (IHotelLogin) o;
    }
}
