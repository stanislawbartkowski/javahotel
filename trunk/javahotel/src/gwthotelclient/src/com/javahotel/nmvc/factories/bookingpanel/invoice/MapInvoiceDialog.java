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

import java.util.HashMap;
import java.util.Map;

import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.IField;
import com.javahotel.common.toobject.InvoiceIssuerP;
import com.javahotel.common.toobject.InvoiceP;

/**
 * @author hotel
 * 
 */
class MapInvoiceDialog {

    private MapInvoiceDialog() {
    }

    static Map<IField, String> getMapHotel() {
        Map<IField, String> ma = new HashMap<IField, String>();
        ma.put(CustomerP.F.name1, InvoiceP.HOTEL_NAME1);
        ma.put(CustomerP.F.name2, InvoiceP.HOTEL_NAME2);
        ma.put(CustomerP.F.address1, InvoiceP.HOTEL_ADDRESS1);
        ma.put(CustomerP.F.address2, InvoiceP.HOTEL_ADDRESS2);
        ma.put(CustomerP.F.city, InvoiceP.HOTEL_CITY);
        ma.put(CustomerP.F.zipCode, InvoiceP.HOTEL_ZIP);
        ma.put(InvoiceIssuerP.F.townMaking, InvoiceP.TOWN_MAKING);
        ma.put(InvoiceIssuerP.F.paymentDays, InvoiceP.NUMBER_OF_DAYS);
        ma.put(InvoiceIssuerP.F.bankAccount, InvoiceP.HOTEL_BANK_ACCOUNT);
        ma.put(DictionaryP.F.name, InvoiceP.HOTEL_DATA_SYMBOL);
        return ma;
    }

    static Map<IField, String> getMapBuyer() {
        Map<IField, String> ma = new HashMap<IField, String>();
        ma.put(DictionaryP.F.name, InvoiceP.BUYER_SYMBOL);
        ma.put(CustomerP.F.name1, InvoiceP.BUYER_NAME1);
        ma.put(CustomerP.F.name2, InvoiceP.BUYER_NAME2);
        ma.put(CustomerP.F.address1, InvoiceP.BUYER_ADDRESS1);
        ma.put(CustomerP.F.address2, InvoiceP.BUYER_ADDRESS2);
        ma.put(CustomerP.F.city, InvoiceP.BUYER_CITY);
        ma.put(CustomerP.F.zipCode, InvoiceP.BUYER_ZIP);
        return ma;
    }

}
