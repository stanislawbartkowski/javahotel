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
package com.javahotel.db.copy;

import java.util.ArrayList;
import java.util.List;

import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.db.context.ICommandContext;
import com.javahotel.db.hotelbase.types.IHotelDictionary;
import com.javahotel.dbjpa.copybean.GetFieldHelper;
import com.javahotel.dbres.messid.IMessId;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class CopyBeanToP {

	interface ICopyHelper {

		boolean eq(Object o1, Object o2);

		Object getI(Object se);

		void copy(final ICommandContext iC, final Object sou, Object dest);
	}

	abstract static class ICopyDicHelper implements ICopyHelper {

		public boolean eq(Object o1, Object o2) {
			DictionaryP oo1 = (DictionaryP) o1;
			IHotelDictionary oo2 = (IHotelDictionary) o2;
			String l1 = oo1.getName();
			String l2 = oo2.getName();
			return (l1.equals(l2));
		}
	}

	static void orig_copyRes1Collection(final ICommandContext iC,
			final AbstractTo sou, final Object dest, final String colField,
			final String upF, final Class<?> cupF, final ICopyHelper i,
			final boolean remove) {
		List col = (List) GetFieldHelper.getterVal(sou, colField,
				iC.getLog());
		List de = (List) GetFieldHelper.getterVal(dest, colField,
				iC.getLog());
		List removeO = new ArrayList();
		if (de == null) {
			de = new ArrayList();
			GetFieldHelper.setterVal(dest, de, colField, List.class, iC
					.getLog());
		} else {
			for (Object o : de) {
				removeO.add(o);
			}
			de.clear();
		}

		if (col != null) {
			for (Object o : col) {
				Object des = null;
				for (Object oo : removeO) {
					if (i.eq(o, oo)) {
						des = oo;
						break;
					}
				}
				if (des == null) {
					des = i.getI(o);
				} else {
					removeO.remove(des);
				}
				i.copy(iC, o, des);
				if (upF != null) {
					GetFieldHelper.setterVal(des, dest, upF, cupF, iC.getLog());
				}
				de.add(des);
			}
		}
		if (remove) {
			for (Object o : removeO) {
				iC.getJpa().removeObject(o);
			}
		}

	}

	static void copyRes1Collection(final ICommandContext iC,
			final AbstractTo sou, final Object dest, final String colField,
			final String upF, final Class<?> cupF, final ICopyHelper i,
			final boolean removefromDB) {
		List<?> col = (List<?>) GetFieldHelper.getterVal(sou,
				colField, iC.getLog());
		List<Object> de = (List<Object>) GetFieldHelper.getterVal(
				dest, colField, iC.getLog());
		if (de == null) {
			de = new ArrayList<Object>();
			GetFieldHelper.setterVal(dest, de, colField, List.class, iC
					.getLog());
		}

		ArrayList<Object> added = new ArrayList<Object>();

		if (col != null) {
			for (Object o : col) {
				Object des = null;
				for (Object oo : de) {
					if (i.eq(o, oo)) {
						des = oo;
						added.add(des);
						break;
					}
				}
				if (des == null) {
					des = i.getI(o);
					de.add(des);
					added.add(des);
				}
				i.copy(iC, o, des);
				if (upF != null) {
					GetFieldHelper.setterVal(des, dest, upF, cupF, iC.getLog());
				}
			}
		}

		boolean wasremoved = true;
		while (wasremoved) {
			wasremoved = false;
			for (Object oo : de) {
				Object rem = oo;
				if (col == null) {
					break;
				}
				for (Object o : added) {
					if (oo == o) { // important: compare references
						rem = null;
						break;
					}
				} // for
				if (rem != null) {
					de.remove(rem);
					if (removefromDB) {
						iC.getJpa().removeObject(rem); // ADDED !
						String logm = iC.logEvent(IMessId.REMOVEFROMCOLLECTION,
								colField, upF, sou.getClass().getSimpleName(),
								rem.getClass().getSimpleName());
						iC.getLog().getL().info(logm);
					}
					wasremoved = true;
					break;
				}
			} // for
		} // while
	}

	static void copyRes1(final ICommandContext iC, final DictionaryP sou,
			final IHotelDictionary dest, final String[] malist,
			final String colField, final String upF, final Class<?> cupF,
			final ICopyHelper i, final boolean remove) {
		CopyHelper.copyDict1(iC, sou, dest, malist);
		copyRes1Collection(iC, sou, dest, colField, upF, cupF, i, remove);
	}
}
