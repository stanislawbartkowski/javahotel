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
package com.javahotel.nmvc.factories.booking.elem;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
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
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ClickButtonType.StandClickEnum;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.javahotel.client.types.DataUtil;
import com.javahotel.common.toobject.BookElemP;

/**
 * @author hotel
 * 
 */
class PaymentRowDetails extends AbstractSlotContainer {

    private final TableDataControlerFactory taFactory;
    private final IDataControler iControler;
    private final BookElemP p;

    private class ReadList implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            IDataListType listType = DataUtil.construct(p.getPaymentrows());
            publish(dType, DataActionEnum.ListReadSuccessSignal, listType);
        }
    }

    private IDataControler createControler() {
        ITableCustomFactories fContainer = GwtGiniInjector.getI()
                .getTableFactoriesContainer();

        // IDataPersistAction persistA = new DataPersistAction();
        IDataPersistAction persistA = null;
        IHeaderListContainer heList = fContainer.getHeaderListFactory()
                .construct(dType);
        IDataModelFactory dataFactory = fContainer.getDataModelFactory();
        IFormTitleFactory formFactory = fContainer.getFormTitleFactory();

        IGetViewControllerFactory fControler = fContainer
                .getGetViewControllerFactory();

        DataListParam dLiParam = new DataListParam(dType, persistA, heList,
                dataFactory, formFactory, fControler);

        List<ControlButtonDesc> dButton = new ArrayList<ControlButtonDesc>();

        dButton.add(cButtonFactory.constructButt(StandClickEnum.SHOWITEM));
        dButton.add(cButtonFactory.constructButt(StandClickEnum.FILTRLIST));
        dButton.add(cButtonFactory.constructButt(StandClickEnum.FIND));

        ListOfControlDesc cList = new ListOfControlDesc(dButton);

        CellId cI = new CellId(0);
        DisplayListControlerParam lParam = taFactory.constructParam(cList, cI,
                dLiParam, null);
        return taFactory.constructDataControler(lParam);
    }

    PaymentRowDetails(IDataType dType, BookElemP p) {
        this.p = p;
        taFactory = GwtGiniInjector.getI().getTableDataControlerFactory();
        this.dType = dType;
        iControler = createControler();
        registerSubscriber(dType, DataActionEnum.ReadListAction, new ReadList());
        setSlContainer(iControler);
    }

    @Override
    public void startPublish(CellId cellId) {
        iControler.startPublish(null);
    }

}
