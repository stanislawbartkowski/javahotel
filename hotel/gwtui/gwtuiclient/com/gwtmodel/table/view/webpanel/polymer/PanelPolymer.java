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
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.ICommand;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.binder.BinderWidget;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.factories.IWebPanelResources;
import com.gwtmodel.table.mm.LogT;
import com.gwtmodel.table.view.util.FormUtil;
import com.gwtmodel.table.view.webpanel.IWebPanel;
import com.gwtmodel.table.view.webpanel.common.AbstractWebPanel;
import com.vaadin.polymer.Polymer;
import com.vaadin.polymer.elemental.Document;
import com.vaadin.polymer.elemental.Node;

public class PanelPolymer extends AbstractWebPanel implements IWebPanel {

	private final PanelElemWidgets uW;

	private Element menuWidget;
	private HTMLPanel mainPa;
	private Element menuiconWidget;
	private IStatusMenuIcon i = null;

	private Element getId(String id) {
		// debug
		String html = uW.htmlPanel.toString();
		Element e = uW.htmlPanel.getElementById(id);
		if (e != null)
			return e;
		String errmess = LogT.getT().PanelCannotFindWidget(id);
		Utils.errAlertB(errmess);
		return null;
	}

	public PanelPolymer(IWebPanelResources pResources, ICommand logOut, PanelElemWidgets uW) {
		super(pResources, logOut);
		this.uW = uW;
		setLabels(uW.productLabel, uW.ownerLabel, uW.personLabel, uW.hotelLabel, uW.infoLabel);
		String ima = pResources.getRes(IWebPanelResources.PROGRESSICON);
		String url = Utils.getImageAdr(ima);
		uW.progressIcon.setUrl(url);
		String h = Utils.getImageAdr(pResources.getRes(IWebPanelResources.IIMAGEPRODUCT));
		uW.titleIcon.setUrl(h);

		String JVersion = pResources.getRes(IWebPanelResources.JUIVERSION);
		String title = CUtil.joinS('\n', pResources.getRes(IWebPanelResources.VERSION),
				LogT.getT().GWTVersion(GWT.getVersion(), JVersion));
		uW.titleIcon.setTitle(title);
		Window.setTitle(pResources.getRes(IWebPanelResources.TITLE));
		menuWidget = getId("leftmenu");
		//  important HTMLPanel
		mainPa = (HTMLPanel) FormUtil.findWidgetByFieldId(uW.htmlPanel, "mainpanel");
		menuiconWidget = getId("menuicon");
		uW.menuIcon.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (i == null)
					return;
				i.click(new WSize(event.getClientY(), event.getClientX(), 0, 0));
			}
		});
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
		// proper way to replace widget
		// 2016/11/29 
		mainPa.clear();
		mainPa.add(w);
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
		return uW.panelWidget;
	}

	private void setMenuIcon(Widget w) {
		uW.htmlPanel.addAndReplaceElement(w, menuiconWidget);
		menuiconWidget = w.getElement();
	}

	/* Widget pa ignored */
	@Override
	public void setMenuPanel(Widget pa, IStatusMenuIcon i) {
		if (pa != null)
			setMenuIcon(pa == null ? new Label() : pa);
		else
			uW.menuIcon.setVisible(i != null);
		this.i = i;
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
