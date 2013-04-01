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
package com.jythonui.server.logmess;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

public class LogMess {

    private static Properties mess = null;
    private static final String fileName = "resources/mess/mess.properties";

    private static void readProp() {
        if (mess != null)
            return;
        mess = new Properties();
        InputStream i = LogMess.class.getClassLoader().getResourceAsStream(
                fileName);
        try {
            mess.load(i);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getMess(String errCode, String key, String... params) {
        readProp();
        String m = mess.getProperty(key);
        if (errCode == null)
            return MessageFormat.format(m, params);
        return errCode + " " + MessageFormat.format(m, params);
    }

    public static String getMessN(String key, String... params) {
        return getMess(null, key, params);
    }

}
