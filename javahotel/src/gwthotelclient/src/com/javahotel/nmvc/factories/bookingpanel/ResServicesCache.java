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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.SynchronizeList;
import com.gwtmodel.table.SynchronizeListSignal;
import com.gwtmodel.table.common.ISignal;
import com.gwtmodel.table.common.PeriodT;
import com.gwtmodel.table.common.dateutil.DateUtil;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.rdef.IFormLineView;
import com.javahotel.client.IResLocator;
import com.javahotel.client.calculateprice.IPaymentData;
import com.javahotel.client.calculateprice.IPaymentData.ISetPaymentRows;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.rdata.RData.IOneList;
import com.javahotel.client.types.DataUtil;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.PaymentRowP;
import com.javahotel.common.toobject.ResObjectP;
import com.javahotel.common.toobject.RoomStandardP;
import com.javahotel.common.toobject.ServiceDictionaryP;

/**
 * @author hotel
 * 
 */
class ResServicesCache {

    private final Map<String, RoomStandardP> cache = new HashMap<String, RoomStandardP>();
    private final Map<String, List<PaymentRowP>> pCache = new HashMap<String, List<PaymentRowP>>();
    private final IResLocator rI = HInjector.getI().getI();
    private final IPaymentData iPayment = HInjector.getI().getPaymentData();
    private final IFormLineView seasonE;
    private final IFormLineView priceE;

    ResServicesCache(IFormLineView seasonE, IFormLineView priceE) {
        this.seasonE = seasonE;
        this.priceE = priceE;
    }

    private class CallBack implements IOneList<RoomStandardP> {

        private final String sName;
        private final SynchronizeList sy;

        CallBack(String sName, SynchronizeList sy) {
            this.sName = sName;
            this.sy = sy;
        }

        @Override
        public void doOne(RoomStandardP val) {
            cache.put(sName, val);
            sy.signalDone();
        }

    }

    private class SetRows implements ISetPaymentRows {

        private final SynchronizeList sy;
        private final String serv;

        SetRows(SynchronizeList sy, String serv) {
            this.sy = sy;
            this.serv = serv;
        }

        @Override
        public void setRow(List<PaymentRowP> col) {
            pCache.put(serv, col);
            sy.signalDone();
        }

    }

    PaymentRowP getPriceForDay(String service, Date day) {
        List<PaymentRowP> li = pCache.get(service);
        assert li != null : LogT.getT().CellCannotBeNull();
        for (PaymentRowP p : li) {
            if (DateUtil.comparePeriod(day, p.getRowFrom(), p.getRowTo()) == 0) {
                return p;
            }
        }
        // assert false : LogT.getT().CellCannotBeNull();
        return null;
    }

    void createPriceCache(PeriodT pe, ISignal sig) {
        Set<String> se = new HashSet<String>();
        for (String s : cache.keySet()) {
            RoomStandardP sa = cache.get(s);
            for (ServiceDictionaryP d : sa.getServices()) {
                se.add(d.getName());
            }
        }
        SynchronizeListSignal sy = new SynchronizeListSignal(se.size(), sig);
        Iterator<String> iS = se.iterator();
        while (iS.hasNext()) {
            String s = iS.next();
            iPayment.setPaymentData((String) seasonE.getValObj(),
                    (String) priceE.getValObj(), pe.getFrom(), pe.getTo(), s,
                    new SetRows(sy, s));
        }

    }

    void createCache(IDataListType iList, ISignal sig) {
        Iterable<ResObjectP> i = DataUtil.getI(iList);
        Set<String> se = new HashSet<String>();
        for (ResObjectP p : i) {
            se.add(p.getRStandard().getName());
        }
        SynchronizeListSignal sy = new SynchronizeListSignal(se.size(), sig);
        Iterator<String> iS = se.iterator();
        while (iS.hasNext()) {
            String s = iS.next();
            CommandParam pa = rI.getR().getHotelCommandParam();
            pa.setDict(DictType.RoomStandard);
            pa.setRecName(s);
            rI.getR().getOne(RType.ListDict, pa, new CallBack(s, sy));
        }
    }

    List<ServiceDictionaryP> getServices(ResObjectP res) {
        String sName = res.getRStandard().getName();
        RoomStandardP sa = cache.get(sName);
        return DataUtil.createListOfServices(res, sa, false);
    }

    /**
     * @return the seasonE
     */
    public IFormLineView getSeasonE() {
        return seasonE;
    }

    /**
     * @return the priceE
     */
    public IFormLineView getPriceE() {
        return priceE;
    }

}
