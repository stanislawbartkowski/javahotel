/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
package com.javahotel.dbres.resources;

import java.text.MessageFormat;
import java.util.Map;

import com.javahotel.dbres.log.HLog;
import com.javahotel.dbutil.prop.ReadProperties;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class GetMess {

	private static final Map<String, String> prop;
	private static final String PROP = IMess.RESOURCEFOLDER
			+ "/messages_pl_PL.properties";
	public static final String INVALIDSESSION = "INVALIDSESSION";

	static {
		prop = ReadProperties.getProperties(PROP, HLog.getL());
	}

	private GetMess() {
	}

	public static String getM(final String mId) {
		return prop.get(mId);
	}

	public static String getFormatMess(final String mId, String... params) {
		String format = getM(mId);
		return MessageFormat.format(format, (Object[]) params);
	}
}
