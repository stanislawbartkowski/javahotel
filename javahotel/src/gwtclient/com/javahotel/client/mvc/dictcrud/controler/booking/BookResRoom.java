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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DefaultMvcWidget;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.dialog.IPersistAction;
import com.javahotel.client.dialog.DictData.SpecE;
import com.javahotel.client.ifield.IChangeListener;
import com.javahotel.client.ifield.ILineField;
import com.javahotel.client.mvc.controller.onerecord.RecordFa;
import com.javahotel.client.mvc.controller.onerecord.RecordFaParam;
import com.javahotel.client.mvc.crud.controler.ICrudPersistSignal;
import com.javahotel.client.mvc.crud.controler.PersistCrudContext;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.dictcrud.controler.AbstractAuxRecordPanel;
import com.javahotel.client.mvc.dictcrud.controler.IModifRecordDef;
import com.javahotel.client.mvc.dictdata.model.IBookingModel;
import com.javahotel.client.mvc.dictdata.model.IOneRecordModel;
import com.javahotel.client.mvc.persistrecord.IPersistResult.PersistResultContext;
import com.javahotel.client.mvc.util.GetFieldModif;
import com.javahotel.client.mvc.validator.IErrorMessage;
import com.javahotel.client.mvc.validator.IErrorMessageContext;
import com.javahotel.client.mvc.validator.IRecordValidator;
import com.javahotel.client.mvc.validator.ISignalValidate;
import com.javahotel.client.mvc.validator.MultiSignalValidator;
import com.javahotel.client.param.ConfigParam;
import com.javahotel.client.rdata.RData;
import com.javahotel.common.command.BookingStateType;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.AdvancePaymentP;
import com.javahotel.common.toobject.BillP;
import com.javahotel.common.toobject.BookRecordP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.BookingStateP;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.util.BillUtil;
import com.javahotel.common.util.GetMaxUtil;
import com.javahotel.types.LId;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class BookResRoom extends AbstractAuxRecordPanel {

	private final IResLocator rI;
	private final VerticalPanel vp = new VerticalPanel();
	private final RecordFa cust;
	private final RecordFa bRec;
	private final RecordFa vRec;
	private final BookingElem bElem;
	private final GetFieldModif mod;
	private final GetFieldModif pAmount;

	public IModifRecordDef getModif() {
		return mod.getModif();
	}

	public IMvcWidget getMWidget() {
		return new DefaultMvcWidget(vp);
	}

	private class CountAdvance implements IChangeListener {

		public void onChange(final ILineField arg0) {
			GetFieldModif mA = vRec.getModif(0);
			BigDecimal c = pAmount.getE().getE().getDecimal();
			BigDecimal c1 = ConfigParam.coundAdvancePay(c);
			mA.getE().getE().setDecimal(c1);
		}
	}

	public IBookingModel getBModel() {
		return new IBookingModel() {

			public CustomerP getBookCustomer() {
				CustomerP p = cust.getExtractFields();
				return p;
			}

			public IOneRecordModel getCustomerModel() {
				return cust;
			}
		};
	}

	public ICrudPersistSignal getPSignal() {
		return new PersistBooking();
	}

	private class PersistBooking implements ICrudPersistSignal {

		public void signal(PersistResultContext re) {
			if (re.getAction() == IPersistAction.DELACTION) {
				return;
			}
			String na = re.getRet().getIdName();
			if (na == null) {
				PersistCrudContext pC = (PersistCrudContext) re.getAuxContext();
				ConflictInfo i = new ConflictInfo(rI, re.getRet());
				i.show(pC.getWDialog());
				pC.setStayDialog(true);
			} else {
				Window.alert(na);
			}
		}
	}

	public BookResRoom(IResLocator rI) {
		this.rI = rI;
		mod = new GetFieldModif(BookingP.F.season);
		RecordFaParam pa = new RecordFaParam();
		pa.setNewchoosetag(true);
		cust = new RecordFa(rI, new DictData(DictType.CustomerList), pa);
		bRec = new RecordFa(rI, new DictData(SpecE.BookingHeader), null,
				BookRecordP.F.oPrice, BookRecordP.F.customerPrice);
		pAmount = bRec.getModif(1);
		pAmount.setChangeL(new CountAdvance());
		vRec = new RecordFa(rI, new DictData(SpecE.ValidationHeader), null,
				AdvancePaymentP.F.amount);
		bElem = new BookingElem(rI, mod.getE(), bRec.getModif(0).getE(),
				pAmount.getE());
		vp.add(cust.getWidget());
		vp.add(bRec.getWidget());
		vp.add(vRec.getWidget());
		vp.add(bElem.getMWidget().getWidget());
	}

	private class EContext implements IErrorMessageContext {
		private final RecordFa fa;

		EContext(RecordFa fa) {
			this.fa = fa;
		}
	}

	private class ValidateV implements IRecordValidator {

		public void validateS(int action, RecordModel a, ISignalValidate sig) {
			MultiSignalValidator msig = new MultiSignalValidator(sig);
			IRecordValidator va = cust.getValidator();
			va.setErrContext(new EContext(cust));
			IBookingModel i = getBModel();
			CustomerP p = i.getBookCustomer();
			RecordModel mo = cust.getModel();
			mo.setA(p);
			va.validateS(action, mo, msig);

			IRecordValidator v1 = bRec.getValidator();
			v1.setErrContext(new EContext(bRec));
			RecordModel mo1 = bRec.getModel();
			v1.validateS(action, mo1, msig);

			IRecordValidator v2 = vRec.getValidator();
			v2.setErrContext(new EContext(vRec));
			RecordModel mo2 = vRec.getModel();
			v2.validateS(action, mo2, msig);

			msig.conclude();
		}

		public boolean isEmpty(RecordModel a) {
			return false;
		}

		public void setErrContext(IErrorMessageContext co) {
		}
	}

	@Override
	public IRecordValidator getValidator() {
		return new ValidateV();
	}

	@Override
	public void showInvalidate(IErrorMessage col) {
		EContext e = (EContext) col.getC();
		e.fa.showInvalidate(col);
	}

	@Override
	public void extractFields(RecordModel mo) {
		BookingP p = (BookingP) mo.getA();

		Collection<BookRecordP> bCol = p.getBookrecords();
		if (bCol == null) {
			bCol = new ArrayList<BookRecordP>();
		}
		BookRecordP br = bRec.getExtractFields();
		br.setBooklist(bElem.extractFields());
		GetMaxUtil.addNextLp(bCol, br);
		p.setBookrecords(bCol);

		Collection<BookingStateP> pCol = p.getState();
		if (pCol == null) {
			pCol = new ArrayList<BookingStateP>();
		}
		BookingStateP sR = new BookingStateP();
		sR.setBState(BookingStateType.WaitingForConfirmation);
		GetMaxUtil.addNextLp(pCol, sR);
		p.setState(pCol);

		BillP bill;
		Collection<BillP> bilCol = p.getBill();
		if (bilCol == null) {
			bill = BillUtil.createPaymentBill();
			bilCol = new ArrayList<BillP>();
			bilCol.add(bill);
			p.setBill(bilCol);
		} else {
			bill = BillUtil.getBill(p);
		}
		Collection<AdvancePaymentP> vCol = bill.getAdvancePay();
		if (vCol == null) {
			vCol = new ArrayList<AdvancePaymentP>();
		}

		AdvancePaymentP pa = (AdvancePaymentP) vRec.getExtractFields();
		GetMaxUtil.addNextLp(vCol, pa);
		bill.setAdvancePay(vCol);
	}

	private class SetCustomerData implements RData.IOneList {

		public void doOne(AbstractTo val) {
			cust.setFields(val);
			cust.show();
		}
	}

	@Override
	public void setFields(RecordModel mo) {
		// toView
		BookingP p = (BookingP) mo.getA();
		BookRecordP b = null;
		if (p.getBookrecords() != null) {
			b = GetMaxUtil.getLastBookRecord(p);
		}
		if (b == null) {
			b = new BookRecordP();
		}

		LId cId = p.getCustomer();
		CustomerP cp = new CustomerP();
		cust.setFields(cp);
		cust.setModifWidgetStatus(cId == null);
		cust.setNewWidgetStatus(true);
		if (cId != null) {
			CommandParam pa = rI.getR().getHotelDictId(DictType.CustomerList,
					cId);
			rI.getR().getOne(RType.ListDict, pa, new SetCustomerData());
		}

		bRec.setFields(b);

		AdvancePaymentP pa = null;
		if (p.getBill() != null) {
			pa = GetMaxUtil.getLastValidationRecord(p);
		}
		if (pa == null) {
			pa = new AdvancePaymentP();
		}
		if (pa.getValidationDate() == null) {
			Date da = ConfigParam.countPayAdvanceDay();
			pa.setValidationDate(da);
		}
		vRec.setFields(pa);

		bElem.setFields(b);
	}

	@Override
	public void changeMode(int actionMode) {
		if (actionMode == IPersistAction.MODIFACTION) {
			cust.changeMode(IPersistAction.DISABLEDIALOGACTION);
		} else {
			cust.changeMode(actionMode);
		}
		bRec.changeMode(actionMode);
		vRec.changeMode(actionMode);
	}

	@Override
	public void show() {
		cust.show();
		bRec.show();
		vRec.show();
		bElem.show();
	}
}
