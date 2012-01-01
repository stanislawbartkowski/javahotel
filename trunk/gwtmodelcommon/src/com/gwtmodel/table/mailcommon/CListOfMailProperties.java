/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.mailcommon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gwtmodel.table.common.CUtil;

/**
 * 
 * @author perseus
 */
public class CListOfMailProperties implements Serializable {

	private List<Map<String, String>> mList;
	private String errMess;
	private final static String F_NAME = "mail.com.filename";
	private final static String B_NAME = "mail.com.displayname";
	private final static String M_FROM = "mail.from";

	public void setmList(List<Map<String, String>> mList) {
		this.mList = mList;
	}

	public void setErrMess(String errMess) {
		this.errMess = errMess;
	}

	public Map<String, String> getM(String n) {
		for (Map<String, String> m : mList) {
			String na = getName(m);
			if (CUtil.EqNS(na, n)) {
				return m;
			}
		}
		return null;
	}

	public String getName(Map<String, String> ma) {
		String s = ma.get(B_NAME);
		if (s != null) {
			return s;
		}
		return ma.get(F_NAME);
	}

	public String getFrom(Map<String, String> ma) {
		return ma.get(M_FROM);
	}

	/**
	 * @return the mList
	 */
	public List<Map<String, String>> getmList() {
		if (mList == null) {
			mList = new ArrayList<Map<String, String>>();
		}
		return mList;
	}

	/**
	 * @return the errMess
	 */
	public String getErrMess() {
		return errMess;
	}
}
