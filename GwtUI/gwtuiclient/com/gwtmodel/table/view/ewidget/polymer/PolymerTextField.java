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

import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.common.CUtil;
import com.vaadin.polymer.paper.widget.PaperInput;

class PolymerTextField extends AbstractWField {

	private final PaperInput in = new PaperInput();

	PolymerTextField(IVField v, String htmlName) {
		super(v);
		in.setLabel(v.getLabel());
		in.addChangeHandler(new ChangeHa());
		in.getPolymerElement().addEventListener("keydown", new TouchEvent());
	}

	@Override
	public Object getValObj() {
		return in.getValue();
	}

	@Override
	public void setValObj(Object o) {
		runOnTouch();
		in.setValue((String) o);
		onChangeEdit(false);
	}

	@Override
	public Widget getGWidget() {
		return in;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		in.setReadonly(readOnly);
//		if (readOnly)
//			setAttr("aria-disabled", "true");
	}

	@Override
	public void setHidden(boolean hidden) {
		in.setVisible(!hidden);
	}

	@Override
	public boolean isHidden() {
		return in.isVisible();
	}

	@Override
	public void setInvalidMess(String errmess) {
		if (CUtil.EmptyS(errmess))
			in.setInvalid(false);
		else {
			in.setErrorMessage(errmess);
			in.setInvalid(true);
		}
	}

	@Override
	public void setGStyleName(String styleMess, boolean set) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getHtmlName() {
		// TODO Auto-generated method stub
		return null;
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

}
