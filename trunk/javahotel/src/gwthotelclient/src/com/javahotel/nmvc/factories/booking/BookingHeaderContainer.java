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
package com.javahotel.nmvc.factories.booking;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.InvalidateFormContainer;
import com.gwtmodel.table.InvalidateMess;
import com.gwtmodel.table.PersistTypeEnum;
import com.gwtmodel.table.datamodelview.DataViewModelFactory;
import com.gwtmodel.table.datamodelview.IDataViewModel;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.factories.IFormDefFactory;
import com.gwtmodel.table.factories.ITableCustomFactories;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.panelview.IPanelView;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.slotmodel.AbstractSlotMediatorContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotCaller;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.slotmodel.SlotSignalContextFactory;
import com.gwtmodel.table.view.ValidateUtil;
import com.gwtmodel.table.view.util.SetVPanelGwt;
import com.javahotel.client.MM;
import com.javahotel.client.abstractto.AUtil;
import com.javahotel.client.types.AddType;
import com.javahotel.client.types.DataType;
import com.javahotel.client.types.HModelData;
import com.javahotel.client.types.VField;
import com.javahotel.client.types.VModelDataFactory;
import com.javahotel.common.command.BookingEnumTypes;
import com.javahotel.common.command.BookingStateType;
import com.javahotel.common.command.DictType;
import com.javahotel.common.dateutil.DateUtil;
import com.javahotel.common.toobject.AdvancePaymentP;
import com.javahotel.common.toobject.BillP;
import com.javahotel.common.toobject.BookRecordP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.BookingStateP;
import com.javahotel.common.util.BillUtil;
import com.javahotel.common.util.GetMaxUtil;

public class BookingHeaderContainer extends AbstractSlotMediatorContainer {

    private final DataType aType;
    private final IDataModelFactory daFactory;

    private final IDataType publishdType;
    private final IDataType subType;
    private final CellId cId = new CellId(IPanelView.CUSTOMID);
    private final CellId aId = new CellId(IPanelView.CUSTOMID + 1);
    private final SetVPanelGwt sPanel = new SetVPanelGwt();
    private final SlotSignalContextFactory sFactory;
    private final DataViewModelFactory daViewFactory;

    private final IDataViewModel bookModel;
    private final IDataViewModel headerModel;

    private void drawV(IDataType paType, IVModelData v) {
        slMediator.getSlContainer().publish(paType,
                DataActionEnum.DrawViewFormAction, v);
    }

    private class DrawModel implements ISlotSignaller {

        @Override
        public void signal(ISlotSignalContext slContext) {
            BookRecordP p = P.getBookR(slContext);
            IVModelData pData;
            if (p == null) {
                pData = daFactory.construct(dType);
            } else {
                pData = VModelDataFactory.construct(p);
            }
            drawV(dType, pData);

            AdvancePaymentP pa = P.getAdvanced(slContext);
            IVModelData aData;
            if (pa != null) {
                aData = VModelDataFactory.construct(pa);
            } else {
                aData = daFactory.construct(aType);
            }
            drawV(aType, aData);
        }
    }

    private void register() {
        bookModel.getSlContainer().registerSubscriber(dType, cId,
                sPanel.constructSetGwt());
        headerModel.getSlContainer().registerSubscriber(aType, aId,
                sPanel.constructSetGwt());
    }

    private class SetGetter implements ISlotCaller {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            BookingP b = getBook(slContext);

            BookingStateP st = null;
            List<BookingStateP> bLi = b.getState();
            if (bLi != null) {
                st = GetMaxUtil.getLastStateRecord(b);
            }
            if ((st == null)
                    || st.getBState() != BookingStateType.WaitingForConfirmation) {
                if (bLi == null) {
                    bLi = new ArrayList<BookingStateP>();
                }
                BookingStateP sta = new BookingStateP();
                sta.setBState(BookingStateType.WaitingForConfirmation);
                GetMaxUtil.setNextLp(bLi, sta);
                bLi.add(sta);
                b.setState(bLi);
            }

            IVModelData vData = daFactory.construct(dType);
            IVModelData pData = slMediator.getSlContainer()
                    .getGetterIVModelData(dType,
                            GetActionEnum.GetViewModelEdited, vData);
            HModelData vv = (HModelData) pData;
            BookRecordP p = (BookRecordP) vv.getA();
            p.setDataFrom(DateUtil.getToday());
            List<BookRecordP> l = new ArrayList<BookRecordP>();
            GetMaxUtil.setNextLp(l, p);
            l.add(p);
            b.setBookrecords(l);

            vData = daFactory.construct(aType);
            pData = slMediator.getSlContainer().getGetterIVModelData(aType,
                    GetActionEnum.GetViewModelEdited, vData);
            vv = (HModelData) pData;
            AdvancePaymentP a = (AdvancePaymentP) vv.getA();

            List<AdvancePaymentP> ll = new ArrayList<AdvancePaymentP>();
            if (!AUtil.emptyAdvance(a)) {
                GetMaxUtil.setNextLp(ll, a);
                ll.add(a);
            }

            List<BillP> bL = new ArrayList<BillP>();
            BillP bb = BillUtil.createPaymentBill();
            bb.setAdvancePay(ll);
            bL.add(bb);
            b.setBill(bL);

            if (b.getBookingType() == null) {
                b.setBookingType(BookingEnumTypes.Reservation);
            }
            return slContext;
        }
    }

    private class GetSlot implements ISlotCaller {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            GetSlowC g = new GetSlowC(slMediator);
            return sFactory.construct(slContext.getSlType(), g);
        }

    }

    private BookingP getBook(ISlotSignalContext slContext) {
        IVModelData mData = slContext.getVData();
        HModelData vData = (HModelData) mData;
        BookingP b = (BookingP) vData.getA();
        return b;
    }

    private class ValidateA implements ISlotSignaller {

        @Override
        public void signal(ISlotSignalContext slContext) {
            PersistTypeEnum en = slContext.getPersistType();
            if (en == PersistTypeEnum.REMOVE) {
                slMediator.getSlContainer().publish(subType,
                        DataActionEnum.ValidSignal);
                return;
            }
            DataType d = new DataType(DictType.BookingList);
            IVModelData pData = slMediator.getSlContainer()
                    .getGetterIVModelData(d,
                            GetActionEnum.GetViewComposeModelEdited);
            BookRecordP p = P.getBookR(pData);
            if (p.getBooklist().size() == 0) {
                List<InvalidateMess> errMess = new ArrayList<InvalidateMess>();
                errMess.add(new InvalidateMess(new VField(
                        BookRecordP.F.customerPrice), MM.L().EnterReservation()));
                InvalidateFormContainer e = new InvalidateFormContainer(errMess);
                slMediator.getSlContainer().publish(dType,
                        DataActionEnum.ChangeViewFormToInvalidAction, e);
                return;
            }
            List<IVField> listE = new ArrayList<IVField>();
            listE.add(new VField(BookRecordP.F.customerPrice));
            listE.add(new VField(BookRecordP.F.oPrice));
            IVModelData bData = VModelDataFactory.construct(p);
            List<InvalidateMess> errMess = ValidateUtil
                    .checkEmpty(bData, listE);
            if (errMess != null) {
                slMediator.getSlContainer().publish(dType,
                        DataActionEnum.ChangeViewFormToInvalidAction,
                        new InvalidateFormContainer(errMess));
                return;
            }

            slMediator.getSlContainer().publish(subType,
                    DataActionEnum.ValidSignal);
        }

    }

    class ChangeMode implements ISlotSignaller {

        @Override
        public void signal(ISlotSignalContext slContext) {
            slMediator.getSlContainer().publish(dType,
                    DataActionEnum.ChangeViewFormModeAction,
                    slContext.getPersistType());
            slMediator.getSlContainer().publish(aType,
                    DataActionEnum.ChangeViewFormModeAction,
                    slContext.getPersistType());
        }

    }

    public BookingHeaderContainer(ICallContext iContext, IDataType subType,
            IFormDefFactory reFactory) {
        publishdType = iContext.getDType();
        this.subType = subType;
        daViewFactory = GwtGiniInjector.getI().getDataViewModelFactory();
        sFactory = GwtGiniInjector.getI().getSlotSignalContextFactory();
        ITableCustomFactories fContainer = GwtGiniInjector.getI()
                .getTableFactoriesContainer();
        daFactory = fContainer.getDataModelFactory();

        dType = new DataType(AddType.BookRecord);
        aType = new DataType(AddType.AdvanceHeader);

        FormLineContainer fCont = reFactory
                .construct(iContext.construct(dType));
        bookModel = daViewFactory.construct(dType, fCont);
        slMediator.registerSlotContainer(cId, bookModel);
        fCont = reFactory.construct(iContext.construct(aType));
        headerModel = daViewFactory.construct(aType, fCont);
        slMediator.registerSlotContainer(aId, headerModel);
        register();

        this.getSlContainer().registerSubscriber(subType,
                DataActionEnum.DrawViewFormAction, new DrawModel());
        this.getSlContainer().registerCaller(subType,
                GetActionEnum.GetViewModelEdited, new SetGetter());
        this.getSlContainer().registerCaller(subType,
                GetActionEnum.GetModelToPersist, new SetGetter());
        this.getSlContainer().registerSubscriber(subType,
                DataActionEnum.ValidateAction, new ValidateA());
        this.getSlContainer().registerSubscriber(subType,
                DataActionEnum.ChangeViewFormModeAction, new ChangeMode());

        this.getSlContainer().registerCaller(GetSlowC.GETSLOTS, new GetSlot());
    }

    @Override
    public void startPublish(CellId cellId) {
        this.getSlContainer().publish(publishdType, cellId,
                sPanel.constructGWidget());
        slMediator.startPublish(null);
    }
}