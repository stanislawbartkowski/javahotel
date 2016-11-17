/*
 * Copyright 2016 stanislawbartkowski@gmail.com 
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

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.jythonui.server.IConsts;
import com.jythonui.server.defa.AbstractServerProperties;
import com.jythonui.server.envvar.IGetEnvVariable;
import com.jythonui.server.envvar.IGetEnvVariable.IEnvVar;
import com.jythonui.server.envvar.IGetResourceJNDI;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.server.resource.IReadResourceFactory;
import com.jythonui.server.resourcemulti.IReadMultiResource;
import com.jythonui.server.resourcemulti.IReadMultiResourceFactory;

public class ServerPropertiesEnv extends AbstractServerProperties {

	private final IGetEnvVariable iGet;

	private final IGetResourceJNDI getJNDI;

	@Inject
	public ServerPropertiesEnv(IGetResourceJNDI getJNDI, IReadResourceFactory iFactory,
			IReadMultiResourceFactory mFactory, IGetEnvVariable iGet) {
		super(iFactory, mFactory);
		this.getJNDI = getJNDI;
		this.iGet = iGet;
	}

	@Override
	public String getEJBHost() {
		IEnvVar e = getEnvString(getJNDI.getEJBHost(), IGetEnvVariable.ResType.STRING, false);
		if (e.isEmpty())
			return null;
		return e.getS();
	}

	@Override
	public String getEJBPort() {
		IEnvVar e = getEnvString(getJNDI.getEJBPort(), IGetEnvVariable.ResType.STRING, false);
		if (e.isEmpty())
			return null;
		return e.getS();
	}

	private IEnvVar getEnvString(String name, IGetEnvVariable.ResType rType, boolean throwerror) {
		return iGet.getEnvString(name, rType, throwerror);
	}

	@Override
	public IReadMultiResource getResource() {
		IEnvVar e = getEnvString(getJNDI.getResourceDir(), IGetEnvVariable.ResType.STRING, false);
		if (!e.isEmpty())
			return mFactory.construct(e.getS());
		return mFactory.construct();
	}

	@Override
	public boolean isCached() {
		IEnvVar e = getEnvString(getJNDI.getCachedValue(), IGetEnvVariable.ResType.LOG, false);
		if (e.getL())
			return true;
		return e.getL();
	}

	@Override
	public Map<String, String> getDataSourceProp() {
		IEnvVar e = getEnvString(getJNDI.dataSourceEnv(), IGetEnvVariable.ResType.STRING, false);
		if (e.isEmpty())
			return null;
		String[] s = e.getS().split(",");
		if (s.length <= 1) {
			// MORETHENONEVALUESEXPECTED
			errorLog(L().getMess(IErrorCode.ERRORCODE140, ILogMess.MORETHENONEVALUESEXPECTED, getJNDI.dataSourceEnv(),
					e.getS(), "" + s.length));
			return null;
		}
		String prefix = s[0];
		Map<String, String> ma = new HashMap<String, String>();
		for (int i = 1; i < s.length; i++) {
			IEnvVar ee = getEnvString(prefix + "." + s[i], IGetEnvVariable.ResType.STRING, true);
			ma.put(s[i], ChangeEnv.replaceV(ee.getS()));
		}
		return ma;
	}

}
