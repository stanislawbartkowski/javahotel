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

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;

/**
 * Script launcher
 * 
 * @author sbartkowski
 * 
 */
public class DB2ScriptLauncher implements ILaunchConfigurationDelegate {

	@Override
	public void launch(ILaunchConfiguration arg0, String arg1, ILaunch arg2,
			IProgressMonitor arg3) throws CoreException {
		// what to run (script or content)
		String scriptToRun = arg0.getAttribute(
				DB2ScriptTabView.DB_SCRIPT_TO_RUN, "");
		if (scriptToRun.equals("")) {
			scriptToRun = null;
		}
		String scriptContent = arg0.getAttribute(
				DB2ScriptTabView.DB_CONTENT_TO_RUN, "");
		if (scriptContent.equals("")) {
			scriptContent = null;
		}
		// get parameters
		DB2AccessData a = DB2AccessData.construct(arg0);
		try {
			// execute
			DB2LineFactory.executeDB2Script(null, arg3, a.getDb2Alias(),
					a.getDb2User(), a.getDb2Password(), scriptToRun,
					scriptContent);
		} catch (IOException e) {			
			e.printStackTrace();
			DB2LogUtil.launchererrorLog(e);
		} catch (InterruptedException e) {
			DB2LogUtil.launchererrorLog(e);
			e.printStackTrace();
		}

	}

}
