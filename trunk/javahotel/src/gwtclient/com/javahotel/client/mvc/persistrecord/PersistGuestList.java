/*
 * Copyright 2009 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.mvc.persistrecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.javahotel.client.CallBackHotel;
import com.javahotel.client.GWTGetService;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.dialog.IPersistAction;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.mvc.auxabstract.ResRoomGuest;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.dictdata.model.IGetBooking;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.HotelOpType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.command.SynchronizeList;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.GuestP;
import com.javahotel.types.LId;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class PersistGuestList implements IPersistRecord {

    private final IResLocator rI;

    PersistGuestList(IResLocator rI) {
        this.rI = rI;
    }

    private SS syncs;
    private Map<String, List<GuestP>> ma;

    private class B extends CallBackHotel<ReturnPersist> {

        private final IPersistResult iRes;

        B(IPersistResult res) {
            super(rI);
            this.iRes = res;
        }

        @Override
        public void onMySuccess(ReturnPersist pe) {
            CallSuccess.callI(iRes, IPersistAction.ADDACION, null, pe);
        }
    }

    private class SS extends SynchronizeList {

        IPersistResult ires;
        IGetBooking ge;

        SS(int no) {
            super(no);
        }

        @Override
        protected void doTask() {
            rI.getR().invalidateCacheList();
            CommandParam pa = rI.getR().getHotelCommandParam();
            pa.setGuests(ma);
            pa.setReservName(ge.getBookName());
            GWTGetService.getService().hotelOp(HotelOpType.PersistGuests, pa,
                    new B(ires));
        }
    }

    private class PE implements IPersistResult {

        private final ResRoomGuest ge;

        PE(ResRoomGuest g) {
            this.ge = g;
        }

        public void success(PersistResultContext re) {
            setC(ge, re.getRet().getId());
        }
    }

    private void setC(ResRoomGuest ge, LId id) {
        String resName = ge.getResName();
        GuestP g = ge.getO1();
        g.setCustomer(id);
        List<GuestP> gl = ma.get(resName);
        if (gl == null) {
            gl = new ArrayList<GuestP>();
            ma.put(resName, gl);
        }
        gl.add(g);
        syncs.signalDone();
    }

    public void persist(int action, RecordModel mod, IPersistResult ires) {
        List<? extends AbstractTo> a = mod.getAList();
        List<? extends AbstractTo> prevA = mod.getBeforeaList();
        IGetBooking get = (IGetBooking) mod.getAuxData1();
        List<ResRoomGuest> gList = (List<ResRoomGuest>) a;
        List<ResRoomGuest> prevList = (List<ResRoomGuest>) prevA;
        syncs = new SS(gList.size());
        syncs.ires = ires;
        syncs.ge = get;
        ma = new HashMap<String, List<GuestP>>();
        for (int i = 0; i < gList.size(); i++) {
            ResRoomGuest ge = gList.get(i);
            ResRoomGuest pge = prevList.get(i);
            LId id = pge.getO2().getId();
            if (id != null) {
                setC(ge, id);
                continue;
            }
            PersistRecordFactory pFactory = HInjector.getI()
                    .getPersistRecordFactory();
            IPersistRecord pe = pFactory.getPersistDict(new DictData(
                    DictType.CustomerList));
            RecordModel mo = new RecordModel(null, null);
            mo.setA(ge.getO2());
            pe.persist(IPersistAction.ADDACION, mo, new PE(ge));
        }

    }
}
