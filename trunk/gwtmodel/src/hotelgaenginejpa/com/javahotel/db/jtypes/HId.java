package com.javahotel.db.jtypes;

import com.google.appengine.api.datastore.Key;
import com.javahotel.types.LId;

public class HId {

	private final Key id;
	private final Long l;

	public Key getId() {
		return id;
	}

	public HId(Key k) {
		this.id = k;
		l = null;
	}

	public HId(LId k) {
		this.l = k.getId();
		this.id = null;
	}

	public HId(Long l) {
		this.l = l;
		id = null;
	}

	public boolean equals(HId p) {
		if (id == null) {
			if (p.getId() == null) {
				return true;
			}
			return false;
		}
		if (p.getId() == null) {
			return false;
		}
		return id.equals(p.getId());
	}

	public Long getL() {
		if (l != null) {
			return l;
		}
		if (id == null) {
			return null;
		}
		return id.getId();
	}

	@Override
	public String toString() {
		if (l != null) {
			return l.toString();
		}
		return id.toString();
	}
	
	public boolean isNull() {
		return ((l == null) && (id == null)); 
	}
}
