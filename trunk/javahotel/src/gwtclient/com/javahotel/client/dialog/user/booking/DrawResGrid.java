/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.dialog.user.booking;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.WidgetSizeFactory;
import com.javahotel.client.roominfo.RoomInfoData;
import com.javahotel.common.command.BookingStateType;
import com.javahotel.common.command.ISignal;
import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.dateutil.DateUtil;
import com.javahotel.common.dateutil.PeriodT;
import com.javahotel.common.rescache.ReadResParam;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.BookingStateP;
import com.javahotel.common.toobject.ResDayObjectStateP;
import com.javahotel.common.toobject.ResObjectP;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@SuppressWarnings("deprecation")
class DrawResGrid {

    private final IResLocator rI;
    private final ResRoomTable resList;
    private final PanelResCalendar pCa;
    private final Grid g;
    private final int startC;
    private final int startL = 1;
    private final RoomInfoData rInfo;
    private final Date today;

    DrawResGrid(final IResLocator rI, final ResRoomTable resList,
            final PanelResCalendar pCa, final Grid g, final Date today) {
        this.rI = rI;
        this.resList = resList;
        this.pCa = pCa;
        this.g = g;
        this.today = today;
        this.startC = resList.colS();
        rInfo = new RoomInfoData(rI);
    }

    private class SetObjectRowState implements ISignal {

        private final List<String> rList;
        private final List<Date> dLine;

        SetObjectRowState(final List<String> r, final List<Date> d) {
            this.rList = r;
            this.dLine = d;
        }

        private class RClick implements ClickListener {

            private final String resName;
            private final Date d;
            private final ResDayObjectStateP p;

            RClick(final String resName, final Date d,
                    final ResDayObjectStateP p) {
                this.resName = resName;
                this.d = d;
                this.p = p;
            }

            public void onClick(Widget sender) {
                PopUpInfoRes pRes = new PopUpInfoRes(rI, rList, dLine, pCa, p,
                        d);
                pRes.showDialog(WidgetSizeFactory.getW(sender), rInfo, resName);
            }
        }

        public void signal() {
            int row = 0;
            int col;
            for (final String s : rList) {
                col = 0;
                int nu = -1;
                for (final Date d : dLine) {
                    nu++;
                    if (nu < pCa.getStartNo()) {
                        continue;
                    }
                    ResDayObjectStateP p = rI.getR().getResState(s, d);
                    assert p != null : "Reservation state, cannot be null";
                    BookingStateP staP = p.getLState();
                    BookingStateType staT = null;
                    if (staP != null) {
                        staT = staP.getBState();
                    }
                    String ss = DateFormatUtil.toS(p.getD());
                    int ro = startL + row;
                    int co = startC + col;
                    Label inC = new Label(ss);
                    inC.addClickListener(new RClick(s, d, p));
                    g.setWidget(ro, co, inC);
                    CellFormatter fo = g.getCellFormatter();
                    if ((staT == null) || (staT == BookingStateType.Canceled)) {
                        fo.removeStyleName(ro, co, "reserved-no-confirmed");
                        fo.removeStyleName(ro, co, "reserved-confirmed");
                        fo.removeStyleName(ro, co, "reserved-stay");
                    } else {
                        switch (staT) {
                            case WaitingForConfirmation:
                                fo.setStyleName(ro, co, "reserved-no-confirmed");
                                break;
                            case Confirmed:
                                fo.setStyleName(ro, co, "reserved-confirmed");
                                break;
                            case Stay:
                                fo.setStyleName(ro, co, "reserved-stay");
                                break;
                            default:
                                assert false : "Cannot be here";
                        }
                    }
                    if (DateUtil.eqDate(today, d)) {
                        fo.addStyleName(ro, co, "res-today");
                    } else {
                        fo.removeStyleName(ro, co, "res-today");
                    }
                    col++;
                    if (col >= pCa.getColNo()) {
                        break;
                    }
                }
                row++;
            }

        }
    }

    void draw() {
        List<? extends AbstractTo> list = resList.getResList();
        List<String> rList = new ArrayList<String>();
        for (final AbstractTo p : list) {
            ResObjectP pO = (ResObjectP) p;
            rList.add(pO.getName());
        }
        List<Date> dLine = pCa.getDLine();

        // prepare param for ReadResParam
        Date dFrom = null;
        Date dTo = null;
        int num = -1;
        int co = 0;
        for (final Date d : dLine) {
            num++;
            if (num < pCa.getStartNo()) {
                continue;
            }
            if (num == pCa.getStartNo()) {
                dFrom = DateUtil.copyDate(d);
            }
            co++;
            if (co >= pCa.getColNo()) {
                dTo = DateUtil.copyDate(d);
                break;
            }
        }
        ReadResParam rParam = new ReadResParam(rList, new PeriodT(dFrom, dTo));
        SetObjectRowState se = new SetObjectRowState(rList, dLine);
        rI.getR().readResObjectState(rParam, se);

    }
}
