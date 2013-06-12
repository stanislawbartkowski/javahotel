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
    String HOTELADMININSTANCEEJBJNDI = "java:global/HotelAdminInstance";
    String HOTELSERVICESJNDI = "java:global/HotelServices";
    String HOTELPRICELISTJNDI = "java:global/HotelPriceList";
    String HOTELPRICEELEMJNDI = "java:global/HotelPriceElem";
    String HOTELROOMSJNDI = "java:global/HotelRooms";
    String HOTELCUSTOMERSJNDI = "java:global/HotelCustomers";

    String HOTELREALM = "HotelRealm";

    String HOTELNAME = "hotelloginname";
    String INSTANCEID = "hotelinstanceid";

    String HOTELURLQUERY = "hotel";

    String HOTELPROP = "hotel";
    String VATPROP = "vat";
    String VATLEVELPROP = "level";

    String PRICELISTSERVICEPROP = "serviceprice";
    String PRICELISTPRICEPROP = "priceprice";

    String CUSTOMERFIRSTNAMEPROP = "firstname";
    String CUSTOMERCOMPANYNAMEPROP = "companyname";
    String CUSTOMERSTREETPROP = "street";
    String CUSTOMERZIPCODEPROP = "zipcode";
    String CUSTOMEREMAILPROP = "email";
    String CUSTOMERPHONEPROP = "phone";

    String INSTANCEDEFAULT = "AppInstanceDefault";
    String INSTANCETEST = "AppInstanceTest";

    String CACHEREALMHOTELINSTANCE = "CACHEREALMHOTELINSTANCE";
}
