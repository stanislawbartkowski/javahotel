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

import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.dialog.DictData.SpecE;
import com.javahotel.client.mvc.contrpanel.model.ContrButton;
import com.javahotel.client.mvc.contrpanel.model.IContrPanel;
import com.javahotel.client.mvc.contrpanel.view.ContrButtonViewFactory;
import com.javahotel.client.mvc.contrpanel.view.IContrButtonView;
import com.javahotel.client.mvc.contrpanel.view.IControlClick;
import com.javahotel.client.mvc.crudtable.controler.ICrudTableControler;
import com.javahotel.client.mvc.dictcrud.controler.RecordAuxParam;
import com.javahotel.client.mvc.recordviewdef.DictButtonFactory;
import com.javahotel.client.mvc.tablecrud.controler.TableDictCrudControlerFactory;
import com.javahotel.client.mvc.util.MDialog;
import com.javahotel.client.widgets.popup.PopupUtil;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.ResDayObjectStateP;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class ConflictInfo {

	private final DialogBox dBox;
	@SuppressWarnings("unused")
	private final IResLocator rI;
	private final ICrudTableControler iC;

	ConflictInfo(final IResLocator rI, final ReturnPersist res) {

		this.rI = rI;

		Collection<ResDayObjectStateP> col = res.getResState();

		RecordAuxParam aux = new RecordAuxParam();
		aux.setModifPanel(false);
		IContrPanel pa = DictButtonFactory.getOkButton(rI);
		IControlClick co = new IControlClick() {

			public void click(ContrButton co, Widget w) {
				dBox.hide();
			}
		};

		final IContrButtonView i = ContrButtonViewFactory.getView(rI, pa, co);

		iC = TableDictCrudControlerFactory.getCrud(rI, new DictData(
				SpecE.ObjectResConflict));
		iC.getTableView().getModel().setList(
				(ArrayList<? extends AbstractTo>) col);

		VerticalPanel vP = new VerticalPanel();
		MDialog mDial = new MDialog(vP, "Nie można potwierdzić rezerwacji") {

			@Override
			protected void addVP(VerticalPanel vp) {
				vp.add(iC.getMWidget().getWidget());
				vp.add(i.getMWidget().getWidget());
			}
		};
		dBox = mDial.getDBox();

	}

	void show(Widget w) {
		PopupUtil.setMiddlePos(dBox, w);
		iC.drawTable();
		dBox.show();
	}
}
