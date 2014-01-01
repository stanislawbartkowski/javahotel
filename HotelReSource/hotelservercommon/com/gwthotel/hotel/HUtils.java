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
package com.gwthotel.hotel;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import com.google.common.base.Optional;
import com.gwthotel.admin.holder.HHolder;
import com.gwthotel.mess.IHError;
import com.gwthotel.mess.IHMess;
import com.gwthotel.shared.IHotelConsts;
import com.gwthotel.shared.PropDescription;
import com.gwtmodel.table.common.dateutil.DateFormatUtil;
import com.jython.ui.shared.MUtil;
import com.jythonui.shared.JythonUIFatal;

public class HUtils {

    private HUtils() {
    }

    static final private Logger log = Logger.getLogger(HUtils.class.getName());

    public static <T extends PropDescription> void toEProperties(String[] prop,
            Object dest, T sou) {
        for (String key : prop) {
            String val = sou.getAttr(key);
            try {
                PropertyUtils.setSimpleProperty(dest, key, val);
            } catch (IllegalAccessException | InvocationTargetException
                    | NoSuchMethodException e) {
                String mess = HHolder.getHM().getMess(IHError.HERROR007,
                        IHMess.BEANCANNOTSETPROPERTY, key, val);
                log.log(Level.SEVERE, mess, e);
                throw new JythonUIFatal(mess);
            }
        }
    }

    public static <T extends PropDescription> void toTProperties(String[] prop,
            T dest, Object sou) {
        for (String key : prop) {
            String val;
            try {
                val = (String) PropertyUtils.getSimpleProperty(sou, key);
            } catch (IllegalAccessException | InvocationTargetException
                    | NoSuchMethodException e) {
                String mess = HHolder.getHM().getMess(IHError.HERROR008,
                        IHMess.BEANCANNOTGETPROPERTY, key);
                log.log(Level.SEVERE, mess, e);
                throw new JythonUIFatal(mess);
            }
            dest.setAttr(key, val);
        }
    }

    public static String[] getCustomerFields() {
        String[] lProperty = { IHotelConsts.CUSTOMERFIRSTNAMEPROP,
                IHotelConsts.CUSTOMERSURNAMEPROP,
                IHotelConsts.CUSTOMERPOSTALCODEPROP,
                IHotelConsts.CUSTOMEREMAILPROP,
                IHotelConsts.CUSTOMERPHONE1PROP,
                IHotelConsts.CUSTOMERDOCNUMBPROP,
                IHotelConsts.CUSTOMERPHONE2PROP, IHotelConsts.CUSTOMERFAXPROP,
                IHotelConsts.CUSTOMERCOUNTRYPROP,
                IHotelConsts.CUSTOMERSTREETPROP, IHotelConsts.CUSTOMERCITYPROP,
                IHotelConsts.CUSTOMERREGIONPROP };
        return lProperty;
    }

    public static BigDecimal roundB(BigDecimal b) {
        return MUtil.roundB(b);
    }

    private static void setD(Object o, String propname) {
        Date d = DateFormatUtil.getToday();
        try {
            BeanUtils.setProperty(o, propname, d);
        } catch (IllegalAccessException | InvocationTargetException e) {
        }
    }

    private static void setPerson(Object o, String propName, String person) {
        try {
            PropertyUtils.setProperty(o, propName, person);
        } catch (IllegalAccessException | InvocationTargetException
                | NoSuchMethodException e) {
        }
    }

    public static void setCreateModif(String person, Object o, boolean create) {
        setD(o, IHotelConsts.MODIFDATEPROPERTY);
        if (create)
            setD(o, IHotelConsts.CREATIONDATEPROPERTY);
        setPerson(o, IHotelConsts.MODIFPERSONPROPERTY, person);
        if (create)
            setPerson(o, IHotelConsts.CREATIONPERSONPROPERTY, person);
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

    public static void retrieveCreateModif(PropDescription dest, Object sou) {
        Optional<Date> d = retrieveD(sou, IHotelConsts.CREATIONDATEPROPERTY);
        if (d != null)
            dest.setCreationDate(d.orNull());
        d = retrieveD(sou, IHotelConsts.MODIFDATEPROPERTY);
        if (d != null)
            dest.setModifDate(d.orNull());
        Optional<String> s = retrieveS(sou, IHotelConsts.CREATIONPERSONPROPERTY);
        if (s != null)
            dest.setCreationPerson(s.orNull());
        s = retrieveS(sou, IHotelConsts.MODIFPERSONPROPERTY);
        if (s != null)
            dest.setModifPerson(s.orNull());

    }

}
