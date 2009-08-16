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
package com.javahotel.dbres.log;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;

import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.dbres.messid.IMessId;
import com.javahotel.dbres.resources.GetLMess;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class ELog {

	private ELog() {
	}

	private static final String LOGINADMIN = "LOGINADMIN";
	private static final String LOGINMESS = "LOGINMESS";
	private static final String LOGOUT = "LOGOUT";
	private static final String SUCCESS = "SUCCESS";
	private static final String FAILURE = "FAILURE";
	private static final String CLEARDATADEF = "CLEARDATADEF";
	private static final String LOGINMESSHOTEL = "LOGINMESSHOTEL";

	private static String getS(final String what, final String session,
			final String user) {
		String form = GetLMess.getM(LOGINMESS);
		String mess = MessageFormat.format(form, new Object[] { what, session,
				user });
		return mess;
	}

	private static String getS(final String what, final String session,
			final String user, String hotel) {
		String form = GetLMess.getM(LOGINMESSHOTEL);
		String mess = MessageFormat.format(form, what, session, user, hotel);
		return mess;
	}

	private static String addRes(final String mess, final boolean success) {
		String m;
		if (success) {
			m = GetLMess.getM(SUCCESS);
		} else {
			m = GetLMess.getM(FAILURE);
		}
		return mess + " " + m;
	}

	private static String logiS(final String a, final String session,
			final String user) {
		String mess = getS(a, session, user);
		mess = addRes(mess, true);
		return mess;
	}

	/**
	 * Create log message.
	 * 
	 * @param eve
	 *            Event name id
	 * @param lId
	 *            Event string id for info
	 * @param session
	 *            Session name
	 * @param user
	 *            User name
	 * @param params
	 *            Parameters for format
	 * @return String created
	 */
	public static String logEventS(final String eve, final String lId,
			final String session, final String user, final Object... params) {
		String mess = getS(eve, session, user);
		String form = GetLMess.getM(lId);
		String mess1 = MessageFormat.format(form, params);
		String l = mess + mess1;
		return l;
	}

	public static String logEventHS(final String eve, final String lId,
			final String session, final String user, final String hotel,
			final Object... params) {
		String mess = getS(eve, session, user, hotel);
		String form = GetLMess.getM(lId);
		String mess1 = MessageFormat.format(form, params);
		String l = mess + mess1;
		return l;
	}


	public static String loginAdminSuccessS(final String session,
			final String user) {
		return logiS(LOGINADMIN, session, user);
	}

	public static String logoutS(final String session, final String user) {
		return logiS(LOGOUT, session, user);
	}

	public static String cleardefS(final String session, final String user) {
		return logiS(CLEARDATADEF, session, user);
	}

	public static void loginAdminFailure(final String session,
			final String user, Throwable t) {
		String mess = getS(LOGINADMIN, session, user);
		mess = addRes(mess, false);
		HLog.getLo().log(Level.SEVERE,mess, t);
	}

	public static String logListS(final String session, String user,
			String lType) {
		return logEventS(IMessId.GETLIST, IMessId.GETLIST, session, user, lType);
	}

	public static String logDrawPeriodS(final String eve, final String session,
			final String user, String hotel, final Date dFrom, final Date Dto) {
		return logEventHS(GetLMess.getM(eve), IMessId.DRAWPERIOD, session, user, hotel,
				DateFormatUtil.toS(dFrom), DateFormatUtil.toS(Dto));
	}

	public static String drawColStringS(final Collection<String> col) {
		return col.toString();
	}
}
