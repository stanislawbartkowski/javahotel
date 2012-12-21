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
package com.javahotel.common.command;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public enum DictType {

    /** Room standard available. */
    RoomStandard,
    /** Additional room equipment/facility. */
    RoomFacility,
    /** Room, object to book. */
    RoomObjects,
    /** List of vat taxes available. */
    VatDict,
    /** List of services/offerings . */
    ServiceDict,
    /** List of season available. */
    OffSeasonDict,
    /** List of prices: services/seasons/standard. */
    PriceListDict,
    /** List of customers. */
    CustomerList,
    /** List of bookings and stays. */
    BookingList,
    /** Registry of auxiliary data. */
    RegistryParam,
    /** List of additional payments. */
    PaymentRowList,
    /** Data for invoice, */
    IssuerInvoiceList,
    /** List of all invoices. */
    InvoiceList
}
