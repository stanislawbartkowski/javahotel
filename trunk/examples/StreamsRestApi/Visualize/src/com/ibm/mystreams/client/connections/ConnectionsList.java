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
package com.ibm.mystreams.client.connections;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.ibm.mystreams.client.M;
import com.ibm.mystreams.client.connections.data.Connection;
import com.ibm.mystreams.client.connections.data.Connection.IButtonAction;
import com.ibm.mystreams.client.database.IDatabase;
import com.ibm.mystreams.client.database.IDatabase.OP;
import com.ibm.mystreams.client.modal.ModalDialog;
import com.ibm.mystreams.client.modal.ModalDialog.IModalDialog;
import com.ibm.mystreams.client.util.Util;
import com.ibm.mystreams.shared.ConnectionData;

public class ConnectionsList extends Composite {

	private static ConnectionsListUiBinder uiBinder = GWT
			.create(ConnectionsListUiBinder.class);
	@UiField(provided = true)
	CellTable<ConnectionData> cellTable = new CellTable<ConnectionData>();
	@UiField
	Button button;
	@UiField
	Button button_1;

	private final IDatabase iData;
	private final List<ConnectionData> cList = new ArrayList<ConnectionData>();

	interface ConnectionsListUiBinder extends UiBinder<Widget, ConnectionsList> {
	}

	private final IModalDialog iClose;

	private void setTable() {
		List<ConnectionData> aList = iData.getList();
		cList.clear();
		cList.addAll(aList);
		cellTable.setRowCount(cList.size(), true);
		cellTable.setRowData(0, cList);
	}

	public ConnectionsList(IModalDialog iClose) {
		initWidget(uiBinder.createAndBindUi(this));
		this.iData = M.getIbase();
		this.iClose = iClose;
		TextColumn<ConnectionData> hostC = new TextColumn<ConnectionData>() {

			@Override
			public String getValue(ConnectionData object) {
				return object.getHost();
			}
		};
		cellTable.addColumn(hostC, M.L().hostLabel());
		TextColumn<ConnectionData> portC = new TextColumn<ConnectionData>() {

			@Override
			public String getValue(ConnectionData object) {
				return object.getPort();
			}
		};
		cellTable.addColumn(portC, M.L().portLabel());
		TextColumn<ConnectionData> userC = new TextColumn<ConnectionData>() {

			@Override
			public String getValue(ConnectionData object) {
				return object.getUser();
			}
		};
		cellTable.addColumn(userC, M.L().userLabel());

		ButtonCell buttonCell = new ButtonCell();
		Column<ConnectionData, String> buttonColumn = new Column<ConnectionData, String>(
				buttonCell) {
			@Override
			public String getValue(ConnectionData object) {
				return M.L().removeButton();
			}
		};
		buttonColumn
				.setFieldUpdater(new FieldUpdater<ConnectionData, String>() {
					public void update(int index, ConnectionData object,
							String value) {
						// Value is the button value. Object is the row object.
						if (Window.confirm(M.L().removeQuestion())) {
							iData.databaseOp(OP.REMOVE, object);
							setTable();
							cellTable.redraw();
						}
					}
				});
		cellTable.addColumn(buttonColumn);

		ButtonCell button1Cell = new ButtonCell();
		Column<ConnectionData, String> button1Column = new Column<ConnectionData, String>(
				button1Cell) {
			@Override
			public String getValue(ConnectionData object) {
				return M.L().changeButton();
			}
		};
		button1Column
				.setFieldUpdater(new FieldUpdater<ConnectionData, String>() {
					public void update(int index, final ConnectionData object,
							String value) {
						final IModalDialog iDial = ModalDialog.construct();
						IButtonAction action = new IButtonAction() {

							@Override
							public void click() {
								if (Window.confirm(M.L().changeQuestion())) {
									iData.databaseOp(OP.CHANGE, object);
									iDial.close();
									setTable();
									cellTable.redraw();
								}
							}
						};
						Connection con = new Connection(iDial, object, action);
						iDial.show(con, M.L().connectionLabel());

					}
				});
		cellTable.addColumn(button1Column);

		ButtonCell button2Cell = new ButtonCell();
		Column<ConnectionData, String> button2Column = new Column<ConnectionData, String>(
				button2Cell) {
			@Override
			public String getValue(ConnectionData object) {
				return M.L().testButton();
			}
		};
		button2Column
				.setFieldUpdater(new FieldUpdater<ConnectionData, String>() {
					public void update(int index, final ConnectionData object,
							String value) {
						Util.testConnection(object);
					}
				});
		cellTable.addColumn(button2Column);

		setTable();
		cellTable.setRowData(0, cList);
	}

	@UiHandler("button")
	void onButtonClick(ClickEvent event) {
		M.getiFrame().refreshListOfHosts();
		iClose.close();
	}

	@UiHandler("button_1")
	void onButton_1Click(ClickEvent event) {
		final IModalDialog iDial = ModalDialog.construct();
		final ConnectionData data = new ConnectionData();
		data.setHost("localhost");
		data.setPort("8443");
		data.setUser("streams");
		data.setPassword("change me");
		IButtonAction action = new IButtonAction() {

			@Override
			public void click() {
				iData.databaseOp(OP.ADD, data);
				iDial.close();
				setTable();
				cellTable.redraw();
			}

		};
		Connection con = new Connection(iDial, data, action);
		iDial.show(con, M.L().connectionLabel());
	}
}
