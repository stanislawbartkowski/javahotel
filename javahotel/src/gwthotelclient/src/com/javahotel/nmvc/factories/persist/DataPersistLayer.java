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

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.common.PersistTypeEnum;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.view.callback.CommonCallBackNo;
import com.javahotel.client.GWTGetService;
import com.javahotel.client.IResLocator;
import com.javahotel.client.PersonHotelRoles;
import com.javahotel.client.types.AccessRoles;
import com.javahotel.client.types.HModelData;
import com.javahotel.client.types.VModelDataFactory;
import com.javahotel.common.command.RType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.nmvc.factories.persist.dict.IHotelPersistFactory;
import com.javahotel.nmvc.factories.persist.dict.IPersistRecord;
import com.javahotel.nmvc.factories.persist.dict.IPersistResult;

public class DataPersistLayer extends AbstractPersistLayer {

//    private IDataListType convertToLogin(IDataListType dataList) {
//        List<IVModelData> li = new ArrayList<IVModelData>();
//        for (IVModelData v : dataList.getList()) {
//            HModelData vda = (HModelData) v;
//            PersonP pe = (PersonP) vda.getA();
//            LoginData lo = new LoginData();
//            String name = pe.getName();
//            lo.setF(new LoginField(LoginField.F.LOGINNAME), name);
//            li.add(lo);
//        }
//        return DataListTypeFactory.construct(li);
//    }

//    private class ReadListDict implements IVectorList<AbstractTo> {
//
//        @Override
//        public void doVList(final List<AbstractTo> val) {
//            IDataListType dataList = DataUtil.construct(val);
//            if (da.isAllPersons()) {
//                dataList = convertToLogin(dataList);
//            }
//            publish(dType, DataActionEnum.ListReadSuccessSignal, dataList);
//        }
//    }

    private class PersistRecord implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            wButton = slContext.getGwtWidget();
            PersistTypeEnum e = slContext.getPersistType();
            IVModelData mData = null;
            if (e == PersistTypeEnum.MODIF || e == PersistTypeEnum.REMOVE) {
                mData = getGetterIVModelData(dType,
                        GetActionEnum.GetListLineChecked);
            }
            IVModelData pData = getGetterIVModelData(dType,
                    GetActionEnum.GetComposeModelToPersist, mData);
            HModelData mo;
            mo = (HModelData) pData;
            iPersist.persist(e, mo, getPersist(slContext.getPersistType()));
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
            // do not touch roles while removing
            if (roles.getLi().isEmpty()
                    || re.getAction() == PersistTypeEnum.REMOVE) {
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

    private class SignalPersistPerson implements ISlotListener {

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

//    private class ReadList implements ISlotListener {
//
//        @Override
//        public void signal(ISlotSignalContext slContext) {
//            CommandParam co = rI.getR().getHotelCommandParam();
//
//            if (da.isDictType()) {
//                co.setDict(da.getdType());
//                rI.getR().getList(RType.ListDict, co, new ReadListDict());
//            } else {
//                rI.getR().getList(da.getrType(), co, new ReadListDict());
//            }
//        }
//    }

    DataPersistLayer(IDataType dd, IResLocator rI,
            IHotelPersistFactory iPersistFactory) {
        super(dd, rI, iPersistFactory);
        // create subscribers - ReadList
//        registerSubscriber(dType, DataActionEnum.ReadListAction, new ReadList());
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