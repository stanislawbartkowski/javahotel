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

import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.editw.IFormChangeListener;
import com.gwtmodel.table.editw.IFormLineView;
import com.gwtmodel.table.editw.ITouchListener;
import com.gwtmodel.table.view.ewidget.widgets.IBinderWidget;

abstract class AbstractBinderWidget implements IFormLineView {

	private final IBinderWidget b;
	private final IVField v;

	AbstractBinderWidget(IVField v, IBinderWidget b) {
		this.b = b;
		this.v = v;
	}

	@Override
	public IVField getV() {
		return v;
	}

	@Override
	public Object getValObj() {
		return b.getValObj();
	}

	@Override
	public void setValObj(Object o) {
		b.setValObj(o);
	}

	@Override
	public void addChangeListener(IFormChangeListener cListener) {
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		b.setReadOnly(readOnly);
	}

	@Override
	public void setHidden(boolean hidden) {
		b.setHidden(hidden);
	}

	@Override
	public void setInvalidMess(String errmess) {
		b.setInvalidMess(errmess);
	}

	@Override
	public void setGStyleName(String styleMess, boolean set) {
		b.setGStyleName(styleMess, set);
	}

	@Override
	public String getHtmlName() {
		return v.getId();
	}

	@Override
	public void setAttr(String attrName, String attrValue) {
	}

	@Override
	public void setCellTitle(String title) {
		b.setCellTitle(title);
	}

	@Override
	public void setSuggestList(List<String> list) {

	}

	@Override
	public void setFocus(boolean focus) {
		b.setFocus(focus);
	}

	@Override
	public boolean isInvalid() {
		return b.isInvalid();
	}

	@Override
	public void setOnTouch(ITouchListener lTouch) {
	}

	@Override
	public Widget getGWidget() {
		return b.getGWidget();
	}

	@Override
	public void replaceWidget(Widget w) {
		Utils.ReplaceWidgetNotImplements(v.getId(), b.getGWidget().getClass().getName());
	}

}
