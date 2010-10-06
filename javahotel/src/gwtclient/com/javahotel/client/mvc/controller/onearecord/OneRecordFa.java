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
package com.javahotel.client.mvc.controller.onearecord;

import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.dialog.IPersistAction;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.mvc.controller.onerecordmodif.IOneRecordModifWidget;
import com.javahotel.client.mvc.contrpanel.model.ContrButton;
import com.javahotel.client.mvc.contrpanel.view.IControlClick;
import com.javahotel.client.mvc.crud.controler.ICrudAccept;
import com.javahotel.client.mvc.crud.controler.ICrudChooseTable;
import com.javahotel.client.mvc.crud.controler.ICrudControler;
import com.javahotel.client.mvc.crud.controler.ICrudRecordControler;
import com.javahotel.client.mvc.crud.controler.ICrudRecordFactory;
import com.javahotel.client.mvc.crud.controler.ICrudView;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.dict.validator.DictValidatorFactory;
import com.javahotel.client.mvc.dictcrud.controler.DictCrudControlerFactory;
import com.javahotel.client.mvc.record.extractor.IRecordExtractor;
import com.javahotel.client.mvc.record.view.IRecordView;
import com.javahotel.client.mvc.table.model.ITableFilter;
import com.javahotel.client.mvc.util.ChooseCrudDialog;
import com.javahotel.client.mvc.validator.IErrorMessage;
import com.javahotel.client.mvc.validator.IRecordValidator;
import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.types.LId;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class OneRecordFa implements IOneARecord {

	private final IResLocator rI;
	private final ICrudRecordFactory conI;
	private final IRecordView iView;
	private final DictData da;
	private final RecordModel mo;
	private IOneRecordModifWidget mWidget;
	private final DictValidatorFactory dFactory;

	OneRecordFa(IResLocator rI, DictData da, ICrudRecordFactory conI,
			IRecordView iView) {
		this.rI = rI;
		this.conI = conI;
		this.iView = iView;
		this.da = da;
		mo = conI.getNew(null, null);
		mo.setRDef(iView.getModel());
		dFactory = HInjector.getI().getDictValidFactory();
	}

	public boolean IsModifCustomer() {
		if (getMWidget() == null) {
			return true;
		}
		return getMWidget().IsModifValue();
	}

	Widget getWidget() {
		return null;
	}

	public IControlClick getClick(ITableFilter tFilter) {
		return new Click(tFilter);
	}

	/**
	 * @return the mWidget
	 */
	public IOneRecordModifWidget getMWidget() {
		return mWidget;
	}

	/**
	 * @param mWidget
	 *            the mWidget to set
	 */
	public void setMWidget(IOneRecordModifWidget mWidget) {
		this.mWidget = mWidget;
	}

	public RecordModel getRModel() {
		return mo;
	}

	public ICrudRecordFactory getFactory() {
		return conI;
	}

	private class ChooseR implements ICrudChooseTable {

		private ChooseCrudDialog cD;

		public void signal(IControlerContext cont, boolean choosed) {
			cD.getDBox().hide();
			if (choosed) {
				AbstractTo a = cont.getControler().getTableView().getClicked();
				setFields(a);
				if (iView.getAuxV() != null) {
					iView.getAuxV().show();
				}
				setModifWidgetStatus(false);
				setNewWidgetStatus(false);
				changeMode(IPersistAction.DELACTION);
			}
		}
	}

	private class Click implements IControlClick {

		private final ITableFilter tFilter;

		Click(ITableFilter tFilter) {
			this.tFilter = tFilter;
		}

		private class Acce implements ICrudAccept {

			private final ICrudRecordControler con;
			ICrudView rview;

			Acce(ICrudRecordControler con) {
				this.con = con;
			}

			public void accept(RecordModel a) {
				con.hideDialog(rview);
				setFields(a.getA());
			}
		}

		public void click(ContrButton co, Widget w) {
			if (co.getActionId() == IOneRecordModifWidget.IMODIFDATADIALOG) {
				ICrudControler iCrud = HInjector.getI().getDictCrudControlerFactory().getCrud(da);
				ICrudRecordFactory fa = iCrud.getF();
				ICrudRecordControler con = fa.getControler();
				int actionId = IPersistAction.MODIFACTION;
				RecordModel a = fa.getNew(null, getExtractFields());
				Acce ace = new Acce(con);
				ICrudView view = fa.getView(a, ace, actionId, null);
				ace.rview = view;
				con.showDialog(actionId, view, w);
			}
			if (co.getActionId() == IOneRecordModifWidget.ICHOOSELIST) {
				ChooseR r = new ChooseR();
				r.cD = new ChooseCrudDialog(rI, da, r, w, tFilter);
			}
			if (co.getActionId() == IOneRecordModifWidget.IMODIFDATA) {
				if (getMWidget().IsModifValue()) {
					return;
				}
				setModifWidgetStatus(true);
				changeMode(IPersistAction.MODIFACTION);
			}

		}
	}

	public void setFields(AbstractTo cp) {
		mo.setBeforea(cp);
		mo.setA(cp);
		IRecordExtractor ex = conI.getExtractor();
		ex.toView(iView, mo);
	}

	public AbstractTo getExtractFields() {
		ExtractFields();
		return mo.getA();
	}

	public void ExtractFields() {
		RecordModel nmod = conI.getNew(mo.getBeforea(), null);
		mo.setA(nmod.getA());
		IRecordExtractor ex = conI.getExtractor();
		ex.toA(mo, iView);
		if (da.getD() == DictType.CustomerList) {
			LId id = mo.getBeforea().getLId();
			mo.getA().setLId(id);
		}
	}

	public void changeMode(int actionMode) {
		iView.changeMode(actionMode);
	}

	public void showInvalidate(IErrorMessage col) {
		iView.showInvalidate(col);
	}

	public IRecordValidator getValidator() {
		IRecordValidator va = dFactory.getValidator(da);
		return va;
	}

	public void setModifWidgetStatus(boolean modif) {
		getMWidget().setModifValue(modif);
	}

	public void setNewWidgetStatus(boolean modif) {
		getMWidget().setNewValue(modif);
	}
}
