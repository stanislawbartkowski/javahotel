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

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.db2.sb.scriptlauncher.messages"; //$NON-NLS-1$
	public static String CONNECTING_AND_RUNNING;
	public static String OK_CONNECTED;
	public static String FAILURE_CANNOT_CONNECT;
	public static String CONNECT_AND_EXECUTE;
	public static String CONNECTING;
	public static String DB2ScriptTabView_DB2_ACCESS_DATA;
	public static String DB2ScriptTabView_DB2_ALIAS_NAME;
	public static String DB2ScriptTabView_DB2_BUTTON_TEST;
	public static String DB2ScriptTabView_DB2_LAUNCHER_NAME;
	public static String DB2ScriptTabView_DB2_PASSWORD;
	public static String DB2ScriptTabView_DB2_USER_NAME;
	public static String DB2ScriptTabView_Choose;
	public static String DB2ScriptTabView_List;
	public static String DB2LauncherError;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
