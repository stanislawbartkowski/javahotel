/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
package com.javahotel.webhelper;

import java.text.MessageFormat;
import java.util.Map;
import java.util.logging.Level;

import com.javahotel.dbutil.log.GetLogger;
import com.javahotel.dbutil.prop.ReadProperties;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class HelperResources {

	static final GetLogger lo = new GetLogger("com.javahotel.webgwt");
	static final Map<String, String> lProp;
	static final String NOTAUTHENTICATED = "NOTAUTHENTICATED";
	static final String STARTINIT = "STARTINIT";
	static final String GETLISTBEAN = "GETLISTBEAN";
	static final String WEBHELPERCACHE = "WEBHELPERCASCHE";

	static {
		lProp = ReadProperties.getRProperties("loginfo.properties", lo);
	}

	static String getInfo(final String rId, String... p) {
		String f = lProp.get(rId);
		String i = MessageFormat.format(f, (Object[])p);
		return i;
	}

	static void logE(final String rId, String... p) {
		String s = getInfo(rId, p);
		RuntimeException e = new RuntimeException(s);
		lo.getL().log(Level.SEVERE,"",e); 
		throw e;
	}

	static void info(final String rId) {
		String f = lProp.get(rId);
		lo.getL().info(f);
	}
}
