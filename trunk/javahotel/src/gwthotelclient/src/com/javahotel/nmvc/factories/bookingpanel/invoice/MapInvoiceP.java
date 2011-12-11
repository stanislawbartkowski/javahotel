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

import com.gwtmodel.table.AbstractListT;
import com.gwtmodel.table.AbstractListT.IGetList;
import com.gwtmodel.table.IMapEntry;
import com.gwtmodel.table.MapEntryFactory;
import com.javahotel.common.toobject.InvoiceP;

/**
 * @author hotel
 * 
 */
class MapInvoiceP {

    private MapInvoiceP() {

    }

    private static class MapI extends AbstractListT {

        /**
         * @param iGet
         */
        protected MapI() {
            super(new I());
        }

    }

    private final static AbstractListT IM = new MapI();

    /**
     * Returns mapping InvoiceP => Form HtmlId;
     * 
     * @return Mapping
     */
    static AbstractListT getM() {
        return IM;
    }

    private static class I implements IGetList {

        @Override
        public List<IMapEntry> getL() {
            List<IMapEntry> ma = new ArrayList<IMapEntry>();
            ma.add(MapEntryFactory.createEntry(InvoiceP.HOTEL_DATA_SYMBOL,
                    IMap.HOTEL_SYMBOL_DATA));
            ma.add(MapEntryFactory.createEntry(InvoiceP.HOTEL_NAME1,
                    IMap.HOTEL_NAME1));
            ma.add(MapEntryFactory.createEntry(InvoiceP.HOTEL_NAME2,
                    IMap.HOTEL_NAME2));
            ma.add(MapEntryFactory.createEntry(InvoiceP.HOTEL_ADDRESS1,
                    IMap.HOTEL_ADDRESS1));
            ma.add(MapEntryFactory.createEntry(InvoiceP.HOTEL_ADDRESS2,
                    IMap.HOTEL_ADDRESS2));
            ma.add(MapEntryFactory.createEntry(InvoiceP.HOTEL_CITY,
                    IMap.HOTEL_CITY));
            ma.add(MapEntryFactory.createEntry(InvoiceP.HOTEL_ZIP,
                    IMap.HOTEL_ZIP));
            ma.add(MapEntryFactory.createEntry(InvoiceP.TOWN_MAKING,
                    IMap.INVOICE_PLACE));
            ma.add(MapEntryFactory.createEntry(InvoiceP.NUMBER_OF_DAYS,
                    IMap.NUMBER_OF_DAYS));
            ma.add(MapEntryFactory.createEntry(InvoiceP.HOTEL_BANK_ACCOUNT,
                    IMap.INVOICE_BANK_ACCOUNT));

            ma.add(MapEntryFactory.createEntry(InvoiceP.BUYER_SYMBOL,
                    IMap.BUYER_SYMBOL));
            ma.add(MapEntryFactory.createEntry(InvoiceP.BUYER_NAME1,
                    IMap.BUYER_NAME1));
            ma.add(MapEntryFactory.createEntry(InvoiceP.BUYER_NAME2,
                    IMap.BUYER_NAME2));
            ma.add(MapEntryFactory.createEntry(InvoiceP.BUYER_ADDRESS1,
                    IMap.HOTEL_ADDRESS1));
            ma.add(MapEntryFactory.createEntry(InvoiceP.BUYER_ADDRESS2,
                    IMap.BUYER_ADDRESS2));
            ma.add(MapEntryFactory.createEntry(InvoiceP.BUYER_CITY,
                    IMap.BUYER_CITY));
            ma.add(MapEntryFactory.createEntry(InvoiceP.BUYER_ZIP,
                    IMap.BUYER_ZIP));

            ma.add(MapEntryFactory.createEntry(InvoiceP.DATE_OF_DSALE,
                    IMap.DATE_OF_SALE));
            ma.add(MapEntryFactory.createEntry(InvoiceP.INVOICE_DATE,
                    IMap.INVOICE_DATE));
            ma.add(MapEntryFactory.createEntry(InvoiceP.PAYMENT_DATE,
                    IMap.PAYMENT_DATE));
            ma.add(MapEntryFactory.createEntry(InvoiceP.PAYMENT_METHOD,
                    IMap.PAYMENT_METHOD));
            ma.add(MapEntryFactory.createEntry(InvoiceP.INVOICENUMBER,
                    IMap.INVOICE_NUMBER));
            return ma;
        }
    }

    // static void fromInvoiceToM(InvoiceP sou, IVModelData dest,
    // MapStringField maps) {
    // DataMapList.DContainer dsou = sou.getInvoiceD().getdFields();
    // for (IMapEntry e : IM.getEntryList()) {
    // Object o = dsou.get(e.getKey());
    // dest.setF(maps.get(e.getValue()), o);
    // }
    //
    // }

}
