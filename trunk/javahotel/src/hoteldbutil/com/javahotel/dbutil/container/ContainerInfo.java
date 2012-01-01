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
package com.javahotel.dbutil.container;

import com.javahotel.remoteinterfaces.HotelServerType;
import java.io.File;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class ContainerInfo {

    private ContainerInfo() {
    }

    private static boolean isJarOnPath(String classPath, final String jarName) {
        String[] pat = classPath.split(File.pathSeparator);
        for (int i = 0; i < pat.length; i++) {
            String fName = pat[i];
            File f = new File(fName);
            String baseName = f.getName();
            if (baseName.equalsIgnoreCase(jarName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns information if transactions are controlled by container
     * 
     * @return true: by container false: no
     */
    public static boolean TransactionContainer() {
        return getContainerType() != HotelServerType.APPENGINE;
    }

    public static HotelServerType getContainerType() {
        String classPath = System.getProperty("java.class.path");
        // null if live google app engine
        if (classPath == null) {
            return HotelServerType.APPENGINE;
        }
        boolean tomcat = isJarOnPath(classPath, "catalina.jar");
        if (tomcat) {
            return HotelServerType.TOMCAT;
        }
        boolean jboss = isJarOnPath(classPath, "run.jar");
        if (jboss) {
            return HotelServerType.JBOSS;
        }
        boolean appengine = isJarOnPath(classPath,
                "appengine-local-runtime-shared.jar");
        if (appengine) {
            return HotelServerType.APPENGINE;
        }
        return HotelServerType.GLASSFISH;
    }
}
