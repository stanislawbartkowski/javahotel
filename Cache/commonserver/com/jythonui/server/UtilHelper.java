/*
 * Copyright 2016 stanislawbartkowski@gmail.com 
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

import java.util.logging.Level;
import java.util.logging.Logger;

import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.holder.SHolder;
import com.jythonui.shared.JythonUIFatal;

abstract public class UtilHelper {

    static final private Logger log = Logger.getLogger(UtilHelper.class
            .getName());

    protected static IGetLogMess L() {
        return SHolder.getM();
    }

    protected static void errorLog(String mess, Exception e)
            throws JythonUIFatal {
        if (e != null)
            log.log(Level.SEVERE, mess, e);
        else
            log.log(Level.SEVERE, mess);
        throw new JythonUIFatal(mess);
    }

    protected static void errorLog(String mess) throws JythonUIFatal {
        errorLog(mess, null);
    }

    static protected void logDebug(String mess) {
        log.log(Level.FINE, mess);
    }

    static protected void info(String mess) {
        log.info(mess);
    }

    static protected void errorMess(IGetLogMess gMess, String errId,
            String messId, Exception e, String... par) throws JythonUIFatal {
        String mess = gMess.getMess(errId, messId, par);
        errorLog(mess, e);
    }

    static protected void errorMess(IGetLogMess gMess, String errId,
            String messId, String... par) throws JythonUIFatal {
        String mess = gMess.getMess(errId, messId, par);
        errorLog(mess);
    }

    static protected void infoMess(IGetLogMess gMess, String messid,
            String... params) {
        String mess = gMess.getMessN(messid, params);
        info(mess);
    }

    static protected void severe(String mess) {
        log.severe(mess);
    }

    static protected void severe(String mess, Exception e) {
        log.log(Level.SEVERE, mess, e);
    }

    static protected void traceLog(String mess) {
        // info(mess);
    }

}
