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
package com.javahotel.client.mvc.controller.onerecord;

import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.mvc.util.*;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DefaultMvcWidget;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.mvc.apanel.GwtPanel;
import com.javahotel.client.mvc.apanel.IPanel;
import com.javahotel.client.mvc.crud.controler.ICrudControler;
import com.javahotel.client.mvc.crud.controler.ICrudRecordFactory;
import com.javahotel.client.mvc.crud.controler.ICrudView;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.dict.validator.DictValidatorFactory;
import com.javahotel.client.mvc.dictcrud.controler.AbstractAuxRecordPanel;
import com.javahotel.client.mvc.dictcrud.controler.DictCrudControlerFactory;
import com.javahotel.client.mvc.dictcrud.controler.IModifRecordDef;
import com.javahotel.client.mvc.dictdata.model.IOneRecordModel;
import com.javahotel.client.mvc.record.model.RecordField;
import com.javahotel.client.mvc.validator.IErrorMessage;
import com.javahotel.client.mvc.validator.IRecordValidator;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.IField;
import java.util.ArrayList;
import com.javahotel.client.mvc.controller.onearecord.IOneARecord;
import com.javahotel.client.mvc.controller.onearecord.OneRecordFactory;
import com.javahotel.client.mvc.record.view.IRecordView;
import com.javahotel.client.mvc.controller.onerecordmodif.IOneRecordModifWidget;
import com.javahotel.client.mvc.controller.onerecordmodif.OneRecordModifWidgetFactory;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class RecordFa extends AbstractAuxRecordPanel implements IOneRecordModel {

	private final IResLocator rI;
	private final ICrudControler custI;
	private final ICrudRecordFactory conI;
	private final IPanel custV = new GwtPanel(new VerticalPanel());
	private final ICrudView cView;
	private final DictData da;
	private final GetFieldModif[] mod;
	private final RecordFaParam par;
	private VerticalPanel modifW = null;
	private final IOneARecord oRecord;

	public boolean IsModifCustomer() {
		if (oRecord.getMWidget() == null) {
			return true;
		}
		return oRecord.getMWidget().IsModifValue();
	}

	public IMvcWidget getMWidget() {
		if (!mPanel()) {
			return new DefaultMvcWidget(custV.getPanel());
		}
		if (modifW == null) {
			modifW = new VerticalPanel();
			modifW.add(oRecord.getMWidget().getWidget());
			modifW.add(custV.getPanel());
		}
		return new DefaultMvcWidget(modifW);
	}

	private class ModifE implements IModifRecordDef {

		public void modifRecordDef(ArrayList<RecordField> dict) {
			for (int i = 0; i < mod.length; i++) {
				mod[i].getModif().modifRecordDef(dict);
			}
		}
	}

	public RecordModel getModel() {
		return oRecord.getRModel();
	}

	private boolean mPanel() {
		if (par == null) {
			return false;
		}
		return par.isNewchoosetag();
	}

	public RecordFa(final IResLocator rI, final DictData da,
			final RecordFaParam par, final IField... f) {
		this.rI = rI;
		this.da = da;
		this.par = par;
		if (f == null) {
			mod = null;
			custI = DictCrudControlerFactory.getCrud(rI, da);
		} else {
			mod = new GetFieldModif[f.length];
			for (int i = 0; i < f.length; i++) {
				mod[i] = new GetFieldModif(f[i]);
			}
			custI = DictCrudControlerFactory.getCrud(rI, da, null,
					new ModifE(), null, null);
		}
		conI = custI.getF();
		cView = conI.getView(null, null, 0, custV);
		IRecordView iView = conI.getRView(cView);
		oRecord = OneRecordFactory.getR(rI, da, conI, iView);
		if (mPanel()) {
			IOneRecordModifWidget iM = OneRecordModifWidgetFactory.getWi(rI,
					oRecord.getClick(null));
			oRecord.setMWidget(iM);

		}
	}

	public GetFieldModif getModif(int i) {
		return mod[i];
	}

	public void setFields(AbstractTo cp) {
		oRecord.setFields(cp);
	}

	public <T> T getExtractFields() {
		return (T) oRecord.getExtractFields();
	}

	public void ExtractFields() {
		oRecord.ExtractFields();
	}

	public Widget getWidget() {
		if (!mPanel()) {
			return custV.getPanel();
		}
		if (modifW == null) {
			modifW = new VerticalPanel();
			modifW.add(oRecord.getMWidget().getWidget());
			modifW.add(custV.getPanel());
		}
		return modifW;
	}

	@Override
	public void changeMode(int actionMode) {
		oRecord.changeMode(actionMode);
	}

	@Override
	public void showInvalidate(IErrorMessage col) {
		oRecord.showInvalidate(col);
	}

	@Override
	public IRecordValidator getValidator() {
		IRecordValidator va = DictValidatorFactory.getValidator(rI, da);
		return va;
	}

	@Override
	public void show() {
		conI.show(cView);
	}

	@Override
	public void hide() {
	}

	public void setModifWidgetStatus(boolean modif) {
		oRecord.setModifWidgetStatus(modif);
	}

	public void setNewWidgetStatus(boolean modif) {
		oRecord.setNewWidgetStatus(modif);
	}
}
