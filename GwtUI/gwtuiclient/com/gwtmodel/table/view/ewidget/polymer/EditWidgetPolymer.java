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
package com.gwtmodel.table.view.ewidget.polymer;

import java.util.List;

import com.google.inject.Inject;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGetDataList;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.editc.IRequestForGWidget;
import com.gwtmodel.table.editw.IFormLineView;
import com.gwtmodel.table.editw.IGetListOfIcons;
import com.gwtmodel.table.factories.IGetCustomValues;
import com.gwtmodel.table.view.ewidget.IEditWidget;
import com.gwtmodel.table.view.ewidget.gwt.RadioBoxString;

public class EditWidgetPolymer implements IEditWidget {

	@Override
	public IFormLineView constructLabelField(IVField v, String displayName) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructLabel");
		return null;
	}

	@Override
	public IFormLineView constructHTMLField(IVField v) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructHTMLField");
		return null;
	}

	@Override
	public IFormLineView constructAnchorField(IVField v) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructAnchorField");
		return null;
	}

	@Override
	public RadioBoxString constructRadioBoxString(IVField v, IGetDataList iGet, boolean enable, String htmlName) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructRadioBoxString");
		return null;
	}

	@Override
	public IFormLineView contructCalculatorNumber(IVField v, String htmlName) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructCalculatorNumber");
		return null;
	}

	@Override
	public IFormLineView constructCheckField(IVField v, String text, String htmlName) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructCheckField");
		return null;
	}

	@Override
	public IFormLineView constructListValuesCombo(IVField v, IDataType dType, String htmlName) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructListComboValues");
		return null;
	}

	@Override
	public IFormLineView constructListValuesCombo(IVField v, IGetDataList iGet, boolean addEmpty, String htmlName) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructListValuesCombo");
		return null;
	}

	@Override
	public IFormLineView constructHelperList(IVField v, IDataType dType, boolean refreshAlways, String htmlName) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructHelperList");
		return null;
	}

	@Override
	public IFormLineView constructPasswordField(IVField v, String htmlName) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructPasswordField");
		return null;
	}

	@Override
	public IFormLineView constructTextField(IVField v, String htmlName) {
		return new PolymerTextField(v, htmlName);
	}

	@Override
	public IFormLineView constructTextField(IVField v, IGetDataList iGet, IRequestForGWidget iHelper, boolean textarea,
			boolean richtext, boolean refreshAlways, String htmlName) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructTextField");
		return null;
	}

	@Override
	public IFormLineView constructLabelFor(IVField v, String la) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructLabelFor");
		return null;
	}

	@Override
	public IFormLineView construcDateBoxCalendar(IVField v, String htmlName) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructDateBoxCalendar");
		return null;
	}

	@Override
	public IFormLineView constructDateBoxCalendarWithHelper(IVField v, IRequestForGWidget i, boolean refreshAlways,
			String htmlName) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructDateBoxCalendarWithHelper");
		return null;
	}

	@Override
	public IFormLineView constructRadioSelectField(IVField v, String htmlName) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructRadioSelectField");
		return null;
	}

	@Override
	public IFormLineView constructListComboValuesHelp(IVField v, IDataType dType, String htmlName) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructListComboValuesHelp");
		return null;
	}

	@Override
	public IFormLineView constructEditFileName(IVField v, String htmlName) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructEditFileName");
		return null;
	}

	@Override
	public IFormLineView constructListCombo(IVField v, List<String> ma, String htmlName) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructListCombo");
		return null;
	}

	@Override
	public IFormLineView constructListCombo(IVField v, List<String> ma, boolean addEmpty, String htmlName) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructListCombo");
		return null;
	}

	@Override
	public IFormLineView constructListCombo(IVField v, String htmlName) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructListCombo");
		return null;
	}

	@Override
	public IFormLineView constructSpinner(IVField v, String htmlName, int min, int max) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructSpinner");
		return null;
	}

	@Override
	public IFormLineView constructSuggestBox(IVField v, IGetDataList iGet, String htmlName) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructSuggestBox");
		return null;
	}

	@Override
	public IFormLineView constructEmail(IVField v, String htmlName) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructEmail");
		return null;
	}

	@Override
	public IFormLineView constructImageButton(IVField v, String htmlName, int imageNo, IGetListOfIcons iList) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructImageButton");
		return null;
	}

	@Override
	public IFormLineView constructDateTimePicker(IVField v, String htmlName) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructDateTimePicker");
		return null;
	}

	@Override
	public IFormLineView constructListComboEnum(IVField v, String htmlName) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructListComboEnum");
		return null;
	}

}
