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

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
import java.util.Collection;

import javax.persistence.EntityManager;

import com.javahotel.db.jtypes.HId;
import com.javahotel.dbjpa.ejb3.CreateNamedManager.EntityInfo;
import com.javahotel.dbutil.log.GetLogger;
import com.javahotel.types.LId;

/**
 * @author sb
 * 
 */
public class JpaEntity {

    private final EntityManager em;
    private final boolean isDB2;
    private final SemaphoreTrans semP;
    private final GetLogger log;
    private boolean insideTrans;
    private boolean semTraStarted;
    private final boolean withFullClassName;

    // default, can be created only from package
    public JpaEntity(final JpaDataId iD, final GetLogger log) {
        EntityInfo ei;
        ei = CreateNamedManager.getManager(iD.getDId());
        em = ei.em;
        isDB2 = ei.isDB2;
        semP = ei.semP;
        this.log = log;
        insideTrans = false;
        semTraStarted = false;
        this.withFullClassName = ei.withFullClassName;
    }

    private void afterLoad(Object res) {
        if (res == null) {
            return;
        }
        if (JpaManagerData.getIA() == null) {
            return;
        }
        JpaManagerData.getIA().afterLoadAction(log, this, res);
    }

    private void beforePersist(Object res) {
        if (res == null) {
            return;
        }
        if (JpaManagerData.getIA() == null) {
            return;
        }
        JpaManagerData.getIA().beforePersistAction(log, this, res);
    }

    private String getClaName(Class<?> cla) {
        if (withFullClassName) {
            return cla.getName();
        }
        return cla.getSimpleName();
    }

    public boolean isDB2() {
        return isDB2;
    }

    public void closeEntity() {
        em.close();
    }

    public void clearEntity() {
        em.clear();
    }

    public void startSemTransaction(final boolean blockP) {
        if (semTraStarted) {
            return;
        }
        semP.startT(blockP);
        semTraStarted = true;
    }

    public void stopSemTransaction(final boolean blockP) {
        if (!semTraStarted) {
            return;
        }
        semP.stopT(blockP);
        semTraStarted = false;
    }

    public void startTransaction(final boolean blockP) {
        startSemTransaction(blockP);
        if (blockP) {
            JpaDb.startTransaction(em);
        }
    }

    public boolean tranIsActive() {
        return em.getTransaction().isActive();
    }

    public void endTransaction(final boolean blockP, final boolean success) {
        try {
            if (blockP) {
                JpaDb.endTransaction(em, success);
            }
        } finally {
            // Very important: should be finally
            // Could be blocked otherwise
            stopSemTransaction(blockP);
        }
    }

    public void endTransaction(final boolean blockP) {
        endTransaction(blockP, true);
    }

    public void addRecord(final Object o) {
        beforePersist(o);
        JpaDb.paddRecord(em, o);
        em.flush();
    }

    public void changeRecord(final Object o) {
        addRecord(o);
    }

    public <T> T getRecord(final Class<?> cla, final HId id) {
        Object o = JpaDb.pgetRecord(em, cla, id);
        afterLoad(o);
        return (T) o;
    }

    public <T> T getRecord(final Class<?> cla, final LId id) {
        Object o = JpaDb.pgetRecord(em, cla, id);
        afterLoad(o);
        return (T) o;
    }

    public <T> Collection<T> getAllListOrdered(final Class<?> cla,
            final String columnId, final boolean desc) {
        Collection<?> col = JpaDb.pgetAllListOrdered(em, getClaName(cla),
                columnId, desc);
        afterLoadCol(col);
        return (Collection<T>) col;
    }

    public <T> Collection<T> getAllList(final Class<?> cla) {
        Collection<?> col = JpaDb.pgetAllList(em, getClaName(cla));
        afterLoadCol(col);
        return (Collection<T>) col;
    }

    public <T> T getOneWhereRecord(final Class<?> cla, final Object... params) {
//		if (tranIsActive()) {
//			int a = 0;
//			int b = 0;
//		}
        Object o = JpaDb.pgetOneWhere(em, getClaName(cla), params);
        afterLoad(o);
        return (T) o;
    }

    public long getNumber(final Class<?> cla) {
        return JpaDb.getNumber(em, getClaName(cla));
    }

    public long getNumberWhereQ(final Class<?> cla, final String field,
            final Object par) {
        return JpaDb.getNumberWhereQ(em, getClaName(cla), field, par);
    }

    @SuppressWarnings("unchecked")
    public <T> Collection<T> getNamedQuery(final String queryName,
            Object... params) {
        Collection<?> col = JpaDb.pgetNamedQuery(em, queryName, params);
        afterLoadCol(col);
        return (Collection<T>) col;
    }

    @SuppressWarnings("unchecked")
    public <T> T getNamedOneQuery(final String queryName,
            final Object... params) {
        Object o = JpaDb.pgetNamedOneQuery(em, queryName, params);
        afterLoad(o);
        return (T) o;
    }

    private void afterLoadCol(Collection<?> col) {
        for (Object o : col) {
            afterLoad(o);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> Collection<T> getListQuery(final Class<?> cla,
            final Object... params) {
        Collection<?> col = JpaDb.pgetList(em, getClaName(cla), params);
        afterLoadCol(col);
        return (Collection<T>) col;
    }

    public void refreshObject(final Object o) {
        JpaDb.refreshRecord(em, o);
    }

    public void removeObject(final Object o) {
        JpaDb.removeRecord(em, o);
    }

    public <T> void removeCollection(final Collection<T> col) {
        JpaDb.premoveCollection(em, col);
    }

    public void removeAll(final Class<?> cla) {
        JpaDb.pRemoveAll(em, getClaName(cla));
    }
}
