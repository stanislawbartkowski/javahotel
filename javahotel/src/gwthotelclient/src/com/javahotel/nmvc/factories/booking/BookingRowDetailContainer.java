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

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.composecontroller.ComposeControllerType;
import com.gwtmodel.table.composecontroller.IComposeControllerTypeFactory;
import com.gwtmodel.table.controler.DataListParam;
import com.gwtmodel.table.controler.DisplayListControlerParam;
import com.gwtmodel.table.controler.IDataControler;
import com.gwtmodel.table.controler.TableDataControlerFactory;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.factories.IDataPersistAction;
import com.gwtmodel.table.factories.IFormTitleFactory;
import com.gwtmodel.table.factories.IGetViewControllerFactory;
import com.gwtmodel.table.factories.IHeaderListContainer;
import com.gwtmodel.table.factories.ITableCustomFactories;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.AbstractSlotMediatorContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.javahotel.client.abstractto.IAbstractFactory;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.types.AddType;
import com.javahotel.client.types.DataType;
import com.javahotel.client.types.VModelDataFactory;
import com.javahotel.common.toobject.AbstractTo;

/**
 * @author hotel
 * 
 */
public class BookingRowDetailContainer extends AbstractSlotMediatorContainer {

    @SuppressWarnings("unused")
    private final IDataType publishdType;
    private final TableDataControlerFactory taFactory;
    private final IDataControler iControler;
    private final DataType fElem = new DataType(AddType.BookRoom);

    private class DrawModel implements ISlotSignaller {

        @Override
        public void signal(ISlotSignalContext slContext) {
            iControler.startPublish(null);
        }
    }

    private class DataPersistAction extends AbstractSlotContainer implements
            IDataPersistAction {

        private class ReadList implements ISlotSignaller {

            @Override
            public void signal(ISlotSignalContext slContext) {
                ISlotable sL = BookingRowDetailContainer.this;
                IAbstractFactory iFactory = HInjector.getI()
                        .getAbstractFactory();
                AbstractTo a = iFactory.construct(fElem);
                IVModelData mData = VModelDataFactory.construct(a);
                mData = sL.getSlContainer().getGetterIVModelData(fElem,
                        GetActionEnum.GetListLineChecked, mData);
                if (mData == null) {
                    return;
                }
            }
        }

        DataPersistAction() {
            this.dType = BookingRowDetailContainer.this.dType;
            registerSubscriber(dType, DataActionEnum.ReadListAction,
                    new ReadList());
        }
        // BookElemP
    }

    // class OneRoomBook extends AbstractSlotContainer {
    //
    // }

    private IDataControler createControler() {
        ITableCustomFactories fContainer = GwtGiniInjector.getI()
                .getTableFactoriesContainer();

        IDataPersistAction persistA = new DataPersistAction();
        IHeaderListContainer heList = fContainer.getHeaderListFactory()
                .construct(dType);
        IDataModelFactory dataFactory = fContainer.getDataModelFactory();
        IFormTitleFactory formFactory = fContainer.getFormTitleFactory();

        // IComposeControllerTypeFactory custFactory = new
        // IComposeControllerTypeFactory() {

        // @Override
        // public ComposeControllerType construct(ICallContext iiContext) {
        // ISlotable iSlo = new OneRoomBook();
        // return new ComposeControllerType(iSlo, dType);
        // }

        // };

        IGetViewControllerFactory fControler = fContainer
                .getGetViewControllerFactory();

        DataListParam dLiParam = new DataListParam(dType, persistA, heList,
                dataFactory, formFactory, fControler);
        CellId cI = new CellId(0);
        DisplayListControlerParam lParam = taFactory.constructParam(cI,
                dLiParam, this.slMediator);
        return taFactory.constructDataControler(lParam);
    }

    class ChangeMode implements ISlotSignaller {

        @Override
        public void signal(ISlotSignalContext slContext) {
            iControler.getSlContainer().publish(dType,
                    DataActionEnum.ChangeViewComposeFormModeAction,
                    slContext.getPersistType());
        }

    }

    public BookingRowDetailContainer(ICallContext iContext, IDataType subType) {
        taFactory = GwtGiniInjector.getI().getTableDataControlerFactory();
        publishdType = iContext.getDType();
        dType = new DataType(AddType.RowPaymentElem);
        slMediator.getSlContainer().registerSubscriber(subType,
                DataActionEnum.DrawViewFormAction, new DrawModel());
        slMediator.getSlContainer().registerSubscriber(subType,
                DataActionEnum.ChangeViewFormModeAction, new ChangeMode());

        iControler = createControler();
    }

    // keep as doing nothing
    @Override
    public void startPublish(CellId cellId) {
    }

}
