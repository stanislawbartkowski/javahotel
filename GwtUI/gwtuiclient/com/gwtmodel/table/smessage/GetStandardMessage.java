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
package com.gwtmodel.table.smessage;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.IMessConsts;
import com.gwtmodel.table.factories.IGetCustomValues;
import com.gwtmodel.table.injector.MM;
import com.gwtmodel.table.slotmodel.ClickButtonType.StandClickEnum;

public class GetStandardMessage implements IGetStandardMessage {

	private final Map<String, String> cValues = new HashMap<String, String>();
	private final IGetCustomValues custValues;

	@Inject
	public GetStandardMessage(IGetCustomValues custValues) {
		cValues.put(IStandMessages.CLOSE, MM.getL().Close());
		cValues.put(IStandMessages.LOGIN, MM.getL().Login());
		cValues.put(IStandMessages.ACCEPT, MM.getL().Accept());
		cValues.put(IStandMessages.PASSWORD, MM.getL().Password());
		cValues.put(IStandMessages.RESIGN, MM.getL().Resign());
		cValues.put(IStandMessages.YES, MM.getL().Yes());
		cValues.put(IStandMessages.NO, MM.getL().No());
		cValues.put(IStandMessages.PRINT, MM.getL().Print());
		cValues.put(IStandMessages.OK, MM.getL().Ok());
		Map<String, String> sBut = MM.getL().ActionName();
		cValues.put(IStandMessages.ADD, sBut.get(StandClickEnum.ADDITEM.name()));
		cValues.put(IStandMessages.REMOVE, sBut.get(StandClickEnum.REMOVEITEM.name()));
		cValues.put(IStandMessages.MODIFY, sBut.get(StandClickEnum.MODIFITEM.name()));
		cValues.put(IStandMessages.SHOW, sBut.get(StandClickEnum.SHOWITEM.name()));
		cValues.put(IStandMessages.FIND, MM.getL().SearchButton());
		cValues.put(IStandMessages.USER, MM.getL().User());
		cValues.put(IStandMessages.SELECT, MM.getL().Select());
		cValues.put(IStandMessages.CHOOSE, MM.getL().Choose());
		cValues.put(IStandMessages.CANNOTBEEMPTY, MM.getL().EmptyFieldMessage());
		this.custValues = custValues;
	}

	@Override
	public String getMessage(String sou) {
		if (CUtil.EmptyS(sou) || sou.length() <= 1 || sou.charAt(0) != IMessConsts.STANDCH)
			return sou;
		char action = sou.charAt(1);
		String key;
		if (Character.isDigit(action))
			key = sou.substring(2);
		else
			key = sou.substring(1);
		String val = custValues.getStandMessage(key);
		if (val == null)
			val = cValues.get(key);
		if (val == null)
			try {
			val = MM.getL().getString(key);
			} catch (java.util.MissingResourceException e) {
				// do nothing on purpose
			}
		if (val == null) {
			return sou;
		}
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
