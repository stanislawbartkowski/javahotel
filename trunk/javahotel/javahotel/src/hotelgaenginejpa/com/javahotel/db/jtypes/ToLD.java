package com.javahotel.db.jtypes;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.javahotel.types.LId;

public class ToLD {

	public static LId toLId(HId id) {
		Key k = id.getId();
		Long l = id.getL();
		if (k == null) {
			return new LId(l);
		}
		return new LId(l, KeyFactory.keyToString(k));
	}

	public static boolean eq(LId id1, HId id2) {
		if (id1 == null) {
			return id2 == null;
		}
		if (id2 == null) {
			return false;
		}
		if (id2.getL() == null) {
			return false;
		}
		return id1.getId().equals(id2.getL());
	}

}
