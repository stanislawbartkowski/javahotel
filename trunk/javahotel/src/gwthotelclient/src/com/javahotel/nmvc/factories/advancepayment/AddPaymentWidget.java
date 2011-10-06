/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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
package com.javahotel.nmvc.factories.advancepayment;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.AbstractLpVModelData;
import com.gwtmodel.table.DataListTypeFactory;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.InvalidateFormContainer;
import com.gwtmodel.table.InvalidateMess;
import com.gwtmodel.table.composecontroller.ComposeControllerType;
import com.gwtmodel.table.composecontroller.IComposeControllerTypeFactory;
import com.gwtmodel.table.controler.BoxActionMenuOptions;
import com.gwtmodel.table.controler.DataListParam;
import com.gwtmodel.table.controler.DisplayListControlerParam;
import com.gwtmodel.table.controler.IDataControler;
import com.gwtmodel.table.controler.TableDataControlerFactory;
import com.gwtmodel.table.datamodelview.DataViewModelFactory;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.factories.IDataValidateAction;
import com.gwtmodel.table.factories.IDataValidateActionFactory;
import com.gwtmodel.table.factories.IFormDefFactory;
import com.gwtmodel.table.factories.IFormTitleFactory;
import com.gwtmodel.table.factories.IGetViewControllerFactory;
import com.gwtmodel.table.factories.IHeaderListContainer;
import com.gwtmodel.table.factories.IHeaderListFactory;
import com.gwtmodel.table.factories.ITableCustomFactories;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.persist.IMemoryListModel;
import com.gwtmodel.table.persist.MemoryGetController;
import com.gwtmodel.table.persist.MemoryListPersist;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.AbstractSlotMediatorContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.view.ValidateUtil;
import com.gwtmodel.table.view.table.VListHeaderContainer;
import com.gwtmodel.table.view.table.VListHeaderDesc;
import com.gwtmodel.table.view.util.SetVPanelGwt;
import com.javahotel.client.gename.FFactory;
import com.javahotel.client.types.VModelDataFactory;
import com.javahotel.common.toobject.BillP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.IField;
import com.javahotel.common.toobject.PaymentP;
import com.javahotel.common.util.BillUtil;

/**
 * @author hotel
 * 
 */
class AddPaymentWidget extends AbstractSlotMediatorContainer {

    private final IMemoryListModel lPersistList;
    private final SetVPanelGwt sPanel = new SetVPanelGwt();
    private final IDataControler dControler;
    private final BookingP p;
    private final ITableCustomFactories tFactories;
    private final IDataType publishType;

    private class DrawModel implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            List<AbstractLpVModelData> li = new ArrayList<AbstractLpVModelData>();
            BillP bi = BillUtil.getBill(p);
            List<IVModelData> vlist = new ArrayList<IVModelData>();
            for (PaymentP pa : bi.getPayments()) {
                vlist.add(VModelDataFactory.constructLp(pa));
            }
            lPersistList.setDataList(DataListTypeFactory.constructLp(li));
            dControler.startPublish(new CellId(0));
        }
    }

    private class CreateFormClass implements IFormDefFactory {

        @Override
        public FormLineContainer construct(ICallContext iContext) {
            List<FormField> fList = new ArrayList<FormField>();
            FFactory.add(fList, getF());
            return new FormLineContainer(fList);
        }

    }

    private class ValidateFactory implements IDataValidateActionFactory {

        private class Validate extends AbstractSlotContainer implements
                IDataValidateAction {

            private class ValidateA implements ISlotListener {

                @Override
                public void signal(ISlotSignalContext slContext) {
                    IVModelData pData = getGetterIVModelData(dType,
                            GetActionEnum.GetViewComposeModelEdited);
                    List<IVField> li = new ArrayList<IVField>();
                    FFactory.addI(li, getF());
                    List<InvalidateMess> errMess = ValidateUtil.checkEmpty(
                            pData, li, null);
                    if (errMess != null) {
                        publish(dType, DataActionEnum.InvalidSignal,
                                new InvalidateFormContainer(errMess));
                        return;
                    }
                    publish(dType, DataActionEnum.ValidSignal);
                }

            }

            Validate() {
                registerSubscriber(dType, DataActionEnum.ValidateAction,
                        new ValidateA());
            }
        }

        @Override
        public IDataValidateAction construct(IDataType dType) {
            return new Validate();
        }

    }

    private class GetTitleFactory implements IFormTitleFactory {

        @Override
        public String getFormTitle(ICallContext iContext) {
            // TODO Auto-generated method stub
            return "xxxxxxxxxxxxxxxx";
        }

    }

    private IField[] getF() {
        IField[] eList = new IField[] { PaymentP.F.datePayment,
                PaymentP.F.amount };
        return eList;

    }

    private class HeaderListContainer extends AbstractSlotContainer implements
            IHeaderListContainer {

        private final VListHeaderContainer vHeader;

        HeaderListContainer(IDataType dType) {
            this.dType = dType;
            IField[] eList = getF();
            List<VListHeaderDesc> fList = FFactory.constructH(null, eList);
            vHeader = new VListHeaderContainer(fList, "XXX");
        }

        @Override
        public void startPublish(CellId cellId) {
            publish(dType, vHeader);
        }
    }

    AddPaymentWidget(IDataType publishType, IDataType dType, BookingP p,
            BoxActionMenuOptions bOptions) {

        this.p = p;
        this.dType = dType;
        this.publishType = publishType;
        lPersistList = new MemoryListPersist(dType);
        slMediator.getSlContainer().registerSubscriber(publishType,
                DataActionEnum.DrawViewFormAction, new DrawModel());
        tFactories = GwtGiniInjector.getI().getTableFactoriesContainer();

        IDataModelFactory iDataModelFactory = tFactories.getDataModelFactory();
        DataViewModelFactory daFactory = GwtGiniInjector.getI()
                .getDataViewModelFactory();
        // IComposeControllerTypeFactory custFactory = new
        // IComposeControllerTypeFactory() {
        //
        // @Override
        // public ComposeControllerType construct(ICallContext iiContext) {
        // return null;
        // }
        // };
        TableDataControlerFactory tFactory = GwtGiniInjector.getI()
                .getTableDataControlerFactory();

        IGetViewControllerFactory iGetCon = new MemoryGetController(
                new CreateFormClass(), iDataModelFactory, daFactory,
                lPersistList, new ValidateFactory(), /* custFactory */null);
        CellId cI = new CellId(0);
        DisplayListControlerParam cParam = tFactory.constructParam(cI,
                new DataListParam(dType, lPersistList, null, iDataModelFactory,
                        new GetTitleFactory(), iGetCon), null);
        dControler = tFactory.constructDataControler(cParam);
        slMediator.registerSlotContainer(dControler);
        slMediator.registerSlotContainer(new HeaderListContainer(dType));

        slMediator.getSlContainer().registerSubscriber(dType, cI,
                sPanel.constructSetGwt());
    }

    @Override
    public void startPublish(CellId cellId) {
        slMediator.startPublish(null);
        slMediator.getSlContainer().publish(publishType, cellId,
                sPanel.constructGWidget());

    }

}
