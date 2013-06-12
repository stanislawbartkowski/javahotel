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
package com.jython.ui.shared;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;

public class UObjects {

    private static final Logger log = Logger
            .getLogger(UObjects.class.getName());

    private UObjects() {

    }

    public static Object get(byte[] value, String keyInfo, IGetLogMess gMess) {
        if (value == null)
            return null;
        ByteArrayInputStream bu = new ByteArrayInputStream(value);
        ObjectInputStream inp;
        try {
            inp = new ObjectInputStream(bu);
            Object val = inp.readObject();
            inp.close();
            return val;
        } catch (IOException e) {
            log.log(Level.SEVERE, gMess.getMess(IErrorCode.ERRORCODE19,
                    ILogMess.GETOBJECTREGISTRYERROR, keyInfo), e);

        } catch (ClassNotFoundException e) {
            log.log(Level.SEVERE, gMess.getMess(IErrorCode.ERRORCODE20,
                    ILogMess.GETOBJECTREGISTRYERROR, keyInfo), e);
        }
        return null;
    }

    public static byte[] put(Object o, String keyInfo, IGetLogMess gMess) {
        ByteArrayOutputStream bu = new ByteArrayOutputStream();
        ObjectOutputStream outP;
        try {
            outP = new ObjectOutputStream(bu);
            outP.writeObject(o);
            outP.close();
        } catch (IOException e) {
            log.log(Level.SEVERE, gMess.getMess(IErrorCode.ERRORCODE18,
                    ILogMess.PUTOBJECTREGISTRYERROR, keyInfo), e);
            return null;
        }
        return bu.toByteArray();
    }

    private static String NUMPATT = "(N)";
    private static String YEARPATT = "(Y)";
    private static String MONTHPATT = "(M)";

    public static String getKey(String pattern, Long l, Date d) {
        String numS = l.toString();
        String yearS = Integer.toString(d.getYear());
        String monthS = Integer.toString(d.getMonth());
        return pattern.replace(NUMPATT, numS).replace(YEARPATT, yearS)
                .replace(MONTHPATT, monthS);
    }

}
