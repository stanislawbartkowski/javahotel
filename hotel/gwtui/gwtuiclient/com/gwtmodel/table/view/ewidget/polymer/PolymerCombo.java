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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IGetDataList;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.editw.IFormChangeListener;
import com.gwtmodel.table.editw.IFormFieldProperties;
import com.gwtmodel.table.editw.IFormLineView;
import com.gwtmodel.table.editw.ITouchListener;
import com.vaadin.polymer.iron.widget.IronSelector;
import com.vaadin.polymer.vaadin.widget.VaadinComboBox;

class PolymerCombo implements IFormLineView {

	private IFormLineView pCombo;
	private final IFormFieldProperties pr;
	private final List<IFormChangeListener> cList = new ArrayList<IFormChangeListener>();

	PolymerCombo(IVField v, IFormFieldProperties pr, IGetDataList iGet, boolean addEmpty) {
		pCombo = new PaperDownPolymerCombo(v, pr, iGet, addEmpty);
		this.pr = pr;
	}

	@Override
	public IVField getV() {
		return pCombo.getV();
	}

	@Override
	public Object getValObj() {
		return pCombo.getValObj();
	}

	@Override
	public void setValObj(Object o) {
		pCombo.setValObj(o);

	}

	@Override
	public Widget getGWidget() {
		return pCombo.getGWidget();
	}

	@Override
	public void addChangeListener(IFormChangeListener cListener) {
		cList.add(cListener);
		pCombo.addChangeListener(cListener);
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		pCombo.setReadOnly(readOnly);
	}

	@Override
	public void setHidden(boolean hidden) {
		pCombo.setHidden(hidden);

	}

	@Override
	public void setInvalidMess(String errmess) {
		pCombo.setInvalidMess(errmess);

	}

	@Override
	public void setGStyleName(String styleMess, boolean set) {
		pCombo.setGStyleName(styleMess, set);

	}

	@Override
	public void setOnTouch(ITouchListener lTouch) {
		pCombo.setOnTouch(lTouch);

	}

	@Override
	public String getHtmlName() {
		return pCombo.getHtmlName();
	}

	@Override
	public void setAttr(String attrName, String attrValue) {
		pCombo.setAttr(attrName, attrValue);

	}

	@Override
	public void setCellTitle(String title) {
		pCombo.setCellTitle(title);

	}

	@Override
	public void setSuggestList(List<String> list) {
		pCombo.setSuggestList(list);

	}

	@Override
	public void setFocus(boolean focus) {
		pCombo.setFocus(focus);

	}

	@Override
	public boolean isInvalid() {
		return pCombo.isInvalid();
	}

	@Override
	public void replaceWidget(Widget w) {
		boolean replayListener = false;
		if (w instanceof IronSelector) {
			pCombo = new IronSelectorPolymerCombo(pCombo.getV(), pr, (IronSelector) w);
			replayListener = true;
		}
		if (w instanceof VaadinComboBox) {
			pCombo = new VaadinComboBoxPolymer(pCombo.getV(), pr, (VaadinComboBox) w);
			replayListener = true;
		}
		if (replayListener) {
			cList.forEach(c -> pCombo.addChangeListener(c));
			return;
		}
		pCombo.replaceWidget(w);
	}

}
