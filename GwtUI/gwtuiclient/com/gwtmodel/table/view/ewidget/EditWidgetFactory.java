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
package com.gwtmodel.table.view.ewidget;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.gwtmodel.table.FieldDataType;
import com.gwtmodel.table.IConsts;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.editw.FormFieldPropFactory;
import com.gwtmodel.table.editw.IFormFieldProperties;
import com.gwtmodel.table.editw.IFormLineView;

public class EditWidgetFactory {

	private EditWidgetFactory() {
	}

	@Inject
	private static @Named(IConsts.GWT) IEditWidget gwtE;
	@Inject
	private static @Named(IConsts.POLYMER) IEditWidget polymerE;

	public static IEditWidget getGwtE() {
		return gwtE;
	}

	public static IEditWidget getE(boolean polymer) {
		return polymer ? polymerE : gwtE;
	}

	public static IFormLineView constructEditWidget(IVField v, IFormFieldProperties fieldProp) {
		FieldDataType.IFormLineViewFactory fa = v.getType().getiFactory();
		if (fa != null) {
			return fa.construct(v);
		}
		if (fieldProp == null)
			fieldProp = FormFieldPropFactory.construct();
		IEditWidget i = getE(fieldProp.isPolymer());
		switch (v.getType().getType()) {
		case DATETIME:
			return i.constructDateTimePicker(v, fieldProp);
		case DATE:
			return i.construcDateBoxCalendar(v, fieldProp);
		case INT:
		case LONG:
		case BIGDECIMAL:
			if (v.getType().getLi() != null) {
				return i.constructListCombo(v, fieldProp);
			}
			return i.contructCalculatorNumber(v, fieldProp);
		case ENUM:
			return i.constructListComboEnum(v, fieldProp);
		case BOOLEAN:
			return i.constructCheckField(v, fieldProp, null);
		default:
			if (v.getType().getLi() != null) {
				return i.constructListCombo(v, fieldProp);
			}
			return i.constructTextField(v, fieldProp);
		}

	}

}
