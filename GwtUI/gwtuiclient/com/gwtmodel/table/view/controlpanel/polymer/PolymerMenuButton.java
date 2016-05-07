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
package com.gwtmodel.table.view.controlpanel.polymer;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.view.controlpanel.IContrButtonView;
import com.gwtmodel.table.view.controlpanel.IControlClick;
import com.gwtmodel.table.view.util.CreateFormView.IGetButtons;
import com.gwtmodel.table.view.util.polymer.CreatePolymerMenu;
import com.vaadin.polymer.paper.widget.PaperMenu;

public class PolymerMenuButton implements IContrButtonView {

	private final PaperMenu menu;

	public PolymerMenuButton(final ListOfControlDesc model, final IControlClick co) {
		menu = CreatePolymerMenu.construct(model, co);
	}

	@Override
	public Widget getGWidget() {
		return menu;
	}

	@Override
	public void setEnable(ClickButtonType actionId, boolean enable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setHidden(ClickButtonType actionId, boolean hidden) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setHtml(IGWidget g) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fillHtml(HTMLPanel pa) {
		// TODO Auto-generated method stub

	}

	@Override
	public void emulateClick(ClickButtonType actionId) {
		// TODO Auto-generated method stub

	}

	@Override
	public IGetButtons construct() {
		// TODO Auto-generated method stub
		return null;
	}

}
