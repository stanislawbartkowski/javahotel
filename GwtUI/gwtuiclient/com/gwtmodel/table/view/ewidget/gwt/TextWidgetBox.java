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

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.IVField;

class TextWidgetBox extends AbstractField {

	private final TextBoxBase t;

	private class CHandler implements ChangeHandler {

		@Override
		public void onChange(ChangeEvent event) {
			runOnChange(TextWidgetBox.this, true);
		}
	}

	TextWidgetBox(IVField v, String htmlName, TextBoxBase t) {
		super(v, htmlName);
		this.t = t;
		t.addChangeHandler(new CHandler());
		initWidget(t);
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		t.setReadOnly(readOnly);
	}

	@Override
	public Object getValObj() {
		String s = t.getValue();
		return FUtils.getValue(v, s);
	}

	@Override
	public void setValObj(Object o) {
		String s = FUtils.getValueOS(o, v);
		t.setValue(s);
	}

	@Override
	public void setFocus(boolean focus) {
		t.setFocus(focus);
	}

}
