/*
 * Copyright 2017 stanislawbartkowski@gmail.com  
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
package com.jythonui.server.fromtoken;

import com.google.inject.Inject;
import com.jython.serversecurity.AppInstanceId;
import com.jython.serversecurity.cache.IGetInstanceOObjectIdCache;
import com.jython.serversecurity.cache.OObjectId;
import com.jythonui.server.IResolveNameFromToken;
import com.jythonui.server.objectauth.ObjectCustom;
import com.jythonui.server.security.ISecurity;
import com.jythonui.server.security.token.ICustomSecurity;

public class ResolveNameFromToken implements IResolveNameFromToken {

	private final ISecurity iSec;
	private final IGetInstanceOObjectIdCache iGet;

	@Inject
	public ResolveNameFromToken(ISecurity iSec, IGetInstanceOObjectIdCache iGet) {
		this.iSec = iSec;
		this.iGet = iGet;
	}

	@Override
	public OObjectId getObject(String token) {
		ICustomSecurity sec = iSec.getCustom(token);
		ObjectCustom cust = (ObjectCustom) sec;
		String instanceId = cust.getInstanceId();
		String hotelName = cust.getObjectName();
		return iGet.getOObject(instanceId, hotelName, iSec.getUserName(token));
	}

	@Override
	public AppInstanceId getInstance(String token) {
		ICustomSecurity sec = iSec.getCustom(token);
		ObjectCustom cust = (ObjectCustom) sec;
		String instanceId = cust.getInstanceId();
		return iGet.getInstance(instanceId, iSec.getUserName(token));
	}

	@Override
	public boolean isCustom(String token) {
		return iSec.getCustom(token) != null;
	}

}
