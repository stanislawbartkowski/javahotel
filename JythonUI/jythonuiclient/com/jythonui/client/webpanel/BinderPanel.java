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
package com.jythonui.client.webpanel;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.ICommand;
import com.gwtmodel.table.ISetSynchData;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.binder.BinderWidget;
import com.gwtmodel.table.factories.IWebPanelResources;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.view.binder.ICreateBinderWidget;
import com.gwtmodel.table.view.util.CreateFormView;
import com.gwtmodel.table.view.webpanel.polymer.PanelElemWidgets;
import com.gwtmodel.table.view.webpanel.polymer.PanelPolymer;
import com.jythonui.client.IUIConsts;
import com.jythonui.client.M;
import com.jythonui.client.readbinder.ReadBinder;
import com.jythonui.client.webpanel.BinderPanelFactory.ISetWebPanel;
import com.vaadin.polymer.paper.widget.PaperDrawerPanel;
import com.vaadin.polymer.paper.widget.PaperIconButton;

class BinderPanel {

	private BinderPanel() {
	}

	private static Map<String, Class> toW = new HashMap<String, Class>();

	static {
		toW.put(IUIConsts.PANELTITLEICON, Image.class);
		toW.put(IUIConsts.PANELPROGRESSICON, Image.class);
		toW.put(IUIConsts.PANELLABELERROR, Label.class);
		toW.put(IUIConsts.PANELMENUICON, PaperIconButton.class);
		toW.put(IUIConsts.PANELPERSONLABEL, Label.class);
		toW.put(IUIConsts.PANELPRODUCTLABEL, Label.class);
		toW.put(IUIConsts.PANELOWNERLABEL, Label.class);
		toW.put(IUIConsts.PANELHOTELLABEL, Label.class);
		toW.put(IUIConsts.PANELINFOLABEL, Label.class);
		toW.put(IUIConsts.PANELEXITICON, PaperIconButton.class);
		toW.put(IUIConsts.PANELDRAWERPANEL, PaperDrawerPanel.class);
		toW.put(IUIConsts.PANELPROGRESSHTML, HTMLPanel.class);
	}

	private static <T extends Widget> T get(Map<String, Widget> maW, String id, String fileName) {
		Widget w = maW.get(id);
		if (w == null) {
			String errmess = M.M().PanelBinderCannotFindWidget(fileName, id);
			Utils.errAlertB(errmess);
		}
		Class cl = toW.get(id);
		if (!w.getClass().equals(cl)) {
			String errmess = M.M().PanelBinderBadWidgetType(fileName, id, w.getClass().getName(), cl.getName());
			Utils.errAlertB(errmess);
		}
		return (T) w;
	}

	private static PanelElemWidgets construct(HTMLPanel pa, String fileName) {
		PanelElemWidgets pW = new PanelElemWidgets();
		Map<String, Widget> maW = CreateFormView.createListOfFieldsId(pa);
		pW.htmlPanel = pa;
		pW.panelWidget = pa;
		pW.titleIcon = get(maW, IUIConsts.PANELTITLEICON, fileName);
		pW.progressIcon = get(maW, IUIConsts.PANELPROGRESSICON, fileName);
		pW.menuIcon = get(maW, IUIConsts.PANELMENUICON, fileName);
		pW.personLabel = get(maW, IUIConsts.PANELPERSONLABEL, fileName);
		pW.productLabel = get(maW, IUIConsts.PANELPRODUCTLABEL, fileName);
		pW.ownerLabel = get(maW, IUIConsts.PANELOWNERLABEL, fileName);
		pW.hotelLabel = get(maW, IUIConsts.PANELHOTELLABEL, fileName);
		pW.infoLabel = get(maW, IUIConsts.PANELINFOLABEL, fileName);
		pW.exitIcon = get(maW, IUIConsts.PANELEXITICON, fileName);
		pW.drawerPanel = get(maW, IUIConsts.PANELDRAWERPANEL, fileName);
		pW.progressHtml = get(maW, IUIConsts.PANELPROGRESSHTML, fileName);

		return pW;
	}

	static void construct(ISetWebPanel f, String fileName, ICommand logOut) {
		IWebPanelResources pResources = GwtGiniInjector.getI().getWebPanelResources();
		ReadBinder.readBinder(new ISetSynchData<BinderWidget>() {

			@Override
			public void set(BinderWidget value) {
				ICreateBinderWidget iBinder = GwtGiniInjector.getI().getCreateBinderWidget();
				HTMLPanel pa = iBinder.create(value);
				f.set(new PanelPolymer(pResources, logOut, construct(pa, fileName)));
			}
		}, fileName);
	}

}
