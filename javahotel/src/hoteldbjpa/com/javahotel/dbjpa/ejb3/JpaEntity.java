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
package com.javahotel.dbjpa.ejb3;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
import javax.persistence.EntityManager;

import com.javahotel.db.jtypes.HId;
import com.javahotel.dbjpa.ejb3.CreateNamedManager.EntityInfo;
import com.javahotel.dbutil.log.GetLogger;
import com.javahotel.types.LId;
import java.util.List;

/**
 * @author sb
 * 
 */
public class JpaEntity {

	private final EntityManager em;
	private final GetLogger log;
	private final boolean withFullClassName;

	// default, can be created only from package
	public JpaEntity(final JpaDataId iD, final GetLogger log) {
		EntityInfo ei;
		ei = CreateNamedManager.getManager(iD.getDId());
		em = ei.em;
		this.log = log;
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

	public void closeEntity() {
		em.close();
	}

	public void clearEntity() {
		em.clear();
	}

	public void startTransaction(final boolean blockP) {
		if (blockP) {
			JpaDb.startTransaction(em);
		}
	}

	public boolean tranIsActive() {
		return em.getTransaction().isActive();
	}

	public void endTransaction(final boolean blockP, final boolean success) {
		if (blockP) {
			JpaDb.endTransaction(em, success);
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

	public <T> List<T> getAllListOrdered(final Class<?> cla,
			final String columnId, final boolean desc) {
		List<?> col = JpaDb.pgetAllListOrdered(em, getClaName(cla), columnId,
				desc);
		afterLoadCol(col);
		return (List<T>) col;
	}

	public <T> List<T> getAllList(final Class<?> cla) {
		List<?> col = JpaDb.pgetAllList(em, getClaName(cla));
		afterLoadCol(col);
		return (List<T>) col;
	}

	public <T> T getOneWhereRecord(final Class<?> cla, final Object... params) {
		// if (tranIsActive()) {
		// int a = 0;
		// int b = 0;
		// }
		Object o = JpaDb.pgetOneWhere(em, getClaName(cla), params);
		afterLoad(o);
		return (T) o;
	}

	public long getNumber(final Class<?> cla) {
		return JpaDb.getNumber(em, getClaName(cla));
	}

	public long getNumberWhereQ(final Class<?> cla, Object... params) {
		return JpaDb.getNumberWhereQ(em, getClaName(cla), params);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getNamedQuery(final String queryName, Object... params) {
		List<?> col = JpaDb.pgetNamedQuery(em, queryName, params);
		afterLoadCol(col);
		return (List<T>) col;
	}

	@SuppressWarnings("unchecked")
	public <T> T getNamedOneQuery(final String queryName,
			final Object... params) {
		Object o = JpaDb.pgetNamedOneQuery(em, queryName, params);
		afterLoad(o);
		return (T) o;
	}

	private void afterLoadCol(List<?> col) {
		for (Object o : col) {
			afterLoad(o);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getListQuery(final Class<?> cla, final Object... params) {
		List<?> col = JpaDb.pgetList(em, getClaName(cla), params);
		afterLoadCol(col);
		return (List<T>) col;
	}

	public void refreshObject(final Object o) {
		JpaDb.refreshRecord(em, o);
	}

	public void removeObject(final Object o) {
		JpaDb.removeRecord(em, o);
	}

	public <T> void removeList(final List<T> col) {
		JpaDb.premoveList(em, col);
	}

	public void removeAll(final Class<?> cla) {
		JpaDb.pRemoveAll(em, getClaName(cla));
	}
}
