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
package com.jythonui.client.enumtypes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IGetDataListCallBack;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.SynchronizeList;
import com.gwtmodel.table.Utils;
import com.jythonui.client.M;
import com.jythonui.client.dialog.IEnumTypesList;
import com.jythonui.client.dialog.VField;
import com.jythonui.client.listmodel.IRowListDataManager;
import com.jythonui.client.util.JUtils;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.ListOfRows;
import com.jythonui.shared.RowIndex;
import com.jythonui.shared.TypedefDescr;

class EnumTypesList implements IEnumTypesList {

	private final DialogFormat d;
	private final IRowListDataManager r;

	public EnumTypesList(DialogFormat d, IRowListDataManager r) {
		this.d = d;
		this.r = r;
	}

	private Map<IVField, EnumSynch> vMap = new HashMap<IVField, EnumSynch>();

	private TypedefDescr verifyT(String customType) {
		TypedefDescr t = d.findCustomType(customType);
		if (t == null)
			Utils.errAlert(customType, M.M().CustomTypeNotDefine());
		return t;
	}

	@Override
	public void add(IVField v, String customType) {
		verifyT(customType);
		EnumSynch sy = new EnumSynch();
		sy.customT = customType;
		vMap.put(v, sy);
	}

	private IDataListType constructI(String customT, ListOfRows r) {
		TypedefDescr type = verifyT(customT);
		List<FieldItem> fList = type.getListOfColumns();
		RowIndex rI = new RowIndex(fList);
		String comboField = type.getAttr(ICommonConsts.COMBOID);
		IVField v = VField.construct(comboField);
		IVField displayV = null;
		if (type.getDisplayName() != null) {
			displayV = VField.construct(type.getDisplayName());
		}
		IDataListType dList = JUtils.constructList(rI, r, v, displayV);
		return dList;

	}

	private class EnumSynch extends SynchronizeList {

		IGetDataListCallBack iCallBack;
		ListOfRows lRows;
		String customT;

		EnumSynch() {
			super(2);
		}

		@Override
		protected void doTask() {
			iCallBack.set(constructI(customT, lRows));
		}
	}

	@Override
	public void add(IVField v, IGetDataListCallBack iCallBack) {
		EnumSynch sy = vMap.get(v);
		sy.iCallBack = iCallBack;
		sy.signalDone();
	}

	@Override
	public void add(String customType, ListOfRows lRows) {
		for (Entry<IVField, EnumSynch> e : vMap.entrySet()) {
			EnumSynch sy = e.getValue();
			if (sy.customT.equals(customType)) {
				r.sendEnum(customType, constructI(customType, lRows));
				sy.lRows = lRows;
				sy.signalDone();
			}
		}
	}
}