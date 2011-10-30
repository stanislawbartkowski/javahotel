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
package com.javahotel.nmvc.factories.booking.elem;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotCallerListener;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.javahotel.client.IResLocator;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.rdata.RData.IOneList;
import com.javahotel.client.types.DataUtil;
import com.javahotel.client.types.VField;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.ResObjectP;
import com.javahotel.common.toobject.RoomStandardP;
import com.javahotel.common.toobject.ServiceDictionaryP;
import com.javahotel.nmvc.ewidget.EWidgetFactory;
import com.javahotel.nmvc.factories.booking.util.IsServiceBooking;

/**
 * @author hotel
 * 
 */
class BookingElem extends AbstractSlotContainer {

    /**
     * This class is created when dialog for booking one room is opened
     * (RoomElemP).
     */

    private final IResLocator rI;
    private final EWidgetFactory eFactory;
    private final BookElementRefreshPayment fPayment;
    private final boolean isFlat;
    private final IsServiceBooking sService;
    private final boolean isBook;

    /**
     * Constructor:
     * 
     * @param iContext
     *            Context when this dialog is opened
     * @param mainSlo
     *            Container for main dialog
     * @param subType
     *            Auxiliary type user for opening and handling this dialog
     */
    BookingElem(ICallContext iContext, ISlotable mainSlo, IDataType subType,
            boolean flat, IsServiceBooking sService, boolean isBook) {
        this.dType = iContext.getDType();
        this.isFlat = flat;
        this.sService = sService;
        this.isBook = isBook;
        /* Event when object (room number) is changed */
        registerSubscriber(dType, new VField(BookElemP.F.resObject),
                new ChangeRoom());
        rI = HInjector.getI().getI();
        eFactory = HInjector.getI().getEWidgetFactory();
        fPayment = new BookElementRefreshPayment(dType, this, iContext,
                mainSlo, isFlat);
        /*
         * Event when room reservation is persisted to attach reservation
         * details
         */
        if (!flat) {
            getSlContainer().registerCaller(subType,
                    GetActionEnum.GetModelToPersist, new SetGetter());
            getSlContainer().registerCaller(subType,
                    GetActionEnum.GetViewModelEdited, new SetGetter());
        }
    }

    /**
     * Caller class to attach reservation details for reservation (before
     * persisting)
     * 
     * @author hotel
     * 
     */
    private class SetGetter implements ISlotCallerListener {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            BookElemP p = DataUtil.getData(slContext);
            p.setPaymentrows(fPayment.getPList());
            return slContext;
        }

    }

    private class ReadStandard implements IOneList<RoomStandardP> {

        private final IFormLineView sView;
        private final ResObjectP r;

        ReadStandard(IFormLineView sView, ResObjectP r) {
            this.sView = sView;
            this.r = r;
        }

        private void setBookServices(RoomStandardP sa) {
            // set services not more persons than possible for this room
            if (!r.getRType().isRoom()) {
                eFactory.setComboDictList(sView, sa.getServices());
                return;
            }
            List<ServiceDictionaryP> li = new ArrayList<ServiceDictionaryP>();
            for (ServiceDictionaryP s : sa.getServices()) {
                if (!s.getServType().isRoomBooking()) {
                    li.add(s);
                    continue;
                }
                if (s.getNoPerson().compareTo(r.getMaxPerson()) > 0) {
                    continue;
                }
                li.add(s);
            }
            eFactory.setComboDictList(sView, li);
        }

        private void setNoBookServices() {
            List<ServiceDictionaryP> li = new ArrayList<ServiceDictionaryP>();
            for (ServiceDictionaryP se : sService.getsList()) {
                if (se.getServType().isBooking()) {
                    continue;
                }
                li.add(se);
            }
            eFactory.setComboDictList(sView, li);
        }

        @Override
        public void doOne(RoomStandardP sa) {
            if (isBook) {
                setBookServices(sa);
            } else {
                setNoBookServices();
            }
        }
    }

    private class ReadRoom implements IOneList<ResObjectP> {

        private final IFormLineView sView;

        ReadRoom(IFormLineView sView) {
            this.sView = sView;
        }

        @Override
        public void doOne(ResObjectP r) {
            DictionaryP stand = r.getRStandard();
            String standName = stand.getName();
            CommandParam p = rI.getR().getHotelCommandParam();
            p.setDict(DictType.RoomStandard);
            p.setRecName(standName);
            rI.getR().getOne(RType.ListDict, p, new ReadStandard(sView, r));
        }

    }

    private class ChangeRoom implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            IFormLineView iView = slContext.getChangedValue();
            String res = (String) iView.getValObj();
            if (res == null) {
                return;
            }
            FormLineContainer fContainer = getGetterContainer(dType);
            IFormLineView sView = fContainer.findLineView(new VField(
                    BookElemP.F.service));
            CommandParam p = rI.getR().getHotelCommandParam();
            p.setDict(DictType.RoomObjects);
            p.setRecName(res);
            rI.getR().getOne(RType.ListDict, p, new ReadRoom(sView));
        }
    }

}
