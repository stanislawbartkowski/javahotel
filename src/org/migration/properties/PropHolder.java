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
package org.migration.properties;

import java.io.IOException;
import java.util.Properties;

public class PropHolder {

	private PropHolder() {

	}

	public static final String TABLESPACE = "table.tablespace";
	public static final String REPLACECOL = "replacecol.";
	public static final String SEQUENCEMAX = "sequence.maxval";
	public static final String PROCTERMINATOR = "sqlproc.terminator";
	
	public static Properties getProp() {
		return prop;
	}

	private static final Properties prop = new Properties();

	static {
		try {
			prop.load(PropHolder.class.getClassLoader().getResourceAsStream("default.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
