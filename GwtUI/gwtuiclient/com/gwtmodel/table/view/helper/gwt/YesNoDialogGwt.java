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
package com.gwtmodel.table.view.helper.gwt;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IClickYesNo;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonFactory;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.common.ISignal;
import com.gwtmodel.table.factories.IGetCustomValues;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.smessage.IGetStandardMessage;
import com.gwtmodel.table.view.controlpanel.IContrButtonView;
import com.gwtmodel.table.view.controlpanel.IContrButtonViewFactory;
import com.gwtmodel.table.view.controlpanel.IControlClick;
import com.gwtmodel.table.view.helper.IStandDialog;
import com.gwtmodel.table.view.util.ModalDialog;

/**
 * 
 * @author stanislaw.bartkowski@gmail.com
 */
public class YesNoDialogGwt extends ModalDialog implements IStandDialog {

	private final String ask;
	private IGetStandardMessage iMess = GwtGiniInjector.getI().getStandardMessage();

	@Override
	protected void addVP(VerticalPanel vp) {
		vp.add(new Label(ask));
	}

	public YesNoDialogGwt(String ask, final IClickYesNo yes) {
		this(ask, null, yes);
	}

	public YesNoDialogGwt(String ask, String title, final IClickYesNo yes) {
		super(new VerticalPanel(), null);
		this.ask = iMess.getMessage(ask);
		if (title == null) {
			IGetCustomValues va = GwtGiniInjector.getI().getCustomValues();
			title = va.getCustomValue(IGetCustomValues.QUESTION);
		}
		setTitle(title);

		ISignal closeC = new ISignal() {

			@Override
			public void signal() {
				hide();
				yes.click(false);
			}
		};

		create(closeC);

		ControlButtonFactory fa = GwtGiniInjector.getI().getControlButtonFactory();
		ListOfControlDesc yesB = fa.constructYesNoButton();

		IControlClick cli = new IControlClick() {

			@Override
			public void click(ControlButtonDesc co, Widget w) {
				hide();
				yes.click(co.getActionId().eq(new ClickButtonType(ClickButtonType.StandClickEnum.ACCEPT)));
			}
		};

		IContrButtonViewFactory ba = GwtGiniInjector.getI().getContrButtonViewFactory();
		IContrButtonView vButton = ba.getView(yesB, cli, true, false);

		vP.add(vButton.getGWidget());
	}
}
