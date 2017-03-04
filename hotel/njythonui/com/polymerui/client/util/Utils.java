/*
 * Copyright 2017 stanislawbartkowski@gmail.com 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not ue this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package com.polymerui.client.util;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.common.CUtil;
import com.polymerui.client.IConsts;

public class Utils {

	private Utils() {

	}

	public static boolean toB(String s) {
		return Boolean.parseBoolean(s);
	}

	public static String getLocale() {
		String loca = LocaleInfo.getCurrentLocale().getLocaleName();
		if ("default".equals(loca))
			return null;
		if ("en".equals(loca))
			return null;
		return loca;
	}

	public static Map<String, String> getURLMap() {
		Map<String, String> ma = new HashMap<String, String>();
		Window.Location.getParameterMap().forEach((key, e) -> {
			String val = "";
			if (!e.isEmpty())
				val = e.get(0);
			ma.put(key, val);

		});
		return ma;
	}

	public static String getHost() {
		return Window.Location.getHostName();
	}

	public static void setWidgetAttribute(Widget w, String attr, String value) {
		w.getElement().setAttribute(attr, value);
	}

	public static String getWidgetAttribute(Widget w, String attr) {
		return w.getElement().getAttribute(attr);
	}
	
	public static String getEmptytyLabel() {
		return ".";
	}


	// -------------------
	public static native Element addStyle(String s) /*-{
		return $wnd.addStyle(s);
	}-*/;

	public static void addStyle(String s, Map<String, String> prop) {
		Element e = addStyle(s);
		for (Map.Entry<String, String> p : prop.entrySet())
			e.setAttribute(p.getKey(), p.getValue());
	}

	// --------------------
	// resources

	private static String addPath(String path, String file) {
		int len = path.length();
		if ((len != 0) && path.charAt(len - 1) == '/') {
			return path + file;
		}
		return path + '/' + file;
	}

	public static String getResAdr(final String res) {
		String path;
		String resF = IConsts.RESOURCEFOLDER;
		path = GWT.getModuleBaseURL();
		if (CUtil.EmptyS(resF)) {
			return addPath(path, res);
		}
		return addPath(addPath(path, resF), res);
	}

	public static String getImageAdr(final String image) {
		String folder = IConsts.IMAGEFOLDER;
		String img;
		if (CUtil.EmptyS(folder))
			img = image;
		else
			img = addPath(folder, image);

		if (!img.contains("."))
			img = img + ".gif";
		String path = getResAdr(img);
		return path;
	}

	// ----------------
	// alerts
	// ----------------

	public static void errAlertB(String s) {
		errAlert(s);
		assert false : s;
	}

	public static void errAlert(String s) {
		Window.alert(s);
	}

	public static void errAlertB(String s1, String s2) {
		errAlert(s1, s2);
		assert false : s1 + " " + s2;
	}

	public static void errAlert(String s1, String s2) {
		errAlert(s1 + " " + s2);
	}

	public static void errAlert(String err, Exception e) {
		errAlert(err, e.getMessage());
	}

	public static void errAlert(String err, String err1, Exception e) {
		errAlert(err + " " + err1, e.getMessage());
	}

}
