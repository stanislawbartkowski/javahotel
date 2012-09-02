/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.factories.mailtest;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.*;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.ISignal;
import com.gwtmodel.table.controlbuttonview.ControlButtonViewFactory;
import com.gwtmodel.table.controlbuttonview.IControlButtonView;
import com.gwtmodel.table.datamodelview.DataViewModelFactory;
import com.gwtmodel.table.datamodelview.IDataViewModel;
import com.gwtmodel.table.factories.IJavaMailAction;
import com.gwtmodel.table.factories.ITableCustomFactories;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.MM;
import com.gwtmodel.table.mail.ListOfMailProperties;
import com.gwtmodel.table.mail.MailResult;
import com.gwtmodel.table.mail.MailToSend;
import com.gwtmodel.table.panelview.IPanelView;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.slotmodel.*;
import com.gwtmodel.table.view.ValidateUtil;
import com.gwtmodel.table.view.ewidget.EditWidgetFactory;
import com.gwtmodel.table.view.util.ModalDialog;
import com.gwtmodel.table.view.util.YesNoDialog;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author perseus
 */
class MailTest extends AbstractSlotMediatorContainer implements IMailTest {

    private final ITableCustomFactories cuFactories;
    private final CellId fId;
    private final CellId bId;
    private final IPanelView pView;
    private final IControlButtonView bView;
    private final EditWidgetFactory wFactory;
    private final DataViewModelFactory dFactory;
    private final IVField v1 = VSField.createVString("F_BOX");
    private final IVField v2 = VSField.createVString("F_HEADER");
    private final IVField v3 = VSField.createVString("F_CONTENT");
    private final IVField v4 = VSField.createVString("F_TO");
    private final IVField v5 = VSField.createVString("F_FROM");
    private FormLineContainer fContainer;
    private ListOfMailProperties maPr;
    private final Wait w = new Wait();
    private final MailToSend startM;
    private final boolean text;
    private final boolean showPost;
    private final WSize wi;

    private class Wait extends ModalDialog {

        private class CloseA implements ISignal {

            @Override
            public void signal() {
                slMediator.getSlContainer().publish(IMailTest.MAIL_SEND,
                        mResult);
            }
        }

        Wait() {
            super(MM.getL().SendingHeader());
            create();
        }
        private Label box = new Label();
        private Label to = new Label();
        private Label header = new Label();
        private Label res = new Label();
        private MailResult mResult = null;

        @Override
        protected void addVP(VerticalPanel vp) {
            vp.add(box);
            vp.add(to);
            vp.add(header);
            vp.add(res);
            this.setOnClose(new CloseA());
        }

        void setL(String b, String t, String hea) {
            box.setText(b);
            to.setText(t);
            header.setText(hea);
            res.setText(MM.getL().SendingMail());
        }

        void setR(String r) {
            res.setText(r);
        }

        void setMailResult(MailResult mResult) {
            this.mResult = mResult;
        }
    }

    private void createMailForm() {
        if (maPr.getErrMess() != null) {
            Label la = new Label(maPr.getErrMess());
            IGWidget g = new GWidget(la);

            slMediator.getSlContainer().publish(dType, fId, g);
            return;

        }
        List<String> li = new ArrayList<String>();
        String lName = null;
        for (Map<String, String> m : maPr.getmList(true)) {
            String na = maPr.getName(m);
            if (!CUtil.EmptyS(na)) {
                lName = na;
            }
            li.add(na);
        }
        IFormLineView dView = wFactory.constructListCombo(v1, li);
        IFormLineView hView = wFactory.constructTextField(v2);
        IFormLineView cView;
        if (text) {
            cView = wFactory.constructTextArea(v3);
        } else {
            cView = wFactory.constructRichTextArea(v3);
        }
        IFormLineView tView = wFactory.constructTextField(v4);
        IFormLineView fromView = wFactory.constructTextField(v5);
        if (startM != null) {
            cView.setValObj(startM.getContent());
            dView.setValObj(startM.getBoxName());
            hView.setValObj(startM.getHeader());
            tView.setValObj(startM.getTo());
            fromView.setValObj(startM.getFrom());
        }
        FormField f1 = new FormField(MM.getL().MailBox(), dView);
        FormField f2 = new FormField(MM.getL().MailSubject(), hView);
        FormField f3 = new FormField(MM.getL().MailContent(), cView);
        FormField f4 = new FormField(MM.getL().MailTo(), tView);
        FormField f5 = new FormField(MM.getL().MailFrom(), fromView);
        List<FormField> fList = new ArrayList<FormField>();
        fList.add(f1);
        fList.add(f4);
        fList.add(f5);
        fList.add(f2);
        fList.add(f3);
        fContainer = new FormLineContainer(fList);
        // disable for one mail box
        if (li.size() == 1) {
            IFormLineView i = fContainer.findLineView(v1);
            i.setValObj(lName);
            i.setReadOnly(true);
        }
        setAfterBoxChange();

        IDataViewModel iView = dFactory.construct(dType, fContainer);
        slMediator.registerSlotContainer(iView);
        iView.startPublish(fId);
    }

    private String getS(IVField v) {
        IFormLineView i = fContainer.findLineView(v);
        return (String) i.getValObj();
    }

    private class YesSend implements IClickYesNo {

        private final Widget wi;

        YesSend(Widget wi) {
            this.wi = wi;
        }

        public void click(boolean yes) {
            if (!yes) {
                return;
            }
            String box = getS(v1);
            String to = getS(v4);
            String header = getS(v2);
            String content = getS(v3);
            String from = getS(v5);
            Map<String, String> ma = maPr.getM(box);
            MailToSend mSend = new MailToSend(maPr.getName(ma), ma, header,
                    content, to, from, text);
            w.setL(box, to, header);
            w.show(wi);
            slMediator.getSlContainer().publish(IJavaMailAction.SEND_MAIL,
                    mSend);
        }
    }

    private class WaitForRes implements ISlotListener {

        public void signal(ISlotSignalContext slContext) {
            ICustomObject o = slContext.getCustom();
            MailResult ma = (MailResult) o;
            w.setMailResult(ma);
            String errMess = ma.getErrMess();
            if (errMess == null) {
                errMess = "OK";
            }
            w.setR(errMess);
        }
    }

    private void setAfterBoxChange() {
        String box = getS(v1);
        if (CUtil.EmptyS(box)) {
            return;
        }
        Map<String, String> ma = maPr.getM(box);
        String from = maPr.getFrom(ma);
        if (CUtil.EmptyS(from)) {
            return;
        }
        IFormLineView i = fContainer.findLineView(v5);
        i.setValObj(from);
    }

    private class ChangeBox implements ISlotListener {

        public void signal(ISlotSignalContext slContext) {
            setAfterBoxChange();
        }
    }

    private final class Accept implements ISlotListener {

        public void signal(ISlotSignalContext slContext) {
            IGWidget w = slContext.getGwtWidget();
            List<IVField> li = new ArrayList<IVField>();
            li.add(v1);
            li.add(v2);
            li.add(v3);
            li.add(v4);
            li.add(v5);
            List<InvalidateMess> lMess = ValidateUtil.checkEmpty(fContainer, li);
            if (lMess != null) {
                slMediator.getSlContainer().publish(dType,
                        DataActionEnum.ChangeViewFormToInvalidAction,
                        new InvalidateFormContainer(lMess));
                return;
            }
            YesNoDialog yes = new YesNoDialog(MM.getL().SendingQuestion(),
                    new YesSend(w.getGWidget()));
            yes.show(w.getGWidget());

        }
    }

    private void sendMail() {
        w.setL(startM.getBoxName(), startM.getTo(), startM.getHeader());
        Map<String, String> ma = maPr.getM(startM.getBoxName());
        startM.setBox(ma);
        w.show(wi);
        slMediator.getSlContainer().publish(IJavaMailAction.SEND_MAIL, startM);
    }

    private final class CreateMail implements ISlotListener {

        public void signal(ISlotSignalContext slContext) {
            ICustomObject o = slContext.getCustom();
            maPr = (ListOfMailProperties) o;
            if (showPost) {
                createMailForm();
            } else {
                sendMail();
            }

        }
    }

    MailTest(IDataType dt, MailToSend startM, ListOfControlDesc ldesc,
            boolean showPost, WSize wi) {
        this.dType = dt;
        this.startM = startM;
        this.showPost = showPost;
        this.wi = wi;
        if (startM == null) {
            text = false;
        } else {
            text = startM.isText();
        }
        cuFactories = GwtGiniInjector.getI().getTableFactoriesContainer();
        wFactory = GwtGiniInjector.getI().getEditWidgetFactory();
        dFactory = tFactories.getdViewFactory();
        IJavaMailAction mAction = cuFactories.getJavaMailActionFactory().contruct();
        slMediator.registerSlotContainer(mAction);

        CellId cId = new CellId(1);
        pView = pViewFactory.construct(dType, cId);
        bId = pView.addCellPanel(0, 0);
        fId = pView.addCellPanel(1, 0);
        pView.createView();
        ControlButtonViewFactory buFactory = tFactories.getbViewFactory();
        bView = buFactory.construct(dType, ldesc);
        slMediator.registerSlotContainer(bId, bView);
    }

    @Override
    public void startPublish(CellId cellId) {
        slMediator.registerSlotContainer(cellId, pView);

        slMediator.getSlContainer().registerSubscriber(dType, v1,
                new ChangeBox());
        slMediator.getSlContainer().registerSubscriber(
                IJavaMailAction.SENDLISTMAILPROPERTIES, new CreateMail());
        slMediator.getSlContainer().registerSubscriber(dType,
                ClickButtonType.StandClickEnum.ACCEPT, new Accept());
        slMediator.getSlContainer().registerSubscriber(
                IJavaMailAction.SEND_RESULT, new WaitForRes());
        slMediator.startPublish(null);
        slMediator.getSlContainer().publish(
                IJavaMailAction.ACTIONGETLISTMAILPROPERTIES);

    }
}
