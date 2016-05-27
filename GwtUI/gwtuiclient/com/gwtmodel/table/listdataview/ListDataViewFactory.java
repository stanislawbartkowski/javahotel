/*
 * Copyright 2016 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.listdataview;

import javax.inject.Inject;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.view.table.GwtTableFactory;
import com.gwtmodel.table.view.table.IGetCellValue;

public class ListDataViewFactory {

	private final GwtTableFactory gFactory;

	@Inject
	public ListDataViewFactory(GwtTableFactory gFactory) {
		this.gFactory = gFactory;
	}

	public IListDataView construct(IDataType dType) {
		return construct(dType, null, true, false, false, null);
	}

	public IListDataView construct(IDataType dType, IGetCellValue getCell, boolean selectedRow, boolean unSelectAtOnce,
			boolean async, String className) {
		return new ListDataView(gFactory, dType, getCell, selectedRow, unSelectAtOnce, async, className);
	}
}
