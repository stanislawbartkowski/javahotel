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
package com.javahotel.nmvc.factories.bookingpanel.checkinguest;

import java.util.ArrayList;

import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.PersistTypeEnum;
import com.gwtmodel.table.SynchronizeList;
import com.gwtmodel.table.factories.IDataPersistAction;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.SlU;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.types.DataType;
import com.javahotel.client.types.DataUtil;
import com.javahotel.client.types.HModelData;
import com.javahotel.client.types.VModelDataFactory;
import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookRecordP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.GuestP;
import com.javahotel.common.util.GetMaxUtil;
import com.javahotel.nmvc.factories.persist.PersistCustomer;
import com.javahotel.nmvc.factories.persist.dict.IHotelPersistFactory;
import com.javahotel.nmvc.factories.persist.dict.IPersistRecord;
import com.javahotel.nmvc.factories.persist.dict.IPersistResult;
import com.javahotel.types.LId;

/**
 * @author hotel
 * 
 */
class PersistGuests extends AbstractSlotContainer implements IDataPersistAction  {

    private final IDataType dType;
    private final BookingP p;
    private final IHotelPersistFactory pFactory;
    private final PersistTypeEnum action = PersistTypeEnum.ADD;

    PersistGuests(IDataType dType, BookingP p) {
        this.dType = dType;
        this.p = p;
        getSlContainer().registerSubscriber(dType,
                DataActionEnum.PersistDataAction, new Persist());
        pFactory = HInjector.getI().getHotelPersistFactory();
    }

    private class GuestsPersisted implements IPersistResult {

        @Override
        public void success(PersistResultContext re) {
            // publish persisted successfully
            getSlContainer().publish(dType,
                    DataActionEnum.PersistDataSuccessSignal);
        }

    }

    /**
     * Class used for synchronizing persisting customers (guests) firstly First
     * persist all guest to receive LId, then persist the Booking
     * 
     * @author hotel
     * 
     */
    private class ModifCust extends SynchronizeList {

        ModifCust(int no) {
            super(no);
        }

        @Override
        protected void doTask() {
            IPersistRecord re = pFactory.construct(new DataType(
                    DictType.BookingList), false);
            HModelData custH = VModelDataFactory.construct(p);
            re.persist(action, custH, new GuestsPersisted());
        }
    }

    private class SaveCustomer implements PersistCustomer.ISetCustomerId {

        private final AbstractToCheckGuest a;
        private final SynchronizeList sy;

        SaveCustomer(AbstractToCheckGuest a, SynchronizeList sy) {
            this.a = a;
            this.sy = sy;
        }

        @Override
        public void setC(LId custId) {
            // assign Customer id to Guest
            a.getO3().setCustomer(custId);
            // add to list of guests
            a.addGuestToList();
            // done
            sy.signalDone();
        }

    }

    /**
     * Persist information about guests checked
     * 
     * @author hotel
     * 
     */
    private class Persist implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            // make empty all lists of guests
            // those list will recreated
            BookRecordP b;
            b = GetMaxUtil.getLastBookRecord(p);
            for (BookElemP bElem : b.getBooklist()) {
                bElem.setGuests(new ArrayList<GuestP>());
            }

            IDataListType dList = SlU.getIDataListType(dType,
                    PersistGuests.this);
            // count number of guests
            UtilCust.ECountParam eC = UtilCust.countGuests(dList);
            SynchronizeList sy = new ModifCust(eC.guestNo);
            Iterable<AbstractToCheckGuest> i = DataUtil.getI(dList);
            for (AbstractToCheckGuest a : i) {
                CustomerP cu = a.getO2();
                if (UtilCust.EmptyC(cu)) {
                    continue;
                }
                PersistCustomer pe = new PersistCustomer();
                pe.persistCustomer(action, a.construct(), new SaveCustomer(a,
                        sy));
            }
        }

    }

}
