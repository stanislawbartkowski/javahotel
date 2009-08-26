package com.javahotel.types;

import java.io.Serializable;

public class LId implements Serializable {

	private Long id;
	private String s;

	public LId() {

	}

	public boolean equals(LId o) {
		return id.equals(o.id);
	}

	public LId(Long id) {
		this.id = id;
		this.s = null;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setS(String s) {
		this.s = s;
	}

	public LId(Long id, String s) {
		this.id = id;
		this.s = s;
	}

	public String getS() {
		return s;
	}

	public Long getId() {
		return id;
	}

	public boolean isNull() {
		return ((s == null) && (id == null));
	}

	@Override
	public String toString() {
		if (s != null) {
			return s;
		}
		if (id != null) {
			return id.toString();
		}
		return "(null)";
	}

	@Override
	public boolean equals(Object o) {
		LId ll = (LId) o;
		if (isNull()) {
			if (ll.isNull()) {
				return true;
			}
			return false;
		}
		if (ll.isNull()) {
			return false;
		}
		if (id != null) {
			if (ll.id == null) {
				return false;
			}
			return id.equals(ll.id);
		}
		if (ll.s == null) {
			return false;
		}
		return s.equals(ll.s);
	}

}
