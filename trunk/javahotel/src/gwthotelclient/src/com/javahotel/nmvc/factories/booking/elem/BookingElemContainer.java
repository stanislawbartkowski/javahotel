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
package com.javahotel.nmvc.factories.booking.elem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.gwtmodel.table.AbstractLpVModelData;
import com.gwtmodel.table.DataListTypeFactory;
import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.SynchronizeList;
import com.gwtmodel.table.WChoosedLine;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.common.ISignal;
import com.gwtmodel.table.common.dateutil.DateUtil;
import com.gwtmodel.table.composecontroller.ComposeControllerType;
import com.gwtmodel.table.composecontroller.IComposeControllerTypeFactory;
import com.gwtmodel.table.controler.DataListParam;
import com.gwtmodel.table.controler.DisplayListControlerParam;
import com.gwtmodel.table.controler.IDataControler;
import com.gwtmodel.table.controler.TableDataControlerFactory;
import com.gwtmodel.table.datamodelview.DataViewModelFactory;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.factories.IDataValidateActionFactory;
import com.gwtmodel.table.factories.IFormDefFactory;
import com.gwtmodel.table.factories.IFormTitleFactory;
import com.gwtmodel.table.factories.IGetViewControllerFactory;
import com.gwtmodel.table.factories.IHeaderListFactory;
import com.gwtmodel.table.factories.ITableCustomFactories;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.persist.IMemoryListModel;
import com.gwtmodel.table.persist.MemoryGetController;
import com.gwtmodel.table.persist.MemoryListPersist;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.slotmodel.AbstractSlotMediatorContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotCallerListener;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.slotmodel.SlU;
import com.gwtmodel.table.view.table.IGetCellValue;
import com.gwtmodel.table.view.util.ClickPopUp;
import com.gwtmodel.table.view.util.SetVPanelGwt;
import com.javahotel.client.abstractto.BookElemPayment;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.types.AddType;
import com.javahotel.client.types.DataType;
import com.javahotel.client.types.DataUtil;
import com.javahotel.client.types.VField;
import com.javahotel.client.types.VModelDataFactory;
import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.PaymentRowP;
import com.javahotel.common.util.PUtil;
import com.javahotel.nmvc.factories.booking.util.IsServiceBooking;
import com.javahotel.nmvc.factories.booking.util.P;

public class BookingElemContainer extends AbstractSlotMediatorContainer {

    public static final String BOOKINGELEMPAY = "BOOKING_ELEM_PAY";

    private final SetVPanelGwt sPanel = new SetVPanelGwt();
    private final IDataType publishdType;
    private final IMemoryListModel lPersistList;
    private final IDataControler dControler;
    private final ITableCustomFactories tFactories;
    private final IFormDefFactory fFactory;
    /** Enter data in one line. */
    private final boolean isFlat;
    /** Book data, otherwise add cost. */
    private final boolean isBook;
    private final IDataType bType = new DataType(DictType.BookingList);

    private final StartDraw sDraw = new StartDraw();
    private final IsServiceBooking iService = new IsServiceBooking(
            new ServiceRead());

    private void drawList(BookingP p) {
        List<AbstractLpVModelData> li = new ArrayList<AbstractLpVModelData>();
        if ((p != null) && (p.getBooklist() != null)) {
            for (BookElemP e : p.getBooklist()) {
                // omit all elements not related
                if (iService.isBooking(e.getService()) != isBook) {
                    continue;
                }
                if (!isFlat) {
                    AbstractLpVModelData lp = VModelDataFactory.constructLp(e);
                    li.add(lp);
                    continue;
                }
                for (PaymentRowP pa : e.getPaymentrows()) {
                    BookElemPayment bP = new BookElemPayment(e, pa);
                    AbstractLpVModelData lp = VModelDataFactory.constructLp(bP);
                    li.add(lp);
                }
            }
        }
        lPersistList.setDataList(DataListTypeFactory.constructLp(li));
        dControler.startPublish(new CellId(0));

    }

    /**
     * Class for synchronizing StartPublish and IsServiceBooking
     * 
     * @author hotel
     * 
     */
    private class StartDraw extends SynchronizeList {

        BookingP p;

        StartDraw() {
            super(2);
        }

        @Override
        protected void doTask() {
            drawList(p);
        }
    }

    private class ServiceRead implements ISignal {

        @Override
        public void signal() {
            sDraw.signalDone();

        }

    }

    /**
     * Draw list listener for booking elements
     * 
     * @author hotel
     * 
     */
    private class DrawModel implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            sDraw.p = P.getBookR(slContext);
            sDraw.signalDone();
        }
    }

    /**
     * Call listener for retrieving current list
     * 
     * @author hotel
     * 
     */
    private class SetGetter implements ISlotCallerListener {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            BookingP p = P.getBookR(slContext);
            IDataListType li = lPersistList.getDataList();
            List<BookElemP> ll = new ArrayList<BookElemP>();
            if (p.getBooklist() != null) {
                for (BookElemP b : p.getBooklist()) {
                    String service = b.getService();
                    if (iService.isBooking(service) != isBook) {
                        ll.add(b);
                    }
                }
            }
            for (IVModelData v : li.getList()) {
                BookElemP e;
                if (!isFlat) {
                    e = DataUtil.getData(v);
                } else {
                    BookElemPayment bP = DataUtil.getData(v);
                    e = bP.getO1();
                    PaymentRowP pa = bP.getO2();
                    Date da = e.getCheckIn();
                    pa.setRowTo(da);
                    pa.setRowFrom(da);
                    e.setCheckOut(da);
                    List<PaymentRowP> pLi = new ArrayList<PaymentRowP>();
                    pLi.add(pa);
                    e.setPaymentrows(pLi);
                }
                ll.add(e);
            }
            p.setBooklist(ll);
            return slContext;
        }
    }

    /**
     * Change mode listener
     * 
     * @author hotel
     * 
     */
    private class ChangeMode implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            slMediator.getSlContainer().publish(dType,
                    DataActionEnum.ChangeViewFormModeAction,
                    slContext.getPersistType());
        }

    }

    private final class GetCell implements IGetCellValue {

        @Override
        public SafeHtml getValue(IVModelData v, IVField fie) {
            BookElemP pe = DataUtil.getData(v);
            PUtil.SumP pSum = PUtil.getPrice(pe.getPaymentrows());
            String s = FUtils.getBigDecimalS(pSum.sumCustomer, fie);
            return LogT.getStrongCell().input(s);
        }

        @Override
        public String getCellStyleNames(IVModelData v, IVField fie) {
            return null;
        }
    }
    
    private class GetW implements ISlotListener {
        
        private final WSize wSi;
        
        GetW(WSize wSi) {
            this.wSi = wSi;
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            IGWidget w = slContext.getGwtWidget();
            ClickPopUp p = new ClickPopUp(wSi,w.getGWidget());
        }
        
    }

    private class ClickedSum implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            WChoosedLine w = SlU.getWChoosedLine(slContext);
            int i = w.getChoosedLine();
            IVModelData va = SlU.getVDataByI(dType, BookingElemContainer.this,
                    i);
            BookElemP pe = DataUtil.getData(va);
            IDataType dType = new DataType(AddType.RowPaymentElem);
            PaymentRowDetails pD = new PaymentRowDetails(dType, pe);
            SlU.registerWidgetListener0(dType, pD, new GetW(w.getwSize()));
            SlU.startPublish0(pD);
        }

    }
    
    private class RefreshAfterPersist implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            if (isFlat) { return ; }

            ISlotable i = BookingElemContainer.this;
            IFormLineView f = SlU.getFormLineView(bType, new VField(BookingP.F.customerPrice), i);
            IFormLineView fin = SlU.getFormLineView(bType, new VField(BookingP.F.checkIn), i);
            IFormLineView fout = SlU.getFormLineView(bType, new VField(BookingP.F.checkOut), i);
            IDataListType li = lPersistList.getDataList();
            Date dMin = null;
            Date dMax = null;
            PUtil.SumP sumP = null;
            for (IVModelData v : li.getList()) {
                BookElemP e;
                e = DataUtil.getData(v);
                List<PaymentRowP> pLi = e.getPaymentrows();
                PUtil.SumP s = PUtil.getPrice(pLi);
                if (sumP == null) {
                    sumP = s;
                }
                else {
                    PUtil.addS(sumP, s);
                }
                if (dMin == null) { dMin = e.getCheckIn(); }
                else {
                    dMin = DateUtil.getMinDate(dMin,e.getCheckIn());
                }
                if (dMax == null) { dMax = e.getCheckOut(); }
                else {
                    dMax = DateUtil.getMaxDate(dMax,e.getCheckOut());
                }
            f.setValObj(sumP.sumCustomer);
            fin.setValObj(dMin);
            fout.setValObj(dMax);
            }
        }
        
        
    }

    /**
     * Constructor
     * 
     * @param dType
     *            IDataType
     * @param iContext
     *            ICallContext
     * @param subType
     *            IDataType used for internal stuff
     * @param flat
     *            : true, res and payment in one line
     */
    public BookingElemContainer(IDataType dType, final ICallContext iContext,
            final IDataType subType, boolean flat) {
        this.isFlat = flat;
        DataType dd = (DataType) dType;
        this.isBook = (dd.getAddType() == AddType.BookRoom);
        this.dType = dType;
        fFactory = HInjector.getI().getFormDefFactory();
        tFactories = iContext.getC();
        IFormTitleFactory tiFactory = tFactories.getFormTitleFactory();
        publishdType = iContext.getDType();
        slMediator.getSlContainer().registerSubscriber(subType,
                DataActionEnum.DrawViewFormAction, new DrawModel());

        lPersistList = new MemoryListPersist(dType);
        TableDataControlerFactory tFactory = GwtGiniInjector.getI()
                .getTableDataControlerFactory();

        DataViewModelFactory daFactory = GwtGiniInjector.getI()
                .getDataViewModelFactory();

        IDataValidateActionFactory vFactory = tFactories
                .getDataValidateFactory();

        CellId cI = new CellId(0);
        IDataModelFactory iDataModelFactory = tFactories.getDataModelFactory();
        IComposeControllerTypeFactory custFactory = new IComposeControllerTypeFactory() {

            @Override
            public ComposeControllerType construct(ICallContext iiContext) {
                ISlotable iSlo = new BookingElem(iiContext,
                        BookingElemContainer.this, subType, isFlat, iService,
                        isBook);
                return new ComposeControllerType(iSlo, subType);
            }

        };
        IGetViewControllerFactory iGetCon = new MemoryGetController(fFactory,
                iDataModelFactory, daFactory, lPersistList, vFactory,
                custFactory);

        DisplayListControlerParam cParam = tFactory.constructParam(cI,
                new DataListParam(dType, lPersistList, null, iDataModelFactory,
                        tiFactory, iGetCon), null, new GetCell());
        IHeaderListFactory he = tFactories.getHeaderListFactory();
        dControler = tFactory.constructDataControler(cParam);
        slMediator.registerSlotContainer(dControler);
        slMediator.registerSlotContainer(he.construct(dType));
        slMediator.getSlContainer().registerSubscriber(dType, cI,
                sPanel.constructSetGwt());
        slMediator.getSlContainer().registerCaller(subType,
                GetActionEnum.GetModelToPersist, new SetGetter());
        slMediator.getSlContainer().registerCaller(subType,
                GetActionEnum.GetViewModelEdited, new SetGetter());
        slMediator.getSlContainer().registerSubscriber(subType,
                DataActionEnum.ChangeViewFormModeAction, new ChangeMode());
        slMediator.getSlContainer().registerSubscriber(dType,
                DataActionEnum.TableCellClicked, new ClickedSum());
        lPersistList.getSlContainer().registerSubscriber(dType,
                DataActionEnum.RefreshAfterPersistActionSignal, new  RefreshAfterPersist());

    }

    @Override
    public void startPublish(CellId cellId) {
        slMediator.startPublish(null);
        slMediator.getSlContainer().publish(publishdType, cellId,
                sPanel.constructGWidget());
    }

}
