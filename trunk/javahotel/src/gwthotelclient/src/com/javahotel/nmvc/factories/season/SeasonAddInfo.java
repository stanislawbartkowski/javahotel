/*
 *  Copyright 2011 stanislawbartkowski@gmail.com
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
package com.javahotel.nmvc.factories.season;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.common.GetMaxUtil;
import com.gwtmodel.table.common.GetMaxUtil.IGetLp;
import com.gwtmodel.table.common.MaxI;
import com.gwtmodel.table.datelist.AbstractDatePeriodE;
import com.gwtmodel.table.datelist.DatePeriodListFactory;
import com.gwtmodel.table.datelist.IDatePeriodFactory;
import com.gwtmodel.table.datelist.IDatePeriodList;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.factories.IDataValidateAction;
import com.gwtmodel.table.factories.IDataValidateActionFactory;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotCaller;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.view.util.ModalDialog;
import com.gwtmodel.table.view.util.OkDialog;
import com.gwtmodel.table.view.util.SetVPanelGwt;
import com.javahotel.client.IResLocator;
import com.javahotel.client.MM;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.types.DataUtil;
import com.javahotel.client.types.HModelData;
import com.javahotel.client.user.season.PanelSeason;
import com.javahotel.common.dateutil.CalendarTable.PeriodType;
import com.javahotel.common.dateutil.DateUtil;
import com.javahotel.common.toobject.OfferSeasonP;
import com.javahotel.common.toobject.OfferSeasonPeriodP;
import com.javahotel.common.toobject.SeasonPeriodT;

/**
 * 
 * @author hotel
 */
public class SeasonAddInfo extends AbstractSlotContainer {

    private final IDatePeriodList outsideSeason;
    private final IDatePeriodList periodSpecial;
    private final DatePeriodListFactory daFactory;
    private final SetVPanelGwt sPanel = new SetVPanelGwt();
    private final MyPeriodFactory pFactory;
    // default ! - not private
    static final String SHOWSEASONSTRING = "SHOW SEASON STRING";

    private class MyPeriodFactory implements IDatePeriodFactory {

        private class DatePeriodS extends AbstractDatePeriodE {
        }

        @Override
        public AbstractDatePeriodE construct(IDataType d) {
            return new DatePeriodS();
        }
    }

    private void toDatePeriodS(AbstractDatePeriodE dest, OfferSeasonPeriodP sou) {
        dest.setdFrom(sou.getStartP());
        dest.setdTo(sou.getEndP());
        dest.setComment(sou.getDescription());
        dest.setCustomData(sou);
    }

    private Long toOfferSeasonPeriodP(OfferSeasonPeriodP dest,
            AbstractDatePeriodE sou, SeasonPeriodT periodT, Long next) {
        dest.setStartP(sou.getdFrom());
        dest.setEndP(sou.getdTo());
        dest.setDescription(sou.getComment());
        dest.setPeriodT(periodT);
        Object o = sou.getCustomData();
        if (o != null) {
            OfferSeasonPeriodP s = (OfferSeasonPeriodP) o;
            dest.setPId(s.getPId());
            return next;
        }
        dest.setPId(next);
        Long l = new Long(next.longValue() + 1);
        return l;
    }

    private List<AbstractDatePeriodE> getP(OfferSeasonP sou,
            SeasonPeriodT periodT) {
        List<AbstractDatePeriodE> li = new ArrayList<AbstractDatePeriodE>();
        if (sou.getPeriods() != null) {
            for (OfferSeasonPeriodP d : sou.getPeriods()) {
                if (d.getPeriodT() != periodT) {
                    continue;
                }
                AbstractDatePeriodE e = pFactory.construct(null);
                toDatePeriodS(e, d);
                li.add(e);
            }
        }
        return li;
    }

    private OfferSeasonP getSeason(ISlotSignalContext slContext) {
        OfferSeasonP cust = DataUtil.getData(slContext);
        return cust;
    }

    private Long getNext(IDataListType dList) {
        IGetLp<IVModelData> i = new IGetLp<IVModelData>() {

            @Override
            public Long getLp(IVModelData t) {
                Object o = t.getCustomData();
                if (o == null) {
                    return new Long(0);
                }
                OfferSeasonPeriodP s = (OfferSeasonPeriodP) o;
                return s.getPId();
            }
        };

        Long next = GetMaxUtil.getNextMax(dList.getList(), i, new Long(1));
        return next;
    }

    private void removePeriodE(OfferSeasonP dest, SeasonPeriodT periodT) {
        if (dest.getPeriods() == null) {
            dest.setPeriods(new ArrayList<OfferSeasonPeriodP>());
        }
        List<OfferSeasonPeriodP> li = dest.getPeriods();
        List<OfferSeasonPeriodP> toRemove = new ArrayList<OfferSeasonPeriodP>();
        for (OfferSeasonPeriodP p : li) {
            if (p.getPeriodT() == periodT) {
                toRemove.add(p);
            }
        }
        li.removeAll(toRemove);
    }

    private Long setPList(OfferSeasonP dest, IDatePeriodList l,
            SeasonPeriodT periodT, Long pNext) {
        removePeriodE(dest, periodT);
        IDataListType dList = l.getMemTable();
        // Long next = getNext(dList);
        Long next = pNext;
        for (IVModelData mo : dList.getList()) {
            AbstractDatePeriodE e = (AbstractDatePeriodE) mo;
            OfferSeasonPeriodP pe = new OfferSeasonPeriodP();
            next = toOfferSeasonPeriodP(pe, e, periodT, next);
            dest.getPeriods().add(pe);
        }
        return next;
    }

    private class SetGetter implements ISlotCaller {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            OfferSeasonP cust = getSeason(slContext);
            // both: low and special season are kept in the same list
            // 'lid' identifiers should consider both sublists
            Long l1 = getNext(outsideSeason.getMemTable());
            Long l2 = getNext(periodSpecial.getMemTable());
            Long next = MaxI.max(l1, l2);
            next = setPList(cust, outsideSeason, SeasonPeriodT.LOW, next);
            setPList(cust, periodSpecial, SeasonPeriodT.SPECIAL, next);
            return slContext;
        }
    }

    private void setMList(OfferSeasonP sou, IDatePeriodList mList,
            SeasonPeriodT periodT) {
        List<AbstractDatePeriodE> li = getP(sou, periodT);
        IDataListType dList = daFactory.construct(li);
        mList.setMemTable(dList);
    }

    private class DrawModel implements ISlotSignaller {

        @Override
        public void signal(ISlotSignalContext slContext) {
            OfferSeasonP cust = getSeason(slContext);

            setMList(cust, outsideSeason, SeasonPeriodT.LOW);
            setMList(cust, periodSpecial, SeasonPeriodT.SPECIAL);
        }
    }

    private class DrawSeason implements ISlotSignaller {

        private final PanelSeason pS;
        private final Grid g = new Grid(2, 1);
        private final IResLocator rI;
        private final HorizontalPanel controlC = new HorizontalPanel();
        private final ICallContext iContext;
        private final IDataModelFactory dFactory;

        private class DrawD extends ModalDialog {

            DrawD(VerticalPanel vp) {
                super(vp, "Pokaż sezony");
                create();
            }

            @Override
            protected void addVP(VerticalPanel vp) {
                vp.add(g);
                vp.add(controlC);
            }
        }

        DrawSeason(ICallContext iContext, IDataType ddType) {
            rI = HInjector.getI().getI();
            pS = new PanelSeason(rI, g, controlC, 0, null, DateUtil.getToday());
            this.iContext = iContext;
            dFactory = iContext.getC().getDataModelFactory();
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            IVModelData mData = dFactory.construct(dType);
            IVModelData pData = iContext
                    .iSlo()
                    .getSlContainer()
                    .getGetterIVModelData(dType,
                            GetActionEnum.GetViewComposeModelEdited, mData);
            HModelData v = (HModelData) pData;
            OfferSeasonP off = (OfferSeasonP) v.getA();
            Date from = off.getStartP();
            Date to = off.getEndP();
            IGWidget w = slContext.getGwtWidget();
            if ((from == null) || (to == null)) {
                OkDialog ok = new OkDialog("Wprowadź datę od i do !",
                        "Nie można nic wyświetlić", null);
                ok.show(w.getGWidget());
                return;
            }
            pS.drawPa(off, PeriodType.byDay);
            VerticalPanel vp = new VerticalPanel();
            ModalDialog m = new DrawD(vp);
            m.show(w.getGWidget());
        }
    }

    public SeasonAddInfo(ICallContext iContext, IDataType ddType) {
        this.dType = iContext.getDType();
        daFactory = GwtGiniInjector.getI().getDatePeriodListFactory();
        pFactory = new MyPeriodFactory();
        IDataValidateActionFactory vFactory = new IDataValidateActionFactory() {

            @Override
            public IDataValidateAction construct(IDataType dType) {
                return new ValidateS(dType);
            }
        };

        outsideSeason = daFactory.construct(MM.L().LowSeasonName(), pFactory,
                sPanel.constructSetGwt(), vFactory);
        periodSpecial = daFactory.construct(MM.L().SpecialSeasonName(),
                pFactory, sPanel.constructSetGwt(), vFactory);
        registerCaller(ddType, GetActionEnum.GetViewModelEdited,
                new SetGetter());
        registerCaller(ddType, GetActionEnum.GetModelToPersist, new SetGetter());
        registerSubscriber(ddType, DataActionEnum.DrawViewFormAction,
                new DrawModel());
        registerSubscriber(SHOWSEASONSTRING, new DrawSeason(iContext, ddType));

    }

    @Override
    public void startPublish(CellId cellId) {
        publish(dType, cellId, sPanel.constructGWidget());
    }
}
