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
package com.gwtmodel.table.view.webpanel.polymer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.ICommand;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.factories.IWebPanelResources;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.view.webpanel.IWebPanel;
import com.gwtmodel.table.view.webpanel.common.AbstractWebPanel;

public class PanelPolymer extends AbstractWebPanel implements IWebPanel {

	private final UIPanelWidget uW = new UIPanelWidget();

	private Element menuWidget;
	private Element mainWidget;
	private Element menuiconWidget;

	public PanelPolymer(IWebPanelResources pResources, ICommand logOut) {
		super(pResources, logOut);
		setLabels(uW.productLabel, uW.ownerLabel, uW.personLabel, uW.hotelLabel, uW.infoLabel);
		String ima = pResources.getRes(IWebPanelResources.PROGRESSICON);
		String url = Utils.getImageAdr(ima);
		uW.progressIcon.setUrl(url);
		String h = Utils.getImageAdr(pResources.getRes(IWebPanelResources.IIMAGEPRODUCT));
		uW.titleIcon.setUrl(h);

		String JVersion = pResources.getRes(IWebPanelResources.JUIVERSION);
		String title = Utils.joinS('\n', pResources.getRes(IWebPanelResources.VERSION),
				LogT.getT().GWTVersion(GWT.getVersion(), JVersion));
		uW.titleIcon.setTitle(title);
		Window.setTitle(pResources.getRes(IWebPanelResources.TITLE));
		menuWidget = uW.htmlPanel.getElementById("leftmenu");
		mainWidget = uW.htmlPanel.getElementById("mainpanel");
		menuiconWidget = uW.htmlPanel.getElementById("menuicon");
	}

	private void setProgIcon(boolean visible) {
		uW.progressIcon.setVisible(visible);
	}

	private void setErrorMess(String errMess) {
		setProgIcon(false);
		if (errMess == null) {
			uW.progressHtml.removeStyleName("error");
			uW.progressHtml.setTitle("");
		} else {
			uW.progressHtml.addStyleName("error");
			uW.progressHtml.setTitle(errMess);
		}
	}

	@Override
	public void setErrorL(String errmess) {
		setErrorMess(errmess);
	}

	@Override
	public void setReplay(int replNo) {
		if (replNo == 1) {
			setErrorMess(null);
			uW.progressHtml.removeStyleName("error");
			setProgIcon(true);
		} else if (replNo == 0)
			setProgIcon(false);
	}

	private void setCentreE(Widget w) {
		uW.htmlPanel.addAndReplaceElement(w, mainWidget);
		mainWidget = w.getElement();
	}

	@Override
	public void setDCenter(Widget w) {
		if (w == null) {
			setCentreE(new Label());
			if (centreHideSignal != null)
				centreHideSignal.signal();

		} else
			setCentreE(w);
		centreHideSignal = null;
	}

	private void setWestE(Widget w) {
		uW.htmlPanel.addAndReplaceElement(w, menuWidget);
		menuWidget = w.getElement();
	}

	@Override
	public void setWest(Widget w) {
		setWestE(w == null ? new Label() : w);
	}

	@Override
	public Widget getWidget() {
		return uW;
	}

	private void setMenuIcon(Widget w) {
		uW.htmlPanel.addAndReplaceElement(w, menuiconWidget);
		menuiconWidget = w.getElement();
	}

	@Override
	public void setMenuPanel(Widget pa) {
		setMenuIcon(pa == null ? new Label() : pa);
	}

	@Override
	public void setPullDownMenu(Widget m) {
		// TODO Auto-generated method stub

	}

	@Override
	public void logOut() {
		uW.exitIcon.setVisible(true);

	}

	@Override
	public void SetMenuSize(String size) {
		uW.drawerPanel.setDrawerWidth(size);
	}

}
