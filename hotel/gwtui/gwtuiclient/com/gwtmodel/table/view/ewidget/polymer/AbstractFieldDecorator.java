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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.editw.IFormChangeListener;
import com.gwtmodel.table.editw.IFormFieldProperties;
import com.gwtmodel.table.editw.IFormLineView;
import com.gwtmodel.table.editw.ITouchListener;

abstract class AbstractFieldDecorator implements IFormLineView  {
	
	protected IFormLineView fV;
	protected final IFormFieldProperties pr;
	private final List<IFormChangeListener> cList = new ArrayList<IFormChangeListener>();
	
	AbstractFieldDecorator(IFormFieldProperties pr) {
		this.pr = pr;
	}
	
	@Override
	public IVField getV() {
		return fV.getV();
	}

	@Override
	public Object getValObj() {
		return fV.getValObj();
	}

	@Override
	public void setValObj(Object o) {
		fV.setValObj(o);

	}

	@Override
	public Widget getGWidget() {
		return fV.getGWidget();
	}

	@Override
	public void addChangeListener(IFormChangeListener cListener) {
		cList.add(cListener);
		fV.addChangeListener(cListener);
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		fV.setReadOnly(readOnly);
	}

	@Override
	public void setHidden(boolean hidden) {
		fV.setHidden(hidden);

	}

	@Override
	public void setInvalidMess(String errmess) {
		fV.setInvalidMess(errmess);

	}

	@Override
	public void setGStyleName(String styleMess, boolean set) {
		fV.setGStyleName(styleMess, set);

	}

	@Override
	public void setOnTouch(ITouchListener lTouch) {
		fV.setOnTouch(lTouch);

	}

	@Override
	public String getHtmlName() {
		return fV.getHtmlName();
	}

	@Override
	public void setAttr(String attrName, String attrValue) {
		fV.setAttr(attrName, attrValue);

	}

	@Override
	public void setCellTitle(String title) {
		fV.setCellTitle(title);

	}

	@Override
	public void setSuggestList(List<String> list) {
		fV.setSuggestList(list);

	}

	@Override
	public void setFocus(boolean focus) {
		fV.setFocus(focus);

	}

	@Override
	public boolean isInvalid() {
		return fV.isInvalid();
	}
	
	protected void replayListener() {
		cList.forEach(c -> fV.addChangeListener(c));
	}

}
