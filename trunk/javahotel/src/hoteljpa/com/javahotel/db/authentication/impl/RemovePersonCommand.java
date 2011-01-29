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

import com.javahotel.dbres.log.ELog;
import com.javahotel.dbres.log.HLog;
import com.javahotel.dbres.messid.IMessId;
import com.javahotel.dbres.resources.GetLMess;
import com.javahotel.remoteinterfaces.SessionT;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class RemovePersonCommand extends CommandTra {

	RemovePersonCommand(final SessionT sessionId, final String person) {
		super(sessionId, person, null, true, true, false, false);
	}

	@Override
	protected void command() {
		String l = ELog.logEventS(IMessId.REMOVEPERSON, IMessId.DESCPERSON, iC
				.getSession().getName(), iC.getHP().getUser(), pe.getName());
		if (pe == null) {
			String e = GetLMess.getM(GetLMess.CANNOTREMOVE);
			HLog.getLo().info(l + e);
			trasuccess = false;
			return;
		}
		HLog.getLo().warning(l);
		iC.getJpa().removeObject(pe);
	}
}
