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
package com.jythonui.server.heroku;

import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

import com.google.inject.Inject;
import com.jythonui.server.Util;
import com.jythonui.server.defa.AbstractServerProperties;
import com.jythonui.server.envvar.impl.ChangeEnv;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.server.resource.IReadResourceFactory;
import com.jythonui.server.resourcemulti.IReadMultiResource;
import com.jythonui.server.resourcemulti.IReadMultiResourceFactory;

public class HerokuServerProperties extends AbstractServerProperties {

	private final static String PROP = "heroku.properties";

	@Inject
	public HerokuServerProperties(IReadResourceFactory iFactory, IReadMultiResourceFactory mFactory) {
		super(iFactory, mFactory);
	}

	@Override
	public IReadMultiResource getResource() {
		return mFactory.construct();
	}

	@Override
	public boolean isCached() {
		return true;
	}

	@Override
	public Map<String, String> getDataSourceProp() {
		URL u = this.getClass().getClassLoader().getResource(PROP);
		if (u == null) {
			errorLog(L().getMess(IErrorCode.ERRORCODE142, ILogMess.CANNOTFINDRESOURCEFILE, PROP));
			return null;
		}
		Map<String, String> ma = Util.readResDataMap(u);
		for (Entry<String, String> e : ma.entrySet()) {
			String val = ChangeEnv.replaceV(e.getValue());
			ma.put(e.getKey(), val);
		}
		return ma;
	}

}
