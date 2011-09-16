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
import com.gwtmodel.table.IDataListType;
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
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotCaller;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.view.util.SetVPanelGwt;
import com.javahotel.client.types.AddType;
import com.javahotel.client.types.DataType;
import com.javahotel.client.types.HModelData;
import com.javahotel.client.types.VModelDataFactory;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookRecordP;

public class BookingElemContainer extends AbstractSlotMediatorContainer {

    private final SetVPanelGwt sPanel = new SetVPanelGwt();
    private final IDataType publishdType;
    private final IMemoryListModel lPersistList;
    private final IDataControler dControler;
    private final ITableCustomFactories tFactories;

    private class DrawModel implements ISlotSignaller {

        @Override
        public void signal(ISlotSignalContext slContext) {
            List<AbstractLpVModelData> li = new ArrayList<AbstractLpVModelData>();
            BookRecordP p = P.getBookR(slContext);
            if ((p != null) && (p.getBooklist() != null)) {
                for (BookElemP e : p.getBooklist()) {
                    AbstractLpVModelData lp = VModelDataFactory.constructLp(e);
                    li.add(lp);
                }
            }
            lPersistList.setDataList(DataListTypeFactory.constructLp(li));
            dControler.startPublish(new CellId(0));
        }
    }

    private class SetGetter implements ISlotCaller {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            BookRecordP p = P.getBookR(slContext);
            IDataListType li = lPersistList.getDataList();
            List<BookElemP> ll = new ArrayList<BookElemP>();
            for (IVModelData v : li.getList()) {
                HModelData vData = (HModelData) v;
                BookElemP e = (BookElemP) vData.getA();
                ll.add(e);
            }
            p.setBooklist(ll);
            return slContext;
        }
    }

    class ChangeMode implements ISlotSignaller {

        @Override
        public void signal(ISlotSignalContext slContext) {
            slMediator.getSlContainer().publish(dType,
                    DataActionEnum.ChangeViewFormModeAction,
                    slContext.getPersistType());
        }

    }

    public BookingElemContainer(final ICallContext iContext,
            final DataType subType, IFormDefFactory fFactory,
            IFormTitleFactory tiFactory) {
        tFactories = iContext.getC();
        publishdType = iContext.getDType();
        dType = new DataType(AddType.BookElem);
        slMediator.getSlContainer().registerSubscriber(subType,
                DataActionEnum.DrawViewFormAction, new DrawModel());

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
            public ComposeControllerType construct(ICallContext iiContext) {
                ISlotable iSlo = new BookingElem(iiContext,
                        BookingElemContainer.this, subType);
                return new ComposeControllerType(iSlo, subType);
            }

        };
        IGetViewControllerFactory iGetCon = new MemoryGetController(fFactory,
                iDataModelFactory, daFactory, lPersistList, vFactory,
                custFactory);

        DisplayListControlerParam cParam = tFactory.constructParam(cI,
                new DataListParam(dType, lPersistList, null, iDataModelFactory,
                        tiFactory, iGetCon), null);
        IHeaderListFactory he = tFactories.getHeaderListFactory();
        dControler = tFactory.constructDataControler(cParam);
        slMediator.registerSlotContainer(dControler);
        slMediator.registerSlotContainer(he.construct(dType));
        slMediator.getSlContainer().registerSubscriber(dType, cI,
                sPanel.constructSetGwt());
        slMediator.getSlContainer().registerCaller(subType,
                GetActionEnum.GetModelToPersist, new SetGetter());
        slMediator.getSlContainer().registerCaller(subType,
                GetActionEnum.GetViewModelEdited, new SetGetter());
        slMediator.getSlContainer().registerSubscriber(subType,
                DataActionEnum.ChangeViewFormModeAction, new ChangeMode());

    }

    @Override
    public void startPublish(CellId cellId) {
        slMediator.startPublish(null);
        slMediator.getSlContainer().publish(publishdType, cellId,
                sPanel.constructGWidget());
    }

}
