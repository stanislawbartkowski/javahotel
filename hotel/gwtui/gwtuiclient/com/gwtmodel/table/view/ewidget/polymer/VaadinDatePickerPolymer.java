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
package com.gwtmodel.table.view.ewidget.polymer;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.editw.IFormFieldProperties;
import com.vaadin.polymer.vaadin.widget.VaadinDatePicker;

class VaadinDatePickerPolymer extends AbstractWField {

	private VaadinDatePicker dp;
	private static final DateTimeFormat te = DateTimeFormat.getFormat("yyyy-MM-dd");

	protected VaadinDatePickerPolymer(IVField v, IFormFieldProperties pr, String standErrMess, VaadinDatePicker w) {
		super(v, pr, standErrMess);
		this.dp = w;
		dp.addValueChangedHandler(handler -> {
			onChangeEdit(true);
		});
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		dp.setReadonly(readOnly);
	}

	@Override
	public void setHidden(boolean hidden) {
		dp.setVisible(!hidden);
	}

	@Override
	public void setInvalidMess(String errmess) {
		if (CUtil.EmptyS(errmess)) {
			dp.setInvalid(false);
			dp.setErrorMessage(getStandErrMess());
		} else {
			dp.setErrorMessage(errmess);
			dp.setInvalid(true);
		}
	}

	@Override
	public boolean isInvalid() {
		return dp.getInvalid();
	}

	@Override
	public Object getValObj() {
		String s = dp.getValue();
		if (CUtil.EmptyS(s))
			return null;
		Date d = te.parse(s);
		return d;
	}

	@Override
	public void setValObj(Object o) {
		if (o == null) {
			dp.setValue("");
			return;
		}
		String s = te.format((Date) o);
		dp.setValue(s);
	}

	@Override
	public Widget getGWidget() {
		return dp;
	}

}
