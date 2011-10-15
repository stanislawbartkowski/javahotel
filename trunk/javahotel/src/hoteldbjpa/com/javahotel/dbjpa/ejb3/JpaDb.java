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

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.javahotel.db.jpaget.JpaGetByKey;
import com.javahotel.db.jtypes.HId;
import com.javahotel.types.LId;

class JpaDb {
	
	private JpaDb() {
		
	}

    static void paddRecord(final EntityManager em, final Object o) {
        em.persist(o);
    }

    static void removeRecord(final EntityManager em, final Object o) {
        em.remove(o);
    }

    static void refreshRecord(final EntityManager em, final Object o) {
        em.refresh(o);
    }

    @SuppressWarnings("unchecked")
    static <T> List<T> pgetAllList(final EntityManager em,
            final String pClass) {
        Query q = em.createQuery("SELECT p FROM " + pClass + " p");
        return q.getResultList();
    }

    @SuppressWarnings("unchecked")
    static List<Object> pgetAllListOrdered(final EntityManager em,
            final String pClass, final String columnId, boolean desc) {
        String query = "SELECT p FROM " + pClass + " p ORDER BY p." + columnId;
        if (desc) {
            query += " DESC";
        }
        Query q = em.createQuery(query);
        List resultList = q.getResultList();
        return resultList;
    }

    static private Object getSingle(final Query q) {
        try {
            Object res = q.getSingleResult();
            return res;
        } catch (NoResultException e) {
            return null;
        }
    }

    static Object pgetOneWhere(final EntityManager em, final String pClass,
            final Object... params) {
        String q = getSelectQuery(pClass);
        q += " " + getWhereQuery(params);
        Query qe = em.createQuery(q);
        querySetParam(qe, params);
        return getSingle(qe);
    }

    static void pRemoveAll(final EntityManager em, final String pClass) {
        List<?> col = pgetAllList(em, pClass);
        premoveList(em, col);

    }

    static void startTransaction(final EntityManager em) {
        em.getTransaction().begin();
    }

    static void endTransaction(final EntityManager em, boolean success) {
        if (success) {
            em.getTransaction().commit();
        } else {
            em.getTransaction().rollback();
        }
    }

    static Object pgetRecord(final EntityManager em, final Class<?> cla,
            final HId key) {
        return JpaGetByKey.getRecord(em, cla, key);
    }

    static Object pgetRecord(final EntityManager em, final Class<?> cla,
            final LId key) {
        Object o = JpaGetByKey.getRecord(em, cla, key);
        return o;
    }

    static void entityStop(final EntityManager em) {
        em.close();
    }

    static Long getNumber(final EntityManager em, final String pClass) {
        Query q = em.createQuery("SELECT COUNT(p) FROM " + pClass + " p ");
        Object o = q.getSingleResult();
        Long l = (Long) o;
        return l;
    }

    static Long getNumberWhereQ(final EntityManager em, final String pClass,
            final Object... params) {
        String q = "SELECT COUNT(p) FROM " + pClass + " p  ";
        q += " " + getWhereQuery(params);
        Query qe = em.createQuery(q);
        querySetParam(qe, params);
        Object o = qe.getSingleResult();
        Long l = (Long) o;
        return l;
    }

    @SuppressWarnings("unchecked")
    static <T> List<T> pgetList(final EntityManager em,
            final String pClass, final Object... params) {
        String q = getSelectQuery(pClass);
        q += " " + getWhereQuery(params);
        Query qe = em.createQuery(q);
        querySetParam(qe, params);
        return qe.getResultList();
    }

    private static String getSelectQuery(String pClass) {
        String q = "SELECT p FROM " + pClass + " p ";
        return q;
    }

    private static String getWhereQuery(final Object... params) {
        String q = "WHERE ";
        boolean first = true;
        boolean firste = true;
        String param = null;
        for (Object o : params) {
            if (first) {
                param = (String) o;
                first = false;
                continue;
            }
            if (!firste) {
                q += " AND ";
            }
            // OpenJPA: p is necessary
            q += "p." + param + " = :" + param;
            first = true;
            firste = false;
            param = null;
        }
        return q;
    }

    private static void querySetParam(Query q, final Object... params) {
        boolean first = true;
        String param = null;
        for (Object o : params) {
            if (first) {
                param = (String) o;
                first = false;
                continue;
            }
            q.setParameter(param, o);
            first = true;
            param = null;
        }

    }

    private static Query qgetNamedQuery(final EntityManager em,
            final String queryName, final Object... params) {
        Query query = em.createNamedQuery(queryName);
        querySetParam(query, params);
        return query;
    }

    @SuppressWarnings("unchecked")
    static <T> List<T> pgetNamedQuery(final EntityManager em,
            final String queryName, final Object... params) {
        Query query = qgetNamedQuery(em, queryName, params);
        List<?> result = query.getResultList();
        return (List<T>) result;
    }

//    @SuppressWarnings("unchecked")
    static <T> T pgetNamedOneQuery(final EntityManager em,
            final String queryName, final Object... params) {
        Query query = qgetNamedQuery(em, queryName, params);
        return (T) getSingle(query);
    }

    static <T> void premoveList(final EntityManager em,
            final List<T> col) {
        for (Object o : col) {
            em.remove(o);
        }
    }
}
