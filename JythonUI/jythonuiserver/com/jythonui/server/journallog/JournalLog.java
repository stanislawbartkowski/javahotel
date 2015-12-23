/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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
package com.jythonui.server.journallog;

import com.google.inject.Inject;
import com.jython.serversecurity.cache.IGetInstanceOObjectIdCache;
import com.jython.serversecurity.cache.OObjectId;
import com.jythonui.server.IConsts;
import com.jythonui.server.IJournalLogin;
import com.jythonui.server.IResolveNameFromToken;
import com.jythonui.server.journal.IJournal;
import com.jythonui.server.journal.JournalRecord;
import com.jythonui.server.objectauth.ObjectCustom;
import com.jythonui.server.security.token.ICustomSecurity;

public class JournalLog implements IJournalLogin {

	private final IJournal iJou;
	private final IGetInstanceOObjectIdCache iCa;
	private final IResolveNameFromToken iRes;

	@Inject
	public JournalLog(IJournal iJou, IGetInstanceOObjectIdCache iCa, IResolveNameFromToken iRes) {
		this.iJou = iJou;
		this.iCa = iCa;
		this.iRes = iRes;

	}

	private void addEntry(OObjectId oObject, String type) {
		JournalRecord elem = new JournalRecord();
		elem.setJournalType(type);
		iJou.addElem(oObject, elem);
	}

	@Override
	public void login(String userName, ICustomSecurity iCust) {
		if (iCust == null || userName == null)
			return;
		ObjectCustom cust = (ObjectCustom) iCust;
		String appId = cust.getInstanceId();
		String objectName = cust.getObjectName();
		// TODO: possible if admin not belonging to anu instance
		// TODO: fix later, 
		if (objectName == null) return;
		OObjectId oObject = iCa.getOObject(appId, objectName, userName);
		addEntry(oObject, IConsts.LOGINJOURNAL);
	}

	@Override
	public void logout(String token) {
		if (!iRes.isCustom(token))
			return;
		OObjectId oObject = iRes.getObject(token);
		// TODO: admin
		if (oObject == null) return;
		addEntry(oObject, IConsts.LOGOUTJOURNAL);
	}

}
