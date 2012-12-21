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
