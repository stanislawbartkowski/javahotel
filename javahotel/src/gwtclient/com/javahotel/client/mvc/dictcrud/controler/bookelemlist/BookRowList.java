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
package com.javahotel.client.mvc.dictcrud.controler.bookelemlist;

import java.util.ArrayList;
import java.util.List;

import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.dialog.DictData.SpecE;
import com.javahotel.client.mvc.crud.controler.ICrudAuxControler;
import com.javahotel.client.mvc.crudtable.controler.ICrudTableControler;
import com.javahotel.client.mvc.dictcrud.controler.RecordAuxParam;
import com.javahotel.client.mvc.tablecrud.controler.TableDictCrudControlerFactory;
import com.javahotel.common.toobject.PaymentRowP;
import com.javahotel.common.util.CollToArray;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class BookRowList implements ICrudAuxControler {

	@SuppressWarnings("unused")
	private final IResLocator rI;
	private final ICrudTableControler iC;

	BookRowList(IResLocator rI) {
		this.rI = rI;
		RecordAuxParam aux = new RecordAuxParam();
		aux.setModifPanel(false);

		iC = TableDictCrudControlerFactory.getCrud(rI, new DictData(
				SpecE.RowPaymentElem));
	}

	@SuppressWarnings("unchecked")
	void drawTable(List<PaymentRowP> col) {
		ArrayList<PaymentRowP> a = (ArrayList<PaymentRowP>) CollToArray
				.toA(col);
		iC.getTableView().getModel().setList(a);
		iC.drawTable();
	}

	public void show() {
	}

	public void hide() {
	}

	public IMvcWidget getMWidget() {
		return iC.getMWidget();
	}
}
