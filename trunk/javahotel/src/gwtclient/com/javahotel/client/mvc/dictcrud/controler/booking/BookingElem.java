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
package com.javahotel.client.mvc.dictcrud.controler.booking;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.dialog.IMvcView;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.dialog.IPersistAction;
import com.javahotel.client.dialog.DictData.SpecE;
import com.javahotel.client.idialog.GetIEditFactory;
import com.javahotel.client.ifield.IChangeListener;
import com.javahotel.client.ifield.ILineField;
import com.javahotel.client.listofserv.ListOfServices;
import com.javahotel.client.mvc.auxabstract.ANumAbstractTo;
import com.javahotel.client.mvc.crud.controler.ICrudControler;
import com.javahotel.client.mvc.crud.controler.ICrudPersistSignal;
import com.javahotel.client.mvc.dictcrud.controler.DictCrudControlerFactory;
import com.javahotel.client.mvc.dictcrud.controler.DictUtil;
import com.javahotel.client.mvc.dictcrud.controler.IModifRecordDef;
import com.javahotel.client.mvc.dictcrud.controler.RecordAuxParam;
import com.javahotel.client.mvc.persistrecord.IPersistResult.PersistResultContext;
import com.javahotel.client.mvc.record.model.RecordField;
import com.javahotel.client.mvc.table.model.ITableModel;
import com.javahotel.client.mvc.table.view.ITableSignalClicked;
import com.javahotel.client.mvc.table.view.ITableView;
import com.javahotel.client.paymentdata.PaymentData;
import com.javahotel.client.paymentdata.PaymentData.ISetPaymentRows;
import com.javahotel.client.rdata.RData.IVectorList;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookRecordP;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.PaymentRowP;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class BookingElem implements IMvcView {

	private final IResLocator rI;
	private final ICrudControler cI;
	private final ListOfServices lO;
	private ILineField service;
	private ILineField resObject;
	private final PaymentData pa;
	private final IGetELineDialog eseason;
	private final IGetELineDialog eprice;
	private final IGetELineDialog psum;
	private final BookRowList bList;

	public IMvcWidget getMWidget() {
		return cI.getMWidget();
	}

	private class SetList implements IVectorList {

		public void doVList(final ArrayList<? extends AbstractTo> val) {
			GetIEditFactory.AddValues(rI, service, DictionaryP.F.name, val);
		}
	}

	private class C implements IChangeListener {

		public void onChange(final ILineField arg0) {
			lO.getServices(resObject.getVal());
		}
	}

	private class MO implements IModifRecordDef {

		public void modifRecordDef(ArrayList<RecordField> dict) {
			for (RecordField re : dict) {
				if (re.getFie() == BookElemP.F.resObject) {
					resObject = re.getELine();
					resObject.setChangeListener(new C());
				}
				if (re.getFie() == BookElemP.F.service) {
					service = re.getELine();
				}
			}
		}
	}

	private class SetRows implements ISetPaymentRows {

		private final BookElemP b;

		SetRows(BookElemP b) {
			this.b = b;
		}

		public void setRow(List<PaymentRowP> col) {
			b.setPaymentrows(col);
			SumPayment.SumRes pRes = SumPayment.sum(cI.getTableView()
					.getModel());
			psum.getE().setDecimal(pRes.sumOffer);
		}
	}

	private class PersistR implements ICrudPersistSignal {

		public void signal(PersistResultContext re) {
			if (re.getAction() == IPersistAction.DELACTION) {
				return;
			}
			ANumAbstractTo<?> a = (ANumAbstractTo<?>) re.getA();
			BookElemP b = (BookElemP) a.getO();
			Date dFrom = b.getCheckIn();
			Date dTo = b.getCheckOut();
			String service = b.getService();
			String season = eseason.getE().getVal();
			String sprice = eprice.getE().getVal();
			pa.getRows(season, sprice, dFrom, dTo, service, new SetRows(b));
		}
	}

	class SetSignal implements ITableSignalClicked {

		public void signal(ClickedContext co) {
			ITableView mo = cI.getTableView();
			AbstractTo re = mo.getClicked();
			List<PaymentRowP> col;
			if (re == null) {
				col = new ArrayList<PaymentRowP>();
			} else {
				ANumAbstractTo<?> a = (ANumAbstractTo<?>) re;
				BookElemP b = (BookElemP) a.getO();
				col = b.getPaymentrows();
			}
			bList.drawTable(col);
		}
	}

	BookingElem(IResLocator rI, IGetELineDialog eseason,
			IGetELineDialog eprice, IGetELineDialog psum) {
		this.rI = rI;
		this.eseason = eseason;
		this.eprice = eprice;
		this.psum = psum;
		lO = new ListOfServices(rI, new SetList());
		pa = new PaymentData(rI);
		RecordAuxParam aux = new RecordAuxParam();
		aux.setModifD(new MO());
		aux.setPSignal(new PersistR());
		aux.setSClicked(new SetSignal());
		bList = new BookRowList(rI);
		cI = DictCrudControlerFactory.getCrud(rI, new DictData(
				SpecE.BookingElem), aux, bList);
	}

	void setFields(BookRecordP b) {
		List<BookElemP> col = b.getBooklist();
		if (col == null) {
			col = new ArrayList<BookElemP>();
		}
		ITableModel mo = cI.getTableView().getModel();
		DictUtil.setList(mo, col);
	}

	List<BookElemP> extractFields() {
		List<BookElemP> col = new ArrayList<BookElemP>();
		ITableModel mo = cI.getTableView().getModel();
		DictUtil.readList(mo, col);
		return col;
	}

	public void show() {
		cI.drawTable();
	}

	public void hide() {
	}
}
