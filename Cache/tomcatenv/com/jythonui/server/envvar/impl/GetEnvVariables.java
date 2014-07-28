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

import java.io.Serializable;

import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.gwtmodel.commoncache.ICommonCache;
import com.gwtmodel.containertype.ContainerInfo;
import com.gwtmodel.containertype.ContainerType;
import com.gwtmodel.mapcache.ICommonCacheFactory;
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.UtilHelper;
import com.jythonui.server.envvar.IGetEnvVariable;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;

public class GetEnvVariables extends UtilHelper implements IGetEnvVariable {

	private final IGetLogMess gMess;
	private final ICommonCache iCache;

	@Inject
	public GetEnvVariables(
			@Named(ISharedConsts.JYTHONMESSSERVER) IGetLogMess gMess,
			ICommonCacheFactory cFactory) {
		this.gMess = gMess;
		this.iCache = cFactory.construct(ISharedConsts.JYTHONENVCACHE);
	}

	private class EnvVar implements Serializable, IEnvVar {
		private static final long serialVersionUID = 1L;
		boolean resL;
		String resS;
		boolean empty = true;

		String toS() {
			if (empty)
				return "empty";
			if (resS != null)
				return resS;
			return new Boolean(resL).toString();
		}

		@Override
		public boolean getL() {
			return resL;
		}

		@Override
		public String getS() {
			return resS;
		}

		@Override
		public boolean isEmpty() {
			return empty;
		}
	}

	private EnvVar getEnvStringDirect(String tName, boolean logVal,
			boolean throwerror) {
		Context initCtx;
		EnvVar r = new EnvVar();
		String eName = tName;
		if (ContainerInfo.getContainerType() == ContainerType.TOMCAT)
			eName = "java:comp/env/" + tName;
		try {
			infoMess(gMess, ILogMess.LOOKFORENVVARIABLE, eName);

			initCtx = new InitialContext();
			Object res = initCtx.lookup(eName);
			if (res == null)
				if (throwerror)
					errorLog(gMess.getMess(IErrorCode.ERRORCODE44,
							ILogMess.CANNOTFINDRESOURCEVARIABLE, eName));
				else
					return r;
			r.empty = false;
			if (logVal)
				r.resL = (Boolean) res;
			else
				r.resS = (String) res;
			// ENVVARIABLEFOUND
			infoMess(gMess, ILogMess.ENVVARIABLEFOUND, eName, r.toS());
			initCtx.close();
			return r;
		} catch (NamingException e) {
			if (throwerror)
				errorLog(gMess.getMess(IErrorCode.ERRORCODE43,
						ILogMess.ERRORWHILEREADINGCONTEXT, eName), e);
			// ENVVARIABLENOTFOUND
			infoMess(gMess, ILogMess.ENVVARIABLENOTFOUND, eName);
			return r;
		}
	}

	@Override
	public IEnvVar getEnvString(String name, boolean logVal, boolean throwerror) {
		Object o = iCache.get(name);
		EnvVar r = (EnvVar) o;
		if (r != null) {

			traceLog(name + " already cached " + r.toS());
			return r;
		}
		r = getEnvStringDirect(name, logVal, throwerror);
		iCache.put(name, r);
		traceLog(name + " to cache " + r.toS());
		return r;
	}

}
