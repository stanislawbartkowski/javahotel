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
package com.jythonui.server.getbundle;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jython.ui.shared.MUtil;
import com.jython.ui.shared.ReadUTF8Properties;
import com.jythonui.server.holder.SHolder;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.shared.JythonUIFatal;

public class ReadBundle {

    static final private Logger log = Logger.getLogger(ReadBundle.class
            .getName());

    static private void error(String mess, Throwable e) {
        log.log(Level.SEVERE, mess);
        throw new JythonUIFatal(mess, e);
    }

    static private void debug(String mess) {
        log.fine(mess);
    }

    public static Map<String, String> getBundle(String loc, String dir,
            String bundle) {
        String propdefaS = dir + "/" + bundle + ".properties";
        String propS = null;
        debug("setMess locale=" + loc);
        if (loc != null) {
            propS = dir + "/" + bundle + "_" + loc + ".properties";
            debug("locale not null " + propS);
        }
        Properties defa = null;
        try {
            defa = ReadUTF8Properties.readProperties(propdefaS);
        } catch (IOException e) {
            error(SHolder.getM().getMess(IErrorCode.ERRORCODE46,
                    ILogMess.ERRORWHILELOADINGBUNDLERESOURCE, dir,bundle), e);
        }
        Properties prop = null;
        try {
            if (propS != null)
                prop = ReadUTF8Properties.readProperties(propS);
        } catch (IOException e1) {
            // expected, do nothing
            debug(propS + " not found");
        }
        Map<String,String> b = new HashMap<String,String>();
        MUtil.toElem(b, defa);
        if (prop != null) {
            MUtil.toElem(b, prop);
            debug("combine : " + propS + " " + propdefaS);            
        }
        return b;
    }

}
