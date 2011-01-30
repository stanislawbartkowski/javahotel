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
