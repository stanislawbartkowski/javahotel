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
package com.gwthotel.shared;

public interface IHotelConsts {

    String ID = "Id";

    String NAME = "Name";

    String DESCRIPTION = "Description";

    String MESSNAMED = "hotelmessnamed";

    String HOTELADMINEJBJNDI = "java:global/HotelAdmin";
    String TESTHOTELADMINEJBJNDI = "java:global/TestHotelAdmin";
    String HOTELSERVICESJNDI = "java:global/HotelServices";
    String TESTHOTELSERVICESJNDI = "java:global/TestHotelServices";
    String HOTELPRICELISTJNDI = "java:global/HotelPriceList";
    String TESTHOTELPRICELISTJNDI = "java:global/TestHotelPriceList";
    String HOTELPRICEELEMJNDI = "java:global/HotelPriceElem";
    String TESTHOTELPRICEELEMJNDI = "java:global/TestHotelPriceElem";

    String TESTHOTELADMIN = "testhoteladmin";
    String TESTFACTORYMANAGER = "testfactorymanager";
    String TESTHOTELSERVICES = "testhotelservices";
    String TESTHOTELPRICELIST = "testhotelpricelist";
    String TESTHOTELPRICEELEM = "testhotelpriceelem";

    String HOTELREALM = "HotelRealm";

    String HOTELNAME = "hotelloginname";

    String HOTELURLQUERY = "hotel";

    String HOTELPROP = "hotel";
    String VATPROP = "vat";
    String VATLEVELPROP = "level";

    String PRICELISTSERVICEPROP = "serviceprice";
    String PRICELISTPRICEPROP = "priceprice";
}
