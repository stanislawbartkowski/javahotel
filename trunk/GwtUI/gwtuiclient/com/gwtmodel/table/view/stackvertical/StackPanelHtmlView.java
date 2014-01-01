/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.view.stackvertical;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IGFocusWidget;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.view.controlpanel.IControlClick;
import com.gwtmodel.table.view.util.CreateFormView;

/**
 * 
 * @author perseus
 */
class StackPanelHtmlView extends AbstractPanelView {

	private final HTMLPanel hPanel;

	StackPanelHtmlView(List<ControlButtonDesc> bList, IControlClick click,
			String html) {
		super(click);
		hPanel = new HTMLPanel(html);
		List<ClickButtonType> cList = new ArrayList<ClickButtonType>();
		List<IGFocusWidget> buList = new ArrayList<IGFocusWidget>();
		for (ControlButtonDesc bu : bList) {
			IGFocusWidget bt = constructButton(bu);
			buList.add(bt);
			cList.add(new ClickButtonType(bu.getActionId().getCustomButt()));
		}
		CreateFormView.setHtml(hPanel, cList, buList);
	}

	public Widget getGWidget() {
		return hPanel;
	}
}
