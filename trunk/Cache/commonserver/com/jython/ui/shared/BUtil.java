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
package com.jython.ui.shared;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.gwtmodel.table.common.dateutil.DateFormatUtil;
import com.jythonui.server.holder.SHolder;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;

public class BUtil extends UtilHelper {

    private static void setD(Object o, String propname) {
        Date d = DateFormatUtil.getToday();
        try {
            BeanUtils.setProperty(o, propname, d);
        } catch (IllegalAccessException | InvocationTargetException e) {
            int k = 1;
        }
    }

    private static void setPerson(Object o, String propName, String person) {
        if (person == null)
            return;
        try {
            PropertyUtils.setProperty(o, propName, person);
        } catch (IllegalAccessException | InvocationTargetException
                | NoSuchMethodException e) {
        }
    }

    public static void setCreateModif(String person, Object o, boolean create) {
        setD(o, ISharedConsts.MODIFDATEPROPERTY);
        if (create)
            setD(o, ISharedConsts.CREATIONDATEPROPERTY);
        setPerson(o, ISharedConsts.MODIFPERSONPROPERTY, person);
        if (create)
            setPerson(o, ISharedConsts.CREATIONPERSONPROPERTY, person);
    }
    
    public static String readFromFileInput(InputStream is) {
        try {
            return CharStreams.toString(new InputStreamReader(is,
                    Charsets.UTF_8));
        } catch (IOException e) {
            errorLog(SHolder.getM().getMess(IErrorCode.ERRORCODE55,
                    ILogMess.FILEIOEXCEPTION), e);
            return null;
        }
    }
    
    private static boolean isFSep(char ch) {
        if (ch == '/') {
            return true;
        }
        if (ch == '\'') {
            return true;
        }
        return false;
    }

    private static String add(String path, String fName) {
        StringBuilder bu = new StringBuilder(path);
        int la = bu.length() - 1;
        char ch = bu.charAt(la);
        if (!isFSep(ch)) {
            return path + File.separator + fName;
        }
        return path + fName;
    }

    public static String addNameToPath(String path, String... fName) {
        String res = path;
        for (String f : fName)
            res = add(res, f);
        return res;
    }



}
