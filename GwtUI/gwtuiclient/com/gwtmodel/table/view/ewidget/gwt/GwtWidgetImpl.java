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
import com.gwtmodel.table.editw.IFormLineView;
import com.gwtmodel.table.editw.IGetListOfIcons;
import com.gwtmodel.table.factories.IGetCustomValues;
import com.gwtmodel.table.view.ewidget.IEditWidget;

public class GwtWidgetImpl implements IEditWidget {

	@Override
	public IFormLineView constructLabelField(IVField v, String displayName) {
		return new VLabel(v, displayName);
	}

	@Override
	public IFormLineView constructHTMLField(IVField v) {
		return new VHtml(v);
	}

	@Override
	public IFormLineView constructAnchorField(IVField v) {
		return new AnchorField(v);
	}

	@Override
	public RadioBoxString constructRadioBoxString(IVField v, IGetDataList iGet, final boolean enable, String htmlName) {
		return new RadioBoxString(v, iGet, enable, htmlName);
	}

	@Override
	public IFormLineView contructCalculatorNumber(IVField v, String htmlName) {
		return new NumberCalculator( v,
				new ExtendTextBox.EParam(false, false, false, false, false, false, null, null), htmlName);
	}

	@Override
	public IFormLineView constructCheckField(IVField v, String text, String htmlName) {
		return new FieldCheckField( v, text, htmlName);
	}

	@Override
	public IFormLineView constructListValuesCombo(IVField v, IDataType dType, String htmlName) {
		GetValueLB lB = new GetValueLB( v, htmlName);
		AddBoxValues.addValues(dType, lB);
		return lB;
	}

	@Override
	public IFormLineView constructListValuesCombo(IVField v, IGetDataList iGet, boolean addEmpty, String htmlName) {
		GetValueLB lB = new GetValueLB( v, addEmpty, htmlName);
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
	public IFormLineView constructHelperList(IVField v, IDataType dType, boolean refreshAlways, String htmlName) {
		ExtendTextBox.EParam e = new ExtendTextBox.EParam(false, true, false, false, false, false, null, null);
		return new ListFieldWithHelp( v, dType, e, refreshAlways, htmlName);
	}

	@Override
	public IFormLineView constructPasswordField(IVField v, String htmlName) {
		return new ExtendTextBox( v, newE(true, false), htmlName);
	}

	@Override
	public IFormLineView constructTextField(IVField v, String htmlName) {
		return constructTextField(v, null, null, false, false, false, htmlName);
	}

	@Override
	public IFormLineView constructTextField(IVField v, IGetDataList iGet, IRequestForGWidget iHelper, boolean textarea,
			boolean richtext, boolean refreshAlways, String htmlName) {
		ExtendTextBox.EParam e;
		boolean panel = (iGet != null || iHelper != null);
		e = new ExtendTextBox.EParam(false, panel, false, textarea, richtext, false, iGet, null);
		if (iHelper == null)
			return new ExtendTextBox( v, e, htmlName);
		return new EditTextFieldWithHelper( v, e, iHelper, refreshAlways, htmlName);
	}

	@SuppressWarnings("unused")
	private IFormLineView constructLabelTextEdit(IVField v, String la, String htmlName) {
		return new LabelEdit( v, newE(false, false), la, htmlName);
	}

	@Override
	public IFormLineView constructLabelFor(IVField v, String la) {
		return new LabelFor(v, la);
	}

	@Override
	public IFormLineView construcDateBoxCalendar(IVField v, String htmlName) {
		return new DateBoxCalendar( v, htmlName);
	}

	@Override
	public IFormLineView constructDateBoxCalendarWithHelper(IVField v, IRequestForGWidget i, boolean refreshAlways,
			String htmlName) {
		return new DateBoxWithHelper( v, i, refreshAlways, htmlName);
	}

	@SuppressWarnings("unused")
	private IFormLineView constructBoxSelectField(IVField v, List<ComboVal> wy, String htmlName) {
		return new ComboBoxField( v, wy, htmlName);
	}

	@Override
	public IFormLineView constructRadioSelectField(IVField v, String htmlName) {
		return new RadioBoxField( v, htmlName);
	}

	@Override
	public IFormLineView constructListComboValuesHelp(IVField v, IDataType dType, String htmlName) {
		GetValueLB lB = new ListBoxWithHelp( v, dType, htmlName);
		AddBoxValues.addValues(dType, lB);
		return lB;
	}

	@Override
	public IFormLineView constructEditFileName(IVField v, String htmlName) {
		return new FileChooser( v, htmlName);
	}

	private List<ComboVal> createVals(List<String> ma) {
		List<ComboVal> vals = new ArrayList<ComboVal>();
		for (String s : ma) {
			vals.add(new ComboVal(s));
		}
		return vals;
	}

	@Override
	public IFormLineView constructListCombo(IVField v, List<String> ma, String htmlName) {
		return new ComboBoxField( v, createVals(ma), htmlName);
	}

	@Override
	public IFormLineView constructListCombo(IVField v, List<String> ma, boolean addEmpty, String htmlName) {
		return new ComboBoxField( v, createVals(ma), addEmpty, htmlName);
	}

	@Override
	public IFormLineView constructListCombo(IVField v, String htmlName) {
		return new ComboListBoxField( v, htmlName);
	}

	@Override
	public IFormLineView constructListComboEnum(IVField v, String htmlName) {
		List<String> la = new ArrayList<String>();
		la.addAll(v.getType().getE().getValues());
		return constructListCombo(v, la, htmlName);
	}

	@Override
	public IFormLineView constructSpinner(IVField v, String htmlName, int min, int max) {
		return new TextWidgetBox( v, htmlName, new SpinnerInt(min, max));
	}

	@Override
	public IFormLineView constructSuggestBox(IVField v, IGetDataList iGet, String htmlName) {
		return new SuggestWidget( v, iGet, htmlName);
	}

	@Override
	public IFormLineView constructEmail(IVField v, String htmlName) {
		return new TextWidgetBox( v, htmlName, new EmailVal());
	}

	@Override
	public IFormLineView constructImageButton(IVField v, String htmlName, int imageNo, IGetListOfIcons iList) {
		return new ImageButton( v, htmlName, imageNo, iList);
	}

	@Override
	public IFormLineView constructDateTimePicker(IVField v, String htmlName) {
		return new DateTimePicker( v, htmlName);
	}
}
