/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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
package com.jythonui.client.storechanges;

import com.google.inject.Inject;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.factories.IDataStoreChanges;
import com.jythonui.client.IUIConsts;
import com.jythonui.client.dialog.DataType;
import com.jythonui.client.dialog.IDialogContainer;
import com.jythonui.client.interfaces.IGenCookieName;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.ListFormat;

public class StoreChanges implements IDataStoreChanges {

	private final IGenCookieName iGen;

	@Inject
	public StoreChanges(IGenCookieName iGen) {
		this.iGen = iGen;
	}

	private ListFormat getL(IDataType dType) {
		DataType d = (DataType) dType;
		String listId = d.getId();
		IDialogContainer dC = d.getD();
		DialogFormat dForm = dC.getD();
		ListFormat li = dForm.findList(listId);
		return li;
	}

	@Override
	public void savePageSize(IDataType dType, int no) {
		String cookieName = iGen.genCookieName(dType, IUIConsts.COOKIEPAGESIZE);
		ListFormat li = getL(dType);
		if (no == li.getPageSize())
			Utils.RemoveCookie(cookieName);
		else
			Utils.SetCookie(cookieName, Integer.toString(no));
	}

	@Override
	public void saveWrapOnOff(IDataType dType, boolean on) {
		String cookieName = iGen.genCookieName(dType, IUIConsts.COOKIENOWRAPON);
		boolean nowrapon = !on;
		ListFormat li = getL(dType);
		if (nowrapon == li.isNoWrap())
			Utils.RemoveCookie(cookieName);
		else
			Utils.SetCookie(cookieName, Utils.LToS(nowrapon));
	}

}
