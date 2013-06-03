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
package com.gwthotel.hotel;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.beanutils.PropertyUtils;

import com.gwthotel.mess.IHError;
import com.gwthotel.mess.IHMess;
import com.gwthotel.shared.IHotelConsts;
import com.gwthotel.shared.PropDescription;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.shared.JythonUIFatal;

public class HUtils {

    private HUtils() {
    }

    static final private Logger log = Logger.getLogger(HUtils.class.getName());

    public static <T extends PropDescription> void toEProperties(
            IGetLogMess lMess, String[] prop, Object dest, T sou) {
        for (String key : prop) {
            String val = sou.getAttr(key);
            try {
                PropertyUtils.setSimpleProperty(dest, key, val);
            } catch (IllegalAccessException | InvocationTargetException
                    | NoSuchMethodException e) {
                String mess = lMess.getMess(IHError.HERROR007,
                        IHMess.BEANCANNOTSETPROPERTY, key, val);
                log.log(Level.SEVERE, mess, e);
                throw new JythonUIFatal(mess);
            }
        }
    }

    public static <T extends PropDescription> void toTProperties(
            IGetLogMess lMess, String[] prop, T dest, Object sou) {
        for (String key : prop) {
            String val;
            try {
                val = (String) PropertyUtils.getSimpleProperty(sou, key);
            } catch (IllegalAccessException | InvocationTargetException
                    | NoSuchMethodException e) {
                String mess = lMess.getMess(IHError.HERROR008,
                        IHMess.BEANCANNOTGETPROPERTY, key);
                log.log(Level.SEVERE, mess, e);
                throw new JythonUIFatal(mess);
            }
            dest.setAttr(key, val);
        }
    }

    public static String[] getCustomerFields() {
        String[] lProperty = { IHotelConsts.CUSTOMERFIRSTNAMEPROP,
                IHotelConsts.CUSTOMERCOMPANYNAMEPROP,
                IHotelConsts.CUSTOMERSTREETPROP,
                IHotelConsts.CUSTOMERZIPCODEPROP,
                IHotelConsts.CUSTOMEREMAILPROP, IHotelConsts.CUSTOMERPHONEPROP };
        return lProperty;
    }

}
