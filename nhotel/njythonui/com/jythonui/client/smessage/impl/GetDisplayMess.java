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
package com.jythonui.client.smessage.impl;

import com.google.inject.Inject;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.IMessConsts;
import com.jythonui.client.smessage.IGetDisplayMess;
import com.jythonui.client.smessage.IGetStandardMessage;

public class GetDisplayMess implements IGetDisplayMess {

	private final IGetStandardMessage iMess;

	@Inject
	public GetDisplayMess(IGetStandardMessage iMess) {
		this.iMess = iMess;
	}

	@Override
	public String getString(String sou) {
		if (CUtil.EmptyS(sou) || sou.length() <= 1 || sou.charAt(0) != IMessConsts.STANDCH)
			return sou;
		char action = sou.charAt(1);
		String key;
		if (Character.isDigit(action))
			key = sou.substring(2);
		else
			key = sou.substring(1);
		String val = iMess.getMessage(key);
		if (val == null)
			return sou;
		switch (action) {
		case IMessConsts.FIRSTUP:
			return Character.toUpperCase(key.charAt(2)) + sou.substring(3);
		case IMessConsts.DOWNCASE:
			return val.toLowerCase();
		case IMessConsts.UPCASE:
			return val.toUpperCase();
		}
		return val;
	}
}
