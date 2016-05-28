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
package com.gwtmodel.table.view.ewidget.gwt;

import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.editw.IFormFieldProperties;

/**
 * 
 * @author perseus
 */
class ComboListBoxField extends GetValueLB {

	ComboListBoxField(IVField v, IFormFieldProperties pr) {
		super(v, pr);
		setList(listT.getListVal());
	}

	@Override
	public void setValObj(Object o) {
		String s = FUtils.getValueOS(o, v);
		String val = listT.getValueS(s);
		super.setValObj(val);

	}

	@Override
	public Object getValObj() {
		String s = getValS();
		String val = listT.getValue(s);
		return FUtils.getValue(v, val);
	}
}
