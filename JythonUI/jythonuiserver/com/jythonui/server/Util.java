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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import com.jython.ui.shared.BUtil;
import com.jython.ui.shared.UtilHelper;
import com.jython.ui.shared.resource.IReadResource;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.holder.Holder;
import com.jythonui.server.holder.SHolder;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.shared.RequestContext;

public class Util extends UtilHelper {

    private Util() {
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
            errorLog(gMess.getMess(IErrorCode.ERRORCODE1,
                    ILogMess.CANNOTFINDRESOURCEFILE, propName), null);
            return null;
        }
        Properties prop = new Properties();
        try {
            prop.load(i);
        } catch (IOException e) {
            errorLog(gMess.getMess(IErrorCode.ERRORCODE2,
                    ILogMess.ERRORWHILEREADINGRESOURCEFILE, propName), e);
            return null;
        }
        return prop;
    }

    public static InputStream getFile(IJythonUIServerProperties p, String name) {
        if (p.getResource() == null) {
            errorLog(
                    SHolder.getM().getMess(IErrorCode.ERRORCODE17,
                            ILogMess.DIALOGDIRECTORYNULL), null);
        }
        URL u = p.getResource().getRes(
                BUtil.addNameToPath(IConsts.DIALOGDIR, name));
        try {
            if (u == null) {
                errorLog(
                        SHolder.getM().getMess(IErrorCode.ERRORCODE80,
                                ILogMess.FILENOTFOUND, name), null);
            }
            return u.openStream();
        } catch (IOException e) {
            errorLog(
                    SHolder.getM().getMess(IErrorCode.ERRORCODE54,
                            ILogMess.FILENOTFOUND, u.toString() + " " + name),
                    e);
            return null;
        }
    }

    public static String getJythonPackageDirectory(IReadResource iRes) {
        return iRes.getRes(IConsts.PACKAGEDIR).getPath();
    }
}
