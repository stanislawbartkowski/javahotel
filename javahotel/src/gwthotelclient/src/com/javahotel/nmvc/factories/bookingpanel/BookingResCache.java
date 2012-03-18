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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.gwtmodel.table.SynchronizeList;
import com.gwtmodel.table.common.ISignal;
import com.javahotel.client.types.BackAbstract;
import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.BookingP;

/**
 * @author hotel
 * 
 */
class BookingResCache {

    private final Map<String, BookingP> bCache = new HashMap<String, BookingP>();

    BookingP getBooking(String resName) {
        return bCache.get(resName);
    }

    private class Sy extends SynchronizeList {

        private final ISignal iSig;

        Sy(int no, ISignal iSig) {
            super(no);
            this.iSig = iSig;
        }

        @Override
        protected void doTask() {
            iSig.signal();
        }
    }

    private class R implements BackAbstract.IRunAction<BookingP> {

        private final String s;
        private final Sy sy;

        R(String s, Sy sy) {
            this.s = s;
            this.sy = sy;
        }

        @Override
        public void action(BookingP t) {
            bCache.put(s, t);
            sy.signalDone();
        }

    }

    void readBooking(Set<String> resName, ISignal iSig) {

        Sy sy = new Sy(resName.size(), iSig);

        Iterator<String> i = resName.iterator();
        while (i.hasNext()) {
            String s = i.next();
            new BackAbstract<BookingP>().readAbstract(DictType.BookingList, s,
                    new R(s, sy));
        }

    }

}
