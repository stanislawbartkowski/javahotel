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

import java.util.ArrayList;
import java.util.List;

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
import com.vaadin.polymer.paper.widget.PaperSubmenu;

public class CreatePolymerMenu {

	private CreatePolymerMenu() {

	}

	private static void constructM(PaperMenu menu, List<ControlButtonDesc> bList, final IControlClick cli) {
		IGetStandardMessage iMess = GwtGiniInjector.getI().getStandardMessage();
		menu.addStyleName("menupaper-custom");
		for (ControlButtonDesc bu : bList) {
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
	}

	public static PaperMenu construct(final ListOfControlDesc coP, final IControlClick cli) {
		PaperMenu menu = new PaperMenu();
		constructM(menu, coP.getcList(), cli);
		return menu;
	}

	// imageHtml is ignored for Polymer
	public static IGWidget createImageMenu(String imageHtml, final ListOfControlDesc coP, final IControlClick cli) {

		return new GWidget(construct(coP, cli));
	}

	private static class MenuD {
		PaperMenu menu = null;
		final List<ControlButtonDesc> subl = new ArrayList<ControlButtonDesc>();
		PaperMenu submenu = null;
		final IControlClick cli;

		MenuD(IControlClick cli) {
			this.cli = cli;
		}

		void addS() {
			if (!subl.isEmpty())
				if (menu == null) {
					menu = new PaperMenu();
					constructM(menu, subl, cli);
				} else
					constructM(submenu, subl, cli);

			subl.clear();
		}
	}

	public static PaperMenu constructStackMenu(List<ControlButtonDesc> bList, final IControlClick cli) {
		MenuD m = new MenuD(cli);
		IGetStandardMessage iMess = GwtGiniInjector.getI().getStandardMessage();
		for (ControlButtonDesc co : bList) {
			if (!co.isMenuTitle()) {
				m.subl.add(co);
				continue;
			}
			m.addS();
			PaperSubmenu subm = new PaperSubmenu(
					"<div class=\"menu-trigger\" id=\"trigger\"></div><div class=\"menu-content\" id=\"content\"></div>");
			String mess = iMess.getMessage(co.getDisplayName());
			PaperItem i = new PaperItem(mess);
			subm.add(i, "trigger");
			m.menu = new PaperMenu();
			subm.add(m.submenu, "content");
		}
		m.addS();
		return m.menu;
	}

}
