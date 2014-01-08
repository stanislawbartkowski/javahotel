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
package com.ibm.mystreams.client.mainframe;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widget.client.TextButton;
import com.ibm.mystreams.client.IMainFrame;
import com.ibm.mystreams.client.M;
import com.ibm.mystreams.client.database.IDatabase;
import com.ibm.mystreams.client.util.Util;
import com.ibm.mystreams.shared.ConnectionData;

public class MainFrame extends Composite {

	private static MainFrameUiBinder uiBinder = GWT
			.create(MainFrameUiBinder.class);
	@UiField
	TextButton textButton;

	@UiField
	Label errLabel;
	@UiField
	ListBox listOfHosts;
	@UiField
	ListBox listOfInstance;
	@UiField
	Button button;
	@UiField
	Grid centrePanel;

	private final IClickNewConnection iClick;
	private final IClickGoButton iGo;

	private final IDatabase iData = M.getIbase();

	private final String CURRENTCON = "Current Connection";

	interface MainFrameUiBinder extends UiBinder<Widget, MainFrame> {
	}

	private void setListOfHosts() {
		List<ConnectionData> cList = iData.getList();
		listOfHosts.clear();
		String val = Util.getCookie(CURRENTCON);
		int i = 0;
		for (ConnectionData da : cList) {
			String s = iData.toS(da);
			listOfHosts.addItem(s);
			if (val != null && val.equals(s)) {
				listOfHosts.setSelectedIndex(i);
			}
			i++;
		}
	}

	private String getCurrentInstance() {
		return listOfHosts.getItemText(listOfHosts.getSelectedIndex());
	}

	private void setListOfInstances() {
		listOfInstance.clear();
		if (listOfHosts.getItemCount() == 0)
			return;
		List<String> lOfInstance = Util.getInstances(getCurrentInstance());
		String currI = Util.getCurrentInstance(getCurrentInstance());
		int i = 0;
		for (String s : lOfInstance) {
			listOfInstance.addItem(s);
			if (currI != null && currI.equals(s))
				listOfInstance.setSelectedIndex(i);
		}
	}

	private class MFrame implements IMainFrame {

		@Override
		public void setStatus(String mess) {
			if (mess == null)
				errLabel.setVisible(false);
			else {
				errLabel.setVisible(true);
				errLabel.setText(mess);
			}

		}

		@Override
		public void setError(String err) {
			setStatus(err);
		}

		@Override
		public void refreshListOfHosts() {
			setListOfHosts();
		}

		@Override
		public void refreshListOfInstances() {
			setListOfInstances();
		}

		@Override
		public void setCentre(POS pos, Widget w) {
			switch (pos) {
			case UPLEFT:
				centrePanel.setWidget(0, 0, w);
				break;
			case UPRIGHT:
				centrePanel.setWidget(0, 1, w);
				break;
			case DOWN:
				centrePanel.setWidget(1, 0, w);
				break;
			}

		}

	}

	public MainFrame(IClickNewConnection iClick, IClickGoButton iGo) {
		initWidget(uiBinder.createAndBindUi(this));
		this.iClick = iClick;
		this.iGo = iGo;
		M.setiFrame(new MFrame());
		setListOfHosts();
		setListOfInstances();
		centrePanel.resize(2, 2);
	}

	@UiHandler("textButton")
	void onTextButtonClick(ClickEvent event) {
		iClick.click();
	}

	@UiHandler("listOfHosts")
	void onListOfHostsChange(ChangeEvent event) {
		Util.setCookie(CURRENTCON, getCurrentInstance());
		setListOfInstances();
	}

	@UiHandler("button")
	void onButtonClick(ClickEvent event) {
		if (listOfHosts.getItemCount() == 0) {
			Window.alert("No connection is defined !");
			return;
		}
		String s = getCurrentInstance();
		ConnectionData data = iData.findS(s);
		iGo.go(data);
	}

	@UiHandler("listOfInstance")
	void onListOfInstanceClick(ClickEvent event) {
		String instance = listOfInstance.getItemText(listOfInstance
				.getSelectedIndex());
		Util.setCurrentInstance(getCurrentInstance(), instance);
	}
}
