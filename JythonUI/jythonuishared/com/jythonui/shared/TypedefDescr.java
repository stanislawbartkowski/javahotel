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
package com.jythonui.shared;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.common.CUtil;

public class TypedefDescr extends ElemDescription {

	private static final long serialVersionUID = 1L;
	private List<FieldItem> lColumns = new ArrayList<FieldItem>();

	public boolean isComboType() {
		return getAttr(ICommonConsts.TYPE).equals(ICommonConsts.COMBOTYPE);
	}

	public boolean isHelperType() {
		return getAttr(ICommonConsts.TYPE).equals(ICommonConsts.HELPER);
	}

	public boolean isSuggestType() {
		return getAttr(ICommonConsts.TYPE).equals(ICommonConsts.SUGGEST);
	}

	public String getComboId() {
		return getAttr(ICommonConsts.COMBOID);
	}

	public List<FieldItem> getListOfColumns() {
		if (!lColumns.isEmpty()) {
			return lColumns;
		}
		String id = getComboId();
		String dName = getDisplayName();
		List<FieldItem> fList = new ArrayList<FieldItem>();
		FieldItem f = new FieldItem();
		f.setId(id);
		fList.add(f);
		if (!CUtil.EmptyS(dName)) {
			f = new FieldItem();
			f.setId(dName);
			fList.add(f);
		}
		return fList;
	}

	public List<FieldItem> getColumns() {
		return lColumns;
	}

}
