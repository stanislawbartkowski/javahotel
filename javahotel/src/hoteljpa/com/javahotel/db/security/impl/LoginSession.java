/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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

import com.javahotel.commoncache.CollCache;
import com.javahotel.dbres.resources.IMess;
import com.javahotel.security.login.HotelLoginP;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class LoginSession {

	private static CollCache<HotelLoginP> ca;

	static {
		ca = new CollCache<HotelLoginP>(IMess.LOGINCACHEID);
	}

	static void addLogin(final String sessionId, final HotelLoginP p) {
		ca.addT(sessionId,p);
	}

	static HotelLoginP getLogin(final String sessionId) {
		return ca.get(sessionId);
	}

	static void removeLogin(final String sessionId) {
		ca.removeT(sessionId);
	}

}
