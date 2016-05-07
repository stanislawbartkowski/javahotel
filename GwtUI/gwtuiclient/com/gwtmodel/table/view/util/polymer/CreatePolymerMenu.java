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
package com.gwtmodel.table.view.util.polymer;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.gwtmodel.table.GWidget;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.smessage.IGetStandardMessage;
import com.gwtmodel.table.view.controlpanel.IControlClick;
import com.vaadin.polymer.paper.widget.PaperItem;
import com.vaadin.polymer.paper.widget.PaperMenu;

public class CreatePolymerMenu {

	private CreatePolymerMenu() {

	}

	public static PaperMenu construct(final ListOfControlDesc coP, final IControlClick cli) {
		IGetStandardMessage iMess = GwtGiniInjector.getI().getStandardMessage();
		PaperMenu menu = new PaperMenu();
		menu.addStyleName("menupaper-custom");
		for (ControlButtonDesc bu : coP.getcList()) {
			String m = iMess.getMessage(bu.getDisplayName());
			PaperItem i = new PaperItem(m);
			i.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					cli.click(bu, i);
				}
			});
			menu.add(i);
		}
		return menu;
	}

	// imageHtml is ignored for Polymer
	public static IGWidget createImageMenu(String imageHtml, final ListOfControlDesc coP, final IControlClick cli) {

		return new GWidget(construct(coP, cli));
	}

}
