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
package com.javahotel.db.authentication.impl;

import java.util.List;

import com.javahotel.common.toobject.HotelP;
import com.javahotel.common.toobject.PersonP;
import com.javahotel.remoteinterfaces.HotelT;
import com.javahotel.remoteinterfaces.SessionT;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class GetList {

	public static List<HotelP> getHotelList(final SessionT sessionId) {
		GetHotelListCommand p = new GetHotelListCommand(sessionId);
		p.run();
		return p.res;
	}

	public static List<PersonP> getPersonList(final SessionT sessionId) {
		GetPersonListCommand p = new GetPersonListCommand(sessionId);
		p.run();
		return p.out;
	}

	public static List<String> getPersonHotelRoles(
			final SessionT sessionId, final String person, final HotelT hotel) {
		GetPersonHotelRoles p = new GetPersonHotelRoles(sessionId, person,
				hotel);
		p.run();
		return p.out;
	}
}
