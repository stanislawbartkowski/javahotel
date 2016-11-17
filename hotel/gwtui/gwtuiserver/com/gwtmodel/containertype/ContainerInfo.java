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
package com.gwtmodel.containertype;

import java.io.File;

import com.jythonui.server.UtilHelper;
import com.jythonui.server.logmess.ILogMess;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class ContainerInfo extends UtilHelper {

	private ContainerInfo() {
	}

	private static boolean isJarOnPath(String classPath, final String jarName) {
		String[] pat = classPath.split(File.pathSeparator);
		for (int i = 0; i < pat.length; i++) {
			String fName = pat[i];
			File f = new File(fName);
			String baseName = f.getName();
			if (baseName.equalsIgnoreCase(jarName))
				return true;
			if (baseName.startsWith(jarName))
				return true;
		}
		return false;
	}

	/**
	 * Returns information if transactions are controlled by container
	 * 
	 * @return true: by container false: no
	 */
	public static boolean TransactionContainer() {
		return getContainerType() != ContainerType.APPENGINE;
	}

	public static boolean isAppEngineLive() {
		String classPath = System.getProperty("java.class.path");
		// null if live google app engine
		if (classPath == null) {
			return true;
		}
		return false;
	}

	// jetty-server-9.1.4.v20140401.jar

	private static ContainerType getContainerTypeP() {
		String classPath = System.getProperty("java.class.path");
		// log.info(classPath);
		// null if live google app engine
		if (classPath == null)
			return ContainerType.APPENGINE;

		// add heroku embedded tomcat
		boolean tomcat = isJarOnPath(classPath, "tomcat-juli.jar");
		if (tomcat)
			return ContainerType.TOMCAT;
		// return ContainerType.HEROKU;

		boolean jboss = isJarOnPath(classPath, "jboss-modules.jar");
		if (jboss)
			return ContainerType.JBOSS;

		if (isJarOnPath(classPath, "webapp-runner.jar"))
			return ContainerType.HEROKU;

		boolean appengine = isJarOnPath(classPath, "appengine-local-runtime-shared.jar");
		if (appengine)
			return ContainerType.APPENGINE;

		if (isJarOnPath(classPath, "jetty-server-"))
			return ContainerType.JETTY;
		return ContainerType.GLASSFISH;
	}

	public static ContainerType getContainerType() {
		ContainerType t = getContainerTypeP();
		// to log message about container
		// for some reason it causes guice binding crash
		// infoMess(L(), ILogMess.CONTAINERRECOGNIZED, t.name());
		traceLog("Container: " + t.name());
		return t;
	}
}
