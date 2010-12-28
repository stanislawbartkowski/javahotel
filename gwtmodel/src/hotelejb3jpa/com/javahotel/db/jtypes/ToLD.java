package com.javahotel.db.jtypes;

import com.javahotel.types.LId;

public class ToLD {

	public static LId toLId(HId id) {
		Long l = id.getL();
		return new LId(l);
	}

	public static boolean eq(LId id1, HId id2) {
		if (id1 == null) {
			return id2 == null;
		}
		if (id2 == null) { return false; }
		if (id2.getL() == null) { return false; }
		return id1.getId().equals(id2.getL());
	}

}
