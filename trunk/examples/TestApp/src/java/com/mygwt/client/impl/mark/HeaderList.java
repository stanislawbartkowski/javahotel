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
package com.mygwt.client.impl.mark;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.IConsts;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.factories.IHeaderListContainer;
import com.gwtmodel.table.listdataview.ClickColumnImageSignal;
import com.gwtmodel.table.listdataview.GetImageColSignal;
import com.gwtmodel.table.listdataview.GetImageColSignalReturn;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ISlotCallerListener;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.tabledef.VListHeaderContainer;
import com.gwtmodel.table.tabledef.VListHeaderDesc;
import com.gwtmodel.table.view.util.OkDialog;

/**
 * @author hotel
 * 
 */
class HeaderList extends AbstractSlotContainer implements IHeaderListContainer {

    private final VListHeaderContainer vHeader;

    @Override
    public void startPublish(CellId cellId) {
        publish(dType, vHeader);
    }

    private class C implements ISlotCallerListener {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            GetImageColSignal sig = (GetImageColSignal) slContext.getCustom();
            String[] res = new String[] { "Calendar.gif", "gwt.png" };
            GetImageColSignalReturn ret = new GetImageColSignalReturn(res);
            return slContextFactory.construct(slContext.getSlType(), ret);
        }

    }

    private class Click implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            ClickColumnImageSignal sig = (ClickColumnImageSignal) slContext
                    .getCustom();
            String mess = sig.getValue() + " " + sig.getImno();
            OkDialog d = new OkDialog(mess,"Clicked");
            d.show(sig.getW());
        }

    }

    static List<VListHeaderDesc> getHList() {
        List<VListHeaderDesc> vList = new ArrayList<VListHeaderDesc>();
        vList.add(new VListHeaderDesc("Mark", ItemVData.fMARKED));
        vList.add(new VListHeaderDesc("EditMark", ItemVData.fEDITMARKED, false,
                null, true, null, null, null, null, null, 0));
        vList.add(new VListHeaderDesc("Number", ItemVData.fNUMBER));
        vList.add(new VListHeaderDesc("Date", ItemVData.fDATE));
        vList.add(new VListHeaderDesc("Name", ItemVData.fNAME));
        vList.add(new VListHeaderDesc("Image Col", ItemVData.fNAME, false,
                null, true, null, null, null, null, null, 2));
        return vList;
    }

    public HeaderList(IDataType dType) {
        this.dType = dType;
        vHeader = new VListHeaderContainer(getHList(), "List of marked items",
                IConsts.defaultPage, null, null, "500px", null);
        registerCaller(GetImageColSignal.constructSlotGetImageCol(dType),
                new C());
        registerSubscriber(
                ClickColumnImageSignal.constructSlotClickColumnSignal(dType),
                new Click());
    }

}
