package com.javahotel.db.jpaget;

import javax.persistence.EntityManager;

import com.javahotel.db.jtypes.HId;
import com.javahotel.types.LId;

public class JpaGetByKey {

	private JpaGetByKey() {
	}

	public static Object getRecord(final EntityManager em, final Class<?> cla,
			final HId id) {
		return em.find(cla, id.getL());
	}

	public static Object getRecord(final EntityManager em, final Class<?> cla,
			final LId id) {
		return em.find(cla, id.getId());
	}

}
