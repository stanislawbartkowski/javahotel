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

import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IGetDataList;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.editw.IFormFieldProperties;
import com.vaadin.polymer.iron.widget.IronSelector;
import com.vaadin.polymer.vaadin.widget.VaadinComboBox;
import com.vaadin.polymer.vaadin.widget.VaadinContextMenu;

class PolymerCombo extends AbstractFieldDecorator {

	PolymerCombo(IVField v, IFormFieldProperties pr, IGetDataList iGet, boolean addEmpty) {
		super(pr);
		fV = new PaperDownPolymerCombo(v, pr, iGet, addEmpty);
	}

	@Override
	public void replaceWidget(Widget w) {
		if (w instanceof IronSelector)
			fV = new IronSelectorPolymerCombo(fV.getV(), pr, (IronSelector) w);
		else if (w instanceof VaadinComboBox)
			fV = new VaadinComboBoxPolymer(fV.getV(), pr, (VaadinComboBox) w);
		else if (w instanceof VaadinContextMenu)
			fV = new VaadinContextMenuCombo(fV.getV(), pr, (VaadinContextMenu) w);
		else {
			fV.replaceWidget(w);
			return;
		}
		replayListener();
	}

}
