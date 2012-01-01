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
package com.javahotel.dbutil.prop;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

import com.javahotel.dbres.resources.IMess;
import com.javahotel.dbutil.log.GetLogger;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class ReadProperties {

    public static InputStream getInputStream(final String propName) {
        InputStream in1 = ReadProperties.class.getClassLoader()
                .getResourceAsStream(propName);
        return in1;
    }

    public static URL getResourceURL(String na) {
        URL u = ReadProperties.class.getClassLoader().getResource(na);
        return u;
    }

    private static Map<String, String> readP2(final String propName,
            final String preFix) throws IOException {
        // application class loader
        InputStream in = getInputStream(propName);
        Properties p = new Properties();
        p.load(in);
        return PropertiesToMap.toM(p, preFix);
    }

    public static Map<String, String> getProperties(final String propName,
            final String preName, final GetLogger log) {
        try {
            return readP2(propName, preName);
        } catch (IOException ex) {
            log.getL().log(Level.SEVERE, "", ex);
        } catch (Exception ex) {
            log.getL().log(Level.SEVERE, "", ex);
        }
        return null;
    }

    public static Map<String, String> getProperties(final String propName,
            final GetLogger log) {
        return getProperties(propName, null, log);
    }

    public static String getResourceName(String rName) {
        String fName = IMess.RESOURCEFOLDER + "/" + rName;
        return fName;
    }

    public static Map<String, String> getRProperties(final String propName,
            final GetLogger log) {
        return getProperties(getResourceName(propName), null, log);
    }
}
