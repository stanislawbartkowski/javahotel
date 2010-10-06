/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.mvc.crudtable.controler;

import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.dialog.DictData.SpecE;
import com.javahotel.client.mvc.crud.controler.ITableModelSignalRead;
import com.javahotel.client.mvc.table.model.ITableModel;
import com.javahotel.client.mvc.table.view.ITableView;
import com.javahotel.client.mvc.table.view.TableViewFactory;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class CrudTableControler implements ICrudTableControler {

	@SuppressWarnings("unused")
	private final IResLocator rI;
	private final ITableModel tmodel;
	private final ITableView view;
	private final CrudTableControlerParam pa;

	CrudTableControler(final IResLocator rI, final DictData da,
			final ITableModel tmodel, final CrudTableControlerParam pa) {
		this.rI = rI;
		this.tmodel = tmodel;
		this.pa = pa;
		if (da.getSE() == SpecE.ResTablePanel) {
			view = TableViewFactory.getGridView(rI, da, pa.getCView(), tmodel, pa
					.getSc(), pa.getIB(), null);
		} else {
			view = TableViewFactory.getView(rI, da, pa.getCView(), tmodel, pa
					.getSc(), pa.getIB(), null);

		}
	}

	public ITableView getTableView() {
		return view;
	}

	public Widget getContrWidget() {
		return view.getContrWidget();
	}

	public IMvcWidget getMWidget() {
		return view.getMWidget();
	}

	private class AfterRead implements ITableModelSignalRead {

		public void successRead() {
			view.drawTable();
			if (pa.getSRead() != null) {
				pa.getSRead().successRead();
			}
		}
	}

	public void drawTable() {
		AfterRead ar = new AfterRead();
		if (pa.getIRead() != null) {
			pa.getIRead().readModel(tmodel, ar);
		} else {
			ar.successRead();
		}
	}
}
