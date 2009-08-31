/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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

import com.javahotel.dbutil.container.ContainerInfo;
import com.javahotel.remoteinterfaces.HotelServerType;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class CreateNamedManager {

    private static boolean EMFPERNAMENT;
    // should be true for appengine
    private static final int MAXPERSIST = 0;
    private static final boolean withFullClassName;

    private static class EntityFactoryInfo {

        final EntityManagerFactory fa;
        final boolean isDB2;
        final SemaphoreTrans semP;

        EntityFactoryInfo(final EntityManagerFactory fa, final boolean isDB2) {
            this.fa = fa;
            this.isDB2 = isDB2;
            semP = new SemaphoreTrans();
        }
    }
    private static Map<String, EntityFactoryInfo> m;
    private static int lastno;

    private static void init() {
        if ((m == null) || !EMFPERNAMENT) {
            m = new HashMap<String, EntityFactoryInfo>();
            lastno = 0;
        }
    }


    static {
        m = null;
        withFullClassName = (ContainerInfo.getContainerType() ==
                HotelServerType.APPENGINE);
        init();
    }

    static class EntityInfo {

        final EntityManager em;
        final boolean isDB2;
        final SemaphoreTrans semP;
        final boolean withFullClassName;

        EntityInfo(final EntityManager em, final boolean isDB2,
                final SemaphoreTrans semP, boolean withFullClassName) {
            this.em = em;
            this.isDB2 = isDB2;
            this.semP = semP;
            this.withFullClassName = withFullClassName;
        }
    }

    static void closeAll() {
        List<String> ss = new ArrayList<String>();
        for (String s : m.keySet()) {
            EntityFactoryInfo ef = m.get(s);
            try {
                ef.fa.close();
            } catch (Exception e) {
                CommonData.getLog().getL().log(Level.SEVERE, "", e);

            }
            ss.add(s);
        }
        init();
    }

    static void setEMFPermanent(boolean p) {
        EMFPERNAMENT = p;
    }

    static synchronized EntityInfo getManager(final String s) {

        EntityFactoryInfo fa = m.get(s);
        EntityManager em = null;

        if (fa == null) {
            String puNameT = CommonData.getPuname();
            PersProperties pers = new PersProperties();
            boolean snew = pers.getProperties(s);

            String url = (String) pers.getMap().get("toplink.target-database");
            // String noDataS = (String)
            // pers.getMap().get("javax.persistence.nonJtaDataSource");
            boolean isDB2 = false;
            if (url != null) {
                isDB2 = (url.toUpperCase().indexOf("DB2") != -1);
            }

            String puName = CommonData.getDM().get(s);

            if (lastno > MAXPERSIST) {
                String emess = MessageFormat.format(
                        "To many persistance units limit {0}, last used {1}, data base name {2}",
                        "" + MAXPERSIST, "" + (lastno - 1), s);
                Exception e = new Exception(emess);
                CommonData.getLog().getL().log(Level.SEVERE, "", e);
            }

            if (puName == null) {
                puName = puNameT + lastno;
                lastno++;
            }
            String msg = MessageFormat.format(
                    "Create Entity Manager {0} database {1} ", puName, s);
            CommonData.getLog().getL().info(msg);

            EntityManagerFactory mfa = Persistence.createEntityManagerFactory(
                    puName, pers.getMap());
            fa = new EntityFactoryInfo(mfa, isDB2);

            m.put(s, fa);
        } // f == null;

        em = fa.fa.createEntityManager();
        return new EntityInfo(em, fa.isDB2, fa.semP, withFullClassName);
    }
}
