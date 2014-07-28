/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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
package com.jythonui.server.envvar.impl;

import javax.inject.Inject;

import com.jythonui.server.defa.AbstractServerProperties;
import com.jythonui.server.envvar.IGetEnvVariable;
import com.jythonui.server.envvar.IGetEnvVariable.IEnvVar;
import com.jythonui.server.envvar.IGetResourceJNDI;
import com.jythonui.server.resource.IReadResource;
import com.jythonui.server.resource.IReadResourceFactory;

public class ServerPropertiesEnv extends AbstractServerProperties {

	private final IGetEnvVariable iGet;

	private final IGetResourceJNDI getJNDI;

	@Inject
	public ServerPropertiesEnv(IGetResourceJNDI getJNDI,
			IReadResourceFactory iFactory, IGetEnvVariable iGet) {
		super(iFactory);
		this.getJNDI = getJNDI;
		this.iGet = iGet;
	}

	@Override
	public String getEJBHost() {
		IEnvVar e = getEnvString(getJNDI.getEJBHost(), false, false);
		if (e.isEmpty())
			return null;
		return e.getS();
	}

	@Override
	public String getEJBPort() {
		IEnvVar e = getEnvString(getJNDI.getEJBPort(), false, false);
		if (e.isEmpty())
			return null;
		return e.getS();
	}

	private IEnvVar getEnvString(String name, boolean logVal, boolean throwerror) {
		return iGet.getEnvString(name, logVal, throwerror);
	}

	@Override
	public IReadResource getResource() {
		IEnvVar e = getEnvString(getJNDI.getResourceDir(), false, false);
		if (!e.isEmpty())
			return iFactory.constructDir(e.getS());
		return iFactory.constructLoader(this.getClass().getClassLoader());
	}

	@Override
	public boolean isCached() {
		IEnvVar e = getEnvString(getJNDI.getCachedValue(), true, false);
		if (e.getL())
			return true;
		return e.getL();
	}

}
