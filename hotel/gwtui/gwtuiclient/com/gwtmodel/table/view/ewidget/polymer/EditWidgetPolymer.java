/*
 * Copyright 2017 stanislawbartkowski@gmail.com  
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

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGetDataList;
import com.gwtmodel.table.IResponseJson;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.editc.IRequestForGWidget;
import com.gwtmodel.table.editw.IFormFieldProperties;
import com.gwtmodel.table.editw.IFormLineView;
import com.gwtmodel.table.editw.IGetListOfIcons;
import com.gwtmodel.table.mm.MM;
import com.gwtmodel.table.view.ewidget.IEditWidget;
import com.gwtmodel.table.view.ewidget.gwt.RadioBoxString;

public class EditWidgetPolymer implements IEditWidget {

	private String regNumberExpr(IVField v) {
		switch (v.getType().getAfterdot()) {
		case 0:
			return "[-+]?[0-9]*";
		case 1:
			return "[-+]?[0-9]*(\\.[0-9])?";
		case 2:
			return "[-+]?[0-9]+(\\.[0-9][0-9]?)?";
		case 3:
			return "[-+]?[0-9]+(\\.[0-9][0-9]?[0-9]?)?";
		case 4:
			return "[-+]?[0-9]+(\\.[0-9][0-9]?[0-9]?[0-9]?)?";
		}
		return null;
	}

	private final static String dateRegexp = "(19|20)\\d{2}\\/(0[1-9]|10|11|12)\\/(0[1-9]|1\\d|2\\d|30|31)";
	private final static String datetimeRegexp = "(19|20)\\d{2}\\/(0[1-9]|10|11|12)\\/(0[1-9]|1\\d|2\\d|30|31)\\ ([0|1]\\d|20|21|22|23):([0|1|2|3|4]\\d|5[0-9]):([0|1|2|3|4]\\d|5[0-9])";

	private String numberErrMess(IVField v) {
		switch (v.getType().getAfterdot()) {
		case 0:
			return MM.getL().DigitsOnly();
		case 1:
			return CUtil.concatS(MM.getL().DigitsOnly(), MM.getL().AfterDot1(), ',');
		case 2:
			return CUtil.concatS(MM.getL().DigitsOnly(), MM.getL().AfterDot2(), ',');
		case 3:
			return CUtil.concatS(MM.getL().DigitsOnly(), MM.getL().AfterDot3(), ',');
		case 4:
			return CUtil.concatS(MM.getL().DigitsOnly(), MM.getL().AfterDot4(), ',');
		}
		return null;
	}

	@Override
	public IFormLineView constructLabelField(IVField v, IFormFieldProperties pr, String displayName) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructLabel");
		return null;
	}

	@Override
	public IFormLineView constructHTMLField(IVField v, IFormFieldProperties pr) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructHTMLField");
		return null;
	}

	@Override
	public IFormLineView constructAnchorField(IVField v, IFormFieldProperties pr) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructAnchorField");
		return null;
	}

	@Override
	public RadioBoxString constructRadioBoxString(IVField v, IFormFieldProperties pr, IGetDataList iGet,
			boolean enable) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructRadioBoxString");
		return null;
	}

	@Override
	public IFormLineView contructCalculatorNumber(IVField v, IFormFieldProperties pr) {
		return new PolymerNumber(v, pr, regNumberExpr(v), numberErrMess(v));
	}

	@Override
	public IFormLineView constructCheckField(IVField v, IFormFieldProperties pr) {
		return new CheckedPolymer(v, pr);
	}

	@Override
	public IFormLineView constructListValuesCombo(IVField v, IFormFieldProperties pr, IDataType dType) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructListComboValues");
		return null;
	}

	@Override
	public IFormLineView constructListValuesCombo(IVField v, IFormFieldProperties pr, IGetDataList iGet,
			boolean addEmpty) {
		return new PolymerCombo(v, pr, iGet, addEmpty);
	}

	@Override
	public IFormLineView constructHelperList(IVField v, IFormFieldProperties pr, IDataType dType,
			boolean refreshAlways) {
		return new TextHelperPolymer(v, pr, dType, refreshAlways);
	}

	@Override
	public IFormLineView constructPasswordField(IVField v, IFormFieldProperties pr) {
		return new PolymerTextField(v, pr, null, null, false, true, false);
	}

	@Override
	public IFormLineView constructTextField(IVField v, IFormFieldProperties pr) {
		return new PolymerTextField(v, pr, null, null, false, false, false);
	}

	@Override
	public IFormLineView constructTextField(IVField v, IFormFieldProperties pr, IGetDataList iGet,
			IRequestForGWidget iHelper, boolean textarea, boolean richtext, boolean refreshAlways) {
		return new PolymerTextField(v, pr, null, null, false, false, textarea);
	}

	@Override
	public IFormLineView constructLabelFor(IVField v, IFormFieldProperties pr, String la) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructLabelFor");
		return null;
	}

	@Override
	public IFormLineView construcDateBoxCalendar(IVField v, IFormFieldProperties pr) {
		return new DateCalendarPolymer(v, pr, dateRegexp, MM.getL().DateOnly());
	}

	@Override
	public IFormLineView constructDateBoxCalendarWithHelper(IVField v, IFormFieldProperties pr, IRequestForGWidget i,
			boolean refreshAlways) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructDateBoxCalendarWithHelper");
		return null;
	}

	@Override
	public IFormLineView constructRadioSelectField(IVField v, IFormFieldProperties pr) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructRadioSelectField");
		return null;
	}

	@Override
	public IFormLineView constructListComboValuesHelp(IVField v, IFormFieldProperties pr, IDataType dType) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructListComboValuesHelp");
		return null;
	}

	@Override
	public IFormLineView constructEditFileName(IVField v, IFormFieldProperties pr) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructEditFileName");
		return null;
	}

	@Override
	public IFormLineView constructListCombo(IVField v, IFormFieldProperties pr, List<String> ma) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructListCombo");
		return null;
	}

	@Override
	public IFormLineView constructListCombo(IVField v, IFormFieldProperties pr, List<String> ma, boolean addEmpty) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructListCombo");
		return null;
	}

	@Override
	public IFormLineView constructListCombo(IVField v, IFormFieldProperties pr) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructListCombo");
		return null;
	}

	@Override
	public IFormLineView constructSpinner(IVField v, IFormFieldProperties pr, int min, int max) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructSpinner");
		return null;
	}

	@Override
	public IFormLineView constructSuggestBox(IVField v, IFormFieldProperties pr, IGetDataList iGet) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructSuggestBox");
		return null;
	}

	@Override
	public IFormLineView constructEmail(IVField v, IFormFieldProperties pr) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructEmail");
		return null;
	}

	@Override
	public IFormLineView constructImageButton(IVField v, IFormFieldProperties pr, int imageNo, IGetListOfIcons iList) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructImageButton");
		return null;
	}

	@Override
	public IFormLineView constructDateTimePicker(IVField v, IFormFieldProperties pr) {
		return new DateTimePolymer(v, pr, datetimeRegexp, MM.getL().DateTimeOnly());
	}

	@Override
	public IFormLineView constructListComboEnum(IVField v, IFormFieldProperties pr) {
		Utils.PolymerNotImplemented("EWidgetPolymer:constructListComboEnum");
		return null;
	}

	@Override
	public IFormLineView constructBinderField(IVField v, IFormFieldProperties pr) {
		return new BinderWidget(v, pr);
	}

	@Override
	public IFormLineView constructAjaxField(IVField v, IFormFieldProperties pr, IResponseJson iR) {
		return new AjaxWidget(v, pr, iR);
	}

}
