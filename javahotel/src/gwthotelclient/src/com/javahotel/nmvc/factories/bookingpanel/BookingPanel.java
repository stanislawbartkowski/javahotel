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
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.FieldDataType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGHeader;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.SynchronizeList;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.factories.IDataPersistAction;
import com.gwtmodel.table.factories.IPersistFactoryAction;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.listdataview.IListDataView;
import com.gwtmodel.table.slotmodel.AbstractSlotMediatorContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.view.table.VListHeaderContainer;
import com.gwtmodel.table.view.table.VListHeaderDesc;
import com.gwtmodel.table.view.util.ClickPopUp;
import com.gwtmodel.table.view.util.SetVPanelGwt;
import com.javahotel.client.ConfigParam;
import com.javahotel.client.IResLocator;
import com.javahotel.client.gename.FFactory;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.rdata.RData;
import com.javahotel.client.types.DataType;
import com.javahotel.client.user.season.SeasonUtil;
import com.javahotel.client.user.widgets.stable.IDrawPartSeason;
import com.javahotel.client.user.widgets.stable.IScrollSeason;
import com.javahotel.client.user.widgets.stable.impl.WidgetScrollSeasonFactory;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.dateutil.CalendarTable;
import com.javahotel.common.dateutil.CalendarTable.PeriodType;
import com.javahotel.common.dateutil.DateUtil;
import com.javahotel.common.dateutil.GetPeriods;
import com.javahotel.common.dateutil.PeriodT;
import com.javahotel.common.scrollseason.model.DaySeasonScrollData;
import com.javahotel.common.seasonutil.CreateTableSeason;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.IField;
import com.javahotel.common.toobject.OfferSeasonP;

/**
 * @author hotel
 * 
 */
public class BookingPanel extends AbstractSlotMediatorContainer {

    private final IDataType roomType = new DataType(DictType.RoomObjects);
    private final SetVPanelGwt v = new SetVPanelGwt();
    private final IListDataView iList;
    private final IDataPersistAction iPersist;
    private final static int NOM = 15;
    private IScrollSeason sCr;

    private class DrawR extends SynchronizeList {

        private Widget dList;
        private Widget scrollW;
        private List<PeriodT> coP;

        DrawR() {
            super(2);
        }

        @Override
        protected void doTask() {
            v.getvPanel().add(scrollW);
            v.getvPanel().add(dList);
        }

    }

    private class RField implements IVField {

        @SuppressWarnings("unused")
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
    
    private class A extends AbstractCell<SafeHtml> {
        
        A(SafeHtml a) {
            super("click");
        }

        /* (non-Javadoc)
         * @see com.google.gwt.cell.client.AbstractCell#render(com.google.gwt.cell.client.Cell.Context, java.lang.Object, com.google.gwt.safehtml.shared.SafeHtmlBuilder)
         */
        @Override
        public void render(com.google.gwt.cell.client.Cell.Context context,
                SafeHtml value, SafeHtmlBuilder sb) {
            sb.append(value);            
        }
        
    }

    private class T extends Header<SafeHtml> {

        private String headerT = "";
        @SuppressWarnings("unused")
        private final int i;
        private boolean today;
        private String sTyle;
        private PeriodT pe;

        public T(int i) {
            super(new A(new SafeHtmlBuilder().appendEscaped("").toSafeHtml()));
            this.i = i;
            today = false;
            sTyle = null;
            pe = null;
        }
        
        @Override
        public void onBrowserEvent(Cell.Context context, Element elem, NativeEvent event) {
            if (pe == null) { return; }
            Widget w = SeasonUtil.createPeriodPopUp(pe);
            new ClickPopUp(new WSize(elem), w);
            
        }

        private void addStyle(SafeHtmlBuilder b, String st, String content,
                String h) {
            String ht = "<div class=\"" + st + "\"";
            if (h != null) {
                ht += " style=\"line-height:" + h + ";\" ";
            }
            ht += ">";
            b.appendHtmlConstant(ht);
            b.appendHtmlConstant(content);
            b.appendHtmlConstant("</div>");

        }

        @Override
        public SafeHtml getValue() {
            SafeHtmlBuilder b = new SafeHtmlBuilder();
            if (today) {
                addStyle(b, SeasonUtil.getTodayStyle(), headerT, null);
            } else {
                b.appendEscaped(headerT);
            }
            if (sTyle != null) {
                addStyle(b, sTyle, "&nbsp;", "70%");
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

        @SuppressWarnings("rawtypes")
        @Override
        public Header getHeader() {
            return t;
        }

        T getT() {
            return t;
        }

    }

    private void sendHeaderInfo() {
        IField[] dList = new IField[] { DictionaryP.F.name,
                DictionaryP.F.description };
        List<VListHeaderDesc> fList = FFactory.constructH(null, dList);

        for (int i = 0; i < NOM; i++) {
            RField r = new RField(i);
            GHeader g = new GHeader(i);
            VListHeaderDesc v = new VListHeaderDesc(g, r);
            fList.add(v);
        }

        VListHeaderContainer vHeader = new VListHeaderContainer(fList, "");
        getSlContainer().publish(roomType, vHeader);
    }

    private class GetGWT implements ISlotSignaller {

        private final DrawR d;

        GetGWT(DrawR d) {
            this.d = d;
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            Widget w = slContext.getGwtWidget().getGWidget();
            d.dList = w;
            d.signalDone();
        }

    }

    private List<GHeader> getHList() {
        ISlotSignalContext slContext = getSlContainer().getGetterContext(
                roomType, GetActionEnum.GetHeaderList);
        VListHeaderContainer listHeader = slContext.getListHeader();
        List<GHeader> g = new ArrayList<GHeader>();
        for (VListHeaderDesc v : listHeader.getAllHeList()) {
            GHeader gElem = (GHeader) v.getgHeader();
            if (gElem != null) {
                g.add(gElem);
            }
        }
        return g;
    }

    private class DrawC implements IDrawPartSeason {

        private final DrawR d;

        DrawC(DrawR d) {
            this.d = d;
        }

        @Override
        public void setW(IGWidget w) {
            d.scrollW = w.getGWidget();
            d.signalDone();
        }

        @Override
        public void refresh(DaySeasonScrollData sData) {
            List<GHeader> gList = getHList();

            for (int i = sData.getFirstD(); i <= sData.getLastD(); i++) {
                int pos = i - sData.getFirstD();
                GHeader gElem = gList.get(pos);
                Date pe = sData.getD(i);
                Date todayD = sData.getTodayC();
                gElem.getT().today = DateUtil.eqDate(pe, todayD);
                String na = SeasonUtil.getDateName(pe);
                gElem.getT().setHeaderT(na);
                gElem.getT().pe = GetPeriods.findPeriod(pe, d.coP);
                gElem.getT().sTyle = SeasonUtil.getStyleForDay(gElem.getT().pe);
            }
            getSlContainer()
                    .publish(roomType, DataActionEnum.RefreshListAction);
        }

    }

    private class ReadOffer implements RData.IVectorList {

        private final DrawR d;

        ReadOffer(DrawR d) {
            this.d = d;
        }

        @Override
        public void doVList(List<? extends AbstractTo> val) {
            AbstractTo a = val.get(0);
            sCr = WidgetScrollSeasonFactory.getScrollSeason(new DrawC(d),
                    DateUtil.getToday());
            OfferSeasonP oP = (OfferSeasonP) a;
            List<Date> dLine = CalendarTable.listOfDates(oP.getStartP(),
                    oP.getEndP(), PeriodType.byDay);
            d.coP = CreateTableSeason.createTable(oP,
                    ConfigParam.getStartWeek());
            sCr.createVPanel(dLine, NOM);
        }

    }

    public BookingPanel(IDataType dType, CellId panelId) {
        this.dType = dType;
        iList = this.tFactories.getlDataFactory().construct(roomType);
        IPersistFactoryAction persistFactoryA = GwtGiniInjector.getI()
                .getTableFactoriesContainer().getPersistFactoryAction();
        iPersist = persistFactoryA.contruct(roomType);
        slMediator.registerSlotContainer(panelId, iList);
        slMediator.registerSlotContainer(iPersist);
        getSlContainer().registerRedirector(
                tFactories.getSlTypeFactory().construct(
                        DataActionEnum.ListReadSuccessSignal, roomType),
                tFactories.getSlTypeFactory().construct(
                        DataActionEnum.DrawListAction, roomType));
        DrawR d = new DrawR();
        iList.getSlContainer().registerSubscriber(roomType, panelId,
                new GetGWT(d));
        IResLocator i = HInjector.getI().getI();
        CommandParam p = i.getR().getHotelCommandParam();
        p.setDict(DictType.OffSeasonDict);
        i.getR().getList(RType.ListDict, p, new ReadOffer(d));
    }

    @Override
    public void startPublish(CellId cellId) {
        slMediator.startPublish(null);
        getSlContainer().publish(roomType, DataActionEnum.ReadListAction);
        getSlContainer().publish(dType, 0, v.constructGWidget());
        sendHeaderInfo();
    }

}
