/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.dateutil.DateUtil;
import com.gwtmodel.table.factories.IGetCustomValues;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.LogT;

public class Utils {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> boolean eqI(IEquatable e1, IEquatable e2) {

		if (e1 == null && e2 == null) {
			return true;
		}
		if (e1 == null) {
			return false;
		}
		if (e2 == null) {
			return false;
		}
		return e1.eq(e2);
	}

	public static <T> boolean eqE(T t1, T t2) {
		if (t1 == null) {
			return (t2 == null);
		}
		if (t2 == null) {
			return false;
		}
		return t1 == t2;
	}

	public static HTML createHTML(final String s) {
		HTML ha = new HTML("<a href='javascript:;'>" + s + "</a>");
		return ha;
	}

	private static String addPath(String path, String file) {
		int len = path.length();
		if ((len != 0) && path.charAt(len - 1) == '/') {
			return path + file;
		}
		return path + '/' + file;
	}

	public static String getResAdr(final String res) {
		String path;
		IGetCustomValues c = GwtGiniInjector.getI().getCustomValues();
		String resF = c.getCustomValue(IGetCustomValues.RESOURCEFOLDER);
		path = GWT.getModuleBaseURL();
		if (CUtil.EmptyS(resF)) {
			return addPath(path, res);
		}
		return addPath(addPath(path, resF), res);
	}

	public static String getImageAdr(final String image) {
		IGetCustomValues c = GwtGiniInjector.getI().getCustomValues();
		String folder = c.getCustomValue(IGetCustomValues.IMAGEFOLDER);
		String img;
		if (CUtil.EmptyS(folder)) {
			img = image;
		} else {
			img = addPath(folder, image);
		}
		if (!img.contains("."))
			img = img + ".gif";
		String path = getResAdr(img);
		return path;
	}

	public static String getEmptytyLabel() {
		return ".";
	}

	public static String getImageHTML(final String imageUrl, int w, int h,
			String name) {
		String s = "<img src='" + getImageAdr(imageUrl) + "'";
		if (w != 0) {
			s += " width='" + w + "px'";
		}
		if (h != 0) {
			s += " height='" + h + "px'";
		}
		if (name != null) {
			s += " name='" + name + "'";
		}
		s += ">";
		return s;
	}

	public static String getImageHTML(final String imageUrl) {
		return getImageHTML(imageUrl, 0, 0, null);
	}

	public static String getImageHTML(final String imageUrl, String name) {
		return getImageHTML(imageUrl, 0, 0, name);
	}

	// int/long utilities
	public static final int BADNUMBER = -1;

	public static int getNum(final String s) {
		if ((s == null) || s.equals("")) {
			return BADNUMBER;
		}
		int i;
		try {
			i = Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return BADNUMBER;
		}
		return i;
	}

	private static Long xtoLong(String s) {
		if (CUtil.EmptyS(s)) {
			return null;
		}
		try {
			return new Long(s);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	private static Integer xtoInteger(String s) {
		if (CUtil.EmptyS(s)) {
			return null;
		}
		try {
			return new Integer(s);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	private static String getCValue(String key) {
		IGetCustomValues c = GwtGiniInjector.getI().getCustomValues();
		assert c != null : LogT.getT().cannotBeNull();
		String f = c.getCustomValue(key);
		return f;

	}

	private static DateTimeFormat getDateFormat(String key) {
		String f = getCValue(key);
		assert f != null : LogT.getT().cannotBeNull();
		DateTimeFormat te = DateTimeFormat.getFormat(f);
		return te;
	}

	public static Date toD(String s) {
		DateTimeFormat te = getDateFormat(IGetCustomValues.DATEFORMAT);
		try {
			return te.parseStrict(s);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	public static String toS(Date d) {
		if (d == null) {
			return null;
		}
		DateTimeFormat te = getDateFormat(IGetCustomValues.DATEFORMAT);
		return te.format(d);
	}

	public static Timestamp toDT(String s) {
		DateTimeFormat te = getDateFormat(IGetCustomValues.DATETIMEFORMAT);
		Date d;
		try {
			d = te.parseStrict(s);
		} catch (IllegalArgumentException e) {
			return null;
		}
		Timestamp t = new Timestamp(d.getTime());
		return t;
	}

	public static String toS(Timestamp t) {
		if (t == null) {
			return null;
		}
		DateTimeFormat te = getDateFormat(IGetCustomValues.DATETIMEFORMAT);
		Date d = new Date(t.getTime());
		return te.format(d);
	}

	public static Date DToD(Object val) {
		if (val instanceof String) {
			return toD((String) val);
		}
		return (Date) val;
	}

	// some 'log' utilities
	public static boolean TrueL(String s) {
		IGetCustomValues c = GwtGiniInjector.getI().getCustomValues();
		String yesv = c.getCustomValue(IGetCustomValues.YESVALUE);
		return CUtil.EqNS(s, yesv);
	}

	public static String LToS(boolean l) {
		IGetCustomValues c = GwtGiniInjector.getI().getCustomValues();
		if (l)
			return c.getCustomValue(IGetCustomValues.YESVALUE);

		return c.getCustomValue(IGetCustomValues.NOVALUE);
	}

	// import : test if int is not undefined
	// in Java one cannot have int undefined
	// but in JS it is possible (while returning from JS native)
	// the quick way is to try change it to Integer
	/**
	 * Check if int is undefined
	 * 
	 * @param i
	 *            Integer
	 * @return true: if undefined
	 */
	public static boolean isUndefined(int i) {
		Integer ii = new Integer(i);
		if (ii.toString().equals("undefined")) {
			return true;
		}
		return false;
	}

	// -------------
	public static int CalculateNOfRows(WSize w) {
		int up = 0;
		if (w != null) {
			up = w.getTop();
		}
		int he = Window.getClientHeight();

		int size = (he - up - 10) / 30;
		return size;
	}

	public static String getURLParam(String key) {
		return Window.Location.getParameter(key);
	}

	public static void setCaption(Widget w, String title, String attr) {
		Element e = DOM.createCaption();
		e.setInnerText(title);
		if (attr != null) {
			e.setAttribute("class", attr);
		}
		NodeList<Node> li = w.getElement().getChildNodes();
		li.getItem(0);
		w.getElement().insertBefore(e, li.getItem(0));
	}

	public static void setName(UIObject o, String name) {
		o.getElement().setAttribute("name", name);

	}

	public static void internalErrorAlert(String s) {
		errAlert(LogT.getT().InternalError(), s);
	}

	public static void errAlert(String s) {
		Window.alert(s);
	}

	public static void errAlertB(String s) {
		Window.alert(s);
		assert false : s;
	}

	public static void errAlert(String s1, String s2) {
		errAlert(s1 + " " + s2);
	}

	public static void errAlert(String err, Exception e) {
		errAlert(err, e.getMessage());
	}

	public static native void callJs(String js) /*-{
		$wnd.eval(js);
	}-*/;

	public static List<IVField> toList(IVField[] a) {
		// return Arrays.asList(a);
		// WARNING: do not replace with Arrays.asList !
		List<IVField> l = new ArrayList<IVField>();
		for (int i = 0; i < a.length; i++) {
			l.add(a[i]);
		}
		return l;
	}

	private static String paraS(String key, String value) {
		String u = "&" + key + "=" + value;
		return u;
	}

	public static String createURL(String u, String firstPar, String firstVal,
			Map<String, String> args) {
		String url = u + "?" + firstPar + "=" + firstVal;
		for (Entry<String, String> e : args.entrySet()) {
			String param = e.getKey();
			String val = e.getValue();
			url += paraS(param, val);
		}
		return url;
	}

	private static void XsetId(Widget w, String id) {
		w.getElement().setId(id);
	}

	public static native void addScript(String s) /*-{
		$wnd.addScript(s);
	}-*/;

	public static native void addStyle(String s) /*-{
		$wnd.addStyle(s);
	}-*/;

	/*
	 * Takes in a trusted JSON String and evals it.
	 * 
	 * @param JSON String that you trust
	 * 
	 * @return JavaScriptObject that you can cast to an Overlay Type
	 */
	public static native JavaScriptObject evalJson(String jsonStr) /*-{
		return eval(jsonStr);
	}-*/;

	public static JavaScriptObject parseJson(String jsonStr) {
		return evalJson("(" + jsonStr + ")");
	}

	public static native String callJsStringFun(String jsonFun, String paramS) /*-{
		return $wnd.eval(jsonFun + '(\'' + paramS + '\')');
	}-*/;

	public static String getLocale() {
		String loca = LocaleInfo.getCurrentLocale().getLocaleName();
		if ("default".equals(loca))
			return null;
		if ("en".equals(loca))
			return null;
		return loca;
	}

	private static String jPrefix() {
		IGetCustomValues c = GwtGiniInjector.getI().getCustomValues();
		return c.getCustomValue(IGetCustomValues.JCOOKIEPREFIX);
	}

	public static List<IMapEntry> getCookies() {
		List<IMapEntry> cList = new ArrayList<IMapEntry>();
		Collection<String> cookies = Cookies.getCookieNames();
		final String p = jPrefix();
		for (final String s : cookies) {
			if (!s.startsWith(p))
				continue;
			final String value = Cookies.getCookie(s);
			IMapEntry i = new IMapEntry() {

				@Override
				public String getKey() {
					return s.substring(p.length());
				}

				@Override
				public String getValue() {
					return value;
				}

			};
			cList.add(i);
		}
		return cList;
	}

	public static void SetCookie(String key, String value) {
		Date today = DateUtil.getToday();
		Date exp = DateUtil.addDaysD(today, 100);
		Cookies.setCookie(jPrefix() + key, value, exp);
	}

	public static void RemoveCookie(String key) {
		Cookies.removeCookie(jPrefix() + key);
	}
}
