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
package com.jythonui.client;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.jythonui.client.dialog.IDialogContainer;
import com.jythonui.client.dialog.LeftMenu;
import com.jythonui.client.service.JythonService;
import com.jythonui.client.service.JythonServiceAsync;
import com.jythonui.client.variables.IVariablesContainer;

/**
 * @author hotel
 * 
 */
public class M {

	private static final JMessages jMess = (JMessages) GWT.create(JMessages.class);
	private static final Logger logS = Logger.getLogger("com.jythonui.client");
	private static final JythonServiceAsync jythonService = GWT.create(JythonService.class);
	private static LeftMenu leftMenu;
	private static IDialogContainer mainD = null;

	private static String userName;

	private static String secToken;

	private static boolean cached = false;
	private static boolean nocharts = false;
	private static boolean polymer = false;

	public static JMessages M() {
		return jMess;
	}

	public static Logger L() {
		return logS;
	}

	private static IVariablesContainer i = null;

	public static IVariablesContainer getVar() {
		return i;
	}

	public static void setVar(IVariablesContainer ii) {
		i = ii;
	}

	public static JythonServiceAsync JR() {
		return jythonService;
	}

	public static void setLeftMenu(LeftMenu l) {
		leftMenu = l;
	}

	public static LeftMenu getLeftMenu() {
		return leftMenu;
	}

	public static String getUserName() {
		return userName;
	}

	public static void setUserName(String userName) {
		M.userName = userName;
	}

	public static String getSecToken() {
		return secToken;
	}

	public static void setSecToken(String secToken) {
		M.secToken = secToken;
	}

	public static IDialogContainer getMainD() {
		return mainD;
	}

	public static void setMainD(IDialogContainer mainD) {
		M.mainD = mainD;
	}

	public static boolean isCached() {
		return cached;
	}

	public static void setCached(boolean cached) {
		M.cached = cached;
	}

	public static boolean isNocharts() {
		return nocharts;
	}

	public static void setNocharts(boolean nocharts) {
		M.nocharts = nocharts;
	}

	public static boolean isPolymer() {
		return polymer;
	}

	public static void setPolymer(boolean polymer) {
		M.polymer = polymer;
	}

}
