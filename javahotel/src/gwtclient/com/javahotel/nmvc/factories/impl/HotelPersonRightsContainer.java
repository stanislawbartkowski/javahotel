/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
package com.javahotel.nmvc.factories.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.SynchronizeList;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.login.LoginData;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotCaller;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.view.grid.GridViewFactory;
import com.gwtmodel.table.view.grid.IGridViewBoolean;
import com.javahotel.client.IResLocator;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.rdata.RData;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.RType;
import com.javahotel.common.roles.HotelRoles;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.HotelP;
import com.javahotel.common.toobject.IField;
import com.javahotel.common.toobject.PersonP;
import com.javahotel.common.toobject.StringP;
import com.javahotel.nmvc.common.AccessRoles;
import com.javahotel.nmvc.common.DataType;
import com.javahotel.nmvc.common.VModelData;

public class HotelPersonRightsContainer extends AbstractSlotContainer implements
        ISlotable {

    private final DataType daType;
    private final IResLocator rI;
    private final List<HotelRoles> roles;
    private final List<String> rNames;
    private final IGridViewBoolean iView;
    private final GridViewFactory gFactory;
    private final List<String> cols;

    private void setColVal(int i, List<String> ro) {
        for (int row = 0; row < roles.size(); row++) {
            Boolean b = null;
            for (String s : ro) {
                HotelRoles hro = null;
                try {
                    hro = HotelRoles.valueOf(s);
                } catch (IllegalArgumentException e) {
                }
                if ((hro != null) && hro.equals(roles.get(row))) {
                    b = new Boolean(true);
                    break;
                }
            }
            if (b == null) {
                b = new Boolean(false);
            }
            iView.setRowBoolean(row, i, b);
        }
    }

    private class BackRoles implements RData.IVectorList {

        final int c;

        BackRoles(final int c) {
            this.c = c;
        }

        public void doVList(List<? extends AbstractTo> val) {
            List<String> roles = new ArrayList<String>();
            for (AbstractTo p : val) {
                StringP pp = (StringP) p;
                roles.add(pp.getName());
            }
            setColVal(c, roles);
        }
    }

    private class DrawList extends SynchronizeList {

        String name;
        int cellId;

        DrawList() {
            super(3);
        }

        @Override
        protected void doTask() {
            publish(cellId, iView);
            int i = 0;
            for (String s : cols) {
                if (CUtil.EmptyS(name)) {
                    setColVal(i, new ArrayList<String>());
                    continue;
                }
                CommandParam p = new CommandParam();
                if (!isPersons()) {
                    p.setHotel(name);
                    p.setPerson(s);
                } else {
                    p.setHotel(s);
                    p.setPerson(name);
                }
                rI.getR().getList(RType.PersonHotelRoles, p, new BackRoles(i));
                i++;
            }

        }
    }

    private final DrawList dList;

    private boolean isPersons() {
        return daType.getrType() == RType.AllPersons;
    }

    private class DrawModel implements ISlotSignaller {

        public void signal(ISlotSignalContext slContext) {
            IVModelData mData = slContext.getVData();
            if (isPersons()) {
                LoginData lo = (LoginData) mData;
                dList.name = lo.getLoginName();
            } else {
                VModelData mDa = (VModelData) mData;
                HotelP ho = (HotelP) mDa.getA();
                dList.name = ho.getName();
            }
            dList.signalDone();
        }
    }

    private class SetCols implements RData.IVectorList {

        private final IField fie;

        SetCols(final IField f) {
            this.fie = f;
        }

        public void doVList(final List<? extends AbstractTo> val) {

            for (final AbstractTo a : val) {
                String s = (String) a.getF(fie);
                cols.add(s);
            }
            String rowTitle;
            if (isPersons()) {
                rowTitle = "Uprawnienia";
            } else {
                rowTitle = "Uprawnienia";
            }
            iView.setCols(rowTitle, cols);
            dList.signalDone();
        }
    }

    private class SetGetter implements ISlotCaller {

        public ISlotSignalContext call(ISlotSignalContext slContext) {
            IVModelData mData = slContext.getVData();
            AccessRoles aroles = new AccessRoles();
            String s;
            if (daType.isAllPersons()) {
                LoginData lo = (LoginData) mData;
                s = lo.getLoginName();
            } else {
                VModelData mo = (VModelData) mData;
                HotelP ho = (HotelP) mo.getA();
                s = ho.getName();
            }
            if (CUtil.EmptyS(s)) {
                return slContext;
            }
            for (int col = 0; col < cols.size(); col++) {
                for (int i = 0; i < roles.size(); i++) {
                    String osoba, hotel;
                    if (daType.isAllPersons()) {
                        osoba = s;
                        hotel = cols.get(col);
                    } else {
                        osoba = cols.get(col);
                        hotel = s;
                    }
                    Boolean b = iView.getCellBoolean(i, col);
                    if (b.booleanValue()) {
                        aroles.addRole(osoba, hotel, roles.get(i).toString());
                    }
                }
            }
            mData.setCustomData(aroles);
            return slContext;
        }

    }

    public HotelPersonRightsContainer(IDataType dType, IDataType cType) {
        this.daType = (DataType) dType;
        dList = new DrawList();
        gFactory = GwtGiniInjector.getI().getGridViewFactory();
        iView = gFactory.constructBoolean(true, true, true);
        rI = HInjector.getI().getI();
        registerSubscriber(DataActionEnum.DrawViewFormAction, cType,
                new DrawModel());
        Map<String, String> m = rI.getLabels().HotelRoles();
        cols = new ArrayList<String>();
        roles = new ArrayList<HotelRoles>();
        rNames = new ArrayList<String>();
        for (HotelRoles ro : HotelRoles.values()) {
            roles.add(ro);
            String s = (String) m.get(ro.toString());
            rNames.add(s);
        }
        iView.setRowBeginning(rNames);
        RType rr = RType.AllHotels;
        IField fie = HotelP.F.name;
        if (!isPersons()) {
            rr = RType.AllPersons;
            fie = PersonP.F.name;
        }
        rI.getR().getList(rr, null, new SetCols(fie));
        registerCaller(GetActionEnum.GetViewModelEdited, cType, new SetGetter());
        registerCaller(GetActionEnum.GetModelToPersist, cType, new SetGetter());
    }

    public void startPublish(int cellId) {
        dList.cellId = cellId;
        dList.signalDone();
    }
}
