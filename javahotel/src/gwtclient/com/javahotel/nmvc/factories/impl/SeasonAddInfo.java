/*
 *  Copyright 2010 stanislawbartkowski@gmail.com
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
package com.javahotel.nmvc.factories.impl;

import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.datelist.AbstractDatePeriodE;
import com.gwtmodel.table.datelist.DatePeriodListFactory;
import com.gwtmodel.table.datelist.IDatePeriodFactory;
import com.gwtmodel.table.datelist.IDatePeriodList;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotCaller;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.view.util.SetVPanelGwt;
import com.javahotel.common.toobject.OfferSeasonP;
import com.javahotel.common.toobject.OfferSeasonPeriodP;
import com.javahotel.common.toobject.SeasonPeriodT;
import com.javahotel.nmvc.common.DataUtil;
import java.util.ArrayList;
import java.util.List;

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

    private class MyPeriodFactory implements IDatePeriodFactory {

        private class DatePeriodS extends AbstractDatePeriodE {
        }

        @Override
        public AbstractDatePeriodE construct(IDataType d) {
            return new DatePeriodS();
        }

        public AbstractDatePeriodE construct() {
            return new DatePeriodS();
        }
    }

    private void toDatePeriodS(AbstractDatePeriodE dest, OfferSeasonPeriodP sou) {
        dest.setdFrom(sou.getStartP());
        dest.setdTo(sou.getEndP());
        dest.setComment(sou.getDescription());
        dest.setCustomData(sou);
    }

    private void toOfferSeasonPeriodP(OfferSeasonPeriodP dest, AbstractDatePeriodE sou,
            SeasonPeriodT periodT) {
        dest.setStartP(sou.getdFrom());
        dest.setEndP(sou.getdTo());
        dest.setDescription(sou.getComment());
        dest.setPeriodT(periodT);
        Object o = sou.getCustomData();
        if (o != null) {
            OfferSeasonPeriodP s = (OfferSeasonPeriodP) o;
            dest.setLId(s.getLId());
        }
    }

    private List<AbstractDatePeriodE> getP(OfferSeasonP sou, SeasonPeriodT periodT) {
        List<AbstractDatePeriodE> li = new ArrayList<AbstractDatePeriodE>();
        if (sou.getPeriods() != null) {
            for (OfferSeasonPeriodP d : sou.getPeriods()) {
                if (d.getPeriodT() != periodT) {
                    continue;
                }
                AbstractDatePeriodE e = pFactory.construct();
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

    private void setPList(OfferSeasonP dest, IDatePeriodList l, SeasonPeriodT periodT) {
        removePeriodE(dest, periodT);
        IDataListType dList = l.getMemTable();
        for (int i = 0; i < dList.rowNo(); i++) {
            IVModelData mo = dList.getRow(i);
            AbstractDatePeriodE e = (AbstractDatePeriodE) mo;
            OfferSeasonPeriodP pe = new OfferSeasonPeriodP();
            toOfferSeasonPeriodP(pe, e, periodT);
            dest.getPeriods().add(pe);
        }
    }

    private class SetGetter implements ISlotCaller {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            OfferSeasonP cust = getSeason(slContext);
            setPList(cust, outsideSeason, SeasonPeriodT.LOW);
            setPList(cust, periodSpecial, SeasonPeriodT.SPECIAL);
            return slContext;
        }
    }

    private void setMList(OfferSeasonP sou, IDatePeriodList mList,
            SeasonPeriodT periodT) {
        List<AbstractDatePeriodE> li = getP(sou,periodT);
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

    public SeasonAddInfo(IDataType dType) {
        this.dType = dType;
        daFactory = GwtGiniInjector.getI().getDatePeriodListFactory();
        pFactory = new MyPeriodFactory();
        outsideSeason = daFactory.construct("Poza sezonem", pFactory, sPanel.constructSetGwt());
        periodSpecial = daFactory.construct("Okresy specjalne", pFactory, sPanel.constructSetGwt());
        registerCaller(GetActionEnum.GetViewModelEdited, dType, new SetGetter());
        registerCaller(GetActionEnum.GetModelToPersist, dType, new SetGetter());
        registerSubscriber(DataActionEnum.DrawViewFormAction, dType,
                new DrawModel());

    }

    @Override
    public void startPublish(CellId cellId) {
        publish(dType, cellId, sPanel.constructGWidget());
    }
}
