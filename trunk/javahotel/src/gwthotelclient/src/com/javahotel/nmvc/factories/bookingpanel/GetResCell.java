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

import java.util.Date;
import java.util.List;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.view.table.IGetCellValue;
import com.javahotel.client.IResLocator;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.types.DataUtil;
import com.javahotel.common.command.BookingStateType;
import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.scrollseason.model.DaySeasonScrollData;
import com.javahotel.common.toobject.PaymentRowP;
import com.javahotel.common.toobject.ResDayObjectStateP;
import com.javahotel.common.toobject.ResObjectP;
import com.javahotel.common.toobject.ServiceDictionaryP;

/**
 * Procedure providing data for reservation columns.
 * 
 * @author hotel
 * 
 */
class GetResCell implements IGetCellValue {

    private DaySeasonScrollData sData;
    private final ResServicesCache rCache;

    GetResCell(ResServicesCache rCache) {
        this.rCache = rCache;
    }

    /**
     * @return the sData
     */
    public DaySeasonScrollData getsData() {
        return sData;
    }

    /**
     * @param sData
     *            the sData to set
     */
    public void setsData(DaySeasonScrollData sData) {
        this.sData = sData;
    }

    private ResDayObjectStateP getResObject(IVModelData v, IVField fie) {
        RField r = (RField) fie;
        Date d = U.getD(sData, r.getI());
        if (d == null) {
            // possible the first time only
            return null;
        }
        // room number
        String s = U.getRoomName(v);
        IResLocator rI = HInjector.getI().getI();
        // reservation data for room and day
        ResDayObjectStateP p = rI.getR().getResState(s, d);
        return p;

    }

    /**
     * Get value for reservation column
     */
    @Override
    public SafeHtml getValue(IVModelData v, IVField fie) {

        ResDayObjectStateP p = getResObject(v, fie);
        if (p == null) {
            return U.getEmpty();
        }
        ResObjectP res = DataUtil.getData(v);
        BookingStateType staT = U.getResState(p);
        SafeHtmlBuilder b = new SafeHtmlBuilder();
        if (U.isBooked(staT)) {
            String ss = DateFormatUtil.toS(p.getD());
            b.appendEscaped(ss);
        } else {
            List<ServiceDictionaryP> sLi = rCache.getServices(res);
            boolean first = true;
            for (ServiceDictionaryP pS : sLi) {
                if (!first) {
                    b.appendHtmlConstant("<br>");
                }
                first = false;
                b.appendEscaped(pS.getName());
                PaymentRowP pa = rCache.getPriceForDay(pS.getName(), p.getD());
                if (pa != null) {
                  String sPa = Utils.DecimalToS(pa.getCustomerRate());
                  b.appendEscaped(" : " + sPa);
                }
            }
        }
        return b.toSafeHtml();
    }

    @Override
    public String getCellStyleNames(IVModelData v, IVField fie) {
        ResDayObjectStateP p = getResObject(v, fie);
        if (p == null) {
            return null;
        }
        BookingStateType staT = U.getResState(p);
        String sTyle = null;
        // enrich cell only if there is a reservation
        if (U.isBooked(staT)) {
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
        return sTyle;
    }

    /**
     * @return the rCache
     */
    ResServicesCache getrCache() {
        return rCache;
    }
}
