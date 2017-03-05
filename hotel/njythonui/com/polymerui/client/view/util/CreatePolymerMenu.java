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
package com.polymerui.client.view.util;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.inject.Inject;
import com.jythonui.client.smessage.IGetDisplayMess;
import com.jythonui.shared.ButtonItem;
import com.polymerui.client.eventbus.ButtonEvent;
import com.polymerui.client.eventbus.IEventBus;
import com.polymerui.client.util.Utils;
import com.vaadin.polymer.elemental.EventListener;
import com.vaadin.polymer.paper.widget.PaperItem;
import com.vaadin.polymer.paper.widget.PaperMenu;
import com.vaadin.polymer.paper.widget.PaperSubmenu;

public class CreatePolymerMenu {

	private CreatePolymerMenu() {

	}

	@Inject
	private static IGetDisplayMess iMess;

	// important: add panel id to put menu item inside menu or submenu htmlpanel
	// otherwise it would be added to the end, after menu htmlpanel
	// important: menu id in html should be the menuId String constant (menu-id)
	private static final String menuC = "<div class=\"menu-content\" id=\"menu-id\"></div>";
	private static final String submenuC = "<div class=\"menu-trigger\" id=\"trigger\"></div><div class=\"menu-content\" id=\"menu-id\"></div>";

	private static final String menuId = "menu-id";

	private static void constructM(PaperMenu menu, List<ButtonItem> bList, IEventBus iBus) {

		menu.addStyleName("menupaper-custom");
		bList.forEach(bu -> {
			String m = iMess.getString(bu.getDisplayName());
			final PaperItem i = new PaperItem(m);
			i.getPolymerElement().addEventListener("click", event -> {
				iBus.publish(new ButtonEvent(bu));
			});
			Utils.addE(menu.getElement(), i.getElement());
		});
	}

	public static PaperMenu construct(List<ButtonItem> coP, IEventBus iBus) {
		PaperMenu menu = new PaperMenu(menuC);
		constructM(menu, coP, iBus);
		return menu;
	}

	static class P {
		PaperSubmenu subm;
		PaperItem i;
	}

	private static void closeOpened(List<P> sList, PaperSubmenu current) {
		sList.stream().filter(p -> current != p.subm && p.subm.getOpened()).forEach(s -> {
			s.subm.setOpened(false);
			s.i.setActive(false);
		});
	}

	public static void constructStackMenu(PaperMenu header, List<ButtonItem> bList, IEventBus iBus) {
		List<ButtonItem> subl = new ArrayList<ButtonItem>();
		// list of submenus
		List<P> sList = new ArrayList<P>();
		PaperMenu smenu = null;
		for (ButtonItem co : bList) {
			if (!co.isHeaderButton()) {
				subl.add(co);
				continue;
			}
			if (smenu != null)
				constructM(smenu, subl, iBus);
			subl.clear();
			// first submenu
			P p = new P();
			p.subm = new PaperSubmenu(submenuC);
			String mess = iMess.getString(co.getDisplayName());
			// item describing submenu
			p.i = new PaperItem(mess);
			p.subm.add(p.i, "trigger");
			smenu = new PaperMenu(menuC);
			p.subm.add(smenu, menuId);
			header.add(p.subm);
			sList.add(p);
			p.subm.addPaperSubmenuOpenHandler(event -> closeOpened(sList, p.subm));
		}
		if (smenu != null && !subl.isEmpty())
			constructM(smenu, subl, iBus);
	}

}
