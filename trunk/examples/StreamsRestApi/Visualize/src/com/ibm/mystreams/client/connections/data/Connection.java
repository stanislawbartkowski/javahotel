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
package com.ibm.mystreams.client.connections.data;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.ibm.mystreams.client.M;
import com.ibm.mystreams.client.database.IDatabase;
import com.ibm.mystreams.client.modal.ModalDialog.IModalDialog;
import com.ibm.mystreams.client.util.Util;
import com.ibm.mystreams.shared.ConnectionData;

public class Connection extends Composite {

	// callback for Accept button
	public interface IButtonAction {
		void click();
	}

	private final IButtonAction iButton;
	private final IModalDialog iClose;
	private final ConnectionData cData;
	private final IDatabase iData = M.getIbase();

	private static ConnectionUiBinder uiBinder = GWT
			.create(ConnectionUiBinder.class);
	@UiField
	Button button;

	@UiField
	TextBox host;
	@UiField
	IntegerBox port;
	@UiField
	TextBox user;
	@UiField
	TextBox password;
	@UiField
	Button button_1;
	@UiField
	Label error_label;
	@UiField
	Button button_2;

	interface ConnectionUiBinder extends UiBinder<Widget, Connection> {
	}

	private void setError(String errmess) {
		error_label.setText(errmess);
		error_label.setVisible(true);
	}

	public Connection(IModalDialog iClose, ConnectionData cData,
			IButtonAction iButton) {
		initWidget(uiBinder.createAndBindUi(this));
		this.cData = cData;
		this.iButton = iButton;
		this.iClose = iClose;
		host.setText(cData.getHost());
		port.setText(cData.getPort());
		user.setText(cData.getUser());
		password.setText(cData.getPassword());
		error_label.setVisible(false);
	}

	@UiHandler("button")
	void onButtonClick(ClickEvent event) {
		iClose.close();
	}

	private void setData(ConnectionData data) {
		data.setHost(host.getText());
		data.setPort(port.getText());
		data.setUser(user.getText());
		data.setPassword(password.getText());
	}

	private String validate() {
		TextBox[] tList = { host, user, password };
		String[] name = { "Host", "User", "Password" };
		for (int i = 0; i < tList.length; i++) {
			if (Strings.isNullOrEmpty(tList[i].getText()))
				return name[i] + " cannot be empty !";
		}
		if (Strings.isNullOrEmpty(port.getText()))
			return "Port - cannot be empty";
		ConnectionData data = new ConnectionData();
		setData(data);
		data.setId(cData.getId());
		if (iData.connectionExists(data))
			return "Connection (Host, Port, User) already exists !";
		return null;
	}

	@UiHandler("button_1")
	void onButton_1Click(ClickEvent event) {
		error_label.setVisible(false);
		String err_mess = validate();
		if (err_mess != null)
			setError(err_mess);
		else {
			error_label.setVisible(false);
			setData(cData);
			iButton.click();
		}
	}

	@UiHandler("button_2")
	void onButton_2Click(ClickEvent event) {
		ConnectionData data = new ConnectionData();
		setData(data);
		Util.testConnection(data);
	}
}
