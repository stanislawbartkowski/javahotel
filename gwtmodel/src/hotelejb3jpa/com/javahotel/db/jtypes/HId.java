package com.javahotel.db.jtypes;

import com.javahotel.types.LId;

public class HId {

	private final Long l;

	public HId(LId k) {
		this.l = k.getId();
	}

	public HId(Long l) {
		this.l = l;
	}

	public boolean equals(HId p) {
		return l.equals(p.l);
	}

	public Long getL() {
		return l;
	}
	
	public boolean isNull() {
		return l == null;
	}
}
