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
package com.javahotel.nmvc.factories.booking;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.AbstractLpVModelData;
import com.gwtmodel.table.DataListTypeFactory;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.composecontroller.ComposeControllerType;
import com.gwtmodel.table.composecontroller.IComposeControllerTypeFactory;
import com.gwtmodel.table.controler.DataListParam;
import com.gwtmodel.table.controler.DisplayListControlerParam;
import com.gwtmodel.table.controler.IDataControler;
import com.gwtmodel.table.controler.TableDataControlerFactory;
import com.gwtmodel.table.datamodelview.DataViewModelFactory;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.factories.IDataValidateActionFactory;
import com.gwtmodel.table.factories.IFormDefFactory;
import com.gwtmodel.table.factories.IFormTitleFactory;
import com.gwtmodel.table.factories.IGetViewControllerFactory;
import com.gwtmodel.table.factories.IHeaderListFactory;
import com.gwtmodel.table.factories.ITableCustomFactories;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.persist.IMemoryListModel;
import com.gwtmodel.table.persist.MemoryGetController;
import com.gwtmodel.table.persist.MemoryListPersist;
import com.gwtmodel.table.slotmodel.AbstractSlotMediatorContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.view.util.SetVPanelGwt;
import com.javahotel.client.types.AddType;
import com.javahotel.client.types.DataType;
import com.javahotel.client.types.HModelData;
import com.javahotel.common.toobject.BookRecordP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.util.GetMaxUtil;

public class BookingElemContainer extends AbstractSlotMediatorContainer {

    private final SetVPanelGwt sPanel = new SetVPanelGwt();
    private final IDataType publishdType;
    private final IMemoryListModel lPersistList;
    private final IDataControler dControler;
    private final ITableCustomFactories tFactories;

    private class DrawModel implements ISlotSignaller {

        @Override
        public void signal(ISlotSignalContext slContext) {
            IVModelData mData = slContext.getVData();
            HModelData vData = (HModelData) mData;
            BookingP b = (BookingP) vData.getA();//
            BookRecordP p = null;
            if (b.getBookrecords() != null) {
                p = GetMaxUtil.getLastBookRecord(b);
            }
            List<AbstractLpVModelData> li = new ArrayList<AbstractLpVModelData>();
            lPersistList.setDataList(DataListTypeFactory.constructLp(li));
            dControler.startPublish(new CellId(0));
        }
    }

    public BookingElemContainer(ICallContext iContext, DataType subType,
            IFormDefFactory fFactory, IFormTitleFactory tiFactory) {
        tFactories = iContext.getC();
        publishdType = iContext.getDType();
        dType = new DataType(AddType.BookElem);
        slMediator.getSlContainer().registerSubscriber(
                DataActionEnum.DrawViewFormAction, subType, new DrawModel());
        lPersistList = new MemoryListPersist(dType);
        TableDataControlerFactory tFactory = GwtGiniInjector.getI()
                .getTableDataControlerFactory();

        DataViewModelFactory daFactory = GwtGiniInjector.getI()
                .getDataViewModelFactory();

        IDataValidateActionFactory vFactory = tFactories
                .getDataValidateFactory();

        CellId cI = new CellId(0);
        IDataModelFactory iDataModelFactory = tFactories.getDataModelFactory();
        IComposeControllerTypeFactory custFactory = new IComposeControllerTypeFactory() {

            @Override
            public ComposeControllerType construct(ICallContext iContext) {
                ISlotable iSlo = new CustomBookingElem(iContext);
                return new ComposeControllerType(iSlo);
            }

        };
        IGetViewControllerFactory iGetCon = new MemoryGetController(fFactory,
                iDataModelFactory, daFactory, lPersistList, vFactory,
                custFactory);

        DisplayListControlerParam cParam = tFactory.constructParam(dType, cI,
                new DataListParam(lPersistList, null, iDataModelFactory,
                        tiFactory, iGetCon), null);
        IHeaderListFactory he = tFactories.getHeaderListFactory();
        dControler = tFactory.constructDataControler(cParam);
        slMediator.registerSlotContainer(dControler);
        slMediator.registerSlotContainer(he.construct(dType));
        slMediator.getSlContainer().registerSubscriber(dType, cI,
                sPanel.constructSetGwt());
    }

    @Override
    public void startPublish(CellId cellId) {
        slMediator.startPublish(null);
        slMediator.getSlContainer().publish(publishdType, cellId,
                sPanel.constructGWidget());
    }

}
