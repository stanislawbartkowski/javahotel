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
package com.javahotel.nmvc.persist;

import java.util.List;

import com.javahotel.client.IResLocator;
import com.javahotel.client.rdata.RData.IVectorList;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.nmvc.common.AbstractSlotContainer;
import com.javahotel.nmvc.common.DataListType;
import com.javahotel.nmvc.common.DataType;
import com.javahotel.nmvc.slotmodel.ISlotSignalContext;
import com.javahotel.nmvc.slotmodel.ISlotSignaller;
import com.javahotel.nmvc.slotmodel.ListEventEnum;
import com.javahotel.nmvc.slotmodel.SlotPublisherType;
import com.javahotel.nmvc.slotmodel.SlotSignalContext;
import com.javahotel.nmvc.slotmodel.SlotType;
import com.javahotel.nmvc.slotmodel.SlotTypeFactory;

class DataPersistLayer extends AbstractSlotContainer implements
        IDataPersistAction {

    private final IResLocator rI;
    private final SlotPublisherType publishList;

    private class ReadListDict implements IVectorList {

        private final ISlotSignalContext slContext;

        ReadListDict(ISlotSignalContext slContext) {
            this.slContext = slContext;
        }

        public void doVList(final List<? extends AbstractTo> val) {
            DataListType dataList = new DataListType(val);
            SlotSignalContext.signal(publishList, slContext.getdType(),
                    dataList);
        }
    }

    private class ReadList implements ISlotSignaller {

        public void signal(ISlotSignalContext slContext) {
            CommandParam co = rI.getR().getHotelCommandParam();
            DataType dType = slContext.getdType();
            co.setDict(dType.getdType());
            rI.getR().getList(RType.ListDict, co, new ReadListDict(slContext));
        }
    }

    DataPersistLayer(IResLocator rI) {
        this.rI = rI;
        // create subscribers - ReadList
        SlotType slType = SlotTypeFactory.contruct(ListEventEnum.ReadList);
        slContainer.addSubscriber(slType, new ReadList());
        // create publisher - ListRead
        publishList = slContainer.addPublisher(SlotTypeFactory
                .contruct(ListEventEnum.ReadListSuccess));
    }

    public void startPublish() {
    }

}
