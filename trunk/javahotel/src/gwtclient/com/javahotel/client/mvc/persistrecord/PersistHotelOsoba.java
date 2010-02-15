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
package com.javahotel.client.mvc.persistrecord;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.view.callback.CommonCallBack;
import com.gwtmodel.table.view.callback.CommonCallBackNo;
import com.javahotel.client.GWTGetService;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.IPersistAction;
import com.javahotel.client.mvc.auxabstract.LoginRecord;
import com.javahotel.client.mvc.checktable.view.ICheckTableView;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.gridmodel.model.view.ColsHeader;
import com.javahotel.common.command.RType;
import com.javahotel.common.roles.HotelRoles;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.HotelP;
import com.javahotel.common.toobject.PersonP;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class PersistHotelOsoba implements IPersistRecord {

    private final IResLocator rI;
    private final RType rt;

    PersistHotelOsoba(IResLocator rI, final RType rt) {
        this.rI = rI;
        this.rt = rt;
    }

    private void removeHo(final RecordModel a, IPersistResult ires) {
        HotelP ho = (HotelP) a.getA();
        GWTGetService.getService().removeHotel(ho,
                new RemoveBack(ires, IPersistAction.DELACTION, a.getA()));
    }

    private void persistHo(final RecordModel a, IPersistResult res) {
        HotelP ho = (HotelP) a.getA();
        GWTGetService.getService().addHotel(ho,
                new PersistBack(res, a, IPersistAction.ADDACION));
    }

    private void removePe(final RecordModel a, IPersistResult ires) {
        LoginRecord le = (LoginRecord) a.getA();
        String na = le.getLogin();
        PersonP pe = new PersonP();
        pe.setName(na);
        GWTGetService.getService().removePerson(pe,
                new RemoveBack(ires, IPersistAction.DELACTION, a.getA()));
    }

    private void persistPe(final RecordModel a, IPersistResult res) {
        LoginRecord le = (LoginRecord) a.getA();
        String na = le.getLogin();
        PersonP pe = new PersonP();
        String pass = le.getPassword();
        pe.setName(na);
        GWTGetService.getService().addPerson(pe, pass,
                new PersistBack(res, a, IPersistAction.ADDACION));
    }

    private void finalPersist(IPersistResult ires, int action, AbstractTo a) {
        invalidateCache();
        CallSuccess.callI(ires, action, a, null);
    }

    private class ActionAB extends CommonCallBackNo {

        private final IPersistResult ires;
        private final int action;
        private final AbstractTo a;

        ActionAB(final int no, IPersistResult ires, int action, AbstractTo a) {
            super(no);
            this.ires = ires;
            this.action = action;
            this.a = a;
        }

        @Override
        protected void go() {
            finalPersist(ires, action, a);
            // invalidateCache();
            // CallSuccess.callI(ires, action, a, null);
        }
    }

    private void setRoles(RecordModel a, IPersistResult res, int action) {
        ICheckTableView iCe = (ICheckTableView) a.getAuxData();
        String na = null;
        AbstractTo ar;
        if (rt == RType.AllHotels) {
            HotelP ho = (HotelP) a.getA();
            na = ho.getName();
            ar = ho;
        } else {
            LoginRecord le = (LoginRecord) a.getA();
            na = le.getLogin();
            ar = le;
        }
        HotelRoles[] rol = HotelRoles.values();
        List<ColsHeader> cols = iCe.getSCol();

        if (cols.size() == 0) {
            finalPersist(res, action, ar);
            return;
        }

        ActionAB aa = new ActionAB(cols.size(), res, action, ar);
        int co = 0;
        for (ColsHeader ch : cols) {
            String s = ch.getHName();
            List<Boolean> val = iCe.getCols(co);
            co++;
            List<String> roles = new ArrayList<String>();
            for (int i = 0; i < val.size(); i++) {
                Boolean b = val.get(i);
                if (b.booleanValue()) {
                    roles.add(rol[i].toString());
                }
            }
            String sperson = null;
            String shotel = null;
            if (rt == RType.AllHotels) {
                shotel = na;
                sperson = s;
            } else {
                shotel = s;
                sperson = na;
            }
            GWTGetService.getService().setRoles(sperson, shotel, roles, aa);
        }

    }

    private class PersistBack extends CommonCallBack<Object> {

        private final IPersistResult ires;
        private final RecordModel a;
        private final int action;

        PersistBack(IPersistResult ires, RecordModel a, int action) {
            this.ires = ires;
            this.a = a;
            this.action = action;
        }

        @Override
        public void onMySuccess(Object arg) {
            setRoles(a, ires, action);
        }
    }

    private class RemoveBack extends CommonCallBack<Object> {

        private final IPersistResult ires;
        private final int action;
        private final AbstractTo a;

        RemoveBack(IPersistResult ires, int action, AbstractTo a) {
            this.ires = ires;
            this.action = action;
            this.a = a;
        }

        @Override
        public void onMySuccess(Object arg) {
            invalidateCache();
            CallSuccess.callI(ires, action, a, null);
        }
    }

    private void invalidateCache() {
        rI.getR().invalidateCache(rt);
        rI.getR().invalidateCache(RType.PersonHotelRoles);

    }

    public void persist(int action, RecordModel a, IPersistResult ires) {
        switch (action) {
        case IPersistAction.ADDACION:
        case IPersistAction.MODIFACTION:
            if (rt == RType.AllHotels) {
                persistHo(a, ires);
            } else {
                persistPe(a, ires);
            }
            break;
        case IPersistAction.DELACTION:
            if (rt == RType.AllHotels) {
                removeHo(a, ires);
            } else {
                removePe(a, ires);
            }
            break;
        default:
            assert false : action + " invalid action code";
            break;
        }
    }
}
