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
package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.migration.properties.PropHolder;

abstract class MainHelper {

	private static String PROP = "prop.properties";

	protected static void e(String s) {
		System.out.println(s);
	}

	protected static void pVersion() {
		e("Version: 2017/07/07");
	}

	protected static void readProp() throws FileNotFoundException, IOException {
		Properties prop = new Properties();
		prop.load(new FileInputStream(PROP));
		// merge with default
		PropHolder.getProp().putAll(prop);

	}

}
