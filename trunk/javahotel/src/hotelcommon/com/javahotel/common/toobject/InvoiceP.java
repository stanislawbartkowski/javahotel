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
package com.javahotel.common.toobject;

import java.util.Date;

import com.gwtmodel.table.mapxml.DataMapList;
import com.javahotel.types.LId;

/**
 * @author hotel
 * 
 */
@SuppressWarnings("serial")
public class InvoiceP extends DictionaryP {

    public final static String INVOICE_DATE = "//InvoiceData/InvoiceDate";
    public final static String DATE_OF_DSALE = "//InvoiceData/DateOfSale";
    public final static String NUMBER_OF_DAYS = "//Payment/DaysForPayment";
    public final static String PAYMENT_METHOD = "//Payment/PaymentMethod";
    public final static String PAYMENT_DATE = "//Payment/TerminOfPayment";
    public final static String INVOICENUMBER = "//InvoiceData/InvoiceNumber";

    public final static String HOTEL_DATA_SYMBOL = "//HotelData/Symbol";
    public final static String HOTEL_NAME1 = "//HotelData/Name1";
    public final static String HOTEL_NAME2 = "//HotelData/Name2";
    public final static String HOTEL_ADDRESS1 = "//HotelData/Address1";
    public final static String HOTEL_ADDRESS2 = "//HotelData/Address2";
    public final static String HOTEL_CITY = "//HotelData/City";
    public final static String HOTEL_ZIP = "//HotelData/ZipCode";
    public final static String TOWN_MAKING = "//InvoiceData/TownMaking";
    public final static String HOTEL_BANK_ACCOUNT = "//InvoiceData/BankAccount";

    public final static String BUYER_SYMBOL = "//Payer/Symbol";
    public final static String BUYER_NAME1 = "//Payer/Name1";
    public final static String BUYER_NAME2 = "//Payer/Name2";
    public final static String BUYER_ADDRESS1 = "//Payer/Address1";
    public final static String BUYER_ADDRESS2 = "//Payer/Address2";
    public final static String BUYER_CITY = "//Payer/City";
    public final static String BUYER_ZIP = "//Payer/ZipCode";

    public final static String LINE_NO = "No";
    public final static String LINE_DESCRIPTION = "Description";
    public final static String LINE_QUANTITY = "Quantity";
    public final static String LINE_RATE = "Rate";
    public final static String LINE_TAX = "Tax";
    public final static String LINE_TOTAL = "Total";
    public final static String LINE_SERVICE_DATE = "ServiceDate";

    private LId customer;
    private LId booking;
    private OperationData op;

    private DMapContainerList invoiceD;

    public InvoiceP() {
        this.op = new OperationData();
        invoiceD = new DMapContainerList();
    }

    /**
     * @return the invoiceD
     */
    public DataMapList<?> getInvoiceD() {
        return invoiceD;
    }

    /**
     * @return the customer
     */
    public LId getCustomer() {
        return customer;
    }

    /**
     * @param customer
     *            the customer to set
     */
    public void setCustomer(LId customer) {
        this.customer = customer;
    }

    /**
     * @return the booking
     */
    public LId getBooking() {
        return booking;
    }

    /**
     * @param booking
     *            the booking to set
     */
    public void setBooking(LId booking) {
        this.booking = booking;
    }

    /**
     * @return the dateOp
     */
    public Date getDateOp() {
        return op.getDateOp();
    }

    /**
     * @param dateOp
     *            the dateOp to set
     */
    public void setDateOp(Date dateOp) {
        op.setDateOp(dateOp);
    }

    /**
     * @return the personOp
     */
    public String getPersonOp() {
        return op.getPersonOp();
    }

    /**
     * @param personOp
     *            the personOp to set
     */
    public void setPersonOp(String personOp) {
        op.setPersonOp(personOp);
    }

    public String getRemarks() {
        return op.getRemarks();
    }

    public void setRemarks(final String remarks) {
        op.setRemarks(remarks);
    }

}
