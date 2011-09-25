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
package com.javahotel.nmvc.factories.bookingpanel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.gwtmodel.table.FieldDataType;
import com.gwtmodel.table.GWidget;
import com.gwtmodel.table.IClickYesNo;
import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGHeader;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.WChoosedLine;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.ISignal;
import com.gwtmodel.table.factories.IDataPersistAction;
import com.gwtmodel.table.factories.IPersistFactoryAction;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.listdataview.IListDataView;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.IFormChangeListener;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.slotmodel.AbstractSlotMediatorContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.SlU;
import com.gwtmodel.table.view.callback.CommonCallBack;
import com.gwtmodel.table.view.table.IGetCellValue;
import com.gwtmodel.table.view.table.VListHeaderContainer;
import com.gwtmodel.table.view.table.VListHeaderDesc;
import com.gwtmodel.table.view.util.ClickPopUp;
import com.gwtmodel.table.view.util.YesNoDialog;
import com.javahotel.client.ConfigParam;
import com.javahotel.client.GWTGetService;
import com.javahotel.client.IResLocator;
import com.javahotel.client.M;
import com.javahotel.client.gename.FFactory;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.types.BackAbstract;
import com.javahotel.client.types.DataType;
import com.javahotel.client.types.VField;
import com.javahotel.client.user.season.SeasonUtil;
import com.javahotel.client.user.widgets.stable.IDrawPartSeason;
import com.javahotel.client.user.widgets.stable.IScrollSeason;
import com.javahotel.client.user.widgets.stable.impl.WidgetScrollSeasonFactory;
import com.javahotel.common.command.BookingStateType;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.HotelOpType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.dateutil.CalendarTable;
import com.javahotel.common.dateutil.CalendarTable.PeriodType;
import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.dateutil.DateUtil;
import com.javahotel.common.dateutil.GetPeriods;
import com.javahotel.common.dateutil.PeriodT;
import com.javahotel.common.rescache.ReadResParam;
import com.javahotel.common.scrollseason.model.DaySeasonScrollData;
import com.javahotel.common.seasonutil.CreateTableSeason;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.BookingStateP;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.IField;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.OfferSeasonP;
import com.javahotel.common.toobject.ResDayObjectStateP;
import com.javahotel.nmvc.factories.bookingpanel.addtobill.AddToBillDialog;
import com.javahotel.nmvc.factories.bookingpanel.checkinguest.CheckinGuest;

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

    // local variable - changeable area
    private IScrollSeason sCr = null;
    private final IFormLineView seasonE;
    private final String seasonName;
    private Widget dList = null;
    private Widget scrollW = null;
    private HorizontalPanel upPanel = null;
    private List<PeriodT> coP = null;
    private DaySeasonScrollData sData;

    interface TemplateClass extends SafeHtmlTemplates {
        @Template("<div class=\"{0}\">{1} </div>")
        SafeHtml input(String style, String content);
    }

    // style=\"line-height:" + h + ";\" ";
    interface TemplateStyleHeight extends SafeHtmlTemplates {
        @Template("<div class=\"{0}\" style=\"line-height:{1};\">{2}</div>")
        SafeHtml input(String style, String height, String content);
    }

    private final static TemplateStyleHeight templateHeight = GWT
            .create(TemplateStyleHeight.class);
    private final static TemplateClass templateClass = GWT
            .create(TemplateClass.class);

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
            va.add(seasonE.getGWidget());
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

    private SafeHtml getEmpty() {
        return new SafeHtmlBuilder().appendEscaped("").toSafeHtml();
    }

    private Date getD(int i) {
        if (sData == null) {
            return null;
        }
        Date d = sData.getD(i + sData.getFirstD());
        return d;
    }

    private String getRoomName(IVModelData v) {
        String s = (String) v.getF(new VField(DictionaryP.F.name));
        return s;
    }

    /**
     * CallBack for DrawC. After re-reading reservation data refresh display
     * panel
     * 
     * @author hotel
     * 
     */
    private class DrawList implements ISignal {

        @Override
        public void signal() {
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

        @Override
        public void setW(IGWidget w) {
            scrollW = w.getGWidget();
            createPanel();
        }

        @Override
        public void refresh(DaySeasonScrollData res) {
            sData = res;
            Date from = sData.getD(sData.getFirstD());
            Date to = sData.getD(sData.getLastD());
            PeriodT pe = new PeriodT(from, to);
            ISlotSignalContext sl = getSlContainer().getGetterContext(roomType,
                    GetActionEnum.GetListData);
            IDataListType dList = sl.getDataList();
            List<String> roomList = new ArrayList<String>();
            for (IVModelData v : dList.getList()) {
                roomList.add(getRoomName(v));
            }
            // firstly re-read reservation data
            rI.getR().readResObjectState(new ReadResParam(roomList, pe),
                    new DrawList());
        }

    }

    private class ReadOffer implements BackAbstract.IRunAction<OfferSeasonP> {

        @Override
        public void action(OfferSeasonP oP) {
            sCr = WidgetScrollSeasonFactory.getScrollSeason(new DrawC(),
                    DateUtil.getToday());
            List<Date> dLine = CalendarTable.listOfDates(oP.getStartP(),
                    oP.getEndP(), PeriodType.byDay);
            coP = CreateTableSeason.createTable(oP, ConfigParam.getStartWeek());
            sCr.createVPanel(dLine, NOM);
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
     * Local implementation for IVField. Contains only column number.
     * 
     * @author hotel
     * 
     */
    private class RField implements IVField {

        /**
         * @return the i
         */
        int getI() {
            return i;
        }

        private final int i;

        RField(int i) {
            this.i = i;
        }

        @Override
        public boolean eq(IVField o) {
            return false;
        }

        @Override
        public FieldDataType getType() {
            return null;
        }

        @Override
        public String getId() {
            return null;
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
            super("click");
        }

        @Override
        public void render(com.google.gwt.cell.client.Cell.Context context,
                SafeHtml value, SafeHtmlBuilder sb) {
            sb.append(value);
        }

    }

    /**
     * Modify html style
     * 
     * @param b
     *            SafeHtmlBuilder
     * @param st
     *            Html style to be added to line
     * @param content
     *            Tag contennt
     * @param h
     *            If not null add line-height css modifier
     */
    private void addStyle(SafeHtmlBuilder b, String st, String content, String h) {
        if (h != null) {
            b.append(templateHeight.input(st, h, content));
        } else {
            b.append(templateClass.input(st, content));
        }

    }

    /**
     * Creates header data for panel, implementation of header interface
     * 
     * @author hotel
     * 
     */
    private class T extends Header<SafeHtml> {

        private String headerT = "";
        private final int i;

        private PeriodT pe;

        public T(int i) {
            super(new A(getEmpty()));
            this.i = i;
            pe = null;
        }

        @Override
        public void onBrowserEvent(Cell.Context context, Element elem,
                NativeEvent event) {
            // do not test the type of event, always 'click'
            if (pe == null) {
                return;
            }
            Widget w = SeasonUtil.createPeriodPopUp(pe);
            new ClickPopUp(new WSize(elem), w);

        }

        @Override
        public SafeHtml getValue() {
            if (sData == null) {
                return getEmpty();
            }
            SafeHtmlBuilder b = new SafeHtmlBuilder();
            Date da = sData.getD(i + sData.getFirstD());
            Date todayD = sData.getTodayC();
            boolean today = DateUtil.eqDate(da, todayD);
            String na = SeasonUtil.getDateName(da);
            setHeaderT(na);
            pe = GetPeriods.findPeriod(da, coP);
            String sTyle = SeasonUtil.getStyleForDay(pe);

            if (today) {
                addStyle(b, SeasonUtil.getTodayStyle(), headerT, null);
            } else {
                b.appendEscaped(headerT);
            }
            if (sTyle != null) {
                // addStyle(b, sTyle, "&nbsp;", "70%");
                addStyle(b, sTyle, ".", "70%");
            }
            return b.toSafeHtml();
        }

        /**
         * @param headerT
         *            the headerT to set
         */
        void setHeaderT(String headerT) {
            this.headerT = headerT;
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
        VListHeaderContainer vHeader = new VListHeaderContainer(fList, "XXX");
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

    private BookingStateType getResState(ResDayObjectStateP p) {
        assert p != null : M.M().ResStateCannotBeNull();
        BookingStateP staP = p.getLState();
        BookingStateType staT = null;
        if (staP != null) {
            staT = staP.getBState();
        }
        return staT;
    }

    /**
     * Test if reservation cell is in booked stated
     * 
     * @param p
     *            BookingStateType (can be null)
     * @return True if booked
     */
    private boolean isBooked(BookingStateType p) {
        if (p == null) {
            return false;
        }
        return p.isBooked();
    }

    /**
     * Procedure providing data for reservation columns.
     * 
     * @author hotel
     * 
     */
    private class GetResCell implements IGetCellValue {

        /**
         * Get value for reservation column
         */
        @Override
        public SafeHtml getValue(IVModelData v, IVField fie) {

            RField r = (RField) fie;
            Date d = getD(r.getI());
            if (d == null) {
                // possible the first time only
                return getEmpty();
            }
            // room number
            String s = getRoomName(v);
            IResLocator rI = HInjector.getI().getI();
            // reservation data for room and day
            ResDayObjectStateP p = rI.getR().getResState(s, d);
            // as a content display the day.
            String ss = DateFormatUtil.toS(p.getD());
            BookingStateType staT = getResState(p);
            String sTyle = null;
            // enrich cell only if there is a reservation
            if (isBooked(staT)) {
                switch (staT) {
                case WaitingForConfirmation:
                    sTyle = "reserved-no-confirmed";
                    break;
                case Confirmed:
                    sTyle = "reserved-confirmed";
                    break;
                case Stay:
                    sTyle = "reserved-stay";
                    break;
                default:
                    assert false : LogT.getT().notExpected();
                }
            }
            SafeHtmlBuilder b = new SafeHtmlBuilder();
            if (sTyle != null) {
                addStyle(b, sTyle, ss, null);
            } else {
                // do not enrich display if not booked at all
                b.appendEscaped(ss);
            }
            return b.toSafeHtml();
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

    /**
     * Class called when panel cell is clicked
     * 
     * @author hotel
     * 
     */
    private class RoomCellClicked implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            IResLocator rI = HInjector.getI().getI();
            // retrieve information
            WChoosedLine wC = SlU.getWChoosedLine(slContext);
            WSize wSize = wC.getwSize();
            IVField v = wC.getvField();
            IVModelData vData = SlU.getVDataByW(roomType, iList, wC);
            assert v != null && wSize != null && vData != null : LogT.getT()
                    .cannotBeNull();
            // room number
            String s = getRoomName(vData);
            // now day
            RField r = (RField) v;
            Date d = getD(r.getI());
            assert d != null : LogT.getT().cannotBeNull();
            ResDayObjectStateP p = rI.getR().getResState(s, d);
            BookingStateType staT = getResState(p);
            Widget w = new ResRoomInfo(s);
            // no reservation : room info only
            if (!isBooked(staT)) {
                new ClickPopUp(wSize, w);
                return;
            }
            // reservation room info + reservation state
            VerticalPanel ve = new VerticalPanel();
            ve.add(w);
            BookingInfo resW = new BookingInfo(rI, p.getBookName());
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
                break;
            }
        }

    }

    public BookingPanel(IDataType dType, CellId panelId) {
        this.dType = dType;
        rI = HInjector.getI().getI();
        // invalidate/refresh cache at the beginning
        // force reading data from database
//        rI.getR().invalidateResCache();
        FormField f = FFactory.construct(OfferPriceP.F.season);
        seasonE = f.getELine();
        seasonE.addChangeListener(new C());
        seasonName = f.getPLabel();
        iList = this.tFactories.getlDataFactory().construct(roomType,
                new GetResCell());
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
    }

    @Override
    public void startPublish(CellId cellId) {
        slMediator.startPublish(null);
        getSlContainer().publish(roomType, DataActionEnum.ReadListAction);
        getSlContainer().publish(dType, 0, new GWidget(v));
        sendHeaderInfo();
    }

}
