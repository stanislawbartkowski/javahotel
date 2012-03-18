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
package com.javahotel.nmvc.factories.bookingpanel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.GWidget;
import com.gwtmodel.table.IClickYesNo;
import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGHeader;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.SynchronizeList;
import com.gwtmodel.table.WChoosedLine;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.ISignal;
import com.gwtmodel.table.common.PeriodT;
import com.gwtmodel.table.common.dateutil.DateUtil;
import com.gwtmodel.table.daytimeline.CalendarTable;
import com.gwtmodel.table.daytimeline.CalendarTable.PeriodType;
import com.gwtmodel.table.factories.IDataPersistAction;
import com.gwtmodel.table.factories.IPersistFactoryAction;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.injector.MM;
import com.gwtmodel.table.listdataview.IListDataView;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.IFormChangeListener;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.slotmodel.AbstractSlotMediatorContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.SlU;
import com.gwtmodel.table.view.callback.CommonCallBack;
import com.gwtmodel.table.view.daytimetable.IDrawPartSeason;
import com.gwtmodel.table.view.daytimetable.IDrawPartSeasonContext;
import com.gwtmodel.table.view.daytimetable.IScrollSeason;
import com.gwtmodel.table.view.daytimetable.impl.WidgetScrollSeasonFactory;
import com.gwtmodel.table.view.table.VListHeaderContainer;
import com.gwtmodel.table.view.table.VListHeaderDesc;
import com.gwtmodel.table.view.util.ClickPopUp;
import com.gwtmodel.table.view.util.EventPopUpHint;
import com.gwtmodel.table.view.util.IEventName;
import com.gwtmodel.table.view.util.YesNoDialog;
import com.javahotel.client.ConfigParam;
import com.javahotel.client.GWTGetService;
import com.javahotel.client.IResLocator;
import com.javahotel.client.gename.FFactory;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.rdata.RData;
import com.javahotel.client.types.BackAbstract;
import com.javahotel.client.types.DataType;
import com.javahotel.client.user.season.SeasonUtil;
import com.javahotel.common.command.BookingStateType;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.HotelOpType;
import com.javahotel.common.command.RType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.dateutil.GetPeriods;
import com.javahotel.common.rescache.ReadResParam;
import com.javahotel.common.seasonutil.CreateTableSeason;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.IField;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.OfferSeasonP;
import com.javahotel.common.toobject.ResDayObjectStateP;
import com.javahotel.nmvc.ewidget.EWidgetFactory;
import com.javahotel.nmvc.factories.booking.util.BookingInfo;
import com.javahotel.nmvc.factories.bookingpanel.addtobill.AddToBillDialog;
import com.javahotel.nmvc.factories.bookingpanel.checkinguest.CheckinGuest;
import com.javahotel.nmvc.factories.bookingpanel.invoicelist.BookingInvoiceList;

/**
 * @author hotel
 * 
 */
public class BookingPanel extends AbstractSlotMediatorContainer {

    private final IDataType roomType = new DataType(DictType.RoomObjects);
    private final VerticalPanel v = new VerticalPanel();
    private final IListDataView iList;
    private final IDataPersistAction iPersist;
    private final static int NOM = 15;
    private final IResLocator rI;
    private final GetResCell rCell;
    private final ResServicesCache rCache;
    private final EWidgetFactory eFactory;
    private final WidgetScrollSeasonFactory wFactory;

    // local variable - changeable area
    private IScrollSeason sCr = null;
    // private final IFormLineView seasonE;
    private final String seasonName;
    // private final IFormLineView priceE;
    private final String priceS;

    private Widget dList = null;
    private Widget scrollW = null;
    private HorizontalPanel upPanel = null;
    private List<PeriodT> coP = null;

    interface TemplateHeader extends SafeHtmlTemplates {
        @Template("<div class=\"{0}\">{1}</div><div class=\"{2}\">{3}</div>")
        SafeHtml input(String style, String up, String style1, String down);
    }

    private final static TemplateHeader templateHeader = GWT
            .create(TemplateHeader.class);

    /**
     * Creates or recreates panel widget.
     */
    private void createPanel() {
        // can perform after list and scroll data are read
        if (dList == null || scrollW == null) {
            return;
        }
        // create panel for the first time
        if (upPanel == null) {
            upPanel = new HorizontalPanel();
            upPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
            VerticalPanel va = new VerticalPanel();
            va.add(new Label(seasonName));
            va.add(rCache.getSeasonE().getGWidget());
            va.add(new Label(priceS));
            va.add(rCache.getPriceE().getGWidget());
            upPanel.add(va);
            upPanel.add(scrollW);
            v.add(upPanel);
            v.add(dList);
        } else {
            // replace scrollW only
            upPanel.remove(1);
            upPanel.add(scrollW);
        }
    }

    /**
     * CallBack for DrawC. After re-reading reservation data refresh display
     * panel
     * 
     * @author hotel
     * 
     */
    private class DrawSynch extends SynchronizeList implements ISignal {

        DrawSynch() {
            super(3);
        }

        @Override
        public void signal() {
            signalDone();
        }

        @Override
        protected void doTask() {
            // refresh panel: header and content
            getSlContainer()
                    .publish(roomType, DataActionEnum.RefreshListAction);
        }

    }

    /**
     * Implementation of IDrawpartSeason. Provides refreshment for scrolling
     * 
     * @author hotel
     * 
     */
    private class DrawC implements IDrawPartSeason {

        private final DrawSynch sy;

        DrawC(DrawSynch sy) {
            this.sy = sy;
        }

        @Override
        public void setW(IGWidget w) {
            scrollW = w.getGWidget();
            createPanel();
        }

        private class ReadResName implements ISignal {
            private final List<String> roomList;
            private final PeriodT pe;

            ReadResName(List<String> roomList, PeriodT pe) {
                this.roomList = roomList;
                this.pe = pe;
            }

            @Override
            public void signal() {
                Set<String> sRoom = new HashSet<String>();
                for (String s : roomList) {
                    Date d = pe.getFrom();
                    do {
                        ResDayObjectStateP p = rI.getR().getResState(s, d);
                        assert p != null : LogT.getT().cannotBeNull();
                        sRoom.add(p.getBookName());
                        DateUtil.NextDay(d);
                    } while (DateUtil.compareDate(d, pe.getTo()) < 0);
                }
                ISignal i = new ISignal() {

                    @Override
                    public void signal() {
                        sy.signalDone();
                    }
                };
                rCell.getbCache().readBooking(sRoom, i);
            }
        }

        @Override
        public void refresh(IDrawPartSeasonContext sData) {
            rCell.setsData(sData);
            Date from = sData.getD(sData.getFirstD());
            Date to = sData.getD(sData.getLastD());
            PeriodT pe = new PeriodT(from, to);
            ISlotSignalContext sl = getSlContainer().getGetterContext(roomType,
                    GetActionEnum.GetListData);
            IDataListType dList = sl.getDataList();
            List<String> roomList = new ArrayList<String>();
            for (IVModelData v : dList.getList()) {
                roomList.add(U.getRoomName(v));
            }
            // firstly re-read reservation data
            rI.getR().readResObjectState(new ReadResParam(roomList, pe), sy);
            rCache.createPriceCache(pe, new ReadResName(roomList, pe));
        }

    }

    private class ReadPriceList implements RData.IVectorList<OfferPriceP> {

        private final OfferSeasonP p;
        private final SynchronizeList sy;

        ReadPriceList(OfferSeasonP p, SynchronizeList sy) {
            this.p = p;
            this.sy = sy;
        }

        @Override
        public void doVList(List<OfferPriceP> val) {
            List<OfferPriceP> sList = new ArrayList<OfferPriceP>();
            for (OfferPriceP o : val) {
                if (o.getSeason().equals(p.getName())) {
                    sList.add(o);
                }
            }
            eFactory.setComboDictList(rCache.getPriceE(), sList);
            sy.signalDone();
        }

    }

    private class ReadOffer implements BackAbstract.IRunAction<OfferSeasonP> {

        @Override
        public void action(OfferSeasonP oP) {
            DrawSynch sy = new DrawSynch();
            sCr = wFactory.getScrollSeason(new DrawC(sy), DateUtil.getToday());
            List<Date> dLine = CalendarTable.listOfDates(oP.getStartP(),
                    oP.getEndP(), PeriodType.byDay);
            coP = CreateTableSeason.createTable(oP, ConfigParam.getStartWeek());
            sCr.createVPanel(dLine, NOM);
            CommandParam pa = rI.getR().getHotelCommandParam();
            pa.setDict(DictType.PriceListDict);
            rI.getR().getList(RType.ListDict, pa, new ReadPriceList(oP, sy));
        }

    }

    private class C implements IFormChangeListener {

        @Override
        public void onChange(IFormLineView sEdit, boolean afterFocus) {
            String s = (String) sEdit.getValObj();
            if (CUtil.EmptyS(s)) {
                return;
            }
            new BackAbstract<OfferSeasonP>().readAbstract(
                    DictType.OffSeasonDict, s, new ReadOffer());
        }

    }

    /**
     * The purpose of this class is to enable "click" event for Header
     * 
     * @author hotel
     * 
     */
    private class A extends AbstractCell<SafeHtml> {

        A(SafeHtml a) {
            // activates onBrowserEvent
            super(IEventName.MOUSEOVER, IEventName.MOUSEOUT);
        }

        @Override
        public void render(com.google.gwt.cell.client.Cell.Context context,
                SafeHtml value, SafeHtmlBuilder sb) {
            sb.append(value);
        }

    }

    /**
     * Creates header data for panel, implementation of header interface
     * 
     * @author hotel
     * 
     */
    private class T extends Header<SafeHtml> {

        private final int i;
        private final EventPopUpHint pHint = new EventPopUpHint();

        private PeriodT pe;

        public T(int i) {
            super(new A(U.getEmpty()));
            this.i = i;
            pe = null;
        }

        @Override
        public void onBrowserEvent(Cell.Context context, Element elem,
                NativeEvent event) {
            String sName = SeasonUtil.getName(pe);
            pHint.setMessage(sName);
            pHint.onBrowser(elem, event);
        }

        @Override
        public SafeHtml getValue() {
            if (rCell.getsData() == null) {
                return U.getEmpty();
            }
            SafeHtmlBuilder b = new SafeHtmlBuilder();
            Date da = rCell.getsData().getD(i + rCell.getsData().getFirstD());
            Date todayD = rCell.getsData().getTodayC();
            boolean today = DateUtil.eqDate(da, todayD);
            String na = SeasonUtil.getDateName(da);
            pe = GetPeriods.findPeriod(da, coP);
            String sTyle = SeasonUtil.getStyleForDay(pe);
            String upStyle = sTyle;
            if (today) {
                upStyle = "day_today";
            }
            String week = MM.getWeekdays()[DateUtil.weekDay(da)];
            b.append(templateHeader.input(upStyle, na, sTyle, week));

            return b.toSafeHtml();
        }

    }

    private class GHeader implements IGHeader {

        private final T t;

        GHeader(int i) {
            t = new T(i);
        }

        @Override
        public Header<?> getHeader() {
            return t;
        }

        @Override
        public Column<?, ?> getColumn() {
            return null;
        }

    }

    /**
     * Creates header info containing two parts: room data and reservation panel
     */
    private void sendHeaderInfo() {
        // First part: room info
        IField[] dList = new IField[] { DictionaryP.F.name,
                DictionaryP.F.description };
        List<VListHeaderDesc> fList = FFactory.constructH(null, dList);

        // Second part : reservation columns
        for (int i = 0; i < NOM; i++) {
            RField r = new RField(i);
            GHeader g = new GHeader(i);
            VListHeaderDesc v = new VListHeaderDesc(g, r);
            fList.add(v);
        }

        // Create header and publish
        VListHeaderContainer vHeader = new VListHeaderContainer(fList, null);
        getSlContainer().publish(roomType, vHeader);
    }

    private class GetGWT implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            Widget w = slContext.getGwtWidget().getGWidget();
            dList = w;
            createPanel();
        }

    }

    private class ChangeToStay implements ClickHandler {

        private final String resName;

        ChangeToStay(String resName) {
            this.resName = resName;
        }

        private class RBack extends CommonCallBack<ReturnPersist> {

            @Override
            public void onMySuccess(ReturnPersist ret) {
                if (ret.getIdName() != null) {
                    Window.alert(ret.getIdName());
                    // clear cache (contains previous booking data)
                    rI.getR().invalidateResCache();
                    // redraw
                    sCr.refresh();
                } else {
                    Window.alert(ret.getErrorMessage());
                }
            }
        }

        @Override
        public void onClick(ClickEvent event) {
            IClickYesNo yes = new IClickYesNo() {

                @Override
                public void click(boolean yes) {
                    if (!yes) {
                        return;
                    }
                    CommandParam pa = rI.getR().getHotelCommandParam();
                    pa.setReservName(resName);
                    GWTGetService.getService().hotelOp(
                            HotelOpType.ChangeBookingToStay, pa, new RBack());
                }

            };
            new YesNoDialog("Na pewno zamieniasz tę rezerwację na pobyt ?", yes)
                    .show(new WSize(event.getRelativeElement()));
        }

    }

    private class R implements BackAbstract.IRunAction<BookingP> {

        private final WSize wSize;

        R(WSize w) {
            this.wSize = w;
        }

        @Override
        public void action(BookingP p) {
            CheckinGuest g = new CheckinGuest();
            g.CheckIn(p, wSize);
        }

    }

    private class RToBill implements BackAbstract.IRunAction<BookingP> {

        private final WSize wSize;

        RToBill(WSize w) {
            this.wSize = w;
        }

        @Override
        public void action(BookingP p) {
            AddToBillDialog a = new AddToBillDialog();
            a.addToBill(p, wSize);
        }

    }

    private class CheckInGuests implements ClickHandler {

        private final String resName;

        CheckInGuests(String resName) {
            this.resName = resName;
        }

        @Override
        public void onClick(ClickEvent event) {
            new BackAbstract<BookingP>().readAbstract(DictType.BookingList,
                    resName, new R(new WSize(event.getRelativeElement())));

        }

    }

    private class AddToBill implements ClickHandler {

        private final String resName;

        AddToBill(String resName) {
            this.resName = resName;
        }

        @Override
        public void onClick(ClickEvent event) {
            new BackAbstract<BookingP>()
                    .readAbstract(DictType.BookingList, resName, new RToBill(
                            new WSize(event.getRelativeElement())));
        }

    }

    private class InvoiceList implements ClickHandler {

        private final String resName;

        InvoiceList(String resName) {
            this.resName = resName;
        }

        @Override
        public void onClick(final ClickEvent event) {

            BackAbstract.IRunAction<BookingP> i = new BackAbstract.IRunAction<BookingP>() {

                @Override
                public void action(BookingP t) {
                    new BookingInvoiceList(t.getId(), new WSize(
                            event.getRelativeElement()));
                }

            };
            new BackAbstract<BookingP>().readAbstract(DictType.BookingList,
                    resName, i);
        }

    }

    /**
     * Class called when panel cell is clicked
     * 
     * @author hotel
     * 
     */
    private class RoomCellClicked implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            // retrieve information
            WChoosedLine wC = SlU.getWChoosedLine(slContext);
            WSize wSize = wC.getwSize();
            IVField v = wC.getvField();
            IVModelData vData = SlU.getVDataByW(roomType, iList, wC);
            assert v != null && wSize != null && vData != null : LogT.getT()
                    .cannotBeNull();
            // room number
            String s = U.getRoomName(vData);
            // now day
            RField r = (RField) v;
            Date d = U.getD(rCell.getsData(), r.getI());
            assert d != null : LogT.getT().cannotBeNull();
            ResDayObjectStateP p = rI.getR().getResState(s, d);
            BookingStateType staT = U.getResState(p);
            Widget w = new ResRoomInfo(s);
            // no reservation : room info only
            if (!U.isBooked(staT)) {
                new ClickPopUp(wSize, w);
                return;
            }
            // reservation room info + reservation state
            VerticalPanel ve = new VerticalPanel();
            ve.add(w);
            BookingInfo resW = new BookingInfo(p.getBookName());
            ve.add(resW);
            new ClickPopUp(wSize, ve);
            Button b;
            switch (staT) {
            case WaitingForConfirmation:
            case Confirmed:
                b = new Button("Zamień na pobyt");
                b.addClickHandler(new ChangeToStay(p.getBookName()));
                ve.add(b);
                break;
            case ChangedToCheckin:
            case Stay:
                b = new Button("Zamelduj gości");
                b.addClickHandler(new CheckInGuests(p.getBookName()));
                ve.add(b);
                b = new Button("Dopisz do rachunku");
                b.addClickHandler(new AddToBill(p.getBookName()));
                ve.add(b);
                b = new Button("Lista faktur do pobytu");
                b.addClickHandler(new InvoiceList(p.getBookName()));
                ve.add(b);
                break;
            }
        }

    }

    private class ListRead implements ISlotListener {

        private class R implements ISignal {

            @Override
            public void signal() {
                sendHeaderInfo();
            }

        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            IDataListType dList = slContext.getDataList();
            rCell.getrCache().createCache(dList, new R());
        }

    }

    public BookingPanel(IDataType dType, CellId panelId) {
        this.dType = dType;
        eFactory = HInjector.getI().getEWidgetFactory();
        wFactory = GwtGiniInjector.getI().getWidgetScrollSeasonFactory();
        rI = HInjector.getI().getI();
        FormField f = FFactory.construct(OfferPriceP.F.season);
        IFormLineView seasonE = f.getELine();
        seasonE.addChangeListener(new C());
        seasonName = f.getPLabel();
        f = FFactory.construct(BookingP.F.oPrice);
        IFormLineView priceE = f.getELine();
        priceS = f.getPLabel();
        rCache = new ResServicesCache(seasonE, priceE);
        rCell = new GetResCell(rCache, new BookingResCache());
        iList = tFactories.getlDataFactory().construct(roomType, rCell, false,
                true);
        IPersistFactoryAction persistFactoryA = GwtGiniInjector.getI()
                .getTableFactoriesContainer().getPersistFactoryAction();
        iPersist = persistFactoryA.contruct(roomType);
        slMediator.registerSlotContainer(panelId, iList);
        slMediator.registerSlotContainer(iPersist);
        getSlContainer().registerRedirector(
                tFactories.getSlTypeFactory().construct(roomType,
                        DataActionEnum.ListReadSuccessSignal),
                tFactories.getSlTypeFactory().construct(roomType,
                        DataActionEnum.DrawListAction));
        iList.getSlContainer().registerSubscriber(roomType, panelId,
                new GetGWT());
        // Raised when cell is clicked : action on reservation
        iList.getSlContainer().registerSubscriber(roomType,
                DataActionEnum.TableCellClicked, new RoomCellClicked());

        slMediator.getSlContainer().registerSubscriber(roomType,
                DataActionEnum.ListReadSuccessSignal, new ListRead());
    }

    @Override
    public void startPublish(CellId cellId) {
        slMediator.startPublish(null);
        getSlContainer().publish(roomType, DataActionEnum.ReadListAction);
        getSlContainer().publish(dType, 0, new GWidget(v));
        // sendHeaderInfo();
    }

}
