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
package com.ibm.gwthadr.client.ui;

import java.util.List;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.ibm.gwthadr.shared.TOPerson;

public class ListPersonsW extends Composite {

	private static final Binder binder = GWT.create(Binder.class);
	@UiField(provided = true)
	CellTable<TOPerson> cellTable = new CellTable<TOPerson>();
	private ListDataProvider<TOPerson> dataProvider = new ListDataProvider<TOPerson>();

	interface Binder extends UiBinder<Widget, ListPersonsW> {
	}

	public ListPersonsW() {
		initWidget(binder.createAndBindUi(this));
	}

	public void drawList(List<TOPerson> pList) {
		Column<TOPerson, String> idNameColumn = new Column<TOPerson, String>(
				new TextCell()) {
			@Override
			public String getValue(TOPerson object) {
				return Integer.toString(object.getId());
			}
		};
		cellTable.addColumn(idNameColumn, "Id");
		Column<TOPerson, String> firstNameColumn = new Column<TOPerson, String>(
				new TextCell()) {
			@Override
			public String getValue(TOPerson object) {
				return object.getName();
			}
		};
		cellTable.addColumn(firstNameColumn, "First name");
		Column<TOPerson, String> lastNameColumn = new Column<TOPerson, String>(
				new TextCell()) {
			@Override
			public String getValue(TOPerson object) {
				return object.getFamilyName();
			}
		};
		cellTable.addColumn(lastNameColumn, "Family name");
		dataProvider.getList().addAll(pList);
		dataProvider.addDataDisplay(cellTable);
	}

}
