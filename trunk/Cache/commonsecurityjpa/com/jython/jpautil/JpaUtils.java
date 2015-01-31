/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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
package com.jython.jpautil;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.jython.serversecurity.cache.OObjectId;
import com.jython.serversecurity.jpa.PropUtils;
import com.jython.serversecurity.jpa.entities.EObjectDict;
import com.jythonui.server.UtilHelper;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.shared.RMap;

public class JpaUtils extends UtilHelper {

    private JpaUtils() {

    }

    public static Query createObjectQuery(EntityManager em, Object o,
            String query) {
        Query q = em.createNamedQuery(query);
        q.setParameter(1, o);
        return q;
    }

    public static Query createObjectIdQuery(EntityManager em, OObjectId hotel,
            String query) {
        return createObjectQuery(em, hotel.getId(), query);
    }

    public static <E extends EObjectDict> E getElem(EntityManager em,
            OObjectId hotel, String qName, String name) {
        Query q = createObjectIdQuery(em, hotel, qName);
        q.setParameter(2, name);
        try {
            @SuppressWarnings("unchecked")
            E pers = (E) q.getSingleResult();
            return pers;
        } catch (NoResultException e) {
            return null;
        }
    }

    public static <E extends EObjectDict> E getElemE(EntityManager em,
            OObjectId hotel, String qName, String name) {
        E e = getElem(em, hotel, qName, name);
        if (e == null)
            errorMess(L(), IErrorCode.ERRORCODE110,
                    ILogMess.OBJECTBYNAMECANNOTBEFOUND, name, hotel.getObject());
        return e;
    }

   
    public static void copyToEDict(OObjectId hotel, EObjectDict pers,
            RMap elem) {
        PropUtils.copyToEDict(pers, elem);
        pers.setOObject(hotel.getId());
    }


}
