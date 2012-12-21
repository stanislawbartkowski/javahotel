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
package com.javahotel.nmvc.factories.booking.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gwtmodel.table.common.ISignal;
import com.javahotel.client.IResLocator;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.rdata.RData.IVectorList;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.ServiceDictionaryP;

/**
 * Class used for informing is service is related to booking
 * 
 * @author hotel
 * 
 */
public class IsServiceBooking {

    /** contains a set of services related to booking. */
    // important: is null at the beginning to break in case of not ready
    private Set<String> bSet = null;
    private final IResLocator rI;
    private List<ServiceDictionaryP> sList;

    /**
     * @return the sList
     */
    public List<ServiceDictionaryP> getsList() {
        return sList;
    }

    /**
     * If service is related to booking
     * 
     * @param service
     *            Service name
     * @return True: if related to booking
     */
    public boolean isBooking(String service) {
        return bSet.contains(service);
    }

    public ServiceDictionaryP getService(String s) {
        for (ServiceDictionaryP se : sList) {
            if (se.getName().equals(s)) {
                return se;
            }
        }
        return null;
    }

    /**
     * Class for receiving the list of services
     * 
     * @author hotel
     * 
     */
    private class ReadListDict implements IVectorList<ServiceDictionaryP> {

        /** interface used for signalling that class is ready. */
        private final ISignal iReady;

        ReadListDict(ISignal iReady) {
            this.iReady = iReady;
        }

        @Override
        public void doVList(final List<ServiceDictionaryP> val) {
            bSet = new HashSet<String>();
            sList = val;
            for (ServiceDictionaryP se : val) {
                if (se.getServType().isBooking()) {
                    bSet.add(se.getName());
                }
            }
            iReady.signal();
        }
    }

    /**
     * Constructor
     * 
     * @param iReady
     *            Signals when class is ready
     */
    public IsServiceBooking(ISignal iReady) {
        rI = HInjector.getI().getI();
        CommandParam co = rI.getR().getHotelCommandParam();
        co.setDict(DictType.ServiceDict);
        rI.getR().getList(RType.ListDict, co, new ReadListDict(iReady));
    }

}
