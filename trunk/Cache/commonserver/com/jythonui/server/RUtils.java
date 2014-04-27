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

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.apache.commons.beanutils.PropertyUtils;

import com.google.common.base.Optional;
import com.jythonui.shared.RMap;

public class RUtils {

    private RUtils() {

    }

    private static Optional<Date> retrieveD(Object o, String propName) {
        Date d = null;
        try {
            d = (Date) PropertyUtils.getProperty(o, propName);
        } catch (IllegalAccessException | InvocationTargetException
                | NoSuchMethodException e) {
            return null;
        }
        return Optional.fromNullable(d);
    }

    private static Optional<String> retrieveS(Object o, String propName) {
        String v = null;
        try {
            v = (String) PropertyUtils.getProperty(o, propName);
        } catch (IllegalAccessException | InvocationTargetException
                | NoSuchMethodException e) {
            return null;
        }
        return Optional.fromNullable(v);
    }

    public static void retrieveCreateModif(RMap dest, Object sou) {
        Optional<Date> d = retrieveD(sou, ISharedConsts.CREATIONDATEPROPERTY);
        if (d != null)
            dest.setCreationDate(d.orNull());
        d = retrieveD(sou, ISharedConsts.MODIFDATEPROPERTY);
        if (d != null)
            dest.setModifDate(d.orNull());
        Optional<String> s = retrieveS(sou,
                ISharedConsts.CREATIONPERSONPROPERTY);
        if (s != null)
            dest.setCreationPerson(s.orNull());
        s = retrieveS(sou, ISharedConsts.MODIFPERSONPROPERTY);
        if (s != null)
            dest.setModifPerson(s.orNull());

    }

}
