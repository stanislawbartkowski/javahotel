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
package com.gwtmodel.table.view.stackvertical;

import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.mm.MM;
import com.gwtmodel.table.view.controlpanel.IControlClick;
import com.gwtmodel.table.view.util.polymer.CreatePolymerMenu;
import com.vaadin.polymer.paper.widget.PaperMenu;

/**
 *
 * @author stanislaw.bartkowski@gmail.com
 */
public class StackPanelFactory {

	public IStackPanelView construct(List<ControlButtonDesc> bList, IControlClick click, String html) {
		if (MM.isPolymer()) {
			PaperMenu menu = CreatePolymerMenu.constructStackMenu(bList, click);
			return new IStackPanelView() {

				@Override
				public Widget getGWidget() {
					return menu;
				}

			};
		}
		if (html == null) {
			return new StackPanelView(bList, click);
		}
		return new StackPanelHtmlView(bList, click, html);
	}
}
