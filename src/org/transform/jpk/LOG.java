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
package org.transform.jpk;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

class LOG {

	private LOG() {

	}

	private static Logger log;

	private static final String CONF = "log.conf";

	static void setConfig(String confDir) throws SecurityException, IOException {
		InputStream i = new FileInputStream(new File(confDir, CONF));
		LogManager logM = LogManager.getLogManager();
		logM.readConfiguration(i);
		log = Logger.getLogger("JPK");
	}

	static void log(String info) {
		log.info(info);
	}

	static void ex(Exception e) {
		log.log(Level.SEVERE, e.getMessage(), e);
	}

}
