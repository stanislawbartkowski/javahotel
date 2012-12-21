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
package com.javahotel.nmvc.factories.persist;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.DataListTypeFactory;
import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.common.PersistTypeEnum;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.javahotel.client.IResLocator;
import com.javahotel.client.abstractto.InvoicePVData;
import com.javahotel.client.rdata.RData.IVectorList;
import com.javahotel.client.types.HModelData;
import com.javahotel.client.types.VModelDataFactory;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.InvoiceP;
import com.javahotel.nmvc.factories.persist.dict.IHotelPersistFactory;

/**
 * @author hotel
 * 
 */
public class InvoicePersistLayer extends AbstractPersistLayer {

    private class ReadListDict implements IVectorList<InvoiceP> {

        @Override
        public void doVList(final List<InvoiceP> val) {
            List<IVModelData> dvList = new ArrayList<IVModelData>();
            for (InvoiceP a : val) {
                InvoicePVData p = new InvoicePVData(a);
                dvList.add(p);
            }
            IDataListType dataList = DataListTypeFactory.construct(dvList);
            publish(dType, DataActionEnum.ListReadSuccessSignal, dataList);
        }
    }

    private class PersistRecord implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            PersistTypeEnum e = slContext.getPersistType();
            IVModelData mData = null;
            if (e == PersistTypeEnum.MODIF || e == PersistTypeEnum.REMOVE) {
                mData = getGetterIVModelData(dType,
                        GetActionEnum.GetListLineChecked);
            }
            IVModelData pData = getGetterIVModelData(dType,
                    GetActionEnum.GetComposeModelToPersist, mData);
            HModelData mo;
            InvoicePVData pa = (InvoicePVData) pData;
            mo = VModelDataFactory.construct(pa.getP());
            mo.setCustomData(pa.getCustomData());
            iPersist.persist(e, mo, getPersist(slContext.getPersistType()));
        }
    }

    private class ReadList implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            CommandParam co = rI.getR().getHotelCommandParam();
            co.setDict(da.getdType());

            if (da.getLParam() != null) {
                co.setBookingId(da.getLParam());
            }
            rI.getR().getList(RType.ListDict, co, new ReadListDict());
        }
    }

    InvoicePersistLayer(IDataType dd, IResLocator rI,
            IHotelPersistFactory iPersistFactory) {
        super(dd, rI, iPersistFactory);
        // create subscribers - ReadList
        registerSubscriber(dType, DataActionEnum.ReadListAction, new ReadList());
        registerSubscriber(dType, DataActionEnum.PersistDataAction,
                new PersistRecord());
        // persist subscriber
    }

}
