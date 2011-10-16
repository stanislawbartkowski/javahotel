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
package com.javahotel.nmvc.factories.bookingpanel.addtobill;

import com.gwtmodel.table.Empty;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.PersistTypeEnum;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.controler.BoxActionMenuOptions;
import com.gwtmodel.table.factories.IFormTitleFactory;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotCallerListener;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.slotmodel.SlU;
import com.gwtmodel.table.slotmodel.SlotSignalContextFactory;
import com.gwtmodel.table.slotmodel.SlotType;
import com.gwtmodel.table.slotmodel.SlotTypeFactory;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.types.AddType;
import com.javahotel.client.types.DataType;
import com.javahotel.client.types.DataTypeSubEnum;
import com.javahotel.client.types.HModelData;
import com.javahotel.client.types.VField;
import com.javahotel.client.types.VModelDataFactory;
import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.BookRecordP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.util.GetMaxUtil;
import com.javahotel.nmvc.factories.booking.GetSlowC;
import com.javahotel.nmvc.factories.booking.elem.BookingElemContainer;
import com.javahotel.nmvc.factories.bookingpanel.checkinguest.RunCompose;
import com.javahotel.nmvc.factories.persist.dict.IHotelPersistFactory;
import com.javahotel.nmvc.factories.persist.dict.IPersistRecord;
import com.javahotel.nmvc.factories.persist.dict.IPersistResult;

/**
 * @author hotel
 * 
 */
public class AddToBillDialog {

    private final RunCompose rCompose;
    private final IDataType dType = Empty.getDataType();
    private final DataType resType = new DataType(AddType.BookNoRoom);
    private final DataType subType = new DataType(resType.getdType(),
            DataTypeSubEnum.Sub1);
    private final SlotTypeFactory slTypeFactory;
    private final IDataType bType = new DataType(DictType.BookingList);
    private final IDataType brType = new DataType(AddType.BookRecord);
    private final SlotSignalContextFactory slContextFactory;
    private final IHotelPersistFactory pFactory;
    private final PersistTypeEnum action = PersistTypeEnum.ADD;

    public AddToBillDialog() {

        slTypeFactory = GwtGiniInjector.getI().getSlotTypeFactory();
        slContextFactory = GwtGiniInjector.getI().getSlotSignalContextFactory();
        pFactory = HInjector.getI().getHotelPersistFactory();

        RunCompose.IRunComposeFactory iFactory = new RunCompose.IRunComposeFactory() {

            @Override
            public IFormTitleFactory getTitle() {
                return new TitleFactory();
            }

            @Override
            public ISlotable constructS(ICallContext iContext, IDataType dType,
                    BookingP p, BoxActionMenuOptions bOptions, SlotType slType) {
                ISlotable iSlo = null;
                iSlo = new BookingElemContainer(resType, iContext, subType,
                        true);

                SlotType from = slTypeFactory.construct(dType,
                        DataActionEnum.DrawViewFormAction);
                SlotType to = slTypeFactory.construct(subType,
                        DataActionEnum.DrawViewFormAction);
                iSlo.getSlContainer().registerRedirector(from, to);
                iSlo.getSlContainer().registerCaller(bType,
                        GetActionEnum.GetFormFieldWidget, new GetValue(p));
                iSlo.getSlContainer().registerCaller(brType,
                        GetActionEnum.GetFormFieldWidget, new GetValue(p));
                iSlo.getSlContainer().registerCaller(GetSlowC.GETSLOTS,
                        new GetSlot(iSlo));

                SlotType validS = slTypeFactory.construct(dType,
                        DataActionEnum.ValidateAction);
                SlotType peristS = slTypeFactory.construct(dType,
                        DataActionEnum.PersistDataAction);
                iSlo.getSlContainer().registerRedirector(validS, peristS);

                iSlo.getSlContainer().registerSubscriber(dType,
                        DataActionEnum.PersistDataAction,
                        new PersistSignaller(iSlo, p));
                bOptions.setAskString(BoxActionMenuOptions.ASK_BEFORE_PERSIST,
                        "Dopisać zmiany do rachunku ?");
                bOptions.setAskStandardResign();

                return iSlo;
            }
        };
        rCompose = new RunCompose(iFactory);
    }

    private class AddBillPersisted implements IPersistResult {

        private final ISlotable iSlo;

        AddBillPersisted(ISlotable iSlo) {
            this.iSlo = iSlo;
        }

        @Override
        public void success(PersistResultContext re) {
            // publish persisted successfully
            iSlo.getSlContainer().publish(dType,
                    DataActionEnum.PersistDataSuccessSignal);
        }

    }

    private class PersistSignaller implements ISlotListener {

        private final ISlotable iSlo;
        private final BookingP p;

        PersistSignaller(ISlotable iSlo, BookingP p) {
            this.iSlo = iSlo;
            this.p = p;
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            HModelData ho = VModelDataFactory.construct(p);
            IVModelData pData = iSlo.getSlContainer().getGetterIVModelData(
                    subType, GetActionEnum.GetModelToPersist, ho);
            IPersistRecord re = pFactory.construct(new DataType(
                    DictType.BookingList), false);
            re.persist(action, ho, new AddBillPersisted(iSlo));
        }

    }

    private class GetSlot implements ISlotCallerListener {

        private final ISlotable iSlo;

        GetSlot(ISlotable iSlo) {
            this.iSlo = iSlo;
        }

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            GetSlowC g = new GetSlowC(iSlo);
            return slContextFactory.construct(slContext.getSlType(), g);
        }

    }

    private class GetValue implements ISlotCallerListener {

        private final BookingP p;

        GetValue(BookingP p) {
            this.p = p;
        }

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            IVField vf = slContext.getVField();
            VField vv = (VField) vf;
            IFormLineView v = null;
            if (vv.getFie() == BookingP.F.season) {
                v = SlU.contructObjectValue(p.getSeason());
            }
            if (vv.getFie() == BookRecordP.F.oPrice) {
                BookRecordP b = null;
                if (p.getBookrecords() != null) {
                    b = GetMaxUtil.getLastBookRecord(p);
                }
                v = SlU.contructObjectValue(b.getOPrice());
            }
            if (v == null) {
                return null;
            }
            return slContextFactory.constructIFormLineView(
                    slContext.getSlType(), v);
        }

    }

    private class TitleFactory implements IFormTitleFactory {

        @Override
        public String getFormTitle(ICallContext iContext) {
            return "Dopisz do rachunku";
        }
    }

    public void addToBill(BookingP p, WSize wSize) {
        rCompose.runDialog(dType, p, wSize, false,null);
    }

}
