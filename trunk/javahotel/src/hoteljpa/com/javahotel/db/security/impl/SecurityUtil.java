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
package com.javahotel.db.security.impl;

import java.text.MessageFormat;
import java.util.logging.Level;

import com.javahotel.dbres.exceptions.HotelException;
import com.javahotel.dbres.log.HLog;
import com.javahotel.dbres.resources.GetLMess;
import com.javahotel.dbres.resources.GetMess;
import com.javahotel.dbres.resources.IMess;
import com.javahotel.security.login.HotelLoginP;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class SecurityUtil {

	private SecurityUtil() {
	}

	public static HotelLoginP getL(final String sessionId) {
		HotelLoginP h = LoginSession.getLogin(sessionId);
		if (h == null) {
			String i = GetMess.getM(GetMess.INVALIDSESSION);
			String mess = MessageFormat.format(i, new Object[] { sessionId });
			HotelException e = new HotelException(mess);
			HLog.getLo().log(Level.SEVERE, mess, e);
			throw e;
		}
		return h;
	}

	public static void validAdmin(final HotelLoginP h) {
		if (h.isAdmin()) {
			return;
		}
		String me = GetLMess.getM(IMess.ADMINEXPECTED);
		HotelException e = new HotelException(me);
		HLog.getLo().log(Level.SEVERE, "", e);
		throw e;
	}
}
