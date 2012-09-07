/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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
package com.ibm.gwthadr.client;

import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.ibm.gwthadr.client.ui.AddPerson;
import com.ibm.gwthadr.client.ui.ListPersonsW;
import com.ibm.gwthadr.shared.TOPerson;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GwtHadr implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	/**
	 * This is the entry point method.
	 */

	/** The main panel */
	private DockPanel dockPanel = new DockPanel();
	/** North element of dock panel containing the current status. */
	private Label statusLabel = new Label();
	/** Console panel having the command output */
	private DockPanel consolePanel = new DockPanel();
	/** North element of the consolePanel having the result of last command. */
	private Label lastResult = new Label();

	private void failureInfo(Throwable caught) {
		statusLabel.setText(SERVER_ERROR);
	}

	private abstract class HelpBack<T> implements AsyncCallback<T> {

		@Override
		public void onFailure(Throwable caught) {
			failureInfo(caught);
		}

	}

	private class CommonBack extends HelpBack {

		@Override
		public void onSuccess(Object result) {
			refreshStatus();
			drawLastResult();
			removeCenter();
		}

	}

	private void refreshStatus() {
		greetingService.getStatus(new HelpBack<String>() {

			@Override
			public void onSuccess(String result) {
				statusLabel.setText(result);
			}
		});
	}

	private void drawLastResult() {
		greetingService.gestLastResult(new HelpBack<String>() {

			@Override
			public void onSuccess(String result) {
				lastResult.setText(result);
			}
		});

	}

	private Widget centreW = null;

	private void removeCenter() {
		if (centreW != null) {
			consolePanel.remove(centreW);
		}
		centreW = null;
	}

	private void setCenter(Widget w) {
		removeCenter();
		consolePanel.add(w, DockPanel.CENTER);
		centreW = w;
	}

	private class SubmitPerson implements Command {

		private AddPerson addW;

		@Override
		public void execute() {
			TOPerson person = new TOPerson();
			person.setFamilyName(addW.getLastName());
			person.setName(addW.getFirstName());
			// validate
			String errInfo = null;
			if (person.getName().equals("")) {
				errInfo = "First name cannot be empty";
			} else if (person.getFamilyName().equals("")) {
				errInfo = "Last name cannot be empty";
			}
			addW.setErrorInfo(errInfo);
			if (errInfo != null) {
				return;
			}
			greetingService.addPerson(person, new CommonBack());
		}

	}

	private void addPerson() {
		SubmitPerson sub = new SubmitPerson();
		sub.addW = new AddPerson(sub);
		setCenter(sub.addW);
	}

	private class ListPersons implements AsyncCallback<List<TOPerson>> {

		@Override
		public void onFailure(Throwable caught) {
			failureInfo(caught);
		}

		@Override
		public void onSuccess(List<TOPerson> result) {
			ListPersonsW pList = new ListPersonsW();
			setCenter(pList);
			pList.drawList(result);
			refreshStatus();
			drawLastResult();
		}

	}

	public void onModuleLoad() {
		
		/** Main , creates the application GUI */

		RootPanel.get().add(dockPanel);
		VerticalPanel vp = new VerticalPanel();
		Button connectB = new Button("Connect");
		connectB.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				greetingService.connect(new CommonBack());
			}
		});
		Button disconnectB = new Button("Disconnect");
		disconnectB.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				greetingService.disconnect(new CommonBack());
			}
		});
		Button autocommitOn = new Button("Autocommit on");
		autocommitOn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				greetingService.setAutocommit(true, new CommonBack());
			}
		});
		Button autocommitOff = new Button("Autocommit off");
		autocommitOff.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				greetingService.setAutocommit(false, new CommonBack());
			}
		});
		Button createTable = new Button("Create table");
		createTable.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				greetingService.createTable(new CommonBack());
			}
		});
		Button dropTable = new Button("Drop table");
		dropTable.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				greetingService.dropTable(new CommonBack());
			}
		});
		Button addPersonB = new Button("Add person");
		addPersonB.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addPerson();
			}
		});

		Button listPerson = new Button("Show persons");
		listPerson.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				greetingService.getListOfPersons(new ListPersons());
			}
		});

		vp.add(connectB);
		vp.add(disconnectB);
		vp.add(autocommitOn);
		vp.add(autocommitOff);
		vp.add(createTable);
		vp.add(dropTable);
		vp.add(addPersonB);
		vp.add(listPerson);
		dockPanel.add(vp, DockPanel.WEST);
		dockPanel.add(statusLabel, DockPanel.NORTH);
		dockPanel.add(consolePanel, DockPanel.CENTER);
		consolePanel.add(lastResult, DockPanel.NORTH);
		refreshStatus();
	}

}
