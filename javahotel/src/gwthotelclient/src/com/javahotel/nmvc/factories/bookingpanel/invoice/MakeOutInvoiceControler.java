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
package com.javahotel.nmvc.factories.bookingpanel.invoice;

import java.util.Date;
import java.util.List;

import com.gwtmodel.table.Empty;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.SynchronizeList;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.ISignal;
import com.gwtmodel.table.datamodelview.SignalSetHtmlId;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotCallerListener;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.SlU;
import com.gwtmodel.table.slotmodel.SlotType;
import com.javahotel.client.IResLocator;
import com.javahotel.client.abstractto.InvoicePVData;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.rdata.RData.IVectorList;
import com.javahotel.client.types.BackAbstract;
import com.javahotel.client.types.BackAbstract.IRunAction;
import com.javahotel.client.types.DataType;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.PaymentMethod;
import com.javahotel.common.command.RType;
import com.javahotel.common.dateutil.DateUtil;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.InvoiceIssuerP;
import com.javahotel.common.toobject.InvoiceP;
import com.javahotel.nmvc.factories.booking.util.BookingCustInfo;
import com.javahotel.nmvc.factories.booking.util.IsServiceBooking;

/**
 * @author hotel
 * 
 */
public class MakeOutInvoiceControler extends AbstractSlotContainer {

    private final IResLocator rI;
    private final Synch sy;
    private final BuyerWidget bWidget;
    private final HotelDataWidget hData;
    private final IDataType cuType = new DataType(DictType.CustomerList);
    private final IDataType iInvoiceData = new DataType(
            DictType.IssuerInvoiceList);
    private final IDataType hotelDataType = iInvoiceData;
    private InvoiceLines iLines;
    private final CellId lineId = new CellId(0);
    private final IDataType lineType = Empty.getDataType();
    final static IVField vPayMethod = MapFields.mapS
            .get(InvoiceP.PAYMENT_METHOD);
    final static IVField vNumbOfDays = MapFields.mapS
            .get(InvoiceP.NUMBER_OF_DAYS);
    final static IVField vTermPay = MapFields.mapS.get(InvoiceP.PAYMENT_DATE);
    final static IVField vInvoiceDate = MapFields.mapS
            .get(InvoiceP.INVOICE_DATE);

    private class Synch extends SynchronizeList {

        List<InvoiceIssuerP> val;
        BookingP p;
        IsServiceBooking iService;
        InvoiceP i;

        Synch() {
            super(6);
        }

        @Override
        protected void doTask() {
            iLines = new InvoiceLines(lineType, lineId, sy.p, sy.iService, sy.i);
            iLines.getSlContainer().registerSubscriber(lineType, lineId,
                    new LineWidget());
            bWidget.startPublish(new CellId(0));
            hData.startPublish(new CellId(0));
            iLines.startPublish(lineId);
            drawDefault();
        }
    }

    private void setTodayDate(InvoicePVData pa, String f) {
        IVField v = MapFields.mapS.get(f);
        Object o = pa.getF(v);
        if (o != null) {
            return;
        }
        IFormLineView vie = SlU.getVWidget(dType, this, v);
        vie.setValObj(DateUtil.getToday());
        pa.setF(v, DateUtil.getToday());
    }

    private void drawDefault() {
        IVModelData v = getGetterIVModelData(dType,
                GetActionEnum.GetViewComposeModelEdited);
        InvoicePVData pa = (InvoicePVData) v;
        String sym = (String) pa.getF(MapFields.mapS
                .get(InvoiceP.HOTEL_DATA_SYMBOL));
        if (CUtil.EmptyS(sym)) {
            AbstractTo a = sy.val.get(0);
            hData.getaSelect().setaObject(a);
            hData.getaSelect().copyFields();
        }
        sym = (String) pa.getF(MapFields.mapS.get(InvoiceP.BUYER_SYMBOL));
        if (CUtil.EmptyS(sym)) {
            bWidget.getaSelect().copyFields();
            bWidget.getaSelect().copyFields(MapInvoiceDialog.getMapGuest());
        }
        setTodayDate(pa, InvoiceP.DATE_OF_DSALE);
        setTodayDate(pa, InvoiceP.INVOICE_DATE);
        setDays();
    }

    private class ReadL implements IVectorList<InvoiceIssuerP> {

        private final Synch sy;

        ReadL(Synch sy) {
            this.sy = sy;
        }

        @Override
        public void doVList(List<InvoiceIssuerP> val) {
            sy.val = val;
            sy.signalDone();
        }
    }

    private class GetCustC implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            IGWidget w = slContext.getGwtWidget();
            publish(SignalSetHtmlId.constructSlot(dType), new SignalSetHtmlId(
                    IMap.CHOOSE_BUYER_WIDGET, w));
        }
    }

    private class GetHotelD implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            IGWidget w = slContext.getGwtWidget();
            publish(SignalSetHtmlId.constructSlot(dType), new SignalSetHtmlId(
                    IMap.CHOOSE_HOTEL_DATA, w));
        }

    }

    private class LineWidget implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            SignalSetHtmlId sId = new SignalSetHtmlId(IMap.INVOICE_LINES,
                    slContext.getGwtWidget());
            publish(SignalSetHtmlId.constructSlot(dType), sId);
        }

    }

    private class SetGetter implements ISlotCallerListener {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            IVModelData va = slContext.getVData();
            InvoicePVData pa = (InvoicePVData) va;
            InvoiceP p = pa.getP();
            p.setBooking(sy.p.getId());
            BookingCustInfo bCust = bWidget.constructC(pa);
            p.setCustomer(bCust.getCust().getId());
            pa.setCustomData(bCust);
            // redirect to iLines
            SlotType sl = slTypeFactory.construct(lineType,
                    slContext.getSlType());
            ISlotSignalContext slC = slContextFactory.construct(sl, slContext);
            return iLines.getSlContainer().call(slC);
        }
    }

    private class AfterDisplayed implements ISlotListener {

        private final Synch sy;

        AfterDisplayed(Synch sy) {
            this.sy = sy;
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            IVModelData va = slContext.getVData();
            InvoicePVData pa = (InvoicePVData) va;
            sy.i = pa.getP();
            sy.signalDone();
        }

    }

    private void changeDisable(boolean disable) {
        SlU.changeEnable(dType, this, vNumbOfDays, !disable);
        SlU.changeEnable(dType, this, vTermPay, !disable);
    }

    private void changeAfterPayMethod() {
        IFormLineView vi = SlU.getVWidget(dType, this, vPayMethod);
        Object o = vi.getValObj();
        PaymentMethod pa = (PaymentMethod) o;
        changeDisable(pa != PaymentMethod.Transfer);
    }

    private class ChangeMode implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            bWidget.ChangeModeToShow();
            changeAfterPayMethod();
        }

    }

    private void setDays() {
        Integer l = SlU.getVWidgetValue(dType, this, vNumbOfDays);
        Date dI = SlU.getVWidgetValue(dType, this, vInvoiceDate);

        Date dToOfPay = null;
        if ((l != null) && (dI != null)) {
            dToOfPay = DateUtil.copyDate(dI);
            DateUtil.addDays(dToOfPay, l.intValue());
        }
        SlU.setVWidgetValue(dType, this, vTermPay, dToOfPay);
    }

    private class ChangeMethodValue implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            if (!SlU.afterFocus(slContext)) {
                return;
            }
            changeAfterPayMethod();
        }
    }

    private class ChangeDaysValue implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            if (!SlU.afterFocus(slContext)) {
                return;
            }
            setDays();
        }
    }

    public MakeOutInvoiceControler(ICallContext iContext, IDataType subType) {
        this.dType = iContext.getDType();
        rI = HInjector.getI().getI();
        DataType da = (DataType) iContext.getDType();
        sy = new Synch();
        bWidget = new BuyerWidget(cuType, MapInvoiceDialog.getMapBuyer(),
                MapFields.mapS, this, dType);

        // -----------
        // read reservation record
        // -----------
        final IRunAction<CustomerP> iCustomer = new IRunAction<CustomerP>() {

            @Override
            public void action(CustomerP t) {
                bWidget.setBuyer(t);
                sy.signalDone();
            }
        };

        BackAbstract.IRunAction<BookingP> i = new BackAbstract.IRunAction<BookingP>() {

            @Override
            public void action(BookingP t) {
                sy.p = t;
                sy.signalDone();
                new BackAbstract<CustomerP>().readAbstract(
                        DictType.CustomerList, t.getCustomer(), iCustomer);
            }

        };
        // param : reservation symbol
        new BackAbstract<BookingP>().readAbstract(DictType.BookingList,
                da.getLParam(), i);
        // --------------
        // read services
        // --------------
        ISignal iReady = new ISignal() {

            @Override
            public void signal() {
                sy.signalDone();
            }
        };
        sy.iService = new IsServiceBooking(iReady);
        // ---------------------------------
        // read list of hotel invoice data
        // ---------------------------------
        CommandParam pa = rI.getR().getHotelCommandParam();
        pa.setDict(DictType.IssuerInvoiceList);
        rI.getR().getList(RType.ListDict, pa, new ReadL(sy));

        // -------------------------------
        // invoice lines
        // -------------------------------

        SlU.registerWidgetListener0(cuType, bWidget, new GetCustC());
        hData = new HotelDataWidget(hotelDataType,
                MapInvoiceDialog.getMapHotel(), MapFields.mapS, this, dType);
        SlU.registerWidgetListener0(hotelDataType, hData, new GetHotelD());

        registerCaller(subType, GetActionEnum.GetViewModelEdited,
                new SetGetter());
        registerCaller(subType, GetActionEnum.GetModelToPersist,
                new SetGetter());
        registerSubscriber(dType, DataActionEnum.AfterDrawViewFormAction,
                new AfterDisplayed(sy));
        registerSubscriber(dType, DataActionEnum.AfterChangeModeFormAction,
                new ChangeMode());
        registerSubscriber(dType, vPayMethod, new ChangeMethodValue());
        registerSubscriber(dType, vNumbOfDays, new ChangeDaysValue());
        registerSubscriber(dType, vInvoiceDate, new ChangeDaysValue());
    }

    @Override
    public void startPublish(CellId cellId) {
        sy.signalDone();
    }

}
