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
package com.gwtmodel.table.view.stackpanel;

import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.smessage.IGetStandardMessage;
import com.gwtmodel.table.view.controlpanel.IControlClick;
import com.gwtmodel.table.view.stackvertical.IStackPanelView;
import com.gwtmodel.table.view.stackvertical.StackPanelFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author perseus
 */
class ViewStackPanel implements IStackPanelView {

	private final StackPanel stackPanel = new StackPanel();
	private VerticalPanel vp = null;
	private final IGetStandardMessage iMess = GwtGiniInjector.getI()
			.getStandardMessage();

	private void createPanelMenu(StackPanelFactory baFactory, String menuName,
			List<ControlButtonDesc> bList, IControlClick click) {
		IStackPanelView w = baFactory.construct(bList, click, null);
		stackPanel.add(w.getGWidget(), iMess.getMessage(menuName));
	}

	ViewStackPanel(StackPanelFactory baFactory, List<ControlButtonDesc> bList,
			IControlClick click) {
		List<ControlButtonDesc> b = null;
		List<ControlButtonDesc> singleB = new ArrayList<ControlButtonDesc>();
		ControlButtonDesc menuName = null;
		for (ControlButtonDesc bu : bList) {
			if (bu.isMenuTitle()) {
				if (menuName != null) {
					if (b == null) {
						singleB.add(menuName);
					} else {
						createPanelMenu(baFactory, menuName.getDisplayName(),
								b, click);
					}
				}
				menuName = bu;
				b = null;
				continue;
			}
			if (b == null) {
				b = new ArrayList<ControlButtonDesc>();
			}
			b.add(bu);
		} // for
		if (menuName != null) {
			if (b == null) {
				singleB.add(menuName);
			} else {
				createPanelMenu(baFactory, menuName.getDisplayName(), b, click);
			}
		}
		if (!singleB.isEmpty()) {
			IStackPanelView w = baFactory.construct(singleB, click, null);
			vp = new VerticalPanel();
			vp.add(w.getGWidget());
			vp.add(stackPanel);
		}
	}

	public Widget getGWidget() {
		if (vp != null) {
			return vp;
		}
		return stackPanel;
	}
}
