/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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
package com.javahotel.nmvc.factories.impl;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.factories.IFormDefFactory;
import com.gwtmodel.table.factories.IFormTitleFactory;
import com.gwtmodel.table.login.LoginField;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.view.ewidget.EditWidgetFactory;
import com.javahotel.client.IResLocator;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.AdvancePaymentP;
import com.javahotel.common.toobject.BookRecordP;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.OfferSeasonP;
import com.javahotel.common.toobject.ResObjectP;
import com.javahotel.common.toobject.ServiceDictionaryP;
import com.javahotel.common.toobject.VatDictionaryP;
import com.javahotel.nmvc.common.DataType;
import com.javahotel.nmvc.common.VField;
import com.javahotel.nmvc.ewidget.EWidgetFactory;

public class RecordFormDefFactory implements IFormTitleFactory, IFormDefFactory {

	private final EditWidgetFactory eFactory;
	private final EWidgetFactory heFactory;
	private final IResLocator rI;

	@Inject
	public RecordFormDefFactory(IResLocator rI, EditWidgetFactory eFactory,
			EWidgetFactory heFactory) {
		this.rI = rI;
		this.eFactory = eFactory;
		this.heFactory = heFactory;
	}

	private List<FormField> getDict(boolean symchecked) {
		List<FormField> fList = new ArrayList<FormField>();
		IFormLineView name;
		IVField vSymbol = new VField(DictionaryP.F.name);
		IVField vNazwa = new VField(DictionaryP.F.description);
		if (symchecked) {
			name = eFactory.constructTextCheckEdit(vSymbol, true);
		} else {
			name = eFactory.constructTextField(vSymbol);
		}
		IFormLineView descr = eFactory.constructTextField(vNazwa);
		fList.add(new FormField("Symbol", name, vSymbol, true));
		fList.add(new FormField("Nazwa", descr, vNazwa));
		return fList;
	}

	@Override
	public String getFormTitle(IDataType dType) {
		DataType dd = (DataType) dType;
		DictType d = dd.getdType();
		switch (d) {
		case CustomerList:
			return "Kontrahent";
		case OffSeasonDict:
			return "Sezony";
		}
		return null;

	}

	@Override
	public FormLineContainer construct(IDataType dType) {
		DataType dd = (DataType) dType;
		List<FormField> fList = new ArrayList<FormField>();
		if (dd.isRType()) {
			switch (dd.getrType()) {
			case AllPersons:
				IVField passwordP = new LoginField(LoginField.F.PASSWORD);
				IVField repasswordP = new LoginField(LoginField.F.REPASSWORD);
				IFormLineView password = eFactory
						.constructPasswordField(passwordP);
				IFormLineView repassword = eFactory
						.constructPasswordField(repasswordP);
				fList.add(new FormField("Symbol", null, new LoginField(
						LoginField.F.LOGINNAME), true));
				fList.add(new FormField("Hasło", password, passwordP));
				fList.add(new FormField("Hasło do sprawdzenia", repassword,
						repasswordP));
				return new FormLineContainer(fList);
			}
			return null;
		}

		CommandParam p;

		if (dd.isAddType()) {
			switch (dd.getAddType()) {
			case BookRecord:
				p = rI.getR().getHotelCommandParam();
				fList.add(new FormField("Suma", null, new VField(
						BookRecordP.F.customerPrice)));
				p.setDict(DictType.PriceListDict);
				IVField priceV = new VField(BookRecordP.F.oPrice);
				IFormLineView oPrice = heFactory.getListValuesBox(priceV, p);
				fList.add(new FormField("Cennik", oPrice, priceV));
				break;
			case AdvanceHeader:
				fList.add(new FormField("Zaliczka", null, new VField(
						AdvancePaymentP.F.amount)));
				fList.add(new FormField("Zapłacić do", null, new VField(
						AdvancePaymentP.F.validationDate)));
				break;
			}
		}
		if (dd.isDictType()) {
			DictType d = dd.getdType();
			switch (d) {
			case VatDict:
				fList = getDict(false);
				fList.add(new FormField("Procent", null, new VField(
						VatDictionaryP.F.vat)));
				break;

			case ServiceDict:
				fList = getDict(false);
				p = rI.getR().getHotelCommandParam();
				p.setDict(DictType.VatDict);
				IVField vatV = new VField(ServiceDictionaryP.F.vat);
				IFormLineView vatB = heFactory.getListValuesBox(vatV, p);
				fList.add(new FormField("Vat", vatB, vatV));
				IVField servT = new VField(ServiceDictionaryP.F.servtype);
				fList.add(new FormField("Typ", null, servT));
				break;

			case RoomObjects:
				fList = getDict(false);
				p = rI.getR().getHotelCommandParam();
				p.setDict(DictType.RoomStandard);
				IVField stanV = new VField(ResObjectP.F.standard);
				IFormLineView standardB = heFactory.getListValuesBox(stanV, p);
				fList.add(new FormField("Standard", standardB, stanV));
				fList.add(new FormField("Liczba osób", null, new VField(
						ResObjectP.F.maxperson)));
				break;

			case OffSeasonDict:
				fList = getDict(false);
				fList.add(new FormField("Od", null, new VField(
						OfferSeasonP.F.startp)));
				fList.add(new FormField("Do", null, new VField(
						OfferSeasonP.F.endp)));
				break;

			case CustomerList:
				fList = getDict(true);
				fList.add(new FormField("Nazwa 1", null, new VField(
						CustomerP.F.name1)));
				fList.add(new FormField("Nazwa 2", null, new VField(
						CustomerP.F.name2)));
				fList.add(new FormField("Rodzaj", null, new VField(
						CustomerP.F.cType)));

				fList.add(new FormField("Pan/Pani", null, new VField(
						CustomerP.F.pTitle)));
				fList.add(new FormField("Imię", null, new VField(
						CustomerP.F.firstName)));
				fList.add(new FormField("Nazwisko", null, new VField(
						CustomerP.F.lastName)));
				fList.add(new FormField("PESEL", null, new VField(
						CustomerP.F.PESEL)));
				fList.add(new FormField("Rodzaj dokumentu", null, new VField(
						CustomerP.F.docType)));
				fList.add(new FormField("Numer dokument", null, new VField(
						CustomerP.F.docNumber)));

				fList.add(new FormField("Kraj", null, new VField(
						CustomerP.F.country)));
				fList.add(new FormField("Kod pocztowy", null, new VField(
						CustomerP.F.zipCode)));
				fList.add(new FormField("Miasto", null, new VField(
						CustomerP.F.city)));
				fList.add(new FormField("Adres 1", null, new VField(
						CustomerP.F.address1)));
				fList.add(new FormField("Adres 2", null, new VField(
						CustomerP.F.address2)));

				break;
			default:
				return null;
			}
		}
		return new FormLineContainer(fList);
	}
}
