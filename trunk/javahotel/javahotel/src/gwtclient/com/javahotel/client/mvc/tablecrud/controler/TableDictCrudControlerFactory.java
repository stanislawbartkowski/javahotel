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
package com.javahotel.client.mvc.tablecrud.controler;

import java.util.ArrayList;

import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.mvc.crud.controler.ICrudReadModel;
import com.javahotel.client.mvc.crudtable.controler.CrudTableControlerFactory;
import com.javahotel.client.mvc.crudtable.controler.CrudTableControlerParam;
import com.javahotel.client.mvc.crudtable.controler.ICrudTableControler;
import com.javahotel.client.mvc.dictcrud.read.CrudReadModelFactory;
import com.javahotel.client.mvc.dictcrud.read.ICrudReadDictModel;
import com.javahotel.client.mvc.dictcrud.tablecallback.CallBackTableFactory;
import com.javahotel.client.mvc.recordviewdef.ColListFactory;
import com.javahotel.client.mvc.recordviewdef.GetIToSFactory;
import com.javahotel.client.mvc.table.model.ColTitle;
import com.javahotel.client.mvc.table.model.ITableModel;
import com.javahotel.client.mvc.table.model.TableModelFactory;
import com.javahotel.client.mvc.table.view.ITableCallBackSetField;
import com.javahotel.common.toobject.AbstractTo;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class TableDictCrudControlerFactory {

	public static ICrudTableControler getCrud(final IResLocator rI,
			final DictData da, final Object dmodel,
			final CrudTableControlerParam ic) {
		ArrayList<ColTitle> cTitle = ColListFactory.getColList(da);
		AbstractTo.IFieldToS iS = GetIToSFactory.getI(rI);
		String header = ColListFactory.getHeader(da);
		ITableModel model = TableModelFactory.getModel(rI, cTitle, iS, header);
		CrudTableControlerParam pa = new CrudTableControlerParam();
		if (ic != null) {
			if (ic.getIB() != null) {
				pa.setIB(ic.getIB());
			}
			if (ic.getIRead() != null) {
				pa.setIRead(ic.getIRead());
			}
			if (ic.getSRead() != null) {
				pa.setSRead(ic.getSRead());
			}
			if (ic.getSc() != null) {
				pa.setSc(ic.getSc());
			}
		}
		if (pa.getIB() == null) {
			ITableCallBackSetField iB = CallBackTableFactory.getBack(rI, da);
			pa.setIB(iB);
		}
		if (pa.getIRead() == null) {
			ICrudReadModel iRead = CrudReadModelFactory.getRead(rI, da,
					(ICrudReadDictModel) dmodel);
			pa.setIRead(iRead);
		}
		ICrudTableControler i = CrudTableControlerFactory.getCrudTable(rI, da,
				model, pa);
		return i;
	}

	public static ICrudTableControler getCrud(final IResLocator rI,
			final DictData da) {
		return getCrud(rI, da, null, null);
	}
}
