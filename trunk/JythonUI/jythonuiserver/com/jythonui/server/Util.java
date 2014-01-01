/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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
package com.jythonui.server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jython.ui.shared.ISharedConsts;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.holder.Holder;
import com.jythonui.server.holder.SHolder;
import com.jythonui.server.impl.JythonUiServerProvider;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.shared.JythonUIFatal;
import com.jythonui.shared.RequestContext;

public class Util {

    private Util() {
    }

    static final private Logger log = Logger.getLogger(Util.class.getName());

    static private void error(String mess, Exception e) {
        if (e != null)
            log.log(Level.SEVERE, mess, e);
        else
            log.log(Level.SEVERE, mess);
        throw new JythonUIFatal(mess);
    }

    public static void setContext(RequestContext context) {
        if (context == null)
            return;
        Holder.setContext(context);
    }

    public static String getToken() {
        RequestContext req = Holder.getRequest();
        if (req == null)
            return null;
        return req.getToken();
    }

    public static String getLocale() {
        RequestContext req = Holder.getRequest();
        if (req == null)
            return null;
        return req.getLocale();
    }

    public static Properties getProperties(String propName) {
        IGetLogMess gMess = SHolder.getM();
        InputStream i = Util.class.getClassLoader().getResourceAsStream(
                propName);
        if (i == null) {
            error(gMess.getMess(IErrorCode.ERRORCODE1,
                    ILogMess.CANNOTFINDRESOURCEFILE, propName), null);
            return null;
        }
        Properties prop = new Properties();
        try {
            prop.load(i);
        } catch (IOException e) {
            error(gMess.getMess(IErrorCode.ERRORCODE2,
                    ILogMess.ERRORWHILEREADINGRESOURCEFILE, propName), e);
            return null;
        }
        return prop;
    }

    public static InputStream getFile(IJythonUIServerProperties p, String name) {
        if (p.getDialogDirectory() == null) {
            error(SHolder.getM().getMess(IErrorCode.ERRORCODE17,
                    ILogMess.DIALOGDIRECTORYNULL), null);
        }
        String dDir = p.getDialogDirectory();
        dDir = dDir + "/" + name;
        InputStream s;
        try {
            s = new FileInputStream(dDir);
            return s;
        } catch (FileNotFoundException e) {
            error(SHolder.getM().getMess(IErrorCode.ERRORCODE54,
                    ILogMess.FILENOTFOUND, dDir), e);
            return null;
        }
    }

    public static String readFromFileInput(InputStream is) {
        byte[] b;
        try {
            b = new byte[is.available()];
            is.read(b);
            String text = new String(b);
            return text;
        } catch (IOException e) {
            error(SHolder.getM().getMess(IErrorCode.ERRORCODE55,
                    ILogMess.FILEIOEXCEPTION), e);
            return null;
        }
    }

    public static String getResourceAdDirectory(String dir) {
        URL u = JythonUiServerProvider.class.getClassLoader().getResource(
                ISharedConsts.RESOURCES + "/" + dir);
        if (u == null)
            return null;
        return u.getPath();
    }

}
