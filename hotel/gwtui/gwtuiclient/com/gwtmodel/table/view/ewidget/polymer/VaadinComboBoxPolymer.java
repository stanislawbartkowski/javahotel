/*
 * Copyright 2017 stanislawbartkowski@gmail.com 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not ue this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package com.gwtmodel.table.view.ewidget.polymer;

import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.editw.IFormFieldProperties;
import com.gwtmodel.table.view.ewidget.ItemsEvent;
import com.gwtmodel.table.view.ewidget.ItemsEventHandler;
import com.vaadin.polymer.vaadin.widget.VaadinComboBox;

class VaadinComboBoxPolymer extends AbstractWField {

	private final VaadinComboBox vBox;

	private String pendingValue = null;

	protected VaadinComboBoxPolymer(IVField v, IFormFieldProperties pr, VaadinComboBox vBox) {
		super(v, pr, null);
		this.vBox = vBox;

		vBox.addHandler(new ItemsEventHandler() {

			@Override
			public void onItemsChange(String val) {
				if (CUtil.EmptyS(pendingValue))
					return;
				// there is pending value to set
				vBox.setValue(pendingValue);
				// set only once
				pendingValue = null;

			}
		}, ItemsEvent.TYPE);

		vBox.addChangeHandler(e -> {
			onChangeEdit(true);
		});

	}

	@Override
	public void setReadOnly(boolean readOnly) {
		vBox.setReadonly(readOnly);
	}

	@Override
	public void setHidden(boolean hidden) {
		vBox.setVisible(!hidden);
	}

	@Override
	public void setInvalidMess(String errmess) {
		if (CUtil.EmptyS(errmess)) {
			vBox.setInvalid(false);
			vBox.setErrorMessage(getStandErrMess());
		} else {
			vBox.setErrorMessage(errmess);
			vBox.setInvalid(true);
		}
	}

	@Override
	public void setGStyleName(String styleMess, boolean set) {
	}

	@Override
	public void setCellTitle(String title) {
		vBox.setTitle(title);
	}

	@Override
	public boolean isInvalid() {
		return vBox.getInvalid();
	}

	@Override
	public Object getValObj() {
		Object o = vBox.getSelectedItem();
		if (o == null)
			return null;
		return o;
	}

	@Override
	public void setValObj(Object o) {
		Object items = vBox.getItems();
		if (items == null) {
			// wait until values are set
			pendingValue = (String) o;
			return;
		}
		vBox.setSelectedItem((String) o);

	}

	@Override
	public Widget getGWidget() {
		return vBox;
	}

}
