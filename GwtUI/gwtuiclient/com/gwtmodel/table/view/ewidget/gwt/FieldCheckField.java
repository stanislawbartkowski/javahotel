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
package com.gwtmodel.table.view.ewidget.gwt;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.editw.IFormChangeListener;
import com.gwtmodel.table.editw.IFormFieldProperties;

/**
 * 
 * @author perseus
 */
class FieldCheckField extends AbstractField {

	private final CheckBox ch;

	@SuppressWarnings("deprecation")
	FieldCheckField(IVField v, IFormFieldProperties pr) {
		super(v, pr);
		ch = new CheckBox();
		if (pr.getDisplayName() != null) {
			ch.setText(pr.getDisplayName());
		}
		ch.setChecked(true);
		initWidget(ch);
		// set html element name
		ch.setName(getHtmlName());
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setValObj(Object o) {
		Boolean b = (Boolean) o;
		if (b != null) {
			ch.setChecked(b.booleanValue());
		}

	}

	@Override
	public Object getValObj() {
		@SuppressWarnings("deprecation")
		Boolean b = new Boolean(ch.isChecked());
		return b;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		ch.setEnabled(!readOnly);
	}

	private class BChange implements ValueChangeHandler<java.lang.Boolean> {

		private final IFormChangeListener l;

		BChange(final IFormChangeListener l) {
			this.l = l;
		}

		@Override
		public void onValueChange(ValueChangeEvent<Boolean> event) {
			l.onChange(FieldCheckField.this, true);

		}

	}

	@Override
	public void addChangeListener(final IFormChangeListener l) {
		super.addChangeListener(l);
		ch.addValueChangeHandler(new BChange(l));
	}

	@Override
	public void setFocus(boolean focus) {
		ch.setFocus(focus);

	}

}
