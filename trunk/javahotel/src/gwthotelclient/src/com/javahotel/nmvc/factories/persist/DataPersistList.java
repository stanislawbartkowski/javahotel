/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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
import com.gwtmodel.table.factories.IDataPersistListAction;
import com.gwtmodel.table.login.LoginData;
import com.gwtmodel.table.login.LoginField;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.javahotel.client.IResLocator;
import com.javahotel.client.rdata.RData.IVectorList;
import com.javahotel.client.types.DataType;
import com.javahotel.client.types.DataUtil;
import com.javahotel.client.types.HModelData;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.PersonP;

/**
 * @author hotel
 * 
 */
class DataPersistList extends AbstractSlotContainer implements
        IDataPersistListAction {

    protected final IResLocator rI;
    protected final DataType da;

    private IDataListType convertToLogin(IDataListType dataList) {
        List<IVModelData> li = new ArrayList<IVModelData>();
        for (IVModelData v : dataList.getList()) {
            HModelData vda = (HModelData) v;
            PersonP pe = (PersonP) vda.getA();
            LoginData lo = new LoginData();
            String name = pe.getName();
            lo.setF(new LoginField(LoginField.F.LOGINNAME), name);
            li.add(lo);
        }
        return DataListTypeFactory.construct(li);
    }

    private class ReadListDict implements IVectorList<AbstractTo> {

        @Override
        public void doVList(final List<AbstractTo> val) {
            IDataListType dataList = DataUtil.construct(val);
            if (da.isAllPersons()) {
                dataList = convertToLogin(dataList);
            }
            publish(dType, DataActionEnum.ListReadSuccessSignal, dataList);
        }
    }

    private class ReadList implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            CommandParam co = rI.getR().getHotelCommandParam();

            if (da.isDictType()) {
                co.setDict(da.getdType());
                rI.getR().getList(RType.ListDict, co, new ReadListDict());
            } else {
                rI.getR().getList(da.getrType(), co, new ReadListDict());
            }
        }
    }

    DataPersistList(IDataType dType, IResLocator rI) {
        this.dType = dType;
        this.rI = rI;
        this.da = (DataType) dType;
        registerSubscriber(dType, DataActionEnum.ReadListAction, new ReadList());
    }

}
