/*

+ * Copyright 2017 stanislawbartkowski@gmail.com 
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
package com.jythonui.client.start.impl;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;
import com.gwtmodel.table.binder.BinderWidget;
import com.jythonui.client.M;
import com.jythonui.client.dialog.IReadDialog;
import com.jythonui.client.dialog.impl.ReadDialog;
import com.jythonui.client.gini.UIGiniInjector;
import com.jythonui.client.start.IJythonClientStart;
import com.jythonui.shared.ClientProp;
import com.polymerui.client.IConsts;
import com.polymerui.client.util.SynchronizeList;
import com.polymerui.client.view.panel.IMainPanel;
import com.polymerui.client.view.panel.MainPanelFactory;
import com.vaadin.polymer.Polymer;
import com.vaadin.polymer.elemental.Function;

public class JythonClientStart implements IJythonClientStart {

	private final MainPanelFactory pFactory;

	@Inject
	public JythonClientStart(MainPanelFactory pFactory) {
		this.pFactory = pFactory;
	}

	private static final String mainPanel = "mainpanel.xml";
	private static final String startP = "startp.xml";

	class WSync extends SynchronizeList {

		BinderWidget w;

		WSync() {
			super(3);
		}

		@Override
		protected void doTask() {
			moveForward(w);
		}
	}

	private class ResBack implements AsyncCallback<ClientProp> {

		private final WSync s;

		ResBack(WSync s) {
			this.s = s;
		}

		private void drawError(Throwable caught) {
			String html = "<H1>" + M.M().CannotLoadClientResource() + "</H1>";
			if (caught != null) {
				html = html + "<p>" + caught.getLocalizedMessage();
			}
			RootPanel.get().add(new HTML(html));
		}

		@Override
		public void onFailure(Throwable caught) {
			drawError(caught);
		}

		@Override
		public void onSuccess(ClientProp result) {
			if (result == null) {
				drawError(null);
				return;
			}
			M.setClientProp(result);
			s.signalDone();
		}
	}

	private class PanelBack implements AsyncCallback<BinderWidget> {

		private final WSync s;

		PanelBack(WSync s) {
			this.s = s;
		}

		@Override
		public void onFailure(Throwable caught) {
			String html = "<H1>" + M.M().CannotLoadMainPanelBinder(mainPanel) + "</H1>";
			if (caught != null) {
				html = html + "<p>" + caught.getLocalizedMessage();
			}
			RootPanel.get().add(new HTML(html));
		}

		@Override
		public void onSuccess(BinderWidget result) {
			s.w = result;
			s.signalDone();
		}

	}

	private void displayPanelInfo(IMainPanel.InfoType t, String pInfo) {
		String i = M.getClientProp().getAttr(pInfo, "");
		M.getPanel().drawInfo(t, i);
	}

	private void moveForward(BinderWidget w) {
		IMainPanel i = pFactory.produceMainPanel(w,
				M.getClientProp().getAttr(IConsts.PANELINFOPRODUCTIMAGE, IConsts.PRODUCTIMAGEDEFAULT),
				M.getClientProp().getAttr(IConsts.PANELINFOVERSIONINFO));
		M.setPanel(i);
		RootPanel.get().add(i.getWidget());
		displayPanelInfo(IMainPanel.InfoType.PRODUCT, IConsts.PANELINFOPRODUCTNAME);
		displayPanelInfo(IMainPanel.InfoType.TITLE, IConsts.PANELINFOTITLE);
		displayPanelInfo(IMainPanel.InfoType.OWNER, IConsts.PANELINFOOWNERNAME);
		IReadDialog d = UIGiniInjector.getI().getReadDialog();
		d.readDialog(startP, true);
	}

	@Override
	public void start() {
		// read two resources, synchronized by sync
		WSync s = new WSync();

		M.JR().getClientRes(UIGiniInjector.getI().getRequestContext(), new ResBack(s));
		M.JR().readBinderWidget(mainPanel, new PanelBack(s));

		List<String> imp = new ArrayList<String>();
		imp.add("iron-icons/iron-icons.html");
		imp.add("iron-icons/communication-icons.html");
		imp.add("iron-icons/device-icons.html");
		imp.add("iron-icons/hardware-icons.html");
		imp.add("iron-icons/image-icons.html");
		imp.add("iron-icons/maps-icons.html");
		imp.add("iron-icons/notification-icons.html");
		imp.add("iron-icons/social-icons.html");
		imp.add("vaadin-icons/vaadin-icons.html");
		imp.add("paper-dropdown-menu/paper-dropdown-menu.html");
		imp.add("paper-listbox/paper-listbox.html");
		imp.add("paper-tabs/paper-tabs.html");
		imp.add("paper-tabs/paper-tab.html");
		// imp.add("paper-tabs/paper-tab-icons.html");
		imp.add("paper-menu/paper-menu.html");
		imp.add("paper-menu/paper-submenu.html");
		imp.add("paper-fab/paper-fab.html");
		imp.add("paper-dialog-scrollable/paper-dialog-scrollable.html");
		// imp.add("iron-drop-down/iron-drop-down.html");
		// imp.add("iron-drop-down/iron-dropdown-scroll-manager.html");
		imp.add("paper-dialog/paper-dialog.html");
		imp.add("paper-item/paper-item.html");
		imp.add("paper-item/paper-item-body.html");
		imp.add("iron-icon/iron-icon.html");
		imp.add("paper-tooltip/paper-tooltip.html");
		Polymer.importHref(imp, new Function() {

			@Override
			public Object call(Object arg) {
				// TODO Auto-generated method stub
				s.signalDone();
				return null;
			}

		});
	}
}
