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
package com.javahotel.client.paymentdata;

import java.util.Date;
import java.util.List;

import com.google.inject.Inject;
import com.javahotel.client.IResLocator;
import com.javahotel.client.param.ConfigParam;
import com.javahotel.client.rdata.RData.IOneList;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.command.SynchronizeList;
import com.javahotel.common.tableprice.TableSeasonPrice;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.OfferSeasonP;
import com.javahotel.common.toobject.PaymentRowP;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class PaymentData {

    private String season;
    private String sprice;
    private final TableSeasonPrice tPrice;
    private final IResLocator rI;

    public interface ISetPaymentRows {

        void setRow(List<PaymentRowP> col);
    }

    private void initT() {
        season = null;
        sprice = null;
    }

    private void setRows(final Date dFrom, final Date dTo,
            final String service, final ISetPaymentRows i) {
        List<PaymentRowP> co = tPrice.getPriceRows(service, dFrom, dTo);
        i.setRow(co);
    }

    @Inject
    public PaymentData(final IResLocator rI) {
        initT();
        this.rI = rI;
        tPrice = new TableSeasonPrice(ConfigParam.getStartWeek());
    }

    private class SRows extends SynchronizeList {

        final Date dFrom;
        final Date dTo;
        final String service;
        final ISetPaymentRows i;

        SRows(final Date dFrom, final Date dTo,
                final String service, final ISetPaymentRows i) {
            super(2);
            this.dFrom = dFrom;
            this.dTo = dTo;
            this.service = service;
            this.i = i;
        }

        @Override
        protected void doTask() {
            setRows(dFrom, dTo, service, i);
        }
    }

    private class CDict implements IOneList {

        private final boolean season;
        private final SRows s;

        CDict(final boolean se, final SRows s) {
            this.season = se;
            this.s = s;
        }

        public void doOne(AbstractTo val) {
            if (season) {
                OfferSeasonP oP = (OfferSeasonP) val;
                tPrice.setPeriods(oP);
            } else {
                OfferPriceP sP = (OfferPriceP) val;
                tPrice.setPriceList(sP);
            }
            s.signalDone();
        }
    }

    public void getRows(final String season, final String sprice,
            final Date dFrom, final Date dTo,
            final String service, final ISetPaymentRows i) {
        boolean start = true;
        if ((this.season != null) && (this.sprice != null)) {
            if (this.season.equals(season) && this.sprice.equals(sprice)) {
                start = false;
            }
        }
        if (!start) {
            setRows(dFrom, dTo, service, i);
            return;
        }
        SRows s = new SRows(dFrom, dTo, service, i);
        CommandParam p = rI.getR().getHotelDictName(DictType.OffSeasonDict,
                season);
        rI.getR().getOne(RType.ListDict, p, new CDict(true, s));
        p = rI.getR().getHotelDictName(DictType.PriceListDict, sprice);
        p.setSeasonName(season);
        rI.getR().getOne(RType.ListDict, p, new CDict(false, s));
    }
}
