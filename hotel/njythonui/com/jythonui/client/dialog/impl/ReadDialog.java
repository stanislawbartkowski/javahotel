/*
 * Copyright 2017 stanislawbartkowski@gmail.com 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not ue this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package com.jythonui.client.dialog.impl;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.gwtmodel.table.binder.BinderWidget;
import com.jythonui.client.M;
import com.jythonui.client.dialog.IReadDialog;
import com.jythonui.client.gini.UIGiniInjector;
import com.jythonui.client.util.ExecuteAction;
import com.jythonui.shared.ButtonItem;
import com.jythonui.shared.DialogInfo;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.ICommonConsts;
import com.polymerui.client.binder.ICreateBinderWidget;
import com.polymerui.client.callback.CommonCallBack;
import com.polymerui.client.eventbus.ButtonEvent;
import com.polymerui.client.eventbus.IEvent;
import com.polymerui.client.eventbus.IEventBus;
import com.polymerui.client.eventbus.ISubscriber;
import com.polymerui.client.util.Utils;
import com.polymerui.client.view.panel.IMainPanel;
import com.polymerui.client.view.util.CreatePolymerMenu;
import com.vaadin.polymer.paper.widget.PaperMenu;

public class ReadDialog implements IReadDialog {

	private final IEventBus iBus;
	private final ICreateBinderWidget iBinder;
	private HTMLPanel ha;

	private void go(DialogInfo arg, DialogVariables va) {
		IMainPanel iP = M.getPanel();
		if (!arg.getDialog().getLeftStackList().isEmpty()) {
			PaperMenu me = iP.getLeftMenu();
			me.clear();
			arg.getDialog().getLeftStackList().forEach(b -> iBus.subscribe(new ButtonEvent(b), new ButtonSubscribe()));
			CreatePolymerMenu.constructStackMenu(me, arg.getDialog().getLeftStackList(), iBus);
		}
		BinderWidget w = arg.getDialog().getBinderW();
		assert w != null;
		ha = iBinder.create(w);
		SetFields.setV(ha, arg, va);
		iP.drawContent(ha);
	}

	@Inject
	public ReadDialog(IEventBus iBus, ICreateBinderWidget iBinder) {
		this.iBus = iBus;
		this.iBinder = iBinder;
	}

	private void verify(DialogInfo d) {
		if (d.getDialog().getBinderW() == null) {
			Utils.errAlertB(M.M().DialogShouldContainBinder(d.getDialog().getId()));
		}
	}

	private class ButtonSubscribe implements ISubscriber<ButtonItem> {

		@Override
		public void raise(IEvent<ButtonItem> i) {
			// TODO Auto-generated method stub
			int k = 0;

		}

	}

	private class BeforeA extends CommonCallBack<DialogVariables> {

		private final DialogInfo d;

		BeforeA(DialogInfo d) {
			this.d = d;
		}

		@Override
		public void onMySuccess(DialogVariables arg) {
			go(d, arg);

		}

	}

	private class CallB extends CommonCallBack<DialogInfo> {

		private final String dialogName;

		CallB(String dialogName) {
			this.dialogName = dialogName;
		}

		@Override
		public void onMySuccess(DialogInfo arg) {
			verify(arg);
			if (!arg.getDialog().isBefore())
				go(arg, new DialogVariables());
			else
				ExecuteAction.action(new DialogVariables(), dialogName, ICommonConsts.BEFORE, new BeforeA(arg));
		}

	}

	@Override
	public void readDialog(String dialogName) {

		M.JR().getDialogFormat(UIGiniInjector.getI().getRequestContext(), dialogName, new CallB(dialogName));

	}

}
