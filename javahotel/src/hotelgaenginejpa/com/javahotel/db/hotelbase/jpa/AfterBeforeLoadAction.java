/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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

package com.javahotel.db.hotelbase.jpa;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.google.appengine.api.datastore.Key;
import com.javahotel.db.jtypes.HId;
import com.javahotel.db.jtypes.IId;
import com.javahotel.dbjpa.copybean.GetFieldHelper;
import com.javahotel.dbjpa.ejb3.IAfterBeforeLoadAction;
import com.javahotel.dbjpa.ejb3.JpaEntity;
import com.javahotel.dbutil.log.GetLogger;

class AfterBeforeLoadAction implements IAfterBeforeLoadAction {

	private void doCollectionAction(GetLogger log, JpaEntity jpa, Object o,
			String keyCollectionField, String objectCollectionField,
			Class<?> cla) {
		List<Long> keyC = (List<Long>) GetFieldHelper.getterVal(o,
				keyCollectionField, log);
		List<Object> objC = (List<Object>) GetFieldHelper
				.getterVal(o, objectCollectionField, log);
		objC.clear();
		if (keyC != null) {
			for (Long l : keyC) {
				if (l == null) {
					continue;
				}
				Object co = jpa.getRecord(cla, new HId(l));
				objC.add(co);
			}
		}
	}

	private void doAction(GetLogger log, JpaEntity jpa, Object o,
			String keyField, String objectField) {
		Object l = GetFieldHelper.getterVal(o, keyField, log);
		HId key = null;
		if (l != null) {
			if (l instanceof Long) {
				key = new HId((Long) l);
			} else if (l instanceof Key) {
				key = new HId((Key) l);
			} else {
				String logs = MessageFormat.format("{0} , class {1} - only Long or Key expected, got {2}",
						keyField, o.getClass().getSimpleName(), l.getClass()
								.getSimpleName());
			    log.getL().log(Level.SEVERE,logs);
			}
		}

		Object destO = null;
		Method me = null;
		String naf = GetFieldHelper.getF(objectField);
		try {
			me = GetFieldHelper.getMe(o, naf);
		} catch (NoSuchMethodException e) {
			log.getL().log(Level.SEVERE,"No setter " + objectField, e);
			return;
		}
		Class<?> cla = me.getReturnType();

		if (l != null) {
			destO = jpa.getRecord(cla, key);
			if (destO == null) {
				String msg = MessageFormat.format("{0} - {1} : not null long {2} - object not found {3}",
						keyField, objectField, l, cla.getSimpleName());
				log.getL().log(Level.SEVERE,msg);
			}
		}
		GetFieldHelper.setterVal(o, destO, objectField, cla, log);
	}

	private void doBeforeAction(GetLogger log, JpaEntity jpa, Object o,
			String keyField, String objectField) {
		IId i = (IId) GetFieldHelper.getterVal(o, objectField, log);
		Object l = null;
		Class<?> cla = Long.class;
		try {
			GetFieldHelper.setMe(o, keyField, Long.class);
			if (i != null) {
				l = i.getId().getL();
			}
		} catch (NoSuchMethodException e) {
			cla = Key.class;
			if (i != null) {
				l = i.getId().getId();
			}
		}
		GetFieldHelper.setterVal(o, l, keyField, cla, log);
	}

	private void doCollectionBeforeAction(GetLogger log, JpaEntity jpa,
			Object o, String keyCollectionField, String objectColllectionField) {
		List<Long> keyC = (List<Long>) GetFieldHelper.getterVal(o,
				keyCollectionField, log);
		List<Object> objC = (List<Object>) GetFieldHelper
				.getterVal(o, objectColllectionField, log);
		if (keyC == null) {
			keyC = new ArrayList<Long>();
			GetFieldHelper.setterVal(o, keyC, keyCollectionField,
					List.class, log);
		}
		keyC.clear();
		for (Object oc : objC) {
			IId id = (IId) oc;
			keyC.add(id.getId().getL());
		}

	}

	private void objectCollection(GetLogger log, JpaEntity jpa, Object o,
			String objectCollectionField) {
		List<Object> objC = (List<Object>) GetFieldHelper
				.getterVal(o, objectCollectionField, log);
		if (objC == null) {
			return;
		}
		for (Object co : objC) {
			afterLoadAction(log, jpa, co);
		}

	}

	private void beforePersisObjectCollection(GetLogger log, JpaEntity jpa,
			Object o, String objectCollectionField) {
		List<Object> objC = (List<Object>) GetFieldHelper
				.getterVal(o, objectCollectionField, log);
		if (objC == null) {
			return;
		}
		for (Object co : objC) {
			beforePersistAction(log, jpa, co);
		}

	}

	public void afterLoadAction(GetLogger log, JpaEntity jpa, Object o) {
		KeyObject k = o.getClass().getAnnotation(KeyObject.class);
		if (k != null) {
			doAction(log, jpa, o, k.keyField(), k.objectField());
		}
		KeyObjects ks = o.getClass().getAnnotation(KeyObjects.class);
		if (ks != null) {
			for (int i = 0; i < ks.keyFields().length; i++) {
				doAction(log, jpa, o, ks.keyFields()[i], ks.objectFields()[i]);
			} // for
		}
		KeyCollectionObject kc = o.getClass().getAnnotation(
				KeyCollectionObject.class);
		if (kc != null) {
			doCollectionAction(log, jpa, o, kc.keyCollectionField(), kc
					.objectCollectionField(), kc.classObject());
		}
		KeyCollectionObjects kcs = o.getClass().getAnnotation(
				KeyCollectionObjects.class);
		if (kcs != null) {
			for (int i = 0; i < kcs.keyCollectionField().length; i++) {
				doCollectionAction(log, jpa, o, kcs.keyCollectionField()[i],
						kcs.objectCollectionField()[i], kcs.classObject()[i]);
			} // for
		} // if
		ObjectCollection ocs = o.getClass().getAnnotation(
				ObjectCollection.class);
		if (ocs != null) {
			objectCollection(log, jpa, o, ocs.objectCollectionField());
		}

		ObjectCollections ocss = o.getClass().getAnnotation(
				ObjectCollections.class);
		if (ocss != null) {
			for (int i = 0; i < ocss.objectCollectionField().length; i++) {
				objectCollection(log, jpa, o, ocss.objectCollectionField()[i]);
			}
		}

	}

	public void beforePersistAction(GetLogger log, JpaEntity jpa, Object o) {
		KeyObject k = o.getClass().getAnnotation(KeyObject.class);
		if (k != null) {
			doBeforeAction(log, jpa, o, k.keyField(), k.objectField());
		}
		KeyObjects ks = o.getClass().getAnnotation(KeyObjects.class);
		if (ks != null) {
			for (int i = 0; i < ks.keyFields().length; i++) {
				doBeforeAction(log, jpa, o, ks.keyFields()[i], ks
						.objectFields()[i]);
			} // for
		}
		KeyCollectionObject kc = o.getClass().getAnnotation(
				KeyCollectionObject.class);
		if (kc != null) {
			doCollectionBeforeAction(log, jpa, o, kc.keyCollectionField(), kc
					.objectCollectionField());
		}
		KeyCollectionObjects kcs = o.getClass().getAnnotation(
				KeyCollectionObjects.class);
		if (kcs != null) {
			for (int i = 0; i < kcs.keyCollectionField().length; i++) {
				doCollectionBeforeAction(log, jpa, o,
						kcs.keyCollectionField()[i], kcs
								.objectCollectionField()[i]);
			} // for
		} // if
		ObjectCollection ocs = o.getClass().getAnnotation(
				ObjectCollection.class);
		if (ocs != null) {
			beforePersisObjectCollection(log, jpa, o, ocs
					.objectCollectionField());
		}

		ObjectCollections ocss = o.getClass().getAnnotation(
				ObjectCollections.class);
		if (ocss != null) {
			for (int i = 0; i < ocss.objectCollectionField().length; i++) {
				beforePersisObjectCollection(log, jpa, o, ocss
						.objectCollectionField()[i]);
			}
		}

	}
}
