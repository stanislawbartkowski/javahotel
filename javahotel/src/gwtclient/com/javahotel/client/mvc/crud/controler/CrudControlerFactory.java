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
package com.javahotel.client.mvc.crud.controler;

import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.mvc.contrpanel.model.IContrPanel;
import com.javahotel.client.mvc.contrpanel.view.IControlClick;
import com.javahotel.client.mvc.table.model.ITableModel;

/**
 * 
 * @author hotel
 */
public class CrudControlerFactory {

	private CrudControlerFactory() {
	}

	public static ICrudControler getCrud(final IResLocator rI, DictData da,
			final ITableModel tmodel, final IContrPanel panel,
			final ICrudRecordFactory fa, IControlClick click) {
		return new CrudControler(rI, da, tmodel, panel, fa, null, click);
	}

	public static ICrudControler getCrud(final IResLocator rI, DictData da,
			final ITableModel tmodel, final IContrPanel panel,
			final ICrudRecordFactory fa, final ICrudAuxControler cAux,
			IControlClick click) {
		return new CrudControler(rI, da, tmodel, panel, fa, cAux, click);
	}
}
