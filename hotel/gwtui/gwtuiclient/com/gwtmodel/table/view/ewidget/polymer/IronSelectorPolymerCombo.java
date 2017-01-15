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

import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.editw.IFormFieldProperties;
import com.vaadin.polymer.iron.event.IronSelectEvent;
import com.vaadin.polymer.iron.widget.IronSelector;

class IronSelectorPolymerCombo extends AbstractWField {

	private final IronSelector iSel;

	protected IronSelectorPolymerCombo(IVField v, IFormFieldProperties pr, IronSelector iSel) {
		super(v, pr, null);
		this.iSel = iSel;

		iSel.getPolymerElement().addEventListener(IronSelectEvent.NAME, e -> {
				onChangeEdit(true);
		});
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		iSel.setDisabled(readOnly);
	}

	@Override
	public void setHidden(boolean hidden) {
		iSel.setVisible(!hidden);
	}

	private String getV(JavaScriptObject o, Object oo) {
		String a = iSel.getAttrForSelected();
		String ss;
		if (oo == null)
			ss = o.toString();
		else
			ss = oo.toString();
		if (!CUtil.EmptyS(a))
			return ss;
		// index
		if (!CUtil.OkInteger(ss))
			return ss;
		int i = CUtil.getNumb(ss);
		JsArray li = iSel.getItems();
		JavaScriptObject obj = li.get(i);
		Element ele = obj.cast();
		String in = ele.getInnerText();
		String sss = obj.getClass().getName();
		return in;
	}

	@Override
	public Object getValObj() {
		if (iSel.getMulti()) {
			JsArray selected = iSel.getSelectedValues();
			if (selected == null)
				return null;
			String res = null;
			for (int i = 0; i < selected.length(); i++) {
				if (res == null)
					res = getV(selected.get(i), null);
				else
					res = res + "," + getV(selected.get(i), null);
			}
			return res;
		}
		Object o = iSel.getSelected();
		return getV(null, o);
	}

	@Override
	public void setValObj(Object o) {
		if (o == null)
			return;
		iSel.setSelected(o);
	}

	@Override
	public Widget getGWidget() {
		return iSel;
	}
}
