/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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
package com.javahotel.dbjpa.ejb3;

import com.javahotel.dbutil.log.GetLogger;
import com.javahotel.dbutil.prop.IGetPropertiesFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class CommonData {

	private static IGetPropertiesFactory getPropFactory;
	private static GetLogger log;
	private static String puname;
	private static final Map<String, String> dM;

	static {
		dM = new HashMap<String, String>();
	}

	static String getPuname() {
		return puname;
	}

	static Map<String, String> getDM() {
		return dM;
	}

	static void setPuname(final String aPuname) {
		puname = aPuname;
	}

	static IGetPropertiesFactory getGetPropFactory() {
		return getPropFactory;
	}

	static void setGetPropFactory(final IGetPropertiesFactory aGetPropFactory) {
		getPropFactory = aGetPropFactory;
	}

	static GetLogger getLog() {
		return log;
	}

	static void setLog(final GetLogger aLog) {
		log = aLog;
	}

	static Map<String, String> getProp() {
		Map<String, String> prop = getPropFactory.getPersistProperties(log);
		return prop;

	}
}
