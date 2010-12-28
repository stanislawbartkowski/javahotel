/*
 *  Copyright 2010 stanislawbartkowski@gmail.com
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
import com.gwtmodel.table.ChooseDictList;
import com.gwtmodel.table.ChooseDictList.ICallBackWidget;
import com.gwtmodel.table.GWidget;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.PersistTypeEnum;
import com.gwtmodel.table.SynchronizeList;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.composecontroller.IComposeController;
import com.gwtmodel.table.controlbuttonview.ControlButtonViewFactory;
import com.gwtmodel.table.controlbuttonview.IControlButtonView;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.factories.IGetViewControllerFactory;
import com.gwtmodel.table.factories.ITableCustomFactories;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.injector.TablesFactories;
import com.gwtmodel.table.panelview.IPanelView;
import com.gwtmodel.table.slotmediator.ISlotMediator;
import com.gwtmodel.table.slotmodel.AbstractSlotMediatorContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.view.util.ModalDialog;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hotel
 */
class EditChooseRecordContainer extends AbstractSlotMediatorContainer implements IEditChooseRecordContainer {

    private final VerticalPanel vp = new VerticalPanel();
    private final Sych sy = new Sych();
    private final IDataType publishdType;
    private final ISlotable pSlotable;
  //  private final IDataType subType;
//    private final ISlotMediator slMediator;
//    private final IDataModelFactory daFactory;
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
            hp.add(cBox);
            hp.add(but);
            vp.add(hp);
            vp.add(cust);
            pSlotable.getSlContainer().publish(publishdType, cellId, new GWidget(vp));
        }
    }

    private class SetWidget implements ISlotSignaller {

        @Override
        public void signal(ISlotSignalContext slContext) {
            IGWidget gwtWidget = slContext.getGwtWidget();
            sy.but = gwtWidget.getGWidget();
            sy.signalDone();
        }
    }

    private class ChooseC implements ISlotSignaller {

        private class SelectC implements ICallBackWidget<IVModelData> {

            private D d;

            private class D extends ModalDialog {

                private final Widget w;

                D(VerticalPanel vp, Widget w) {
                    super(vp, "");
                    this.w = w;
                    create();
                }

                @Override
                protected void addVP(VerticalPanel vp) {
                    vp.add(w);
                }
            }

            @Override
            public void setWidget(WSize ws, IGWidget w) {
                VerticalPanel vp = new VerticalPanel();
                d = new D(vp, w.getGWidget());
                d.show(ws);
            }

            @Override
            public void setChoosed(IVModelData vData, IVField comboFie) {
                assert vData != null : LogT.getT().cannotBeNull();
                LogT.getL().info(LogT.getT().choosedEdit(vData.toString()));
                slMediator.getSlContainer().publish(
                        DataActionEnum.DrawViewComposeFormAction, dType, vData);
                slMediator.getSlContainer().publish(
                        DataActionEnum.ChangeViewComposeFormModeAction, dType, PersistTypeEnum.SHOWONLY);
                SetNewChange(false, false);
                d.hide();
            }

            @Override
            public void setResign() {
                d.hide();
            }
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            WSize w = new WSize(slContext.getGwtWidget().getGWidget());
            ChooseDictList<IVModelData> c = new ChooseDictList<IVModelData>(dType, w, new SelectC());
        }
    }

    private class SetWidgetCust implements ISlotSignaller {

        @Override
        public void signal(ISlotSignalContext slContext) {
            IGWidget gwtWidget = slContext.getGwtWidget();
            sy.cust = gwtWidget.getGWidget();
            sy.signalDone();
        }
    }

    private void register(ISlotable pView, ISlotable cust) {
        pView.getSlContainer().registerSubscriber(dType, bId,
                new SetWidget());
        cust.getSlContainer().registerSubscriber(dType, cId, new SetWidgetCust());
    }

    EditChooseRecordContainer(ICallContext iContext, IDataType publishdType, IDataType subType) {
        pSlotable = iContext.iSlo();
        this.dType = iContext.getDType();
//        this.subType = subType;
        this.publishdType = publishdType;
//        TablesFactories tFactories = iContext.getT();

 //       ITableCustomFactories fContainer = GwtGiniInjector.getI().getTableFactoriesContainer();
//        daFactory = fContainer.getDataModelFactory();
//        slMediator = tFactories.getSlotMediatorFactory().construct();

        ClickButtonType sChoose = new ClickButtonType(LIST_BUTTON);
        ControlButtonDesc bChoose = new ControlButtonDesc("Wybierz z listy",
                sChoose);
        List<ControlButtonDesc> bList = new ArrayList<ControlButtonDesc>();
        bList.add(bChoose);
        ListOfControlDesc cList = new ListOfControlDesc(bList);
        slMediator.getSlContainer().registerSubscriber(sChoose, new ChooseC());
        ControlButtonViewFactory bFactory = GwtGiniInjector.getI().getControlButtonViewFactory();
        IControlButtonView bView = bFactory.construct(dType, cList);
        IGetViewControllerFactory fa = GwtGiniInjector.getI().getTableFactoriesContainer().getGetViewControllerFactory();
        bId = new CellId(IPanelView.CUSTOMID);
        cId = new CellId(IPanelView.CUSTOMID + 1);
        IComposeController cust = fa.construct(iContext.construct(dType));
        cust.createComposeControle(cId);

        register(bView, cust);
        slMediator.registerSlotContainer(cId, cust);
        slMediator.registerSlotContainer(bId, bView);
        
//        slMediator.registerSlotContainer(pSlotable);
    }

    @Override
    public void startPublish(CellId cellId) {
        sy.cellId = cellId;
        sy.signalDone();
        slMediator.startPublish(null);
//        slMediator.registerSlotContainer(pSlotable);
    }
}
