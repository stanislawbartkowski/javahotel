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
import java.util.Enumeration;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Helper class for logging
 */

class LOG {

	private LOG() {
	}

	private static Logger log;

	private static final String CONF = "log.conf";

	static void setConfig(String confDir, String logFile) throws SecurityException, IOException {
		InputStream i = new FileInputStream(new File(confDir, CONF));
		LogManager logM = LogManager.getLogManager();
		logM.readConfiguration(i);
		log = Logger.getLogger("");
		// add file formatter, should be stored in work directory
		FileHandler fh = new FileHandler(logFile, true); // append
		log.addHandler(fh);
		// find and set Formatter from ConsoleHandler defined in log.conf
		Enumeration<String> e = logM.getLoggerNames();
		Formatter fo = null;
		while (e.hasMoreElements()) {
			Logger lo = logM.getLogger(e.nextElement());
			Handler[] haList = lo.getHandlers();
			for (Handler ha : haList)
				if (ha instanceof ConsoleHandler)
					fo = ha.getFormatter();
		}
		// copy Console formatter to File formatter
		if (fo != null)
			fh.setFormatter(fo);
	}

	static void log(String info) {
		log.info(info);
	}

	static void ex(Exception e) {
		log.log(Level.SEVERE, e.getMessage(), e);
	}

}
