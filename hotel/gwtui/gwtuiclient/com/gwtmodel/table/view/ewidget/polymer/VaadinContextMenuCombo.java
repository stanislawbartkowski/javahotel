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
import com.gwtmodel.table.editw.IFormFieldProperties;
import com.vaadin.polymer.Polymer;
import com.vaadin.polymer.elemental.HTMLElement;
import com.vaadin.polymer.iron.event.IronSelectEvent;
import com.vaadin.polymer.vaadin.widget.VaadinContextMenu;

public class VaadinContextMenuCombo extends AbstractWField {

	private final VaadinContextMenu co;

	private String selected = null;

	protected VaadinContextMenuCombo(IVField v, IFormFieldProperties pr, VaadinContextMenu co) {
		super(v, pr, null);
		this.co = co;
		co.getPolymerElement().addEventListener(IronSelectEvent.NAME, e -> {
			HTMLElement item = Polymer.property(e.getDetail(), "item");
			selected = item.getAttribute("value");
			onChangeEdit(true);
		});
	}

	@Override
	public Object getValObj() {
		return selected;
	}

	@Override
	public void setValObj(Object o) {
		// TODO Auto-generated method stub

	}

	@Override
	public Widget getGWidget() {
		return co;
	}

}
