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

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.ICustomObject;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.SynchronizeList;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.ISignal;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.CustomStringSlot;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.SlU;
import com.gwtmodel.table.view.callback.CommonCallBack;
import com.gwtmodel.table.view.util.ModalDialog;
import com.gwtmodel.table.view.webpanel.IWebPanel;
import com.gwtmodel.table.view.webpanel.IWebPanel.InfoType;
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
    private final SynchM syM = new SynchM();

    private class Synch extends SynchronizeList {

        DialogFormat d;
        DialogContainer dI = null;
        ISlotListener sl;
        ISlotSignalContext slW;
        boolean mainW = false;

        Synch() {
            super(3);
        }

        @Override
        protected void doTask() {
            sl.signal(slW);
        }

    }

    private class SynchM extends SynchronizeList {

        MDialog mDial = null;
        DialogFormat d;

        SynchM() {
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
            syM.d = sy.d;
            syM.signalDone();
            if (sy.mainW) {
                String dTitle = sy.d.getDisplayName();
                // display dialog title in the status bar
                IWebPanel i = GwtGiniInjector.getI().getWebPanel();
                i.setPaneText(InfoType.UPINFO, dTitle);
            }
        }

    }

    private class CloseI implements ISignal {

        private final IDataType dType;

        CloseI(IDataType dType) {
            this.dType = dType;
        }

        @Override
        public void signal() {
            CloseDialogByImage sig = new CloseDialogByImage();
            CustomStringSlot sl = CloseDialogByImage.constructSignal(dType);
            sy.dI.getSlContainer().publish(sl, sig);
        }

    }

    private class MDialog extends ModalDialog {

        private final Widget w;

        MDialog(Widget w, IDataType dType) {
            super("");
            this.w = w;
            create();
            this.setOnClose(new CloseI(dType));
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
        public void signal(final ISlotSignalContext slContext) {
            final Widget wd = slContext.getGwtWidget().getGWidget();
            // important : to show modal properly (calculate height and width)
            Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                @Override
                public void execute() {
                    syM.mDial = new MDialog(wd, slContext.getSlType()
                            .getdType());
                    syM.mDial.show(w);
                    syM.signalDone();
                }
            });
            // syM.mDial = new MDialog(wd, slContext.getSlType().getdType());
            // syM.mDial.show(w);
            // syM.signalDone();
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
            if (syM.mDial != null) {
                syM.mDial.hide();
                // release reference
                syM.mDial = null;
            } else if (sy.mainW) {
                IWebPanel i = GwtGiniInjector.getI().getWebPanel();
                i.setDCenter(null);
            }

        }

    }

    private class BeforeFinished implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            sy.signalDone();
        }

    }

    private class GetW implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            sy.slW = slContext;
            sy.signalDone();
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
            // SlU.registerWidgetListener0(dType, d, getW);
            SlU.registerWidgetListener0(dType, d, new GetW());
            d.getSlContainer().registerSubscriber(
                    SendCloseSignal.constructSignal(dType), new CloseDialog());
            d.getSlContainer().registerSubscriber(
                    SignalAfterBefore.constructSignal(dType),
                    new BeforeFinished());
            CellId cId = new CellId(0);
            sy.dI = d;
            sy.sl = getW;
            d.startPublish(cId);
        }
    }

    @Override
    public void start(String startdialogName) {
        IDataType dType = DataType.construct(startdialogName, null);
        sy.mainW = true;
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
