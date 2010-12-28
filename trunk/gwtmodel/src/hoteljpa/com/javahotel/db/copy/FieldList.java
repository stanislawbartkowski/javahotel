/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
package com.javahotel.db.copy;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class FieldList {

	public static final String[] HotelList;
	public static final String[] PersonList;
	public static final String[] DictList;
	public static final String[] ObjectList;
	public static final String[] DictVatList;
	public static final String[] DictServiceList;
	public static final String[] DictSeasonOfferList;
	public static final String[] DictSeasonOfferPeriodList;
	public static final String[] PriceListOfferList;
	public static final String[] PriceListSpecialOfferList;
	public static final String[] PriceServiceListOffer;
	public static final String[] CustomerList;
	public static final String[] RemarkList;
	public static final String[] PhoneList;
	public static final String[] BankList;
	public static final String[] BookingList;
	public static final String[] PaymentList;
	public static final String[] ValidationList;
	public static final String[] StateList;
	public static final String[] BookRecordList;
	public static final String[] BookElemList;
	public static final String[] PaymentRowList;
	public static final String[] BillList;
	public static final String[] GuestList;
	public static final String[] AddPayList;

	static {
		HotelList = new String[] { "name", "description", "database" };
		PersonList = new String[] { "name" };
		DictList = new String[] { "name", "description" };
		ObjectList = new String[] { "name", "description", "noPerson",
				"maxPerson", "rType" };
		DictVatList = new String[] { "name", "description", "vatPercent",
				"defVat" };
		DictServiceList = new String[] { "name", "description", "servType" };
		DictSeasonOfferList = new String[] { "name", "description", "startP",
				"endP" };
		DictSeasonOfferPeriodList = new String[] { "pId", "startP", "endP",
				"periodT", "description" };
		PriceListOfferList = new String[] { "name", "description", "season" };
		PriceListSpecialOfferList = new String[] { "price", "specialperiod" };
		PriceServiceListOffer = new String[] { "highseasonprice",
				"lowseasonprice", "highseasonweekendprice",
				"lowseasonweekendprice" };
		CustomerList = new String[] { "name", "description", "zipCode",
				"name1", "name2", "city", "stateUS", "country", "address1",
				"address2", "cType", "NIP", "PESEL", "docType", "docNumber",
				"firstName", "lastName", "pTitle" };
		RemarkList = new String[] { "lp", "remark", "addDate" };
		PhoneList = new String[] { "phoneNumber" };
		BankList = new String[] { "accountNumber" };
		BookingList = new String[] { "name", "description", "checkIn",
				"checkOut", "noPersons", "bookingType", "resName", "season" };
		PaymentList = new String[] { "amount", "sumOp", "dateOp", "payMethod",
				"remarks", "personOp", "datePayment" };
		ValidationList = new String[] { "amount", "validationDate", "remarks",
				"dateOp" };
		StateList = new String[] { "dateOp", "bState", "remarks" };
		BookRecordList = new String[] { "dataFrom", "customerPrice", "oPrice", "seqId" };
		BookElemList = new String[] { "checkIn", "checkOut", "resObject",
				"service" };
		PaymentRowList = new String[] { /* "id" */"rowFrom", "rowTo",
				"offerPrice", "customerPrice" };
		BillList = new String[] { /* "id" */"name", "description", "billType",
				"oPrice" };
		GuestList = new String[] { "checkIn", "checkOut" };
		AddPayList = new String[] { "lp", "payDate", "offerPrice",
				"customerPrice", "personOp", "dateOp", "remarks", "noSe",
				"customerSum", "sumOp" };
	}
}
