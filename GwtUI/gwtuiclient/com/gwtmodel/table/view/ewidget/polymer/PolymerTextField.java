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

import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.binder.WidgetTypes;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.editw.IFormFieldProperties;
import com.gwtmodel.table.mm.LogT;
import com.vaadin.polymer.paper.widget.PaperInput;
import com.vaadin.polymer.paper.widget.PaperTextarea;

class PolymerTextField extends AbstractWField {

	// not final
	// protected PaperInput in = new PaperInput();
	protected final TextAWidget in = new TextAWidget();
	private final String pattern;
	private final boolean autovalidate;
	private final boolean password;

	private void setP() {
		if (CUtil.EmptyS(in.getLabel()))
			in.setLabel(v.getLabel());
		in.addChangeHandler(new ChangeHa());
		in.addEventListener("keydown", new TouchEvent());
		if (pr.isNotEmpty())
			in.setRequired(true);
		if (pattern != null) {
			in.setPattern(pattern);
			if (autovalidate)
				in.setAutoValidate(true);
		}
		in.setErrorMessage(getStandErrMess());
		if (password)
			in.setType("password");
	}

	PolymerTextField(IVField v, IFormFieldProperties pr, String pattern, String standErrMess, boolean autovalidate,
			boolean password, boolean textarea) {
		super(v, pr, standErrMess);
		if (textarea)
			in.setP(new PaperTextarea());
		else
			in.setP(new PaperInput());
		this.password = password;
		this.pattern = pattern;
		this.autovalidate = autovalidate;
		setP();
	}

	@Override
	public Object getValObj() {
		String s = in.getValue();
		return FUtils.getValue(v, s);
	}

	@Override
	public void setValObj(Object o) {
		runOnTouch();
		String s = FUtils.getValueOS(o, v);
		in.setValue(s);
		onChangeEdit(false);
	}

	@Override
	public Widget getGWidget() {
		return in.getGWidget();
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		in.setReadonly(readOnly);
		// if (readOnly)
		// setAttr("aria-disabled", "true");
	}

	@Override
	public void setHidden(boolean hidden) {
		in.setVisible(!hidden);
	}

	@Override
	public void setInvalidMess(String errmess) {
		if (CUtil.EmptyS(errmess)) {
			in.setInvalid(false);
			in.setErrorMessage(getStandErrMess());
		} else {
			in.setErrorMessage(errmess);
			in.setInvalid(true);
		}
	}

	@Override
	public void setGStyleName(String styleMess, boolean set) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setCellTitle(String title) {
		in.setTitle(title);
	}

	@Override
	public void setSuggestList(List<String> list) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFocus(boolean focus) {
		in.setFocused(focus);
	}

	@Override
	public boolean isInvalid() {
		in.validate();
		return in.getInvalid();
	}

	@Override
	public void replaceWidget(Widget w) {
		if (in.isPaperInput()) {
			if (!(w instanceof PaperInput))
				Utils.errAlertB(LogT.getT().ReplaceTypeNotCorrect(WidgetTypes.PaperInput.name(),
						PaperInput.class.getName(), Widget.class.getName()));
			in.setP((PaperInput) w);
		} else {
			if (!(w instanceof PaperTextarea))
				Utils.errAlertB(LogT.getT().ReplaceTypeNotCorrect(WidgetTypes.PaperTextarea.name(),
						PaperTextarea.class.getName(), Widget.class.getName()));
			in.setP((PaperTextarea) w);
		}
		setP();
	}

}
