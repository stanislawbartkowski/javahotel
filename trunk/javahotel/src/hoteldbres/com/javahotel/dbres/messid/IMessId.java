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
package com.javahotel.dbres.messid;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public interface IMessId {

    String ADDHOTEL = "ADDHOTEL";
    String CHANGEHOTEL = "CHANGEHOTEL";
    String DESCHOTEL = "DESCHOTEL";
    String REMOVEHOTEL = "REMOVEHOTEL";
    String ADDPERSON = "ADDPERSON";
    String CHANGEPERSON = "CHANGEPERSON";
    String DESCPERSON = "DESCPERSON";
    String REMOVEPERSON = "REMOVEPERSON";
    String REMOVEGRANT = "REMOVEGRANT";
    String SETGRANT = "SETGRANT";
    String GETLIST = "GETLIST";
    String UNKNOWNHOTEL = "UNKNOWNHOTEL";
    String EMPTYCOMMANDPARAM = "EMPTYCOMMANDPARAM";
    String EMPTYVATFIELD = "EMPTYVATFIELD";
    String FATALLOGININFO = "FATALLOGININFO";
    String VATNOTEFINED = "VATNOTEFINED";
    String SPECIALPERIODNOTFOUND = "SPECIALPERIODNOTFOUND";
    String DRAWPERIOD = "DRAWPERIOD";
    String READRESTATE = "READRESSTATE";
    String NULLSTATERES = "NULLSTATERES";
    String NULLSTATERESBOOKING = "NULLSTATERESBOOKING";
    String NULLCUSTOMER = "NULLCUSTOMER";
    String CANNOTFINDBOOKING = "CANNOTFINDBOOKING";
    String CLEARHOTELDATA = "CLEARHOTELDATA";
    String ADDDICTRECORD = "ADDDICTRECORD";
    String MODIFDICTRECORD = "MODIFDICTRECORD";
    String DICTRECORDDESCRIPTION = "DICTRECORDDESCRIPTION";
    String DELETEDICTRECORD = "DELETEDICTRECORD";
    String REMOVEFROMCOLLECTION = "REMOVEFROMCOLLECTION";
    String GETLISTDICT = "GETLISTDIC";
    String RESSTATERES = "RESSTATERES";
    String RESPAYMENTROW = "RESPAYMENTROW";
    String RESGETDATA = "RESGETDATA";
    String RESADDBOOK = "RESADDBOOK";
    String RESOMITDATE = "RESOMITDATE";
    String RESOMITNOTEQUAL = "RESOMITNOTEQUAL";
    String RESOMMITEDBADSTATE = "RESOMMITEDBADSTATE";
    String NOPAYMENTROWS = "NOPAYMENTROWS";
    String MORETHENONERESERVATION = "MORETHENONERESERVATION";
    String INPROPERINVOICEXML = "INPROPERINVOICEXML";
    String NULLCUSTOMERINVOICE = "NULLCUSTOMERINVOICE";
    String NULLBOOKINGINVOICE = "NULLBOOKINGINVOICE";
}
