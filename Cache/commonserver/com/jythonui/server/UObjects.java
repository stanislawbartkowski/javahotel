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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

import com.gwtmodel.table.common.dateutil.DateFormatUtil;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;

public class UObjects extends UtilHelper {

    private UObjects() {

    }

    public static Object get(byte[] value, String keyInfo) {
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
            errorLog(
                    L().getMess(IErrorCode.ERRORCODE19,
                            ILogMess.GETOBJECTREGISTRYERROR, keyInfo), e);

        } catch (ClassNotFoundException e) {
            errorLog(
                    L().getMess(IErrorCode.ERRORCODE20,
                            ILogMess.GETOBJECTREGISTRYERROR, keyInfo), e);
        }
        return null;
    }

    public static byte[] put(Object o, String keyInfo) {
        ByteArrayOutputStream bu = new ByteArrayOutputStream();
        ObjectOutputStream outP;
        try {
            outP = new ObjectOutputStream(bu);
            outP.writeObject(o);
            outP.close();
        } catch (IOException e) {
            errorLog(
                    L().getMess(IErrorCode.ERRORCODE18,
                            ILogMess.PUTOBJECTREGISTRYERROR, keyInfo), e);
            return null;
        }
        return bu.toByteArray();
    }

    private static String NUMPATT = "(N)";
    private static String YEARPATT = "(Y)";
    private static String MONTHPATT = "(M)";

    public static String genName(String pattern, Long l, Date d) {
        String numS = l.toString();
        String yearS = Integer.toString(DateFormatUtil.getY(d));
        String monthS = Integer.toString(DateFormatUtil.getM(d));
        return pattern.replace(NUMPATT, numS).replace(YEARPATT, yearS)
                .replace(MONTHPATT, monthS);
    }

}
