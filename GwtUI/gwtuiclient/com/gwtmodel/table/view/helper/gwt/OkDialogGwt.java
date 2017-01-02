/*
 * Copyright 2017 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.view.helper.gwt;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonFactory;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.common.ISignal;
import com.gwtmodel.table.factories.IGetCustomValues;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.smessage.IGetStandardMessage;
import com.gwtmodel.table.view.controlpanel.IContrButtonView;
import com.gwtmodel.table.view.controlpanel.IContrButtonViewFactory;
import com.gwtmodel.table.view.controlpanel.IControlClick;
import com.gwtmodel.table.view.helper.IStandDialog;
import com.gwtmodel.table.view.util.ModalDialog;

/**
 * 
 * @author perseus
 */
public class OkDialogGwt extends ModalDialog implements IStandDialog {

	private final String kom;
	private IGetStandardMessage iMess = GwtGiniInjector.getI().getStandardMessage();

	@Override
	protected void addVP(VerticalPanel vp) {
		vp.add(new Label(kom));
	}

	private void close() {
		hide();
	}

	public OkDialogGwt(String kom, String title) {
		super(new VerticalPanel(), null);
		this.kom = iMess.getMessage(kom);
		if (title == null) {
			IGetCustomValues va = GwtGiniInjector.getI().getCustomValues();
			title = va.getCustomValue(IGetCustomValues.INFO);
		}
		setTitle(title);

		ISignal co = new ISignal() {

			@Override
			public void signal() {
				close();
			}
		};

		create(co);

		ControlButtonFactory fa = GwtGiniInjector.getI().getControlButtonFactory();
		ListOfControlDesc yesB = fa.constructOkButton();

		IControlClick cli = new IControlClick() {

			@Override
			public void click(ControlButtonDesc co, Widget w) {
				close();
			}
		};

		IContrButtonViewFactory ba = GwtGiniInjector.getI().getContrButtonViewFactory();
		IContrButtonView vButton = ba.getView(yesB, cli, true, false);

		vP.add(vButton.getGWidget());
	}
}
