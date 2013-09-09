/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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
package com.jythonui.client.dialog;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.ICustomObject;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.SynchronizeList;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.SlU;
import com.gwtmodel.table.view.callback.CommonCallBack;
import com.gwtmodel.table.view.util.ModalDialog;
import com.gwtmodel.table.view.webpanel.IWebPanel;
import com.jythonui.client.IJythonUIClient;
import com.jythonui.client.M;
import com.jythonui.client.util.IExecuteAfterModalDialog;
import com.jythonui.client.util.ISendCloseAction;
import com.jythonui.client.util.RequestContextFactory;
import com.jythonui.client.variables.IVariablesContainer;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogInfo;
import com.jythonui.shared.DialogVariables;

/**
 * @author hotel
 * 
 */
public class RunAction implements IJythonUIClient {

	private final Synch sy = new Synch();

	private class Synch extends SynchronizeList {

		DialogFormat d;
		MDialog mDial = null;

		Synch() {
			super(2);
		}

		@Override
		protected void doTask() {
			if (!CUtil.EmptyS(d.getDisplayName())) {
				mDial.setTitle(d.getDisplayName());
			}
		}

	}

	private class GetDialog implements ISlotListener {

		@Override
		public void signal(ISlotSignalContext slContext) {
			ICustomObject o = slContext.getCustom();
			SendDialogFormSignal sig = (SendDialogFormSignal) o;
			sy.d = sig.getValue();
			sy.signalDone();
		}

	}

	private class MDialog extends ModalDialog {

		private final Widget w;

		MDialog(Widget w) {
			super("");
			this.w = w;
			create();
		}

		@Override
		protected void addVP(VerticalPanel vp) {
			vp.add(w);
		}
	}

	private class GetUpWidget implements ISlotListener {

		private final WSize w;

		GetUpWidget(WSize w) {
			this.w = w;
		}

		@Override
		public void signal(ISlotSignalContext slContext) {
			Widget wd = slContext.getGwtWidget().getGWidget();
			sy.mDial = new MDialog(wd);
			sy.mDial.show(w);
			sy.signalDone();
		}
	}

	private class GetCenterWidget implements ISlotListener {

		@Override
		public void signal(ISlotSignalContext slContext) {
			Widget w = slContext.getGwtWidget().getGWidget();
			IWebPanel i = GwtGiniInjector.getI().getWebPanel();
			i.setDCenter(w);
		}

	}

	private class CloseDialog implements ISlotListener {

		@Override
		public void signal(ISlotSignalContext slContext) {
			if (sy.mDial != null) {
				sy.mDial.hide();
				// release reference
				sy.mDial = null;
			} else {
				IWebPanel i = GwtGiniInjector.getI().getWebPanel();
				i.setDCenter(null);
			}

		}

	}

	private class StartBack extends CommonCallBack<DialogInfo> {

		private final IDataType dType;
		private final ISlotListener getW;
		private final IVariablesContainer iCon;
		private final ISendCloseAction iClose;
		private final DialogVariables addV;
		private final IExecuteAfterModalDialog iEx;
		private final String startVal;

		StartBack(IDataType dType, ISlotListener getW,
				IVariablesContainer iCon, ISendCloseAction iClose,
				DialogVariables addV, IExecuteAfterModalDialog iEx,
				String startVal) {
			this.dType = dType;
			this.getW = getW;
			this.iCon = iCon;
			this.iClose = iClose;
			this.addV = addV;
			this.iEx = iEx;
			this.startVal = startVal;
		}

		@Override
		public void onMySuccess(DialogInfo arg) {
			DialogContainer d = new DialogContainer(dType, arg, iCon, iClose,
					addV, iEx, startVal);
			d.getSlContainer().registerSubscriber(
					SendDialogFormSignal.constructSignal(dType),
					new GetDialog());
			SlU.registerWidgetListener0(dType, d, getW);
			d.getSlContainer().registerSubscriber(
					SendCloseSignal.constructSignal(dType), new CloseDialog());
			CellId cId = new CellId(0);
			d.startPublish(cId);
		}
	}

	@Override
	public void start(String startdialogName) {
		IDataType dType = DataType.construct(startdialogName, null);
		M.JR().getDialogFormat(
				RequestContextFactory.construct(),
				startdialogName,
				new StartBack(dType, new GetCenterWidget(), null, null, null,
						null, null));
	}

	/**
	 * Starts modal dialog, next dialog in stack to dialogs
	 * 
	 * @param dialogName
	 *            Dialog name
	 * @param w
	 *            WSize to position model dialog on the screen
	 * @param iCon
	 *            Variable top copy from
	 */
	public void upDialog(String dialogName, WSize w, IVariablesContainer iCon,
			IExecuteAfterModalDialog iEx, String startVal) {
		IDataType dType = DataType.construct(dialogName, null);

		M.JR().getDialogFormat(
				RequestContextFactory.construct(),
				dialogName,
				new StartBack(dType, new GetUpWidget(w), iCon, null, null, iEx,
						startVal));
	}

	public void getHelperDialog(String dialogName, ISlotListener sl,
			IVariablesContainer iCon, ISendCloseAction iClose,
			DialogVariables addV) {
		IDataType dType = DataType.construct(dialogName, null);

		M.JR().getDialogFormat(RequestContextFactory.construct(), dialogName,
				new StartBack(dType, sl, iCon, iClose, addV, null, null));
	}

}
