/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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
package com.javahotel.dbjpa.ejb3;

import java.util.List;

import com.javahotel.dbutil.log.GetLogger;
import com.javahotel.dbutil.prop.IGetPropertiesFactory;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class JpaManagerData {

    private static IAfterBeforeLoadAction iA;

    public static IAfterBeforeLoadAction getIA() {
        return iA;
    }

    public static void setIA(IAfterBeforeLoadAction ia) {
        iA = ia;
    }

    public static void setLog(GetLogger aLog) {
        CommonData.setLog(aLog);
    }

    public static void setDataBaseMapping(final String dbId, String managerID) {
        CommonData.getDM().put(dbId, managerID);

    }

    public static void clearAll() {
        CreateNamedManager.closeAll();
    }

    public static void setGetPropFactory(IGetPropertiesFactory aGetPropFactory, boolean EMFPermanent) {
        CommonData.setGetPropFactory(aGetPropFactory);
        CreateNamedManager.setEMFPermanent(EMFPermanent);
    }

    public static void setPuName(final String p) {
        CommonData.setPuname(p);
    }

    public static List<String> getDataBaseNames() {
        return GetValidDataBaseNames.getNames();
    }

    static boolean isDataBaseIdValid(final String rel) {
        List<String> col = getDataBaseNames();
        for (final String s : col) {
            String ds = s + ".";
            if (ds.equals(PersProperties.DEFAULT)) {
                return true;
            }
            if (s.equals(rel)) {
                return true;
            }
        }
        return false;
    }
}
