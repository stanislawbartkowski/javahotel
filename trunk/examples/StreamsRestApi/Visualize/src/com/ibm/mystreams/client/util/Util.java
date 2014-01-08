/*
 * Copyright 2014 stanislawbartkowski@gmail.com
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
package com.ibm.mystreams.client.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.ibm.mystreams.client.M;
import com.ibm.mystreams.shared.ConnectionData;

public class Util {

	private Util() {

	}

	private static class TestResult extends CommonCallBack {

		@Override
		public void doErrorResult(String errmess) {
			Window.alert(M.L().failedTestInfo() + errmess);
		}

		@Override
		public void doResult(String jSon) {
			Window.alert(M.L().connectedTestInfo());
		}
	}

	public static void testConnection(ConnectionData data) {
		M.getS().callRest(data, "resources", new TestResult());
	}

	public static JSONArray getArray(String jSon, String id) {
		JSONValue jsonValue = JSONParser.parseStrict(jSon);
		JSONObject jsonObject = jsonValue.isObject();
		JSONArray jsonArray = jsonObject.get(id).isArray();
		return jsonArray;
	}

	public static String getJobId(String jSon, String jobName) {
		JSONArray jsonArray = getArray(jSon, "jobs");
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject instance = jsonArray.get(i).isObject();
			JSONString instanceName = instance.get("dataPath").isString();
			if (instanceName.stringValue().contains(jobName)) {
				return instance.get("restid").isString().stringValue();
			}
		}
		return null;
	}

	public static List<String> getListOfInstances(String jSon) {
		List<String> iList = new ArrayList<String>();
		JSONArray jsonArray = getArray(jSon, "instances");
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject instance = jsonArray.get(i).isObject();
			JSONString instanceName = instance.get("restid").isString();
			String restId = instanceName.stringValue();
			iList.add(restId);
		}
		return iList;
	}

	private final static String COOKIEPREFIX = "IBM InfoSphereStreams ";

	public static String getCookie(String cookieName) {
		return Cookies.getCookie(COOKIEPREFIX + " " + cookieName);
	}

	public static void setCookie(String cookieName, String value) {
		Date d = new Date();
		CalendarUtil.addDaysToDate(d, 300);
		Cookies.setCookie(COOKIEPREFIX + " " + cookieName, value, d);
	}

	public static void removeCookie(String cookieName) {
		Cookies.removeCookie(COOKIEPREFIX + " " + cookieName);
	}

	public static List<String> getInstances(String s) {
		List<String> instance = new ArrayList<String>();
		String iS = getCookie("InstanceList " + s);
		if (iS != null) {
			String[] l = iS.split(",");
			for (String i : l)
				instance.add(i);
		}
		return instance;
	}

	public static void setInstances(String s, List<String> iList) {
		Joiner joiner = Joiner.on(",");
		String lI = joiner.join(iList);
		setCookie("InstanceList " + s, lI);
	}

	public static void setCurrentInstance(String s, String instance) {
		setCookie("CurrentInstance " + s, instance);
	}

	public static String getCurrentInstance(String s) {
		return getCookie("CurrentInstace " + s);
	}

}
