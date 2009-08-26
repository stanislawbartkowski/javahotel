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
package com.javahotel.client.roominfo;

import com.javahotel.client.IResLocator;
import com.javahotel.client.rdata.RData.IOneList;
import com.javahotel.client.rdata.RData.IVectorList;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.ResObjectP;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class RoomInfoData {

    private final IResLocator rI;
    private Collection<ResObjectP> resC;
    private final Collection<RI> wList;
    private boolean launched;

    private class RI {

        private final String resName;
        private final IOneList i;

        RI(final String resName, final IOneList i) {
            this.resName = resName;
            this.i = i;
        }
    }

    private ResObjectP getRoomInfo(final String rName) {
        for (ResObjectP p : resC) {
            if (p.getName().equals(rName)) {
                return p;
            }
        }
        assert false : "Cannot find info on " + rName;
        return null;
    }

    private void doInfo() {
        Object[] ta = wList.toArray();
        for (int i = 0; i < ta.length; i++) {
            RI r = (RI) ta[i];
            ResObjectP p = getRoomInfo(r.resName);
            r.i.doOne(p);
        }
    }

    public RoomInfoData(final IResLocator rI) {
        this.rI = rI;
        wList = new ArrayList<RI>();
        launched = false;
        resC = null;
    }

    private class R implements IVectorList {

        public void doVList(final ArrayList<? extends AbstractTo> val) {
            resC = (Collection<ResObjectP>) val;
            launched = false;
            doInfo();
        }
    }

    public void getInfo(final String resName, final IOneList i) {
        wList.add(new RI(resName, i));
        if ((resC == null) && launched) {
            return;
        }
        if (resC == null) {
            launched = true;
            CommandParam p = rI.getR().getHotelCommandParam();
            p.setDict(DictType.RoomObjects);
            rI.getR().getList(RType.ListDict, p, new R());
            return;
        }
        doInfo();
    }
}
