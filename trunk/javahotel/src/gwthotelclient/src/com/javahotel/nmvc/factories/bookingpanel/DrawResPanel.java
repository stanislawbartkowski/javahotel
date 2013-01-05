/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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

import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.ISetGWidget;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.SynchronizeList;
import com.gwtmodel.table.common.ISignal;
import com.gwtmodel.table.common.PeriodT;
import com.gwtmodel.table.common.dateutil.DateUtil;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.view.daytimetable.IDrawPartSeason;
import com.gwtmodel.table.view.daytimetable.IDrawPartSeasonContext;
import com.javahotel.client.IResLocator;
import com.javahotel.common.rescache.ReadResParam;
import com.javahotel.common.toobject.ResDayObjectStateP;

/**
 * @author hotel
 * 
 */
class DrawResPanel {

    private final GetResCell rCell;
    private final ResServicesCache rCache;
    private final IResLocator rI;
    private final IDataType roomType;
    private final ISlotable iSlo;

    DrawResPanel(IDataType roomType, ISlotable iSlo, IResLocator rI,
            GetResCell rCell, ResServicesCache rCache) {
        this.roomType = roomType;
        this.iSlo = iSlo;
        this.rI = rI;
        this.rCache = rCache;
        this.rCell = rCell;
    }

    /**
     * Implementation of IDrawpartSeason. Provides refreshment for scrolling
     * 
     * @author hotel
     * 
     */
    private class DrawC implements IDrawPartSeason {

        private final SynchronizeList sy;
        private final ISetGWidget iSet;

        DrawC(SynchronizeList sy, ISetGWidget iSet) {
            this.sy = sy;
            this.iSet = iSet;
        }

        @Override
        public void setW(IGWidget w) {
            iSet.setW(w);
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
                        if (p.getBookName() != null) {
                            sRoom.add(p.getBookName());
                        }
                        d = DateUtil.NextDayD(d);
                    } while (DateUtil.compareDate(d, pe.getTo()) < 1);
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
            final PeriodT pe = new PeriodT(from, DateUtil.NextDayD(to));
            ISlotSignalContext sl = iSlo.getSlContainer().getGetterContext(
                    roomType, GetActionEnum.GetListData);
            IDataListType dList = sl.getDataList();
            final List<String> roomList = new ArrayList<String>();
            for (IVModelData v : dList.getList()) {
                roomList.add(U.getRoomName(v));
            }
            ISignal i = new ISignal() {

                @Override
                public void signal() {
                    // after reading reservation data
                    rCache.createPriceCache(pe, new ReadResName(roomList, pe));
                }
            };
            // firstly re-read reservation data
            rI.getR().readResObjectState(new ReadResParam(roomList, pe), i);
        }

    }

    IDrawPartSeason construct(SynchronizeList sy, ISetGWidget iSet) {
        return new DrawC(sy, iSet);
    }

    /**
     * @return the rCell
     */
    public GetResCell getrCell() {
        return rCell;
    }

    /**
     * @return the rCache
     */
    public ResServicesCache getrCache() {
        return rCache;
    }

}
