/*
 * Copyright 2009 stanislawbartkowski@gmail.com 
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
import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IClickYesNo;
import com.gwtmodel.table.view.callback.CommonCallBack;
import com.gwtmodel.table.view.util.YesNoDialog;
import com.javahotel.client.GWTGetService;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.IWidgetSize;
import com.javahotel.client.mvc.contrpanel.model.ContrButton;
import com.javahotel.client.mvc.contrpanel.model.ContrButtonFactory;
import com.javahotel.client.mvc.contrpanel.model.IContrPanel;
import com.javahotel.client.mvc.contrpanel.view.IControlClick;
import com.javahotel.client.mvc.persistrecord.IPersistResult;
import com.javahotel.client.rdata.RData.IOneList;
import com.javahotel.client.roominfo.RoomInfoData;
import com.javahotel.client.widgets.popup.PopUpWithMenuClose;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.HotelOpType;
import com.javahotel.common.command.RType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.ResDayObjectStateP;
import com.javahotel.common.toobject.ResObjectP;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class PopUpInfoRes {

    private final static int CHANGETOSTAY = 0;
    private final static int CHECKINPERSONS = 1;
    private final static int ADDPAYMENT = 2;
    private final IResLocator rI;
    private final List<String> rList;
    private final List<Date> dLine;
    private final PanelResCalendar pCa;
    private final ResDayObjectStateP p;
    private final Date d;
    private BookingP book;
    private PopUpWithMenuClose aPanel;
    private Widget inW = null;

    PopUpInfoRes(IResLocator rI, final List<String> r, final List<Date> dLine,
            PanelResCalendar pCa, ResDayObjectStateP p, Date d) {
        this.rI = rI;
        this.rList = r;
        this.dLine = dLine;
        this.pCa = pCa;
        this.p = p;
        this.d = d;
        book = null;
    }

    private class R implements IOneList<BookingP> {

        private final AddPayment pa;

        R(AddPayment pa) {
            this.pa = pa;
        }

        public void doOne(BookingP val) {
            book = val;
            Widget w = new BookingInfo(rI, book, p.getPaymentRowId(), p
                    .getLState());
            VerticalPanel h = aPanel.getVP();
            if (inW != null) {
                h.remove(inW);
            }
            h.add(w);
            inW = w;
            if (pa != null) {
                pa.drawBill(book);
            }
        }
    }

    private void setBooking(AddPayment pay) {
        if (p.getBookName() != null) {
            CommandParam pa = rI.getR().getHotelDictName(DictType.BookingList,
                    p.getBookName());
            rI.getR().getOne(RType.ListDict, pa, new R(pay));
        }

    }

    private class PE implements IPersistResult {

        public void success(PersistResultContext re) {
            setBooking(null);
        }
    }

    private class PEE implements IPersistResult {

        private final AddPayment pa;

        PEE(AddPayment pa) {
            this.pa = pa;
        }

        public void success(PersistResultContext re) {
            setBooking(pa);

        }
    }

    private class RInfo implements IOneList<ResObjectP> {

        public void doOne(ResObjectP r) {
            VerticalPanel h = aPanel.getVP();
            Widget w = pCa.getPPSeason().getDInfo(d);
            h.add(w);
            w = new ResRoomInfo(rI, r);
            h.add(w);
            setBooking(null);
        }
    }

    private class YesB implements IClickYesNo {

        private class RBack extends CommonCallBack<ReturnPersist> {

            @Override
            public void onMySuccess(ReturnPersist ret) {
                if (ret.getIdName() != null) {
                    Window.alert(ret.getIdName());
                } else {
                    Window.alert(ret.getErrorMessage());
                }
            }
        }

        public void click(boolean yes) {
            if (!yes) {
                return;
            }
            CommandParam pa = rI.getR().getHotelCommandParam();
            pa.setReservName(p.getBookName());
            GWTGetService.getService().hotelOpRet(
                    HotelOpType.ChangeBookingToStay, pa, new RBack());
        }
    }

    void showDialog(final IWidgetSize arg0, final RoomInfoData rInfo,
            final String resName) {
        IControlClick cli = new IControlClick() {

            public void click(ContrButton co, Widget w) {
                if (book == null) {
                    return;
                }
                switch (co.getActionId()) {
                case CHANGETOSTAY:
                    YesNoDialog dial = new YesNoDialog("Zamieniasz", null,
                            new YesB());
                    dial.show(w);
                    break;

                case CHECKINPERSONS:
                    CheckInPersons pe = new CheckInPersons(rI, book, new PE());
                    pe.showDialog(w);
                    break;
                case ADDPAYMENT:
                    AddPayment pa = new AddPayment(rI);
                    pa.setPe(new PEE(pa));
                    pa.showDialog(w);
                    pa.drawBill(book);
                    break;
                }
            }
        };
        List<ContrButton> aButton = new ArrayList<ContrButton>();
        aButton.add(new ContrButton(null, "Zamień na pobyt", CHANGETOSTAY));
        aButton.add(new ContrButton(null, "Melduj osoby", CHECKINPERSONS));
        aButton.add(new ContrButton(null, "Dopisz do rachunku", ADDPAYMENT));
        IContrPanel cPa = ContrButtonFactory.getContr(rI, aButton);

        aPanel = new PopUpWithMenuClose(arg0, cPa, cli);
        rInfo.getInfo(resName, new RInfo());
    }
}
