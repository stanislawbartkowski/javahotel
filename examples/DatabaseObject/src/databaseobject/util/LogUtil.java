/*
 * Copyright 2012 stanislawbartkowski@gmail.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package databaseobject.util;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Status;

import databaseobject.Activator;

/**
 * Utility class with methods for putting info in Eclipse logger
 * 
 * @author sbartkowski
 * 
 */
public class LogUtil {

	private LogUtil() {
	}

	private static ILog getLog() {
		ILog log = Activator.getDefault().getLog();
		return log;
	}

	private static void log(String msg, int id, Exception e) {
		getLog().log(
				new Status(Status.INFO, Activator.PLUGIN_ID, id, msg, null));
	}

	public static void log(String msg) {
		log(msg, Status.OK, null);
	}

	public static void log(String msg, Exception e) {
		log(msg, Status.ERROR, e);
	}

	public static void logError(String msg) {
		log(msg, Status.ERROR, null);
	}

}
