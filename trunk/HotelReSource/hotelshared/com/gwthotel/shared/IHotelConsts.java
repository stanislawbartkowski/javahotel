/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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

    int PERSONIDNO = -1;

    String NAME = "name";
    String DESCRIPTION = "descr";

    String MESSNAMED = "hotelmessnamed";

    String HOTELADMINEJBJNDI = "java:global/HotelAdmin";
    String HOTELADMININSTANCEEJBJNDI = "java:global/HotelAdminInstance";
    String HOTELSERVICESJNDI = "java:global/HotelServices";
    String HOTELPRICELISTJNDI = "java:global/HotelPriceList";
    String HOTELPRICEELEMJNDI = "java:global/HotelPriceElem";
    String HOTELROOMSJNDI = "java:global/HotelRooms";
    String HOTELCUSTOMERSJNDI = "java:global/HotelCustomers";
    String HOTELRESERVATIONJNDI = "java:global/HotelReservation";
    String HOTELRESERVATIONOPJNDI = "java:global/HotelReservationOp";
    String HOTELCLEAROPJNDI = "java:global/HotelClearOp";
    String HOTELBILLJNDI = "java:global/HotelBills";
    String HOTELPAYMENTOPJNDI = "java:global/HotelPaymentOp";

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
    String CUSTOMERSURNAMEPROP = "surname";
    String CUSTOMERDOCNUMBPROP = "docnumb";
    String CUSTOMEREMAILPROP = "email";
    String CUSTOMERPHONE1PROP = "phone1";
    String CUSTOMERPHONE2PROP = "phone2";
    String CUSTOMERFAXPROP = "fax";
    String CUSTOMERCOUNTRYPROP = "country";
    String CUSTOMERSTREETPROP = "street";
    String CUSTOMERPOSTALCODEPROP = "postalcode";
    String CUSTOMERCITYPROP = "city";
    String CUSTOMERREGIONPROP = "region";

    String RESDETSERVICENAMEPROP = "servicename";
    String RESDETVATPROP = "rasvat";
    String RESDETPRICELISTNAMEPROP = "pricelistname";

    String RESGUESTCUSTID = "guestcustomer";
    String RESGUESTROOMID = "guestroom";

    String BILLPAYER = "billpayer";
    String BILLRESE = "billreservation";

    String RESCUSTOMERPROP = "rescustomer";

    String RESQUERYROOMPROP = "roomname";

    String RESDATAROOMPROP = "room";
    String RESDATARESERVATIONID = "resid";

    String PAYMENTBILLNAME = "paymentbillname";
    String PAYMENTMETHOD = "paymentmethod";

    String INSTANCEDEFAULT = "AppInstanceDefault";
    String INSTANCETEST = "AppInstanceTest";

    String CACHEREALMHOTELINSTANCE = "CACHEREALMHOTELINSTANCE";

    String PATTPROP = "autompatt";

    String HOTELPERSISTENCEPROVIDER = "hoteladmin";

    String PROPAUTOM = "autompatterns.properties";

    String CREATIONPERSON = "creationperson";
    String MODIFPERSON = "modifperson";

    //String CREATIONPERSONPROPERTY = "creationPerson";
    //String CREATIONDATEPROPERTY = "creationDate";
    //String MODIFPERSONPROPERTY = "modifPerson";
    //String MODIFDATEPROPERTY = "modifDate";

}
