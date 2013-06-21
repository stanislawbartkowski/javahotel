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
package com.jythonui.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gwtmodel.table.common.CUtil;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.holder.Holder;
import com.jythonui.server.holder.SHolder;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.shared.RequestContext;

public class Util {

    private Util() {
    }

    static final private Logger log = Logger.getLogger(Util.class.getName());

    public static void setLocale(RequestContext context) {
        String locale = context.getLocale();
        if (!CUtil.EmptyS(locale))
            Holder.SetLocale(locale);
    }

    public static Properties getProperties(String propName) {
        IGetLogMess gMess = SHolder.getM();
        InputStream i = Util.class.getClassLoader().getResourceAsStream(
                propName);
        if (i == null) {
            log.log(Level.SEVERE, gMess.getMess(IErrorCode.ERRORCODE1,
                    ILogMess.CANNOTFINDRESOURCEFILE, propName));
            return null;
        }
        Properties prop = new Properties();
        try {
            prop.load(i);
        } catch (IOException e) {
            log.log(Level.SEVERE, gMess.getMess(IErrorCode.ERRORCODE2,
                    ILogMess.ERRORWHILEREADINGRESOURCEFILE, propName), e);
            return null;
        }
        return prop;
    }
}
