/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.dialog.user.booking;

import com.google.gwt.user.client.ui.Grid;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.dialog.DictData.SpecE;
import com.javahotel.common.command.SynchronizeList;
import com.javahotel.common.toobject.AbstractTo;
import java.util.Collection;
import com.javahotel.client.mvc.crudtable.controler.ICrudTableControler;
import com.javahotel.client.mvc.tablecrud.controler.TableDictCrudControlerFactory;
import com.javahotel.client.mvc.crud.controler.ITableModelSignalRead;
import com.javahotel.client.mvc.crudtable.controler.CrudTableControlerParam;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */

class ResRoomTable {

	private final IResLocator rI;
	private final ICrudTableControler iTable;
	private final SynchronizeList sSync;

	Grid getG() {
		return iTable.getTableView().getGrid();
	}

	int colS() {
		return iTable.getTableView().getModel().colNum();
	}

	Collection<? extends AbstractTo> getResList() {
		return iTable.getTableView().getModel().getList();
	}

	ResRoomTable(final IResLocator rI, final SynchronizeList sSyncc) {
		this.rI = rI;
		this.sSync = sSyncc;

		ITableModelSignalRead iSig = new ITableModelSignalRead() {

			public void successRead() {
				sSync.signalDone();
			}
		};
		CrudTableControlerParam pa = new CrudTableControlerParam();
		pa.setSRead(iSig);
		iTable = TableDictCrudControlerFactory.getCrud(rI, new DictData(
				SpecE.ResTablePanel), null, pa);
		iTable.drawTable();
	}
}
