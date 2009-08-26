/*
 * Copyright 2009 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.dialog.eadmin;

import java.util.ArrayList;

import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.mvc.auxabstract.LoginRecord;
import com.javahotel.client.mvc.crud.controler.ICrudControler;
import com.javahotel.client.mvc.dictcrud.controler.DictCrudControlerFactory;
import com.javahotel.client.mvc.dictcrud.controler.RecordAuxParam;
import com.javahotel.client.mvc.table.model.ITableConverter;
import com.javahotel.client.stackmenu.model.IStackButtonClick;
import com.javahotel.client.stackmenu.model.StackButtonElem;
import com.javahotel.client.stackmenu.model.StackButtonHeader;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.PersonP;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class AdminHotelFactory {

	private static class PersonConv implements ITableConverter {

		public AbstractTo convertA(AbstractTo a) {
			LoginRecord lo = new LoginRecord();
			PersonP pe = (PersonP) a;
			lo.setLogin(pe.getName());
			return lo;
		}

	}

	static private StackButtonElem getPanel(final IResLocator rI,
			final String bName, final RType r) {

		IStackButtonClick iClick = new IStackButtonClick() {

			private ICrudControler iCrud;

			public void beforeDrawAction() {
				RecordAuxParam aux = new RecordAuxParam();
				if (r == RType.AllPersons) {
					aux.setIConv(new PersonConv());
				}
				iCrud = DictCrudControlerFactory.getCrud(rI, new DictData(r),
						aux, null);
				iCrud.drawTable();
			}

			public IMvcWidget getMWidget() {
				return iCrud.getMWidget();
			}

			public void drawAction() {
				iCrud.drawTable();
			}
		};

		return new StackButtonElem(bName, iClick);
	}
	
	static private StackButtonElem getRemoveData(final IResLocator rI) {
		IStackButtonClick iClick = new IStackButtonClick() {
			
			ClearHotelDataWidget ha;

			public void beforeDrawAction() {
				ha = new ClearHotelDataWidget(rI);
			}

			public void drawAction() {
				ha.drawData();
			}

			public IMvcWidget getMWidget() {
				return ha.getW();
			}
		};
		
		return new StackButtonElem("Usuń dane",iClick);
	}

	static ArrayList<StackButtonHeader> getAList(IResLocator rI) {
		ArrayList<StackButtonHeader> hList = new ArrayList<StackButtonHeader>();
		ArrayList<StackButtonElem> aList = new ArrayList<StackButtonElem>();
		aList.add(getPanel(rI, "Osoby", RType.AllPersons));
		aList.add(getPanel(rI, "Hotele", RType.AllHotels));
		aList.add(getRemoveData(rI));
		hList.add(new StackButtonHeader("Admin", "people.gif", aList));
		return hList;

	}
}
