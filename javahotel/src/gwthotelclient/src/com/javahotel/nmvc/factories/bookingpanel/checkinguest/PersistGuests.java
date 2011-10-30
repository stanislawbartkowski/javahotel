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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.PersistTypeEnum;
import com.gwtmodel.table.SynchronizeList;
import com.gwtmodel.table.factories.IDataPersistAction;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.SlU;
import com.gwtmodel.table.view.callback.CommonCallBack;
import com.javahotel.client.GWTGetService;
import com.javahotel.client.IResLocator;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.types.DataUtil;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.HotelOpType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.GuestP;
import com.javahotel.nmvc.factories.persist.PersistCustomer;
import com.javahotel.types.LId;

/**
 * @author hotel
 * 
 */
class PersistGuests extends AbstractSlotContainer implements IDataPersistAction {

    private final IDataType dType;
    private final BookingP p;
    private final PersistTypeEnum action = PersistTypeEnum.ADD;
    private final IResLocator rI;

    PersistGuests(IDataType dType, BookingP p) {
        this.dType = dType;
        this.p = p;
        this.rI = HInjector.getI().getI();
        getSlContainer().registerSubscriber(dType,
                DataActionEnum.PersistDataAction, new Persist());
    }

    private class OkGuests extends CommonCallBack<ReturnPersist> {

        @Override
        public void onMySuccess(ReturnPersist arg) {
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

        private final Map<String, List<GuestP>> li = new HashMap<String, List<GuestP>>();

        ModifCust(int no) {
            super(no);
            for (BookElemP bElem : p.getBooklist()) {
                li.put(bElem.getResObject(), new ArrayList<GuestP>());
            }
        }

        @Override
        protected void doTask() {
            CommandParam par = rI.getR().getHotelCommandParam();
            par.setGuests(li);
            par.setReservName(p.getName());
            par.setoP(HotelOpType.PersistGuests);
            GWTGetService.getService().hotelOp(par, new OkGuests());
        }
    }

    private class SaveCustomer implements PersistCustomer.ISetCustomerId {

        private final AbstractToCheckGuest a;
        private final ModifCust sy;

        SaveCustomer(AbstractToCheckGuest a, ModifCust sy) {
            this.a = a;
            this.sy = sy;
        }

        @Override
        public void setC(LId custId) {
            // assign Customer id to Guest
            a.getO3().setCustomer(custId);
            // add to list of guests
            a.addGuestToList(sy.li);
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

            IDataListType dList = SlU.getIDataListType(dType,
                    PersistGuests.this);
            // count number of guests
            UtilCust.ECountParam eC = UtilCust.countGuests(dList);
            ModifCust sy = new ModifCust(eC.guestNo);
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
