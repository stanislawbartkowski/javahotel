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
