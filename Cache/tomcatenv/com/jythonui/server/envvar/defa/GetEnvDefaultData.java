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
package com.jythonui.server.envvar.defa;

import com.google.inject.Inject;
import com.jythonui.server.IGetEnvDefaultData;
import com.jythonui.server.envvar.IGetEnvVariable;

public class GetEnvDefaultData implements IGetEnvDefaultData {

	private final IGetEnvVariable iGet;

	@Inject
	public GetEnvDefaultData(IGetEnvVariable iGet) {
		this.iGet = iGet;
	}

	@Override
	public String getVal(String key) {
		IGetEnvVariable.IEnvVar e = iGet.getEnvString(key,
				IGetEnvVariable.ResType.STRING, false);
		if (e.isEmpty())
			return null;
		return e.getS();
	}

}
