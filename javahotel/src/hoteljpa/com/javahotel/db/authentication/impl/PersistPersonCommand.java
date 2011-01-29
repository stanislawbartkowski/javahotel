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
package com.javahotel.db.authentication.impl;

import com.javahotel.db.authentication.jpa.Person;
import com.javahotel.dbres.log.ELog;
import com.javahotel.dbres.log.HLog;
import com.javahotel.dbres.messid.IMessId;
import com.javahotel.remoteinterfaces.PasswordT;
import com.javahotel.remoteinterfaces.SessionT;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class PersistPersonCommand extends CommandTra {

	private final PasswordT password;

	PersistPersonCommand(final SessionT sessionId, final String person,
			final PasswordT password) {
		super(sessionId, person, null, true, true, false, false);
		this.password = password;

	}

	@Override
	protected void command() {
		String e = IMessId.CHANGEPERSON;
		if (pe == null) {
			pe = new Person();
			pe.setName(person);
			e = IMessId.ADDPERSON;
		}
		String logs = ELog.logEventS(e, IMessId.DESCPERSON, iC.getSession()
				.getName(), iC.getHP().getUser(), pe.getName());
		HLog.getLo().info(logs);
		pe.setPassword(password.getPassword());
		iC.getJpa().changeRecord(pe);
	}
}
