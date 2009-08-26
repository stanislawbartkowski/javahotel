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
package com.javahotel.client.mvc.dictcrud.controler;

import com.javahotel.client.mvc.recordviewdef.GetIToSFactory;
import com.javahotel.client.mvc.recordviewdef.ColListFactory;
import com.javahotel.client.mvc.dictcrud.controler.hotelperson.HotelPersonRoleView;
import com.javahotel.client.mvc.dictcrud.controler.season.SpecPeriod2View;
import com.javahotel.client.mvc.dictcrud.controler.season.SeasonOfferAuxPanel;
import com.javahotel.client.mvc.dictcrud.controler.priceoffer.PriceListAuxView;
import com.javahotel.client.mvc.recordviewdef.DictButtonFactory;
import com.javahotel.client.mvc.crud.controler.ICrudControler;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.mvc.contrpanel.model.IContrPanel;
import com.javahotel.client.mvc.contrpanel.view.IContrButtonView;
import com.javahotel.client.mvc.crud.controler.CrudControlerFactory;
import com.javahotel.client.mvc.crud.controler.ICrudAuxControler;
import com.javahotel.client.mvc.crud.controler.ICrudPersistSignal;
import com.javahotel.client.mvc.dictcrud.controler.booking.BookResRoom;
import com.javahotel.client.mvc.dictcrud.controler.customer.CustomerAuxPanel;
import com.javahotel.client.mvc.table.model.ColTitle;
import com.javahotel.client.mvc.table.model.ITableModel;
import com.javahotel.client.mvc.table.model.TableModelFactory;
import java.util.ArrayList;
import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.AbstractTo;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class DictCrudControlerFactory {

	private static void setAuxV(IResLocator rI, RecordAuxParam a, DictType d) {
		DictCheckView va = new DictCheckView(rI, d);
		a.setAuxV(va);
		a.setAuxO(va.getAuxO());
		a.setBSignal(va.getBefore());

	}

	private static RecordAuxParam getAuxV(final IResLocator rI,
			final DictData da, final ITableModel md, Object auxV1) {
		RecordAuxParam a = new RecordAuxParam();
		a.setAuxO1(auxV1);
		if (da.getD() != null) {
			switch (da.getD()) {
			case RoomStandard:
				setAuxV(rI, a, DictType.ServiceDict);
				break;
			case RoomObjects:
				setAuxV(rI, a, DictType.RoomFacility);
				break;
			case OffSeasonDict:
				a.setAuxV(new SpecPeriod2View(rI));
				a.setAuxO(a.getAuxV());
				SeasonOfferAuxPanel sAux = new SeasonOfferAuxPanel(rI);
				a.setAuxW(sAux.getMWidget().getWidget());
				a.setInfoP(sAux);
				break;
			case PriceListDict:
				PriceListAuxView au = new PriceListAuxView(rI);
				a.setAuxV(au);
				a.setAuxO(au.getAuxO());
				a.setModifD(au.getModif());
				a.setBSignal(au.getBSignal());
				break;
			case CustomerList:
				CustomerAuxPanel auC = new CustomerAuxPanel(rI);
				a.setAuxV(auC);
				a.setAuxO(auC.getV());
				break;
			case BookingList:
				BookResRoom bRoom = new BookResRoom(rI);
				a.setAuxV(bRoom);
				a.setModifD(bRoom.getModif());
				a.setAuxO1(bRoom.getBModel());
				a.setPSignal(bRoom.getPSignal());
				break;
			default:
			}
		} else if (da.getRt() != null) {
			switch (da.getRt()) {
			case AllPersons:
			case AllHotels:
				HotelPersonRoleView v = new HotelPersonRoleView(rI, da.getRt());
				a.setAuxV(v);
				a.setAuxO(v.getAuxO());
			}
		} else {
			switch (da.getSE()) {
			case SpecialPeriod:
				a.setAuxO(md);
				break;
			case CustomerPhone:
			case CustomerAccount:
				a.setAuxO(md);
				break;
			case BookingElem:
				a.setAuxO(md);
				break;
			// case BillsList:
			case AddPaymentList:
				a.setAuxO(md);
				break;
			}
		}
		return a;
	}

	public static ICrudControler getCrud(final IResLocator rI,
			final DictData da, RecordAuxParam aux, ICrudAuxControler cAux) {
		ArrayList<ColTitle> cTitle = ColListFactory.getColList(da);
		AbstractTo.IFieldToS iS = GetIToSFactory.getI(rI);
		String header = ColListFactory.getHeader(da);
		ITableModel model = TableModelFactory.getModel(rI, cTitle, iS, header,
				aux.getIFilter(), aux.getIConv());
		RecordAuxParam auxV = getAuxV(rI, da, model, aux.getAuxO1());
		if (aux.getModifD() != null) {
			auxV.setModifD(aux.getModifD());
		}
		if (aux.getPSignal() != null) {
			auxV.setPSignal(aux.getPSignal());
		}
		if (aux.getSClicked() != null) {
			auxV.setSClicked(aux.getSClicked());
		}
		IContrPanel cpanel = null;
		if (aux.isModifPanel()) {
			cpanel = DictButtonFactory.getDictButt(rI);
		}
		if (aux.getCPanel() != null) {
			cpanel = aux.getCPanel();
		}
		if (aux.getIChoose() != null) {
			auxV.setIChoose(aux.getIChoose());
		}
		if (auxV.getIChoose() != null) {
			cpanel = DictButtonFactory.getDictChooseButt(rI);
		}
		auxV.setModifPanel(aux.isModifPanel());
		if (auxV.getBSignal() == null) {
			auxV.setBSignal(aux.getBSignal());
		}
		if (auxV.getAuxV() == null) {
			auxV.setAuxV(aux.getAuxV());
		}
		if (aux.getIConv() != null) {
			auxV.setIConv(aux.getIConv());
		}

		ICrudControler crud = CrudControlerFactory.getCrud(rI, da, model,
				cpanel, new DictRecordControler(rI, da, auxV), cAux, aux
						.getIClick());
		return crud;
	}

	public static ICrudControler getCrud(final IResLocator rI,
			final DictData da, final Object auxV1, IModifRecordDef mDef,
			ICrudAuxControler cAux, ICrudPersistSignal pSignal) {
		RecordAuxParam aux = new RecordAuxParam();
		aux.setAuxO1(auxV1);
		aux.setModifD(mDef);
		aux.setPSignal(pSignal);
		return getCrud(rI, da, aux, cAux);
	}

	public static ICrudControler getCrud(final IResLocator rI, final DictData da) {
		return getCrud(rI, da, new RecordAuxParam(), null);
	}

	public static ICrudControler getCrud(final IResLocator rI,
			final DictData da, RecordAuxParam param) {
		return getCrud(rI, da, param, null);
	}

	public static ICrudControlerDialog getCrudD(final IResLocator rI,
			final DictData da, RecordAuxParam aux, ICrudAuxControler cAux,
			IContrButtonView bView) {
		ICrudControler iC = getCrud(rI, da, aux, cAux);
		return new CrudControlerDialog(iC, bView);
	}

}
