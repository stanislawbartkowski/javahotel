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
package com.javahotel.nmvc.factories.booking;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotCallerListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.SlotSignalContextFactory;
import com.javahotel.client.types.DataUtil;
import com.javahotel.common.command.BookingEnumTypes;
import com.javahotel.common.command.BookingStateType;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.BookingStateP;
import com.javahotel.common.util.GetMaxUtil;

/**
 * @author hotel
 * 
 */
public class BookingControler extends AbstractSlotContainer {

    private final SlotSignalContextFactory sFactory;

    private class GetSlot implements ISlotCallerListener {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            GetSlowC g = new GetSlowC(BookingControler.this);
            return sFactory.construct(slContext.getSlType(), g);
        }

    }

    private class SetGetter implements ISlotCallerListener {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            BookingP b = DataUtil.getData(slContext);

            BookingStateP st = null;
            List<BookingStateP> bLi = b.getState();
            if (bLi != null) {
                st = GetMaxUtil.getLastStateRecord(b);
            }
            if ((st == null)
                    || st.getBState() != BookingStateType.WaitingForConfirmation) {
                if (bLi == null) {
                    bLi = new ArrayList<BookingStateP>();
                }
                BookingStateP sta = new BookingStateP();
                sta.setBState(BookingStateType.WaitingForConfirmation);
                GetMaxUtil.setNextLp(bLi, sta);
                bLi.add(sta);
                b.setState(bLi);
            }

            if (b.getBookingType() == null) {
                b.setBookingType(BookingEnumTypes.Reservation);
            }
            return slContext;
        }
    }

    public BookingControler(ICallContext iContext, IDataType subType) {
        this.dType = iContext.getDType();
        sFactory = GwtGiniInjector.getI().getSlotSignalContextFactory();
        this.getSlContainer().registerCaller(GetSlowC.GETSLOTS, new GetSlot());
        this.getSlContainer().registerCaller(subType,
                GetActionEnum.GetViewModelEdited, new SetGetter());
        this.getSlContainer().registerCaller(subType,
                GetActionEnum.GetModelToPersist, new SetGetter());

    }

}
