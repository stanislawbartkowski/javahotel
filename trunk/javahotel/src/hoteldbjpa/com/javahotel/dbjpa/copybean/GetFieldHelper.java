/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
package com.javahotel.dbjpa.copybean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;

import com.javahotel.dbutil.log.GetLogger;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class GetFieldHelper {

    public static String getF(final String na) {
        char fir = na.charAt(0);
        fir = Character.toUpperCase(fir);
        String naf = fir + na.substring(1);
        return naf;
    }

    public static Method getMe(final Object sou, final String naf)
            throws NoSuchMethodException {
        String getterN = "get" + naf;
        Class<?>[] em = {};
        Method m;
        try {
            m = sou.getClass().getMethod(getterN, em);
        } catch (NoSuchMethodException ex) {
            getterN = "is" + naf;
            m = sou.getClass().getMethod(getterN, em);
        }
        return m;
    }

    public static Method setMe(final Object dest, final String naf,
            final Class<?> destcla) throws NoSuchMethodException {
        String setterN = "set" + getF(naf);
        Class<?>[] em = {destcla};
        Method m = dest.getClass().getMethod(setterN, em);
        return m;
    }

    public static Object getVal(final Object sou, final Method m,
            final GetLogger log) {
        try {
            Object val = m.invoke(sou, new Object[]{});
            return val;
        } catch (IllegalAccessException ex) {
            log.getL().log(Level.SEVERE, "", ex);
        } catch (IllegalArgumentException ex) {
            log.getL().log(Level.SEVERE, "", ex);
        } catch (InvocationTargetException ex) {
            log.getL().log(Level.SEVERE, "", ex);
        }
        return null;
    }

    public static Object getterVal(final Object sou, final String na,
            final GetLogger log) {
        try {
            String naf = getF(na);
            Method m = getMe(sou, naf);
            Object val = m.invoke(sou, new Object[]{});
            return val;
        } catch (IllegalAccessException ex) {
        } catch (IllegalArgumentException ex) {
            log.getL().log(Level.SEVERE, "", ex);
        } catch (InvocationTargetException ex) {
            log.getL().log(Level.SEVERE, "", ex);
        } catch (NoSuchMethodException ex) {
            log.getL().log(Level.SEVERE, "", ex);
        }
        return null;
    }

    static void setValC(final Object dest, final Object val, final String na,
            final Class<?> destcla) throws NoSuchMethodException,
            IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        Method m1 = setMe(dest, na, destcla);
        Object[] par = new Object[]{val};
        m1.invoke(dest, par);
    }

    static void setVal(final Object dest, final Object val, final String na,
            final Method m) throws NoSuchMethodException,
            IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        setValC(dest, val, na, m.getReturnType());
    }

    public static void setterVal(final Object dest, final Object val,
            final String na, final Class<?> destcla, final GetLogger log) {
        try {
            setValC(dest, val, na, destcla);
        } catch (NoSuchMethodException ex) {
            log.getL().log(Level.SEVERE, "", ex);
        } catch (IllegalAccessException ex) {
            log.getL().log(Level.SEVERE, "", ex);
        } catch (IllegalArgumentException ex) {
            log.getL().log(Level.SEVERE, "", ex);
        } catch (InvocationTargetException ex) {
            log.getL().log(Level.SEVERE, "", ex);
        }
    }
}
