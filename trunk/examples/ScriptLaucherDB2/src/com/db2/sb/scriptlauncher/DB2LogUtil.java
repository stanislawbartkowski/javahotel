/*
 * Copyright 2011 stanislawbartkowski@gmail.com
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

package com.db2.sb.scriptlauncher;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Status;

import scriptlaucherdb2.Activator;

public class DB2LogUtil {

	private static ILog getLog() {
		ILog log = Activator.getDefault().getLog();
		return log;
	}

	private static void log(String msg, int id, Exception e) {
		getLog().log(
				new Status(Status.INFO, Activator.PLUGIN_ID, id, msg, null));
	}

	static void log(String msg) {
		log(msg, Status.OK, null);
	}

	static void log(String msg, Exception e) {
		log(msg, Status.ERROR, e);
	}

	static void logError(String msg) {
		log(msg, Status.ERROR, null);
	}

	static void launchererrorLog(Exception e) {
		log(Messages.DB2LauncherError, e);
	}

}
