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
package com.javahotel.client.listofserv;

import java.util.ArrayList;

import com.javahotel.client.IResLocator;
import com.javahotel.client.rdata.RData.IOneList;
import com.javahotel.client.rdata.RData.IVectorList;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.ResObjectP;
import com.javahotel.common.toobject.RoomStandardP;
import com.javahotel.common.toobject.ServiceDictionaryP;
import com.javahotel.common.util.CollToArray;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class ListOfServices {

    private final IResLocator rI;
    private final IVectorList i;

    public ListOfServices(final IResLocator rI, final IVectorList i) {
        this.rI = rI;
        this.i = i;
    }

    private class GetS implements IOneList<RoomStandardP> {

        public void doOne(final RoomStandardP p) {
            i.doVList(CollToArray.toA(p.getServices()));
        }
    }

    private class GetR implements IOneList<ResObjectP> {

        public void doOne(final ResObjectP re) {
            DictionaryP d = re.getRStandard();
            String name = d.getName();
            CommandParam p = rI.getR().getHotelDictName(DictType.RoomStandard,
                    name);
            rI.getR().getOne(RType.ListDict, p, new GetS());
        }
    }

    public void getServices(final String resRoom) {
        if (resRoom == null) {
            i.doVList(new ArrayList<ServiceDictionaryP>());
            return;
        }
        CommandParam p = rI.getR().getHotelDictName(DictType.RoomObjects,
                resRoom);
        rI.getR().getOne(RType.ListDict, p, new GetR());
    }
}
