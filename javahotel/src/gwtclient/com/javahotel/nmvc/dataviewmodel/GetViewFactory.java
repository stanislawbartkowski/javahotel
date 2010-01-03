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
package com.javahotel.nmvc.dataviewmodel;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.DataListType;
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
import com.gwtmodel.table.factories.IGetViewControllerFactory;
import com.gwtmodel.table.factories.IPersistFactoryAction;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.ResObjectP;
import com.javahotel.common.toobject.RoomStandardP;
import com.javahotel.common.toobject.ServiceDictionaryP;
import com.javahotel.nmvc.common.DataType;
import com.javahotel.nmvc.common.DataTypeSubEnum;
import com.javahotel.nmvc.common.DataUtil;
import com.javahotel.nmvc.common.VModelData;
import com.javahotel.nmvc.factories.FormDefFactory;

public class GetViewFactory implements IGetViewControllerFactory {

    private final FormDefFactory fFactory;
    private final ComposeControllerFactory coFactory;
    private final DataViewModelFactory daFactory;
    private final IDataValidateActionFactory vFactory;
    private final IPersistFactoryAction peFactory;

    public GetViewFactory(FormDefFactory fFactory,
            IDataValidateActionFactory vFactory, IPersistFactoryAction peFactory) {
        this.fFactory = fFactory;
        coFactory = GwtGiniInjector.getI().getComposeControllerFactory();
        daFactory = GwtGiniInjector.getI().getDataViewModelFactory();
        this.peFactory = peFactory;
        this.vFactory = vFactory;

    }

    private class InfoExtractRoom implements CheckStandardContainer.InfoExtract {

        public List<String> getString(IVModelData mData) {
            VModelData vModel = (VModelData) mData;
            ResObjectP roomO = (ResObjectP) vModel.getA();
            List<DictionaryP> dList = roomO.getFacilities();
            return DataUtil.fromDicttoString(dList);
        }

        public void setStrings(IVModelData mData, List<String> strings,
                DataListType dataList) {
            VModelData vModel = (VModelData) mData;
            ResObjectP roomS = (ResObjectP) vModel.getA();
            List<DictionaryP> persistList = new ArrayList<DictionaryP>();
            List<DictionaryP> dList = DataUtil.construct(dataList);
            DataUtil.fromStringToDict(strings, dList, persistList);
            roomS.setFacilities(persistList);
        }

    }

    private class InfoExtractStandard implements
            CheckStandardContainer.InfoExtract {

        public List<String> getString(IVModelData mData) {
            VModelData vModel = (VModelData) mData;
            RoomStandardP roomS = (RoomStandardP) vModel.getA();
            List<ServiceDictionaryP> dList = roomS.getServices();
            return DataUtil.fromDicttoString(dList);
        }

        public void setStrings(IVModelData mData, List<String> strings,
                DataListType dataList) {
            VModelData vModel = (VModelData) mData;
            RoomStandardP roomS = (RoomStandardP) vModel.getA();
            List<ServiceDictionaryP> persistList = new ArrayList<ServiceDictionaryP>();
            List<ServiceDictionaryP> dList = DataUtil.construct(dataList);
            DataUtil.fromStringToDict(strings, dList, persistList);
            roomS.setServices(persistList);
        }

    }

    public IComposeController construct(IDataType dType) {
        FormLineContainer fContainer = fFactory.construct(dType);
        IComposeController iCon = coFactory.construct(dType);
        IDataViewModel daModel = daFactory.construct(dType, fContainer);
        IDataValidateAction vAction = vFactory.construct(dType);
        IDataPersistAction persistA = peFactory.contruct(dType);

        ComposeControllerType cType = new ComposeControllerType(daModel, dType,
                0, 0);
        iCon.registerController(cType);
        iCon.registerController(new ComposeControllerType(vAction, dType));
        iCon.registerController(new ComposeControllerType(persistA, dType));
        DataType dd = (DataType) dType;
        DataType subType = new DataType(dd.getdType(), DataTypeSubEnum.Sub1);
        switch (dd.getdType()) {
        case RoomObjects:
            cType = new ComposeControllerType(new CheckStandardContainer(
                    peFactory, subType, new DataType(DictType.RoomFacility),
                    new InfoExtractRoom()), subType, 0, 1);
            iCon.registerController(cType);
            break;
        case RoomStandard:
            cType = new ComposeControllerType(new CheckStandardContainer(
                    peFactory, subType, new DataType(DictType.ServiceDict),
                    new InfoExtractStandard()), subType, 0, 1);
            iCon.registerController(cType);
            break;
        }
        return iCon;
    }

}
