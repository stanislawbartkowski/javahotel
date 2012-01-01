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
package com.javahotel.nmvc.factories.advancepayment;

import java.util.List;

import com.gwtmodel.table.Empty;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.controler.BoxActionMenuOptions;
import com.gwtmodel.table.factories.IFormTitleFactory;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.slotmodel.SlotType;
import com.gwtmodel.table.slotmodel.SlotTypeFactory;
import com.gwtmodel.table.view.callback.CommonCallBack;
import com.javahotel.client.GWTGetService;
import com.javahotel.client.IResLocator;
import com.javahotel.client.M;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.types.AddType;
import com.javahotel.client.types.DataType;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.HotelOpType;
import com.javahotel.common.command.RType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.PaymentP;
import com.javahotel.nmvc.factories.booking.util.RunCompose;

/**
 * @author hotel
 * 
 */
class AddPayment {

    private final RunCompose rCompose;
    static final IDataType dType = Empty.getDataType();
    private final SlotTypeFactory slTypeFactory;
    private final IResLocator rI;

    private class PersistSignaller implements ISlotListener {

        private final AddPaymentWidget pW;
        private final BookingP p;

        PersistSignaller(AddPaymentWidget pW, BookingP p) {
            this.pW = pW;
            this.p = p;
        }

        private class Persisted extends CommonCallBack<ReturnPersist> {

            @Override
            public void onMySuccess(ReturnPersist arg) {
                // invalidate, force reading from database to display payment
                // properly
                rI.getR().invalidateCache(RType.DownPayments, RType.ListDict);
                // publish success
                pW.getSlContainer().publish(dType,
                        DataActionEnum.PersistDataSuccessSignal);
            }

        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            List<PaymentP> pLi = pW.getList();
            CommandParam co = rI.getR().getHotelCommandParam();
            co.setListP(pLi);
            co.setoP(HotelOpType.AddDownPayment);
            co.setReservName(p.getName());
            GWTGetService.getService().hotelOp(co, new Persisted());
        }

    }

    AddPayment() {
        rI = HInjector.getI().getI();
        slTypeFactory = GwtGiniInjector.getI().getSlotTypeFactory();

        RunCompose.IRunComposeFactory iFactory = new RunCompose.IRunComposeFactory() {

            @Override
            public IFormTitleFactory getTitle() {
                return new IFormTitleFactory() {

                    @Override
                    public String getFormTitle(ICallContext iContext) {
                        return M.L().PaymentToAdvance();
                    }
                };
            }

            @Override
            public ISlotable constructS(ICallContext iContext, IDataType dType,
                    BookingP p, BoxActionMenuOptions bOptions, SlotType slType) {
                AddPaymentWidget iSlo = new AddPaymentWidget(dType,
                        new DataType(AddType.Payment), p, bOptions);
                bOptions.setAskString(BoxActionMenuOptions.ASK_BEFORE_PERSIST,
                        M.L().QuestionSavePaymentsToAdvance());
                bOptions.setAskStandardResign();
                // skip validate - just persist
                SlotType validS = slTypeFactory.construct(dType,
                        DataActionEnum.ValidateAction);
                SlotType peristS = slTypeFactory.construct(dType,
                        DataActionEnum.PersistDataAction);
                iSlo.getSlContainer().registerRedirector(validS, peristS);
                iSlo.getSlContainer().registerSubscriber(dType,
                        DataActionEnum.PersistDataAction,
                        new PersistSignaller(iSlo, p));
                return iSlo;
            }
        };
        rCompose = new RunCompose(iFactory);
    }

    public void addPayment(BookingP p, WSize wSize, ISlotable iSlo) {
        rCompose.runDialog(dType, p, wSize, false, iSlo);
    }

}
