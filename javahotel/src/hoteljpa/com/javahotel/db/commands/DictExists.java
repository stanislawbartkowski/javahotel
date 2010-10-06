/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
package com.javahotel.db.commands;

import java.text.MessageFormat;
import java.util.logging.Level;

import com.javahotel.common.command.DictType;
import com.javahotel.db.context.ICommandContext;
import com.javahotel.db.util.CommonHelper;
import com.javahotel.dbjpa.copybean.GetFieldHelper;
import com.javahotel.dbres.entityconstr.EntityConstr;
import com.javahotel.dbres.entityconstr.EntityConstrFactory;
import com.javahotel.dbres.entityconstr.IEntityConstr;
import com.javahotel.dbres.exceptions.HotelException;
import com.javahotel.dbres.log.HLog;
import com.javahotel.dbres.resources.GetMess;
import com.javahotel.dbutil.log.GetLogger;

class DictExists {

	private static final IEntityConstr co;
	private static final GetLogger log;

	private DictExists() {
	}

	static {
		log = HLog.getL();
		co = EntityConstrFactory.createEntityConstr(log);
	}

	static class ErrKom {
		private final String errInfo;
		private final String viewName;

		ErrKom(String p, String p1) {
			errInfo = p;
			viewName = p1;
		}

		public String getViewName() {
			return viewName;
		}

		public String getErrInfo() {
			return errInfo;
		}
	}

	static ErrKom existsAlready(ICommandContext iC, Object a, DictType d) {
		Class<?> persistClass;
		if (d == null) {
			persistClass = ObjectFactory.getP(a.getClass());
		} else {
			persistClass = ObjectFactory.getC(d);
		}
		EntityConstr e = co.getEntityC(persistClass.getSimpleName());
		if (e == null) {
			return null;
		}
		String fname = e.getSymname();
		Object val = GetFieldHelper.getterVal(a, fname, log);
		if (val == null) {
			String s = MessageFormat.format("{0} field: {1} - inproper field name !", a
					.getClass().getSimpleName(), fname);
			log.getL().log(Level.SEVERE,s);
			throw new HotelException(s);
		}
		String name = (String) val;
		Object o;
		if (d == null) {
			o = iC.getJpa().getOneWhereRecord(persistClass, fname, name);
		} else {
			o = CommonHelper.getA(iC, persistClass, name);
		}
		if (o == null) {
			return null;
		}
		String kom = GetMess.getFormatMess(e.getKomname(), name);
		return new ErrKom(kom, e.getViewname());
	}

}
