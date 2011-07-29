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
package com.javahotel.nmvc.factories.persist;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.DataListTypeFactory;
import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.PersistTypeEnum;
import com.gwtmodel.table.factories.IDataPersistAction;
import com.gwtmodel.table.login.LoginData;
import com.gwtmodel.table.login.LoginField;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.view.callback.CommonCallBackNo;
import com.javahotel.client.GWTGetService;
import com.javahotel.client.IResLocator;
import com.javahotel.client.PersonHotelRoles;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.rdata.RData.IVectorList;
import com.javahotel.client.types.AccessRoles;
import com.javahotel.client.types.DataType;
import com.javahotel.client.types.DataUtil;
import com.javahotel.client.types.HModelData;
import com.javahotel.client.types.VModelDataFactory;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.RType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.PersonP;
import com.javahotel.nmvc.factories.persist.dict.IPersistRecord;
import com.javahotel.nmvc.factories.persist.dict.IPersistResult;

public class DataPersistLayer extends AbstractSlotContainer implements
        IDataPersistAction {

    private final IResLocator rI;
    private final IPersistRecord iPersist;
    private final DataType da;

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

    private class ReadListDict implements IVectorList {

        @Override
        public void doVList(final List<? extends AbstractTo> val) {
            IDataListType dataList = DataUtil.construct(val);
            if (da.isAllPersons()) {
                dataList = convertToLogin(dataList);
            }
            publish(dType, DataActionEnum.ListReadSuccessSignal, dataList);
        }
    }

    private class AfterPersist implements IPersistResult {

        private final PersistTypeEnum persistTypeEnum;

        AfterPersist(PersistTypeEnum persistTypeEnum) {
            this.persistTypeEnum = persistTypeEnum;
        }

        @Override
        public void success(PersistResultContext re) {
            publish(dType, DataActionEnum.PersistDataSuccessSignal,
                    persistTypeEnum);
        }
    }

    private class PersistRecord implements ISlotSignaller {

        @Override
        public void signal(ISlotSignalContext slContext) {
            IVModelData pData = getGetterIVModelData(dType,
                    GetActionEnum.GetComposeModelToPersist);
            PersistTypeEnum action = slContext.getPersistType();
            HModelData mo = (HModelData) pData;
            iPersist.persist(action, mo,
                    new AfterPersist(slContext.getPersistType()));
        }
    }

    private void sendSignal(PersistTypeEnum persistTypeEnum) {
        rI.getR().invalidateCache(RType.AllHotels, RType.AllPersons,
                RType.PersonHotelRoles);
        publish(dType, DataActionEnum.PersistDataSuccessSignal, persistTypeEnum);
    }

    private class PersisRoles extends CommonCallBackNo<ReturnPersist> {

        private final PersistTypeEnum persistTypeEnum;

        PersisRoles(int no, PersistTypeEnum persistTypeEnum) {
            super(no);
            this.persistTypeEnum = persistTypeEnum;
        }

        @Override
        protected void go() {
            sendSignal(persistTypeEnum);
        }
    }

    private class PeSuccess implements IPersistResult {

        private final PersistTypeEnum persistTypeEnum;
        private final AccessRoles roles;

        PeSuccess(PersistTypeEnum persistTypeEnum, AccessRoles roles) {
            this.persistTypeEnum = persistTypeEnum;
            this.roles = roles;
        }

        @Override
        public void success(PersistResultContext re) {
            if (roles.getLi().isEmpty()) {
                sendSignal(persistTypeEnum);
                return;
            }
            PersisRoles rol = new PersisRoles(roles.getLi().size(),
                    persistTypeEnum);
            for (PersonHotelRoles pe : roles.getLi()) {
                GWTGetService.getService().setRoles(pe.getPerson(),
                        pe.getHotel(), pe.getRoles(), rol);
            }
        }
    }

    private class SignalPersistPerson implements ISlotSignaller {

        private final IPersistRecord iPersist;

        SignalPersistPerson(IPersistRecord iPersist) {
            this.iPersist = iPersist;
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            IVModelData pData = getGetterIVModelData(dType,
                    GetActionEnum.GetComposeModelToPersist);
            AccessRoles roles = (AccessRoles) pData.getCustomData();
            HModelData ho;
            if (da.isAllPersons()) {
                AbstractTo a = ConvertP.toLoginP(pData);
                ho = VModelDataFactory.construct(a);
            } else {
                ho = (HModelData) pData;
            }
            iPersist.persist(slContext.getPersistType(), ho, new PeSuccess(
                    slContext.getPersistType(), roles));
        }
    }

    private class ReadList implements ISlotSignaller {

        @Override
        public void signal(ISlotSignalContext slContext) {
            CommandParam co = rI.getR().getHotelCommandParam();
            DataType daType = (DataType) dType;

            if (daType.isDictType()) {
                co.setDict(daType.getdType());
                rI.getR().getList(RType.ListDict, co, new ReadListDict());
            } else {
                rI.getR().getList(daType.getrType(), co, new ReadListDict());
            }
        }
    }

    public DataPersistLayer(IDataType dd) {
        this.rI = HInjector.getI().getI();
        this.dType = dd;
        this.da = (DataType) dd;
        iPersist = HInjector.getI().getHotelPersistFactory()
                .construct(da, false);
        // create subscribers - ReadList
        registerSubscriber(dType, DataActionEnum.ReadListAction, new ReadList());
        if (da.isAllPersons() || da.isAllHotels()) {
            registerSubscriber(dType, DataActionEnum.PersistDataAction,
                    new SignalPersistPerson(iPersist));
        } else {
            registerSubscriber(dType, DataActionEnum.PersistDataAction,
                    new PersistRecord());
        }
        // persist subscriber
    }
}
