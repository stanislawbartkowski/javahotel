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
package com.javahotel.dbres.log;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.javahotel.common.command.CommandParam;
import com.javahotel.dbres.exceptions.HotelException;
import com.javahotel.dbres.messid.IMessId;
import com.javahotel.dbres.resources.GetLMess;
import com.javahotel.dbutil.log.GetLogger;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class HLog {

	private static final String LOGGERNAME = "com.javahotel.log";
	private static final GetLogger logL;

	static {
		logL = new GetLogger(LOGGERNAME);
	}

	private HLog() {
	}

	public static GetLogger getL() {
		return logL;
	}

	public static Logger getLo() {
		return logL.getL();
	}

	public static String failureS(final String messId, String param1) {
		String mess = GetLMess.getM(messId);
		String m = MessageFormat.format(mess, new Object[] { param1 });
		return m;
	}

	private static String getFatalLogin(final String session,
			final String user, final String hotel) {
		String form = GetLMess.getM(IMessId.FATALLOGININFO);
		String mess = MessageFormat.format(form, session, user, hotel);
		return mess;
	}

	private static String failureS(final String session, final String user,
			final String hotel, final String messid, final Object... params) {
		String lI = getFatalLogin(session, user, hotel);
		String form = GetLMess.getM(messid);
		if (params.length == 0) {
			return lI + " " + form;
		}
		String s = MessageFormat.format(form, params);
		return lI + " " + s;
	}

	public static void logFailureS(final String session, final String user,
			final String hotel, final String messid, final Object... params) {
		String s = failureS(session, user, hotel, messid, params);
		logL.getL().log(Level.SEVERE, s);
	}

	private static void throwE(final String s) {
		GetLogger l = HLog.getL();
		HotelException e = new HotelException(s);
		l.getL().log(Level.SEVERE,"",e); 
		throw e;
	}

	public static void logFailureSE(final String session, final String user,
			final String hotel, final String messid, final Object... params) {
		String s = failureS(session, user, hotel, messid, params);
		throwE(s);
	}

	public static void failureE(final String messId, final String param1) {
		String m = failureS(messId, param1);
		throwE(m);
	}

	public static void fatalFailureE(Throwable e) {
		GetLogger l = HLog.getL();
		l.getL().log(Level.SEVERE,"",e); 
	}

	public static CommandParam.ILog getILog() {

		CommandParam.ILog i = new CommandParam.ILog() {

			public void logEmptyParam(final String pa) {
				try {
					failureE(IMessId.EMPTYCOMMANDPARAM, pa);
				} catch (HotelException ex) {
				}
			}
		};

		return i;

	}
}
