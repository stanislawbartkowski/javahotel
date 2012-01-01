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
package com.javahotel.nmvc.factories.bookingpanel.invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.TextHeader;
import com.gwtmodel.table.AbstractListT;
import com.gwtmodel.table.AbstractListT.IGetList;
import com.gwtmodel.table.DataListTypeFactory;
import com.gwtmodel.table.Empty;
import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGHeader;
import com.gwtmodel.table.IMapEntry;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.MapEntryFactory;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.controler.DataListParam;
import com.gwtmodel.table.controler.DisplayListControlerParam;
import com.gwtmodel.table.controler.TableDataControlerFactory;
import com.gwtmodel.table.factories.IDataPersistAction;
import com.gwtmodel.table.factories.IHeaderListContainer;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.mapxml.IDataContainer;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ClickButtonType.StandClickEnum;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotCallerListener;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.slotmodel.SlU;
import com.gwtmodel.table.view.table.VListHeaderContainer;
import com.gwtmodel.table.view.table.VListHeaderDesc;
import com.javahotel.client.abstractto.InvoicePVData;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.DContainer;
import com.javahotel.common.toobject.InvoiceP;
import com.javahotel.common.toobject.PaymentRowP;
import com.javahotel.common.toobject.ServiceDictionaryP;
import com.javahotel.common.toobject.VatDictionaryP;
import com.javahotel.nmvc.factories.booking.util.IsServiceBooking;

/**
 * @author hotel
 * 
 */
class InvoiceLines extends AbstractSlotContainer {

    private final BookingP bo;
    private final ISlotable i;
    private final IsServiceBooking iService;
    private final InvoiceP startP;

    private final static String SERVICE_DESCRIPTION = "line_service_description";
    private final static String QUANTITY = "line_quantity";
    private final static String TOTAL = "line_total";
    private final static String TAX_VAT = "line_tax";
    private final static String SERVICE_DATE = "line_service_date";
    private final static String RATE = "line_rate";
    private final static String ID = "No";

    private final MapS mapF = new MapS();
    private final AbstractListT A = new MapI();
    private final List<String> fieldList = new ArrayList<String>();

    private static class I implements IGetList {

        @Override
        public List<IMapEntry> getL() {
            List<IMapEntry> ma = new ArrayList<IMapEntry>();
            ma.add(MapEntryFactory.createEntry(InvoiceP.LINE_DESCRIPTION,
                    SERVICE_DESCRIPTION));
            ma.add(MapEntryFactory
                    .createEntry(InvoiceP.LINE_QUANTITY, QUANTITY));
            ma.add(MapEntryFactory.createEntry(InvoiceP.LINE_TAX, TAX_VAT));
            ma.add(MapEntryFactory.createEntry(InvoiceP.LINE_TOTAL, TOTAL));
            ma.add(MapEntryFactory.createEntry(InvoiceP.LINE_SERVICE_DATE,
                    SERVICE_DATE));
            ma.add(MapEntryFactory.createEntry(InvoiceP.LINE_RATE, RATE));
            ma.add(MapEntryFactory.createEntry(InvoiceP.LINE_NO, ID));
            return ma;
        }
    }

    private static class MapI extends AbstractListT {

        /**
         * @param iGet
         */
        protected MapI() {
            super(new I());
        }

    }

    private class MapS extends MapStringField {

        @Override
        SType GetType(String s) {
            if (s.equals(QUANTITY) || s.equals(ID)) {
                return SType.INT;
            }
            if (s.equals(TOTAL)) {
                return SType.AMOUNT;
            }
            if (s.equals(RATE)) {
                return SType.AMOUNT;
            }
            if (s.equals(SERVICE_DATE)) {
                return SType.DATE;
            }
            return SType.STRING;
        }
    }

    private class ModifHeader implements IGHeader {

        private final TextHeader header = new TextHeader("Faktura");
        private final Column<Integer, Boolean> col;

        private InvoiceLineAbstractTo getI(int index) {
            IDataListType dList = SlU.getIDataListType(dType, i);
            IVModelData va = dList.getList().get(index);
            InvoiceLineAbstractTo a = (InvoiceLineAbstractTo) va;
            return a;
        }

        ModifHeader() {
            col = new Column<Integer, Boolean>(new CheckboxCell()) {

                @Override
                public Boolean getValue(Integer object) {
                    InvoiceLineAbstractTo a = getI(object);
                    return a.isCheck();
                }

            };
            col.setFieldUpdater(new FieldUpdater<Integer, Boolean>() {

                @Override
                public void update(int index, Integer object, Boolean value) {
                    InvoiceLineAbstractTo a = getI(index);
                    a.setCheck(value.booleanValue());
                }

            });
        }

        @Override
        public Header<?> getHeader() {
            return header;
        }

        @Override
        public Column<?, ?> getColumn() {
            return col;
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
            VListHeaderDesc v = new VListHeaderDesc(null, mapF.get(ID), true);
            vList.add(v);
            addH(vList, "Data", SERVICE_DATE);
            addH(vList, "Opis", SERVICE_DESCRIPTION);
            addH(vList, "Ilość", QUANTITY);
            addH(vList, "Cena", RATE);
            addH(vList, "VAT", TAX_VAT);
            addH(vList, "Wartość", TOTAL);
            VListHeaderDesc vE = new VListHeaderDesc(new ModifHeader(),
                    Empty.getFieldType());
            vList.add(0, vE);

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
                List<IVModelData> dList = new ArrayList<IVModelData>();
                IDataListType listType = DataListTypeFactory.construct(dList);
                for (BookElemP bElem : bo.getBooklist()) {
                    for (PaymentRowP rowP : bElem.getPaymentrows()) {
                        ServiceDictionaryP se = iService.getService(bElem
                                .getService());
                        VatDictionaryP va = se.getVat();
                        BigDecimal total = rowP.getCustomerPrice();
                        InvoiceLineAbstractTo a = new InvoiceLineAbstractTo();
                        a.setF(mapF.get(ID), rowP.getId().getId());
                        a.setF(mapF.get(SERVICE_DESCRIPTION),
                                se.getDescription());
                        a.setF(mapF.get(QUANTITY), new Integer(1));
                        a.setF(mapF.get(TAX_VAT), va.getName());
                        a.setF(mapF.get(RATE), total);
                        a.setF(mapF.get(TOTAL), total);
                        a.setF(mapF.get(SERVICE_DATE), rowP.getRowFrom());
                        if (startP.getInvoiceD().getdLines().isEmpty()) {
                            a.setCheck(true);
                        } else {
                            Long l = rowP.getId().getId();
                            boolean check = false;
                            for (IDataContainer da : startP.getInvoiceD()
                                    .getdLines()) {
                                Long la = (Long) da.get(InvoiceP.LINE_NO);
                                if ((la != null) && la.equals(l)) {
                                    check = true;
                                }
                            } // for
                            a.setCheck(check);
                        }

                        dList.add(a);
                    }
                }
                publish(dType, DataActionEnum.ListReadSuccessSignal, listType);
            }

        }

        PersistClass() {
            this.dType = InvoiceLines.this.dType;
            registerSubscriber(dType, DataActionEnum.ReadListAction,
                    new ReadList());
        }

    }

    private class SetGetter implements ISlotCallerListener {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            IVModelData va = slContext.getVData();
            InvoicePVData pa = (InvoicePVData) va;
            InvoiceP p = pa.getP();
            p.getInvoiceD().getdLines().clear();
            IDataListType dList = SlU.getIDataListType(dType, i);
            for (IVModelData v : dList.getList()) {
                InvoiceLineAbstractTo a = (InvoiceLineAbstractTo) v;
                if (!a.isCheck()) {
                    continue;
                }
                DContainer da = (DContainer) p.getInvoiceD().addToLines();
                for (String s : fieldList) {
                    String key = A.getValue(s);
                    Object o = v.getF(mapF.get(s));
                    da.put(key, o);
                }
            }

            return slContext;
        }
    }

    InvoiceLines(IDataType publishType, CellId panelId, BookingP p,
            IsServiceBooking iService, InvoiceP iP) {
        assert p != null : LogT.getT().cannotBeNull();
        this.bo = p;
        this.iService = iService;
        this.dType = publishType;
        this.startP = iP;
        // this.dType = Empty.getDataType();
        TableDataControlerFactory tFactory = GwtGiniInjector.getI()
                .getTableDataControlerFactory();
        fieldList.add(SERVICE_DESCRIPTION);
        fieldList.add(QUANTITY);
        fieldList.add(TAX_VAT);
        fieldList.add(TOTAL);
        fieldList.add(SERVICE_DATE);
        fieldList.add(RATE);
        fieldList.add(ID);
        mapF.createMap(fieldList);

        List<ControlButtonDesc> dButton = new ArrayList<ControlButtonDesc>();
        dButton.add(cButtonFactory.constructButt(StandClickEnum.FILTRLIST));
        dButton.add(cButtonFactory.constructButt(StandClickEnum.FIND));

        ListOfControlDesc cList = new ListOfControlDesc(dButton);
        DataListParam lParam = new DataListParam(dType, new PersistClass(),
                new HeaderList(), null, null, null, null);
        DisplayListControlerParam dList = tFactory.constructParam(cList,
                panelId, lParam, null);
        i = tFactory.constructDataControler(dList);
        registerCaller(dType, GetActionEnum.GetViewModelEdited, new SetGetter());
        registerCaller(dType, GetActionEnum.GetModelToPersist, new SetGetter());
        this.setSlContainer(i);
    }

    @Override
    public void startPublish(CellId cellId) {
        i.startPublish(cellId);
    }

}
