package com.javahotel.db.jpaget;

import javax.persistence.EntityManager;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.javahotel.db.jtypes.HId;
import com.javahotel.types.LId;

public class JpaGetByKey {

	private JpaGetByKey() {
	}

	public static Object getRecord(final EntityManager em, final Class<?> cla,
			final HId id) {
		if (id.getId() != null) {
			return em.find(cla, id.getId());
		}
		return em.find(cla, id.getL());
	}
	
	public static Object getRecord(final EntityManager em, final Class<?> cla,
			final LId id) {
		HId hid;
		if (id.getS() == null) {
			hid = new HId(id.getId());
		}
		else {
			Key k = KeyFactory.stringToKey(id.getS());
			hid = new HId(k);
		}
		return getRecord(em,cla,hid);
	}
	
}
