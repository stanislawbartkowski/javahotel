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
package com.javahotel.nmvc.factories.bookingpanel.invoice;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.AbstractLpVModelData;
import com.gwtmodel.table.DataListTypeFactory;
import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.controler.DataListParam;
import com.gwtmodel.table.controler.DisplayListControlerParam;
import com.gwtmodel.table.controler.TableDataControlerFactory;
import com.gwtmodel.table.factories.IDataPersistAction;
import com.gwtmodel.table.factories.IHeaderListContainer;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ClickButtonType.StandClickEnum;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.view.table.VListHeaderContainer;
import com.gwtmodel.table.view.table.VListHeaderDesc;
import com.javahotel.common.toobject.BookingP;

/**
 * @author hotel
 * 
 */
class InvoiceLines extends AbstractSlotContainer {

    private final BookingP p;
    // private final IDataType publishType;
    private final ISlotable i;

    private final static String SERVICE_DESCRIPTION = "line_service_description";
    private final static String QUANTITY = "line_quantity";
    private final static String TOTAL = "line_total";
    private final static String TAX_VAT = "line_tax";

    private final MapS mapF = new MapS();

    private class MapS extends MapStringField {

        @Override
        SType GetType(String s) {
            if (s.equals(QUANTITY)) {
                return SType.INT;
            }
            if (s.equals(TOTAL)) {
                return SType.AMOUNT;
            }
            return SType.STRING;
        }
    }

    private class HeaderList extends AbstractSlotContainer implements
            IHeaderListContainer {

        HeaderList() {
            this.dType = InvoiceLines.this.dType;
        }

        private void addH(List<VListHeaderDesc> hList, String headerS, String id) {
            VListHeaderDesc v = new VListHeaderDesc(headerS, mapF.get(id));
            hList.add(v);
        }

        @Override
        public void startPublish(CellId cellId) {
            String title = "Pozycje do faktury";
            List<VListHeaderDesc> vList = new ArrayList<VListHeaderDesc>();
            addH(vList, "Opis", SERVICE_DESCRIPTION);
            addH(vList, "Ilość", QUANTITY);
            addH(vList, "VAT", TAX_VAT);
            addH(vList, "Wartość", TOTAL);
            VListHeaderContainer vHeader = new VListHeaderContainer(vList,
                    title);
            publish(dType, vHeader);
        }

    }

    private class PersistClass extends AbstractSlotContainer implements
            IDataPersistAction {

        private class ReadList implements ISlotListener {

            @Override
            public void signal(ISlotSignalContext slContext) {
                List<AbstractLpVModelData> dList = new ArrayList<AbstractLpVModelData>();
                IDataListType listType = DataListTypeFactory.constructLp(dList);
                publish(dType, DataActionEnum.ListReadSuccessSignal, listType);
            }

        }

        PersistClass() {
            this.dType = InvoiceLines.this.dType;
            registerSubscriber(dType, DataActionEnum.ReadListAction,
                    new ReadList());
        }

    }

    InvoiceLines(IDataType publishType, CellId panelId, BookingP p) {
        this.p = p;
        this.dType = publishType;
        // this.dType = Empty.getDataType();
        TableDataControlerFactory tFactory = GwtGiniInjector.getI()
                .getTableDataControlerFactory();
        List<String> sList = new ArrayList<String>();
        sList.add(SERVICE_DESCRIPTION);
        sList.add(QUANTITY);
        sList.add(TAX_VAT);
        sList.add(TOTAL);
        mapF.createMap(sList);

        List<ControlButtonDesc> dButton = new ArrayList<ControlButtonDesc>();
        dButton.add(cButtonFactory.constructButt(StandClickEnum.FILTRLIST));
        dButton.add(cButtonFactory.constructButt(StandClickEnum.FIND));

        ListOfControlDesc cList = new ListOfControlDesc(dButton);
        DataListParam lParam = new DataListParam(dType, new PersistClass(),
                new HeaderList(), null, null, null, null);
        DisplayListControlerParam dList = tFactory.constructParam(dType, cList,
                panelId, lParam, null);
        i = tFactory.constructDataControler(dList);
        this.setSlContainer(i);
    }

    @Override
    public void startPublish(CellId cellId) {
        i.startPublish(cellId);
        // publish(dType, DataActionEnum.DrawViewFormAction);
    }

}
