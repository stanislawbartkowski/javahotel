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
import com.gwtmodel.table.editw.IFormFieldProperties;
import com.vaadin.polymer.paper.widget.PaperCheckbox;

class CheckedPolymer extends AbstractWField {

	private final PaperCheckbox ch;

	protected CheckedPolymer(IVField v, IFormFieldProperties pr) {
		super(v, pr, null);
		if (pr.getDisplayName() != null)
			ch = new PaperCheckbox(pr.getDisplayName());
		else
			ch = new PaperCheckbox();
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		ch.setDisabled(readOnly);
	}

	@Override
	public void setHidden(boolean hidden) {
		ch.setVisible(!hidden);
	}

	@Override
	public void setInvalidMess(String errmess) {
		if (CUtil.EmptyS(errmess))
			ch.setInvalid(false);
		else
			ch.setInvalid(true);

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
		ch.setTitle(title);
	}

	@Override
	public void setSuggestList(List<String> list) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFocus(boolean focus) {
		ch.setFocused(focus);

	}

	@Override
	public boolean isInvalid() {
		return ch.getInvalid();
	}

	@Override
	public Object getValObj() {
		return new Boolean(ch.getChecked());
	}

	@Override
	public void setValObj(Object o) {
		Boolean b = (Boolean) o;
		if (b == null)
			return;
		ch.setChecked(b.booleanValue());
	}

	@Override
	public Widget getGWidget() {
		return ch;
	}

}
