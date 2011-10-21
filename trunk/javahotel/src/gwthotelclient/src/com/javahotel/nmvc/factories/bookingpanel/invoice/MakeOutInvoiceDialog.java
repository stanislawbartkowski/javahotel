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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.VModelData;
import com.gwtmodel.table.VSField;
import com.gwtmodel.table.datamodelview.DataViewModelFactory;
import com.gwtmodel.table.datamodelview.IDataViewModel;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.view.ewidget.EditWidgetFactory;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.IField;
import com.javahotel.common.toobject.InvoiceIssuerP;

/**
 * @author hotel
 * 
 */
class MakeOutInvoiceDialog extends AbstractSlotContainer {

    private final InvoiceIssuerP iIssuer;
    private final String html;
    private final IDataViewModel iView;
    private final DataViewModelFactory daFactory;
    private final EditWidgetFactory eFactory;
    private final IDataType publishType;
    private final CustomerP buyer;

    private Map<IField, String> getMapF() {
        Map<IField, String> ma = new HashMap<IField, String>();
        ma.put(CustomerP.F.name1, "hotel_name1");
        ma.put(CustomerP.F.name2, "hotel_name2");
        ma.put(CustomerP.F.address1, "hotel_address1");
        ma.put(CustomerP.F.address2, "hotel_address2");
        ma.put(CustomerP.F.city, "hotel_city");
        ma.put(CustomerP.F.zipCode, "hotel_zip");
        return ma;
    }

    private Map<IField, String> getMapGuest() {
        Map<IField, String> ma = new HashMap<IField, String>();
        ma.put(CustomerP.F.name1, "buyer_name1");
        ma.put(CustomerP.F.name2, "buyer_name2");
        ma.put(CustomerP.F.address1, "buyer_address1");
        ma.put(CustomerP.F.address2, "buyer_address2");
        ma.put(CustomerP.F.city, "buyer_city");
        ma.put(CustomerP.F.zipCode, "buyer_zip");
        return ma;
    }

    private List<String> getFieldList() {
        List<String> fList = new ArrayList<String>();
        Map<IField, String> ma = getMapF();
        for (String s : ma.values()) {
            fList.add(s);
        }
        ma = getMapGuest();
        for (String s : ma.values()) {
            fList.add(s);
        }
        return fList;
    }

    private FormLineContainer createForm() {
        List<String> fList = getFieldList();
        List<FormField> foList = new ArrayList<FormField>();
        for (String f : fList) {
            IVField v = VSField.createVString(f);
            IFormLineView e = eFactory.constructTextField(v);
            FormField fie = new FormField(null, e);
            fie.setHtmlId(f);
            foList.add(fie);
        }
        return new FormLineContainer(foList, html);
    }

    private class GetWidget implements ISlotListener {

        private final CellId cellId;

        GetWidget(CellId cellId) {
            this.cellId = cellId;
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            publish(publishType, cellId, slContext.getGwtWidget());
        }

    }

    private class DrawWidget implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            IVModelData va = new VModelData();

            for (Entry<IField, String> e : getMapF().entrySet()) {
                Object o = iIssuer.getF(e.getKey());
                IVField v = VSField.createVString(e.getValue());
                va.setF(v, o);
            }
            for (Entry<IField, String> e : getMapGuest().entrySet()) {
                Object o = buyer.getF(e.getKey());
                IVField v = VSField.createVString(e.getValue());
                va.setF(v, o);
            }
            iView.getSlContainer().publish(dType,
                    DataActionEnum.DrawViewFormAction, va);
        }

    }

    MakeOutInvoiceDialog(IDataType publishType, IDataType dType,
            InvoiceIssuerP iIssuer, String html, CustomerP buyer) {
        this.iIssuer = iIssuer;
        this.dType = dType;
        this.html = html;
        this.publishType = publishType;
        this.buyer = buyer;
        daFactory = GwtGiniInjector.getI().getDataViewModelFactory();
        eFactory = GwtGiniInjector.getI().getEditWidgetFactory();
        iView = daFactory.construct(dType, createForm());

        // SlU.registerWidgetListener0(dType, iView, new GetWidget());
        this.registerSubscriber(publishType, DataActionEnum.DrawViewFormAction,
                new DrawWidget());
    }

    @Override
    public void startPublish(CellId cellId) {
        iView.getSlContainer().registerSubscriber(dType, cellId,
                new GetWidget(cellId));
        iView.startPublish(cellId);
    }

}
