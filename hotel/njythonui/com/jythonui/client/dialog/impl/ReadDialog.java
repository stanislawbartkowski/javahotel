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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.gwtmodel.table.binder.BinderWidget;
import com.gwtmodel.table.common.CUtil;
import com.jythonui.client.M;
import com.jythonui.client.dialog.IReadDialog;
import com.jythonui.client.dialog.util.ActionButton;
import com.jythonui.client.dialog.util.EnrichWidgets;
import com.jythonui.client.dialog.util.RunAction;
import com.jythonui.client.dialog.util.SetFields;
import com.jythonui.client.dialog.util.SetVariables;
import com.jythonui.client.dialog.util.VerifyDialog;
import com.jythonui.client.gini.UIGiniInjector;
import com.jythonui.client.util.ExecuteAction;
import com.jythonui.client.var.ISetJythonVariables;
import com.jythonui.client.var.JythonVariables;
import com.jythonui.shared.ButtonItem;
import com.jythonui.shared.DialogInfo;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.FieldValue;
import com.jythonui.shared.ICommonConsts;
import com.polymerui.client.binder.ICreateBinderWidget;
import com.polymerui.client.callback.CommonCallBack;
import com.polymerui.client.eventbus.ButtonEvent;
import com.polymerui.client.eventbus.ChangeEvent;
import com.polymerui.client.eventbus.EventDialogGetHTML;
import com.polymerui.client.eventbus.IEvent;
import com.polymerui.client.eventbus.IEventBus;
import com.polymerui.client.eventbus.ISubscriber;
import com.polymerui.client.eventbus.ResultButtonAction;
import com.polymerui.client.eventbus.StandardDialogEvent;
import com.polymerui.client.eventbus.StandardDialogResult;
import com.polymerui.client.util.Utils;
import com.polymerui.client.view.panel.IMainPanel;
import com.polymerui.client.view.panel.IMainPanel.InfoType;
import com.polymerui.client.view.util.CreatePolymerMenu;
import com.vaadin.polymer.paper.widget.PaperMenu;

public class ReadDialog implements IReadDialog {

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

	private class ButtonSubscribe implements ISubscriber<ButtonItem> {

		@Override
		public void raise(IEvent e, ButtonItem i) {
			if (!VerifyDialog.verify(iBus, i))
				return;
			if (i.isAction()) {
				RunAction.buttonAction(iBus, i);
				return;
			}
			ActionButton.call(iBus, JythonVariables.constructVar(), i);
		}
	}

	private class ChangeSubscriber implements ISubscriber<String> {

		@Override
		public void raise(IEvent e, String fieldid) {
			DialogVariables v = JythonVariables.constructVar();
			v.setValueS(ICommonConsts.SIGNALCHANGEFIELD, fieldid);
			v.setValueB(ICommonConsts.SIGNALAFTERFOCUS, true);
			ActionButton.callA(iBus, v, ICommonConsts.SIGNALCHANGE);
		}

	}

	private class StandardDialogSubscriber implements ISubscriber<StandardDialogResult> {

		@Override
		public void raise(IEvent e, StandardDialogResult i) {
			DialogVariables v = JythonVariables.constructVar();
			v.setValueB(ICommonConsts.JYESANSWER, i.isOk());
			ActionButton.callA(iBus, v, i.getAction());
		}

	}

	private class ResultAction implements ISubscriber<DialogVariables> {

		@Override
		public void raise(IEvent e, DialogVariables i) {
			int k = 0;
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
				ExecuteAction.action(JythonVariables.constructVar(), dialogName, ICommonConsts.BEFORE,
						new BeforeA(arg));
		}

	}

	private class PC implements IMainPanel.IContent {

		@Override
		public HTMLPanel getH() {
			return ha;
		}

		ISetJythonVariables getS() {
			return iSet;
		}

	}

	private final IEventBus iBus;

	private final ICreateBinderWidget iBinder;

	private HTMLPanel ha;

	private DialogInfo d;

	private boolean main;

	private String displayName;

	private final List<FieldItem> dynamicList = new ArrayList<FieldItem>();

	private final ISetJythonVariables iSet = new ISetJythonVariables() {

		@Override
		public void set(DialogVariables v) {
			SetVariables.set(v, iBus);
		}
	};

	@Inject
	public ReadDialog(IEventBus iBus, ICreateBinderWidget iBinder) {
		this.iBus = iBus;
		this.iBinder = iBinder;
	}

	@Override
	public DialogInfo getD() {
		return d;
	}

	@Override
	public HTMLPanel getH() {
		return ha;
	}

	private void go(DialogInfo arg, DialogVariables va) {

		iBus.registerInfoProvider(new EventDialogGetHTML(), () -> ReadDialog.this);
		iBus.subscribe(new ButtonEvent(), new ButtonSubscribe());
		iBus.subscribe(new ResultButtonAction(), new ResultAction());
		iBus.subscribe(new StandardDialogEvent(), new StandardDialogSubscriber());
		iBus.subscribe(new ChangeEvent(), new ChangeSubscriber());
		d = arg;
		IMainPanel iP = M.getPanel();
		String uDisplay = displayName;
		if (!CUtil.EmptyS(arg.getDialog().getDisplayName()))
			uDisplay = arg.getDialog().getDisplayName();
		iP.drawInfo(InfoType.UPINFO, uDisplay);
		if (!arg.getDialog().getLeftStackList().isEmpty()) {
			PaperMenu me = iP.getLeftMenu();
			me.clear();
			CreatePolymerMenu.constructStackMenu(me, arg.getDialog().getLeftStackList(), iBus);
		}
		BinderWidget w = arg.getDialog().getBinderW();
		assert w != null;
		ha = iBinder.create(w);
		SetFields.setV(va, iBus);

		EnrichWidgets.enrich(iBus);
		M.getPanel().replaceContent(new PC());
		JythonVariables.registerVar(iSet);
	}

	@Override
	public void readDialog(String dialogName, String displayName, boolean main) {

		this.main = main;
		this.displayName = displayName;

		if (main) {
			PC p = (PC) M.getPanel().getCurrentContent();
			// remove previous main dialog
			if (p != null)
				JythonVariables.resetVar();
			// JythonVariables.deregisterVar(p.getS());
		}
		M.JR().getDialogFormat(UIGiniInjector.getI().getRequestContext(), dialogName, new CallB(dialogName));

	}

	private void verify(DialogInfo d) {
		if (d.getDialog().getBinderW() == null) {
			Utils.errAlertB(M.M().DialogShouldContainBinder(d.getDialog().getId()));
		}
	}

	@Override
	public List<FieldItem> getDynamicList() {
		return dynamicList;
	}

}
