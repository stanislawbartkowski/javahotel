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
package com.javahotel.client.mvc.dictcrud.controler.hotelperson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.dialog.IPersistAction;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.mvc.auxabstract.LoginRecord;
import com.javahotel.client.mvc.checktable.view.ICheckTableView;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.dictcrud.controler.AbstractAuxRecordPanel;
import com.javahotel.client.mvc.gridmodel.model.view.ColsHeader;
import com.javahotel.client.rdata.RData;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.RType;
import com.javahotel.common.roles.HotelRoles;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.HotelP;
import com.javahotel.common.toobject.IField;
import com.javahotel.common.toobject.PersonP;
import com.javahotel.common.toobject.StringP;
import com.javahotel.common.util.StringU;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class HotelPersonRoleView extends AbstractAuxRecordPanel {

    private final IResLocator rI;
    private final RType r;
    private final ICheckTableView cView;
    private final List<HotelRoles> roles;
    private final List<ColsHeader> cols;

    public Object getAuxO() {
        return cView;
    }

    public IMvcWidget getMWidget() {
        return cView.getMWidget();
    }

    private class SetCols implements RData.IVectorList {

        private final IField fie;

        SetCols(final IField f) {
            this.fie = f;
        }

        public void doVList(final List<? extends AbstractTo> val) {

            for (final AbstractTo a : val) {
                String s = (String) a.getF(fie);
                cols.add(new ColsHeader(s));
            }
            String rowTitle;
            if (r == RType.AllHotels) {
                rowTitle = "Uprawnienia";
            } else {
                rowTitle = "Uprawnienia";
            }

            cView.setCols(new ColsHeader(rowTitle), cols);
        }
    }

    public HotelPersonRoleView(final IResLocator rI, final RType r) {
        this.rI = rI;
        this.r = r;
//        cView = CheckTableViewFactory.getCheckView(rI);
        cView = HInjector.getI().getCheckTableView();
        roles = new ArrayList<HotelRoles>();
        cols = new ArrayList<ColsHeader>();
        ArrayList<String> rNames = new ArrayList<String>();
        Map m = rI.getLabels().HotelRoles();
        for (HotelRoles ro : HotelRoles.values()) {
            roles.add(ro);
            String s = (String) m.get(ro.toString());
            rNames.add(s);
        }
        cView.setRows(rNames);
        RType rr = RType.AllHotels;
        IField fie = HotelP.F.name;
        if (r == RType.AllHotels) {
            rr = RType.AllPersons;
            fie = PersonP.F.name;
        }
        rI.getR().getList(rr, null, new SetCols(fie));

    }

    private class DC implements RData.IVectorList {

        final int c;

        DC(final int c) {
            this.c = c;
        }

        public void doVList(List<? extends AbstractTo> val) {
            List<Boolean> v = new ArrayList<Boolean>();
            for (int cc = 0; cc < cView.RowNo(); cc++) {
                Boolean b = new Boolean(false);
                String ro = roles.get(cc).toString();
                for (AbstractTo p : val) {
                    StringP pp = (StringP) p;
                    if (pp.getName().equals(ro)) {
                        b = new Boolean(true);
                        break;
                    }
                }
                v.add(b);
            }
            cView.setColVal(c, v);
        }
    }

    private void clearData() {
        List<Boolean> clear = new ArrayList<Boolean>();
        Boolean f = new Boolean(false);
        for (int c = 0; c < cView.ColNo(); c++) {
            clear.add(f);
        }
        for (int r = 0; r < cView.RowNo(); r++) {
            cView.setRowVal(r, clear);
        }
    }

    private void setD(AbstractTo a) {
        clearData();
        String na = null;
        switch (r) {
        case AllHotels:
            na = a.getS(HotelP.F.name);
            break;
        case AllPersons:
            na = a.getS(LoginRecord.F.login);
            break;
        default:
            return;
        }
        if (StringU.isEmpty(na)) {
            return;
        }
        int i = 0;
        for (ColsHeader ch : cols) {
            String s = ch.getHName();
            CommandParam p = new CommandParam();
            if (r == RType.AllHotels) {
                p.setHotel(na);
                p.setPerson(s);
            } else {
                p.setHotel(s);
                p.setPerson(na);
            }
            rI.getR().getList(RType.PersonHotelRoles, p, new DC(i));
            i++;
        }
    }

    @Override
    public void setFields(RecordModel a) {
        setD(a.getA());
    }

    @Override
    public void changeMode(int actionMode) {
        boolean enable = true;
        if (actionMode == IPersistAction.DELACTION) {
            enable = false;
        }
        cView.setEnable(enable);
    }
}
