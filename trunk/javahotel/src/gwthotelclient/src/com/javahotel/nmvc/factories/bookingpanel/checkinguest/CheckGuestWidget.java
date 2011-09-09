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
package com.javahotel.nmvc.factories.bookingpanel.checkinguest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.TextHeader;
import com.google.gwt.user.client.ui.Composite;
import com.gwtmodel.table.DataListTypeFactory;
import com.gwtmodel.table.Empty;
import com.gwtmodel.table.FieldDataType;
import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGHeader;
import com.gwtmodel.table.IGetSetVField;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.SynchronizeList;
import com.gwtmodel.table.WChoosedLine;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.listdataview.EditRowsSignal;
import com.gwtmodel.table.listdataview.IListDataView;
import com.gwtmodel.table.listdataview.ListDataViewFactory;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.CustomStringDataTypeSlot;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.slotmodel.SlU;
import com.gwtmodel.table.view.table.VListHeaderContainer;
import com.gwtmodel.table.view.table.VListHeaderDesc;
import com.gwtmodel.table.view.util.SetVPanelGwt;
import com.javahotel.client.abstractto.IAbstractFactory;
import com.javahotel.client.gename.FFactory;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.types.AddType;
import com.javahotel.client.types.BackAbstract;
import com.javahotel.client.types.DataType;
import com.javahotel.client.types.DataUtil;
import com.javahotel.client.types.HModelData;
import com.javahotel.client.types.VField;
import com.javahotel.client.types.VModelDataFactory;
import com.javahotel.common.command.CustomerType;
import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookRecordP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.GuestP;
import com.javahotel.common.toobject.IField;
import com.javahotel.common.toobject.ResObjectP;
import com.javahotel.common.util.GetMaxUtil;

/**
 * @author hotel
 * 
 */
public class CheckGuestWidget extends Composite {

    private final SetVPanelGwt v = new SetVPanelGwt();
    private final BookingP p;

    private Map<String, ResObjectP> rMap = new HashMap<String, ResObjectP>();
    private ReadList rSynch;

    private final ListDataViewFactory liFactory;
    private final IListDataView iList;
    private final IDataType dType = Empty.getDataType();
    private final IAbstractFactory aFactory = HInjector.getI()
            .getAbstractFactory();
    private final List<BookElemP> bList;
    private final IField[] eList = new IField[] { GuestP.F.checkIn,
            GuestP.F.checkOut, CustomerP.F.pTitle, CustomerP.F.firstName,
            CustomerP.F.lastName, CustomerP.F.address1 };

    private class ReadList extends SynchronizeList {

        ReadList(int no) {
            super(no);
        }

        @Override
        protected void doTask() {
            List<IVModelData> vlist = new ArrayList<IVModelData>();

            for (BookElemP b : bList) {
                ResObjectP resO = rMap.get(b.getResObject());
                for (int i = 0; i < resO.getMaxPerson(); i++) {
                    GuestP guest = (GuestP) aFactory.construct(new DataType(
                            AddType.GuestElem));
                    DataUtil.copyField(
                            b,
                            guest,
                            new IField[] { BookElemP.F.checkIn,
                                    BookElemP.F.checkOut },
                            new IField[] { GuestP.F.checkIn, GuestP.F.checkOut });
                    CustomerP cust = (CustomerP) aFactory
                            .construct(new DataType(DictType.CustomerList));
                    cust.setCType(CustomerType.Person);
                    AbstractToCheckGuest g = new AbstractToCheckGuest(b, resO,
                            cust, guest);
                    vlist.add(VModelDataFactory.construct(g));
                }
            }
            IDataListType dList = DataListTypeFactory.construct(vlist);
            iList.getSlContainer().publish(dType,
                    DataActionEnum.DrawListAction, dList);
        }
    }

    private class ReadResObject implements BackAbstract.IRunAction<ResObjectP> {

        @Override
        public void action(ResObjectP t) {
            rMap.put(t.getName(), t);
            rSynch.signalDone();
        }

    }

    private class ModifHeader implements IGHeader {

        private final TextHeader header = new TextHeader("Gość");
        private final Column<Integer, Boolean> col;

        ModifHeader() {
            col = new Column<Integer, Boolean>(new CheckboxCell()) {

                @Override
                public Boolean getValue(Integer object) {
                    return false;
                }

            };
            col.setFieldUpdater(new FieldUpdater<Integer, Boolean>() {

                @Override
                public void update(int index, Integer object, Boolean value) {
                    EditRowsSignal e = new EditRowsSignal(object.intValue(),
                            value.booleanValue(), DataUtil.toList(eList));
                    IVModelData v = SlU.getVDataByI(dType, iList,
                            object.intValue());
                    HModelData ha = (HModelData) v;
                    AbstractToCheckGuest a = (AbstractToCheckGuest) ha.getA();
                    a.setEditable(value.booleanValue());
                    iList.getSlContainer().publish(
                            new CustomStringDataTypeSlot(
                                    EditRowsSignal.EditSignal, dType), e);
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

    private class ClickCust implements ISlotSignaller {

        @Override
        public void signal(ISlotSignalContext slContext) {

            WChoosedLine w = SlU.getWChoosedLine(slContext);
            IVField vie = w.getvField();
            IVModelData v = SlU.getVDataByW(dType, iList, w);
            List<IGetSetVField> vList = SlU.getVListFromEditTable(dType, iList,
                    w.getChoosedLine());
            DrawGuest.drawGuest(vie,v, w.getwSize(), vList);
        }

    }

    public CheckGuestWidget(BookingP p) {
        this.p = p;
        initWidget(v.getvPanel());
        liFactory = GwtGiniInjector.getI().getTablesFactories()
                .getlDataFactory();
        // create list container
        iList = liFactory.construct(dType);
        CellId cI = new CellId(0);
        // register widget catcher
        iList.getSlContainer().registerSubscriber(dType, cI,
                v.constructSetGwt());
        // start working
        iList.startPublish(cI);
        // create header
        IField[] dList = new IField[] { DictionaryP.F.name,
                DictionaryP.F.description };
        List<VListHeaderDesc> fList = FFactory.constructH(dList, eList,
                FFactory.createSet(eList));
        VListHeaderDesc vE = new VListHeaderDesc(new ModifHeader(),
                Empty.getFieldType());
        fList.add(0, vE);
        VListHeaderDesc bAction = new VListHeaderDesc("Pokaż", new VField(
                AbstractToCheckGuest.F.DrawC, FieldDataType.constructString()),
                false, AbstractToCheckGuest.buttonString, false);
        fList.add(3, bAction);
        bAction = new VListHeaderDesc("Wybierz", new VField(
                AbstractToCheckGuest.F.ChooseC, FieldDataType.constructString()),
                false, AbstractToCheckGuest.chooseCust, false);
        fList.add(3, bAction);

        // Create header and publish
        VListHeaderContainer vHeader = new VListHeaderContainer(fList, "XXX");
        iList.getSlContainer().publish(dType, vHeader);
        iList.getSlContainer().registerSubscriber(dType,
                DataActionEnum.TableCellClicked, new ClickCust());

        BookRecordP b;
        b = GetMaxUtil.getLastBookRecord(p);
        assert b != null : LogT.getT().cannotBeNull();
        bList = b.getBooklist();
        assert bList != null && bList.size() != 0 : LogT.getT()
                .CellCannotBeNull();
        for (BookElemP bElem : bList) {
            rMap.put(bElem.getResObject(), null);
        }
        rSynch = new ReadList(rMap.size());
        for (String s : rMap.keySet()) {
            new BackAbstract<ResObjectP>().readAbstract(DictType.RoomObjects,
                    s, new ReadResObject());
        }
    }

}
