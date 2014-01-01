/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table;

import com.gwtmodel.table.factories.IDataPersistListAction;
import com.gwtmodel.table.factories.IPersistFactoryAction;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;

public class ReadDictList<T extends IDataListType> {

    public interface IListCallBack<T extends IDataListType> {

        void setList(T dList);
    }

    private class R implements ISlotListener {

        private final IListCallBack<T> iList;

        R(IListCallBack<T> iList) {
            this.iList = iList;
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            IDataListType dataList = slContext.getDataList();
            T t = (T) dataList;
            iList.setList(t);
        }
    }

    public void readList(IDataType dType, IListCallBack<T> iList) {
        IPersistFactoryAction persistFactoryA = GwtGiniInjector.getI().getTableFactoriesContainer().getPersistFactoryAction();
        IDataPersistListAction persistL = persistFactoryA.constructL(dType);
        persistL.getSlContainer().registerSubscriber(dType,
                DataActionEnum.ListReadSuccessSignal, new R(iList));
        persistL.getSlContainer().publish(dType, DataActionEnum.ReadListAction);
    }
}
