/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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

/**
 * @author hotel
 * 
 */
public interface IMap {

    String NUMBER_OF_DAYS = "invoice_number_of_days";
    String HOTEL_NAME1 = "hotel_name1";
    String HOTEL_NAME2 = "hotel_name2";
    String HOTEL_ADDRESS1 = "hotel_address1";
    String HOTEL_ADDRESS2 = "hotel_address2";
    String HOTEL_CITY = "hotel_city";
    String HOTEL_ZIP = "hotel_zip";
    String HOTEL_SYMBOL_DATA = "hotel_symbol_data";    
    
    String INVOICE_PLACE = "invoice_place";
    String INVOICE_BANK_ACCOUNT = "invoice_bank_account";
    String BUYER_SYMBOL = "buyer_symbol";
    String BUYER_NAME1 = "buyer_name1";
    String BUYER_NAME2 = "buyer_name2";
    String BUYER_ADDRESS1 = "buyer_address1";
    String BUYER_ADDRESS2 = "buyer_address2";
    String BUYER_CITY = "buyer_city";
    String BUYER_ZIP = "buyer_zip";

    String INVOICE_DATE = "invoice_date";
    String DATE_OF_SALE = "date_of_sale";
    String PAYMENT_METHOD = "invoice_payment_method";
    String PAYMENT_DATE = "invoice_payment_date";
    String INVOICE_NUMBER = "invoice_number";
    
    String INVOICE_LINES = "invoice_lines";

    String CHOOSE_BUYER_WIDGET = "choose_buyer";
    String CHOOSE_HOTEL_DATA = "choose_hotel_data";
    
    String GUEST_TITLE = "guest_title";
    String GUEST_NAME1 = "guest_name1";
    String GUEST_NAME2 = "guest_name2";
}
