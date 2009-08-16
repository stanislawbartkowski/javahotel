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

import com.javahotel.db.authentication.jpa.Hotel;
import com.javahotel.db.authentication.jpa.Person;
import com.javahotel.db.hoteldb.HotelStore;
import com.javahotel.remoteinterfaces.SessionT;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class ClearAuthBaseCommand extends CommandTra {

	ClearAuthBaseCommand(final SessionT sessionId) {
		super(sessionId, null, null, true, true, false, false);

	}

	@Override
	protected void command() {
		iC.getJpa().removeAll(Hotel.class);
		iC.getJpa().removeAll(Person.class);
		// iC.getJpa().removeAll(GroupD.class);
		HotelStore.invalidateCache();
	}
}
