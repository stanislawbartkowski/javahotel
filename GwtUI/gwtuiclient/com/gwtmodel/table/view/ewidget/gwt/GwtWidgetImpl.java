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

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.gwtmodel.table.FieldDataType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGetDataList;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.editc.IRequestForGWidget;
import com.gwtmodel.table.editw.IFormFieldProperties;
import com.gwtmodel.table.editw.IFormLineView;
import com.gwtmodel.table.editw.IGetListOfIcons;
import com.gwtmodel.table.factories.IGetCustomValues;
import com.gwtmodel.table.view.ewidget.IEditWidget;
import com.gwtmodel.table.view.ewidget.comboutil.AddBoxValues;

public class GwtWidgetImpl implements IEditWidget {

	@Override
	public IFormLineView constructLabelField(IVField v, IFormFieldProperties pr, String displayName) {
		return new VLabel(v, displayName);
	}

	@Override
	public IFormLineView constructHTMLField(IVField v, IFormFieldProperties pr) {
		return new VHtml(v);
	}

	@Override
	public IFormLineView constructAnchorField(IVField v, IFormFieldProperties pr) {
		return new AnchorField(v);
	}

	@Override
	public RadioBoxString constructRadioBoxString(IVField v, IFormFieldProperties pr, IGetDataList iGet,
			final boolean enable) {
		return new RadioBoxString(v, pr, iGet, enable);
	}

	@Override
	public IFormLineView contructCalculatorNumber(IVField v, IFormFieldProperties pr) {
		return new NumberCalculator(v, pr,
				new ExtendTextBox.EParam(false, false, false, false, false, false, null, null));
	}

	@Override
	public IFormLineView constructCheckField(IVField v, IFormFieldProperties pr) {
		return new FieldCheckField(v, pr);
	}

	@Override
	public IFormLineView constructListValuesCombo(IVField v, IFormFieldProperties pr, IDataType dType) {
		GetValueLB lB = new GetValueLB(v, pr);
		AddBoxValues.addValues(dType, lB);
		return lB;
	}

	@Override
	public IFormLineView constructListValuesCombo(IVField v, IFormFieldProperties pr, IGetDataList iGet,
			boolean addEmpty) {
		GetValueLB lB = new GetValueLB(v, pr, addEmpty);
		AddBoxValues.addValues(v, iGet, lB);
		return lB;
	}

	@SuppressWarnings("unused")
	private void setComboList(IFormLineView i, IGetDataList iGet) {
		GetValueLB lB = (GetValueLB) i;
		AddBoxValues.addValues(i.getV(), iGet, lB);
	}

	private ExtendTextBox.EParam newE(boolean password, boolean area) {
		return new ExtendTextBox.EParam(password, false, false, area, false, false, null, null);
	}

	@Override
	public IFormLineView constructHelperList(IVField v, IFormFieldProperties pr, IDataType dType,
			boolean refreshAlways) {
		ExtendTextBox.EParam e = new ExtendTextBox.EParam(false, true, false, false, false, false, null, null);
		return new ListFieldWithHelp(v, pr, dType, e, refreshAlways);
	}

	@Override
	public IFormLineView constructPasswordField(IVField v, IFormFieldProperties pr) {
		return new ExtendTextBox(v, pr, newE(true, false));
	}

	@Override
	public IFormLineView constructTextField(IVField v, IFormFieldProperties pr) {
		return constructTextField(v, pr, null, null, false, false, false);
	}

	@Override
	public IFormLineView constructTextField(IVField v, IFormFieldProperties pr, IGetDataList iGet,
			IRequestForGWidget iHelper, boolean textarea, boolean richtext, boolean refreshAlways) {
		ExtendTextBox.EParam e;
		boolean panel = (iGet != null || iHelper != null);
		e = new ExtendTextBox.EParam(false, panel, false, textarea, richtext, false, iGet, null);
		if (iHelper == null)
			return new ExtendTextBox(v, pr, e);
		return new EditTextFieldWithHelper(v, pr, e, iHelper, refreshAlways);
	}

	@SuppressWarnings("unused")
	private IFormLineView constructLabelTextEdit(IVField v, IFormFieldProperties pr, String la) {
		return new LabelEdit(v, pr, newE(false, false), la);
	}

	@Override
	public IFormLineView constructLabelFor(IVField v, IFormFieldProperties pr, String la) {
		return new LabelFor(v, la);
	}

	@Override
	public IFormLineView construcDateBoxCalendar(IVField v, IFormFieldProperties pr) {
		return new DateBoxCalendar(v, pr);
	}

	@Override
	public IFormLineView constructDateBoxCalendarWithHelper(IVField v, IFormFieldProperties pr, IRequestForGWidget i,
			boolean refreshAlways) {
		return new DateBoxWithHelper(v, pr, i, refreshAlways);
	}

	@SuppressWarnings("unused")
	private IFormLineView constructBoxSelectField(IVField v, IFormFieldProperties pr, List<ComboVal> wy) {
		return new ComboBoxField(v, pr, wy);
	}

	@Override
	public IFormLineView constructRadioSelectField(IVField v, IFormFieldProperties pr) {
		return new RadioBoxField(v, pr);
	}

	@Override
	public IFormLineView constructListComboValuesHelp(IVField v, IFormFieldProperties pr, IDataType dType) {
		GetValueLB lB = new ListBoxWithHelp(v, pr, dType);
		AddBoxValues.addValues(dType, lB);
		return lB;
	}

	@Override
	public IFormLineView constructEditFileName(IVField v, IFormFieldProperties pr) {
		return new FileChooser(v, pr);
	}

	private List<ComboVal> createVals(List<String> ma) {
		List<ComboVal> vals = new ArrayList<ComboVal>();
		for (String s : ma) {
			vals.add(new ComboVal(s));
		}
		return vals;
	}

	@Override
	public IFormLineView constructListCombo(IVField v, IFormFieldProperties pr, List<String> ma) {
		return new ComboBoxField(v, pr, createVals(ma));
	}

	@Override
	public IFormLineView constructListCombo(IVField v, IFormFieldProperties pr, List<String> ma, boolean addEmpty) {
		return new ComboBoxField(v, pr, createVals(ma), addEmpty);
	}

	@Override
	public IFormLineView constructListCombo(IVField v, IFormFieldProperties pr) {
		return new ComboListBoxField(v, pr);
	}

	@Override
	public IFormLineView constructListComboEnum(IVField v, IFormFieldProperties pr) {
		List<String> la = new ArrayList<String>();
		la.addAll(v.getType().getE().getValues());
		return constructListCombo(v, pr, la);
	}

	@Override
	public IFormLineView constructSpinner(IVField v, IFormFieldProperties pr, int min, int max) {
		return new TextWidgetBox(v, pr, new SpinnerInt(min, max));
	}

	@Override
	public IFormLineView constructSuggestBox(IVField v, IFormFieldProperties pr, IGetDataList iGet) {
		return new SuggestWidget(v, pr, iGet);
	}

	@Override
	public IFormLineView constructEmail(IVField v, IFormFieldProperties pr) {
		return new TextWidgetBox(v, pr, new EmailVal());
	}

	@Override
	public IFormLineView constructImageButton(IVField v, IFormFieldProperties pr, int imageNo, IGetListOfIcons iList) {
		return new ImageButton(v, pr, imageNo, iList);
	}

	@Override
	public IFormLineView constructDateTimePicker(IVField v, IFormFieldProperties pr) {
		return new DateTimePicker(v, pr);
	}
}
