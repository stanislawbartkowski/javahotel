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
package com.javahotel.nmvc.factories;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.composecontroller.ComposeControllerFactory;
import com.gwtmodel.table.composecontroller.ComposeControllerType;
import com.gwtmodel.table.composecontroller.IComposeController;
import com.gwtmodel.table.datamodelview.DataViewModelFactory;
import com.gwtmodel.table.datamodelview.IDataViewModel;
import com.gwtmodel.table.factories.IDataPersistAction;
import com.gwtmodel.table.factories.IDataValidateAction;
import com.gwtmodel.table.factories.IDataValidateActionFactory;
import com.gwtmodel.table.factories.IFormDefFactory;
import com.gwtmodel.table.factories.IFormTitleFactory;
import com.gwtmodel.table.factories.IGetViewControllerFactory;
import com.gwtmodel.table.factories.IPersistFactoryAction;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.javahotel.client.types.AddType;
import com.javahotel.client.types.DataType;
import com.javahotel.client.types.DataTypeSubEnum;
import com.javahotel.client.types.DataUtil;
import com.javahotel.client.types.HModelData;
import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.ResObjectP;
import com.javahotel.common.toobject.RoomStandardP;
import com.javahotel.common.toobject.ServiceDictionaryP;
import com.javahotel.nmvc.factories.booking.BookingControler;
import com.javahotel.nmvc.factories.booking.BookingCustomerContainer;
import com.javahotel.nmvc.factories.booking.elem.BookingElemContainer;
import com.javahotel.nmvc.factories.bookingpanel.invoice.MakeOutInvoiceControler;
import com.javahotel.nmvc.factories.customer.CustomerAddInfo;
import com.javahotel.nmvc.factories.hotelperson.HotelPersonRightsContainer;
import com.javahotel.nmvc.factories.price.PriceListContainer;
import com.javahotel.nmvc.factories.season.SeasonAddInfo;

class GetViewFactory implements IGetViewControllerFactory {

    private final ComposeControllerFactory coFactory;
    private final DataViewModelFactory daFactory;
    private final IDataValidateActionFactory vFactory;
    private final IPersistFactoryAction peFactory;
    private final IFormDefFactory reFactory;
    @SuppressWarnings("unused")
    private final IFormTitleFactory tiFactory;

    GetViewFactory(IDataValidateActionFactory vFactory,
            IPersistFactoryAction peFactory, IFormDefFactory reFactory,
            IFormTitleFactory tiFactory) {
        coFactory = GwtGiniInjector.getI().getComposeControllerFactory();
        daFactory = GwtGiniInjector.getI().getDataViewModelFactory();
        this.peFactory = peFactory;
        this.vFactory = vFactory;
        this.reFactory = reFactory;
        this.tiFactory = tiFactory;
    }

    private class InfoExtractRoom implements CheckStandardContainer.InfoExtract {

        @Override
        public List<String> getString(IVModelData mData) {
            HModelData vModel = (HModelData) mData;
            ResObjectP roomO = (ResObjectP) vModel.getA();
            List<DictionaryP> dList = roomO.getFacilities();
            return DataUtil.fromDicttoString(dList);
        }

        @Override
        public void setStrings(IVModelData mData, List<String> strings,
                IDataListType dataList) {
            HModelData vModel = (HModelData) mData;
            ResObjectP roomS = (ResObjectP) vModel.getA();
            List<DictionaryP> persistList = new ArrayList<DictionaryP>();
            List<DictionaryP> dList = DataUtil.construct(dataList);
            DataUtil.fromStringToDict(strings, dList, persistList);
            roomS.setFacilities(persistList);
        }
    }

    private class InfoExtractStandard implements
            CheckStandardContainer.InfoExtract {

        @Override
        public List<String> getString(IVModelData mData) {
            HModelData vModel = (HModelData) mData;
            RoomStandardP roomS = (RoomStandardP) vModel.getA();
            List<ServiceDictionaryP> dList = roomS.getServices();
            return DataUtil.fromDicttoString(dList);
        }

        @Override
        public void setStrings(IVModelData mData, List<String> strings,
                IDataListType dataList) {
            HModelData vModel = (HModelData) mData;
            RoomStandardP roomS = (RoomStandardP) vModel.getA();
            List<ServiceDictionaryP> persistList = new ArrayList<ServiceDictionaryP>();
            List<ServiceDictionaryP> dList = DataUtil.construct(dataList);
            DataUtil.fromStringToDict(strings, dList, persistList);
            roomS.setServices(persistList);
        }
    }

    @Override
    public IComposeController construct(ICallContext iContext) {
        IDataType dType = iContext.getDType();
        FormLineContainer fContainer = reFactory.construct(iContext);
        IComposeController iCon = coFactory.construct(dType);
        IDataViewModel daModel = daFactory.construct(dType, fContainer);
        IDataValidateAction vAction = vFactory.construct(dType);
        IDataPersistAction persistA = peFactory.contruct(dType);

        ComposeControllerType cType = new ComposeControllerType(daModel, dType,
                0, 0);
        iCon.registerControler(cType);
        iCon.registerControler(new ComposeControllerType(vAction, dType));
        iCon.registerControler(new ComposeControllerType(persistA, dType));
        DataType dd = (DataType) dType;
        DataType subType = new DataType(dd.getdType(), DataTypeSubEnum.Sub1);
        DataType sub1Type = new DataType(dd.getdType(), DataTypeSubEnum.Sub2);
        DataType sub2Type = new DataType(dd.getdType(), DataTypeSubEnum.Sub3);
        DataType sub3Type = new DataType(dd.getdType(), DataTypeSubEnum.Sub4);
        ISlotable cContainer = null;
        ISlotable cContainer1 = null;
        ISlotable cContainer2 = null;
        ISlotable cContainer3 = null;
        if (dd.isRType()) {
            switch (dd.getrType()) {
            case AllPersons:
            case AllHotels:
                cContainer = new HotelPersonRightsContainer(dd, subType);
                break;
            }
        }
        if (dd.isDictType()) {
            switch (dd.getdType()) {
            case RoomStandard:
                cContainer = new CheckStandardContainer(dType, subType,
                        new DataType(DictType.ServiceDict),
                        new InfoExtractStandard());
                break;
            case RoomObjects:
                cContainer = new CheckStandardContainer(dType, subType,
                        new DataType(DictType.RoomFacility),
                        new InfoExtractRoom());
                break;
            case CustomerList:
                cContainer = new CustomerAddInfo(dType, subType, false);
                break;
            case OffSeasonDict:
                cContainer = new SeasonAddInfo(iContext.construct(iCon),
                        subType);
                break;
            case PriceListDict:
                cContainer = new PriceListContainer(peFactory, dType, subType);
                break;
            case BookingList:
                cContainer = new BookingCustomerContainer(iContext, subType);
                cContainer1 = new BookingElemContainer(new DataType(
                        AddType.BookRoom), iContext, sub1Type, false);
                // cContainer2 = new BookingRowDetailContainer(iContext,
                // sub3Type);
                cContainer3 = new BookingControler(iContext, sub3Type);
                break;
            case InvoiceList:
                cContainer3 = new MakeOutInvoiceControler(iContext, sub3Type);
                break;
            }

        }
        if ((cContainer != null) || (cContainer3 != null)) {
            if (cContainer != null) {
                cType = new ComposeControllerType(cContainer, subType, 0, 1);
                iCon.registerControler(cType);
            }
            if (cContainer1 != null) {
                cType = new ComposeControllerType(cContainer1, sub1Type, 0, 2);
                iCon.registerControler(cType);
            }
            if (cContainer2 != null) {
                cType = new ComposeControllerType(cContainer2, sub2Type, 1, 2);
                iCon.registerControler(cType);
            }
            if (cContainer3 != null) {
                cType = new ComposeControllerType(cContainer3, sub3Type);
                iCon.registerControler(cType);
            }
        }
        return iCon;
    }
}
