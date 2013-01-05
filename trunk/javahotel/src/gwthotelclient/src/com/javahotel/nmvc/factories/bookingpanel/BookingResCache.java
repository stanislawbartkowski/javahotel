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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.gwtmodel.table.SynchronizeList;
import com.gwtmodel.table.SynchronizeListSignal;
import com.gwtmodel.table.common.ISignal;
import com.javahotel.client.types.BackAbstract;
import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.types.LId;

/**
 * @author hotel
 * 
 */
class BookingResCache {

    private final Map<String, BookingP> bCache = new HashMap<String, BookingP>();
    private final Map<LId, CustomerP> cCache = new HashMap<LId, CustomerP>();

    BookingP getBooking(String resName) {
        return bCache.get(resName);
    }

    CustomerP getCustomer(LId l) {
        return cCache.get(l);
    }

    private class R implements BackAbstract.IRunAction<BookingP> {

        private final String s;
        private final SynchronizeList sy;

        R(String s, SynchronizeList sy) {
            this.s = s;
            this.sy = sy;
        }

        @Override
        public void action(BookingP t) {
            bCache.put(s, t);
            sy.signalDone();
        }

    }

    private class C implements BackAbstract.IRunAction<CustomerP> {

        private final LId lId;
        private final SynchronizeListSignal sy;

        C(LId lId, SynchronizeListSignal sy) {
            this.lId = lId;
            this.sy = sy;
        }

        @Override
        public void action(CustomerP t) {
            cCache.put(lId, t);
            sy.signalDone();
        }

    }
    
    private class S implements ISignal {
        
        private final ISignal iSig;
        
        S(ISignal iSig) {
            this.iSig = iSig;
        }

        @Override
        public void signal() {

            Set<LId> se = new HashSet<LId>();
            for (BookingP p : bCache.values()) {
                se.add(p.getCustomer());
            }
            SynchronizeListSignal sy = new SynchronizeListSignal(se.size(),
                    iSig);
            Iterator<LId> i = se.iterator();
            while (i.hasNext()) {
                LId s = i.next();
                new BackAbstract<CustomerP>().readAbstract(
                        DictType.CustomerList, s, new C(s, sy));
            }
            
        }
        
    }


    void readBooking(Set<String> resName, ISignal iSig) {

        SynchronizeListSignal sy = new SynchronizeListSignal(resName.size(), new S(iSig));

        Iterator<String> i = resName.iterator();
        while (i.hasNext()) {
            String s = i.next();
            new BackAbstract<BookingP>().readAbstract(DictType.BookingList, s,
                    new R(s, sy));
        }

    }

}
