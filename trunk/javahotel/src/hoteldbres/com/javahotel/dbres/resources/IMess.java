/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
package com.javahotel.dbres.resources;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public interface IMess {

	String INVALID_SESSION = "InvalidSession";
	String PUPREFIX = "PUPrefix";
	String PUSECURITY = "EJBPersonRealmPU";
	String DATABASESECURITYID = "SecurityDB";
	String ADMINEXPECTED = "ADMINEXPECTED";
	String HOTELPASSWORDFILE = "hotelpassword";
	String RESOURCENOTDEFINED = "RESOURCENOTDEFINED";
	String USERPASSWORDNOTVALID = "USERPASSWORDNOTVALID";
	String VATDICTXMLFILE = "VatDictDefault";
	String VATDICTTAGNAME = "VatDictTagName";
	String BOOKINGPATTID = "BookingPattId";
	String BOOKINGPATT = "BookingPatt";
	String STAYPATTID = "StayPattId";
	String STAYPATT = "StayPatt";
	String BILLPATT = "BillPatt";
	String BILLPATTID = "BillPattId";
	String BOOKRECORDSEQID = "BookRecordSeqId";
	String userTAG = "name";
	String passwdTAG = "password";
	String DATEOPFIELD = "dateOp";
	String PERSONOPFIELD = "personOp";
	String CUSTOMERPERSONPATTID = "CustomerPersonPattId";
	String CUSTOMERPERSONPATT = "CustomerPersonPatt";
	String CUSTOMERCOMPANYPATTID = "CustomerCompanyPattId";
	String CUSTOMERCOMPANYPATT = "CustomerCompanyPatt";
	String RESERVATIONNOTSTAY = "ReservationNotStay";
	String LOGINCACHEID = "LOGINCACHEID";
	String HOTELCACHEID = "HOTELCACHEID";
	String CONTRXMLDEF = "entityconstraints";
	String RESOURCEFOLDER = "RESOURCES";
	String RESOMITNOTEQUAL = "RESOMITNOTEQUAL";
}
