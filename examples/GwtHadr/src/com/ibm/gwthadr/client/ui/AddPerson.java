package com.ibm.gwthadr.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class AddPerson extends DialogBox {

	private static final Binder binder = GWT.create(Binder.class);
	@UiField
	TextBox firstName;
	@UiField
	TextBox familyName;
	@UiField
	Label errorLabel;
	@UiField
	Button button;
	private final Command submit;

	interface Binder extends UiBinder<Widget, AddPerson> {
	}

	public AddPerson(Command submit) {
		setWidget(binder.createAndBindUi(this));
		this.submit = submit;
	}

	@UiHandler("button")
	void onButtonClick(ClickEvent event) {
		submit.execute();
	}
	
	public String getFirstName() {
		return firstName.getText();
	}
	
	public String getLastName() {
		return familyName.getText();
	}
	
	public void setErrorInfo(String errInfo) {
		errorLabel.setText(errInfo);
	}
}
