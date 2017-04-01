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
package com.jython.ui.server.guice;

import com.jythonui.server.envvar.IGetResourceJNDI;

public class GetResourceJNDI implements IGetResourceJNDI {

	private final static String RESOURCEDIR = "sampleapp/resource";
	private final static String CACHED = "sampleapp/cached";
	private final static String DATASOURCEENV = "sampleapp/datasource";
	private final static String MAILSESSION = "mail/Session";

	@Override
	public String getResourceDir() {
		return RESOURCEDIR;
	}

	@Override
	public String getCachedValue() {
		return CACHED;
	}

	@Override
	public String getEJBHost() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEJBPort() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMailName() {
		return MAILSESSION;
	}

	@Override
	public String dataSourceEnv() {
		return DATASOURCEENV;
	}

}
