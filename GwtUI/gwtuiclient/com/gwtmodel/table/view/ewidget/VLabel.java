/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.view.ewidget;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.rdef.IFormChangeListener;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.rdef.ITouchListener;

class VLabel implements IFormLineView {

	private final IVField v;
	private final Label l = new Label();

	VLabel(IVField v, String displayName) {
		this.v = v;
		l.setText(displayName);
	}

	@Override
	public IVField getV() {
		return v;
	}

	@Override
	public Object getValObj() {
		return l.getText();
	}

	@Override
	public void setValObj(Object o) {
		l.setText((String) o);
	}

	@Override
	public Widget getGWidget() {
		return l;
	}

	@Override
	public void addChangeListener(IFormChangeListener cListener) {
	}

	@Override
	public void setReadOnly(boolean readOnly) {
	}

	@Override
	public void setHidden(boolean hidden) {
		l.setVisible(!hidden);
	}

	@Override
	public boolean isHidden() {
		return !l.isVisible();
	}

	@Override
	public void setInvalidMess(String errmess) {
	}

	@Override
	public void setGStyleName(String styleMess, boolean set) {
	}

	@Override
	public void setOnTouch(ITouchListener lTouch) {
	}

	@Override
	public int getChooseResult() {
		return 0;
	}

	@Override
	public String getHtmlName() {
		return v.getId();
	}

	@Override
	public void setAttr(String attrName, String attrValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setCellTitle(String title) {
		// TODO Auto-generated method stub
		
	}

}
