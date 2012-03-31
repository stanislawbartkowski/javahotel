/*
 *  Copyright 2012 stanislawbartkowski@gmail.com
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package com.gwtmodel.table.editc;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.*;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.common.PersistTypeEnum;
import com.gwtmodel.table.composecontroller.IComposeController;
import com.gwtmodel.table.controlbuttonview.ControlButtonViewFactory;
import com.gwtmodel.table.controlbuttonview.IControlButtonView;
import com.gwtmodel.table.factories.IGetViewControllerFactory;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.injector.MM;
import com.gwtmodel.table.panelview.IPanelView;
import com.gwtmodel.table.slotmodel.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hotel
 */
class EditChooseRecordContainer extends AbstractSlotMediatorContainer implements
        IEditChooseRecordContainer {

    private final VerticalPanel vp = new VerticalPanel();
    private final Sych sy = new Sych();
    private final IDataType publishdType;
    private final ISlotable pSlotable;
    private final static String LIST_BUTTON = "HOTEL-LIST-BUTTON";
    private final CellId bId;
    private final CellId cId;
    private final AddChangeBox cBox = new AddChangeBox(new ChangeC());

    @Override
    public void SetNewChange(boolean newc, boolean changec) {
        cBox.setNewCheck(newc);
        cBox.setChangeCheck(changec);
    }

    @Override
    public void ModifForm() {
        boolean newc = cBox.getNewCheck();
        boolean changec = cBox.getChangeCheck();
        PersistTypeEnum p;
        if (newc) {
            p = PersistTypeEnum.ADD;
        } else {
            if (changec) {
                p = PersistTypeEnum.MODIF;
            } else {
                p = PersistTypeEnum.SHOWONLY;
            }
        }
        modifList(p);
    }

    private void modifList(PersistTypeEnum e) {
        slMediator.getSlContainer().publish(dType,
                DataActionEnum.ChangeViewComposeFormModeAction, e);
        slMediator.getSlContainer().publish(
                dType,
                new ClickButtonType(LIST_BUTTON),
                new ButtonAction(
                e.readOnly() ? ButtonAction.Action.DisableButton
                : ButtonAction.Action.EnableButton));
    }

    @Override
    public boolean getNewCheck() {
        return cBox.getNewCheck();
    }

    @Override
    public boolean getChangeCheck() {
        return cBox.getChangeCheck();
    }

    private class ChangeC implements ITransferClick {

        @Override
        public void signal(IChangeObject o) {
            // send 'change' signal, change box has been check / uncheck
            getSlContainer().publish(IChangeObject.signalString, o);
        }
    }

    private class Sych extends SynchronizeList {

        CellId cellId;
        Widget but;
        Widget cust;

        Sych() {
            super(3);
        }

        @Override
        protected void doTask() {
            HorizontalPanel hp = new HorizontalPanel();
            // two checkboxes
            hp.add(cBox);
            // select button
            hp.add(but);
            vp.add(hp);
            if (cId != null) {
                // widget with record data
                vp.add(cust);
            }
            // public created widget
            pSlotable.getSlContainer().publish(publishdType, cellId,
                    new GWidget(vp));
        }
    }

    private class SetWidget implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            IGWidget gwtWidget = slContext.getGwtWidget();
            sy.but = gwtWidget.getGWidget();
            sy.signalDone();
        }
    }

    private class ChooseC extends AbstractChooseListener {

        /**
         * @param dType
         * @param iSlo
         */
        ChooseC(IDataType dType, ISlotable iSlo) {
            super(dType, iSlo);
        }

        @Override
        void modifAfterSelect() {
            SetNewChange(false, false);
            ModifForm();
        }
    }

    private class SetWidgetCust implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            IGWidget gwtWidget = slContext.getGwtWidget();
            sy.cust = gwtWidget.getGWidget();
            sy.signalDone();
        }
    }

    private void register(ISlotable pView, ISlotable cust) {
        pView.getSlContainer().registerSubscriber(dType, bId, new SetWidget());
        if (cId != null) {
            cust.getSlContainer().registerSubscriber(dType, cId,
                    new SetWidgetCust());
        } else {
            sy.signalDone();
        }
    }

    EditChooseRecordContainer(ICallContext iContext, IDataType publishdType,
            boolean withoutForm) {
        // pSlotable - where to send the SetWidget signal
        if (iContext.iSlo() == null) {
            pSlotable = this;
        } else {
            pSlotable = iContext.iSlo();
        }
        this.dType = iContext.getDType();
        this.publishdType = publishdType;

        ClickButtonType sChoose = new ClickButtonType(LIST_BUTTON);
        ControlButtonDesc bChoose = new ControlButtonDesc(MM.getL().ChooseFromList(), sChoose);
        List<ControlButtonDesc> bList = new ArrayList<ControlButtonDesc>();
        bList.add(bChoose);
        ListOfControlDesc cList = new ListOfControlDesc(bList);
        slMediator.getSlContainer().registerSubscriber(dType, sChoose,
                new ChooseC(dType, slMediator));
        ControlButtonViewFactory bFactory = GwtGiniInjector.getI().getControlButtonViewFactory();
        IControlButtonView bView = bFactory.construct(dType, cList);
        IGetViewControllerFactory fa = GwtGiniInjector.getI().getTableFactoriesContainer().getGetViewControllerFactory();
        bId = new CellId(IPanelView.CUSTOMID);
        if (!withoutForm) {
            cId = new CellId(IPanelView.CUSTOMID + 1);
            IComposeController cust = fa.construct(iContext.construct(dType));
            cust.createComposeControler(cId);

            register(bView, cust);
            slMediator.registerSlotContainer(cId, cust);
        } else {
            register(bView, null);
            cId = null;
        }
        slMediator.registerSlotContainer(bId, bView);
    }

    @Override
    public void startPublish(CellId cellId) {
        sy.cellId = cellId;
        sy.signalDone();
        slMediator.startPublish(null);
    }

    @Override
    public void ChangeViewForm(PersistTypeEnum e) {
        cBox.setReadOnly(e.readOnly());
        modifList(e);
    }
}
