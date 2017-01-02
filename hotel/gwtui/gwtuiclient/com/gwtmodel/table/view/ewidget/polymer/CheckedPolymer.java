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
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.editw.IFormFieldProperties;
import com.vaadin.polymer.paper.widget.PaperCheckbox;
import com.vaadin.polymer.paper.widget.PaperToggleButton;

class CheckedPolymer extends AbstractWField {

	private class CheckWidget {
		private PaperCheckbox ch;
		private PaperToggleButton bu;

		CheckWidget(PaperCheckbox ch) {
			this.ch = ch;
			this.bu = null;
		}

		void setDisabled(boolean readOnly) {
			if (ch != null)
				ch.setDisabled(readOnly);
			else
				bu.setDisabled(readOnly);
		}

		void setVisible(boolean hidden) {
			if (ch != null)
				ch.setVisible(!hidden);
			else
				bu.setVisible(!hidden);
		}

		void setInvalid(boolean invalid) {
			if (ch != null)
				ch.setInvalid(invalid);
			else
				bu.setInvalid(invalid);
		}

		void setTitle(String title) {
			if (ch != null)
				ch.setTitle(title);
			else
				bu.setTitle(title);
		}

		void setFocused(boolean focused) {
			if (ch != null)
				ch.setFocused(focused);
			else
				bu.setFocused(focused);
		}

		boolean getInvalid() {
			if (ch != null)
				return ch.getInvalid();
			return bu.getInvalid();
		}

		boolean getChecked() {
			if (ch != null)
				return ch.getChecked();
			return bu.getChecked();
		}

		void setChecked(boolean checked) {
			if (ch != null)
				ch.setChecked(checked);
			else
				bu.setChecked(checked);
		}

		Widget getWidget() {
			if (ch != null)
				return ch;
			return bu;
		}

		void setChangeHandler() {
			// only when state changed due to user interaction
			if (ch != null)
				ch.addChangeHandler(i -> onChangeEdit(true));
			else
				bu.addChangeHandler(i -> onChangeEdit(true));
		}

	}

	private final CheckWidget chW;

	protected CheckedPolymer(IVField v, IFormFieldProperties pr) {
		super(v, pr, null);
		PaperCheckbox ch;
		if (pr.getDisplayName() != null)
			ch = new PaperCheckbox(pr.getDisplayName());
		else
			ch = new PaperCheckbox();
		chW = new CheckWidget(ch);
		chW.setChangeHandler();
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		chW.setDisabled(readOnly);
	}

	@Override
	public void setHidden(boolean hidden) {
		chW.setVisible(!hidden);
	}

	@Override
	public void setInvalidMess(String errmess) {
		if (CUtil.EmptyS(errmess))
			chW.setInvalid(false);
		else
			chW.setInvalid(true);

	}

	@Override
	public void setGStyleName(String styleMess, boolean set) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setCellTitle(String title) {
		chW.setTitle(title);
	}

	@Override
	public void setSuggestList(List<String> list) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFocus(boolean focus) {
		chW.setFocused(focus);

	}

	@Override
	public boolean isInvalid() {
		return chW.getInvalid();
	}

	@Override
	public Object getValObj() {
		return new Boolean(chW.getChecked());
	}

	@Override
	public void setValObj(Object o) {
		Boolean b = (Boolean) o;
		if (b == null)
			return;
		chW.setChecked(b.booleanValue());
	}

	@Override
	public Widget getGWidget() {
		return chW.getWidget();
	}

	@Override
	public void replaceWidget(Widget w) {
		chW.ch = null;
		chW.bu = null;
		if (w instanceof PaperCheckbox)
			chW.ch = (PaperCheckbox) w;
		else if (w instanceof PaperToggleButton)
			chW.bu = (PaperToggleButton) w;
		else
			Utils.ReplaceForClassNotImplemented(v.getId(), w.getClass().getName());
		chW.setChangeHandler();
	}

}
