/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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
package com.gwthotel.admintest.suite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.ServiceType;
import com.gwthotel.hotel.customer.HotelCustomer;
import com.gwthotel.hotel.reservation.ReservationForm;
import com.gwthotel.hotel.reservation.ReservationPaymentDetail;
import com.gwthotel.hotel.rooms.HotelRoom;
import com.gwthotel.hotel.services.HotelServices;
import com.gwthotel.shared.IHotelConsts;
import com.gwtmodel.table.common.DateFormat;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;

public class Test16 extends TestHelper {

	private final static String SERVICE1 = "beef";
	private final static String RESNAME = "2013/R/2";

	@Before
	public void before() {
		clearObjects();
		createHotels();
		setTestToday(DateFormat.toD(2013, 6, 13));
	}

	@Test
	public void test1() {
		HotelCustomer p = (HotelCustomer) hObjects.construct(getH(HOTEL), HotelObjects.CUSTOMER);
		p = iCustomers.addElem(getH(HOTEL), p);
		ReservationForm r = (ReservationForm) hObjects.construct(getH(HOTEL), HotelObjects.RESERVATION);
		r.setCustomerName(p.getName());
		r.setName(RESNAME);
		r = iRes.addElem(getH(HOTEL), r);
		System.out.println(r.getName());

		assertNotNull(r.getName());
		HotelRoom ho = new HotelRoom();
		ho.setName("P10");
		ho.setNoPersons(3);
		iRooms.addElem(getH(HOTEL), ho);

		HotelServices se = new HotelServices();
		se.setName(SERVICE1);
		se.setDescription("Restaurant");
		se.setAttr(IHotelConsts.VATPROP, "7%");
		se.setServiceType(ServiceType.OTHER);
		iServices.addElem(getH(HOTEL), se);
		se = iServices.findElem(getH(HOTEL), "beef");

		p = (HotelCustomer) hObjects.construct(getH(HOTEL), HotelObjects.CUSTOMER);
		String guest1 = iCustomers.addElem(getH(HOTEL), p).getName();

		ReservationPaymentDetail add = new ReservationPaymentDetail();
		add.setDescription("Beverage");
		add.setGuestName(guest1);
		add.setPrice(new BigDecimal(100));
		add.setPriceList(new BigDecimal(150.00));
		add.setPriceTotal(new BigDecimal(200.0));
		add.setQuantity(2);
		add.setRoomName("P10");
		add.setServDate(toDate(2013, 4, 5));
		add.setService(SERVICE1);
		add.setVat("22%");
		iResOp.addResAddPayment(getH(HOTEL), RESNAME, add);

		List<ReservationPaymentDetail> aList = iResOp.getResAddPaymentList(getH(HOTEL), RESNAME);
		assertEquals(1, aList.size());
		add = aList.get(0);
		assertEquals("Beverage", add.getDescription());
		assertEquals(guest1, add.getGuestName());
		assertEqB(100.0, add.getPrice());
		assertEqB(150.0, add.getPriceList());
		assertEqB(200.0, add.getPriceTotal());
		assertEquals(2, add.getQuantity());
		eqD(2013, 4, 5, add.getServDate());
		assertEquals(SERVICE1, add.getService());
		assertEquals("22%", add.getVat());
	}

	@Test
	public void test2() {
		test1();
		HotelCustomer p = (HotelCustomer) hObjects.construct(getH(HOTEL), HotelObjects.CUSTOMER);
		String guest1 = iCustomers.addElem(getH(HOTEL), p).getName();

		for (int i = 0; i < 100; i++) {
			ReservationPaymentDetail add = new ReservationPaymentDetail();
			add.setDescription("Beverage");
			add.setGuestName(guest1);
			add.setPrice(new BigDecimal(100));
			add.setPriceList(new BigDecimal(150.00));
			add.setPriceTotal(new BigDecimal(200.0));
			add.setQuantity(2);
			add.setRoomName("P10");
			add.setServDate(toDate(2013, 4, 5));
			add.setVat("7%");
			iResOp.addResAddPayment(getH(HOTEL), RESNAME, add);
		}
		List<ReservationPaymentDetail> aList = iResOp.getResAddPaymentList(getH(HOTEL), RESNAME);
		assertEquals(101, aList.size());
	}

	@Test
	public void test3() {
		test1();
		HotelCustomer p = (HotelCustomer) hObjects.construct(getH(HOTEL), HotelObjects.CUSTOMER);
		String guest1 = iCustomers.addElem(getH(HOTEL), p).getName();

		ReservationPaymentDetail add = new ReservationPaymentDetail();
		add.setDescription("Beverage");
		add.setGuestName(guest1);
		add.setPrice(new BigDecimal(100));
		add.setPriceList(new BigDecimal(150));
		// decimal part
		add.setPriceTotal(new BigDecimal(200));
		add.setQuantity(2);
		add.setRoomName("P10");
		add.setServDate(toDate(2013, 4, 5));
		add.setVat("7%");
		iResOp.addResAddPayment(getH(HOTEL), RESNAME, add);

		List<ReservationPaymentDetail> aList = iResOp.getResAddPaymentList(getH(HOTEL), RESNAME);
		assertEquals(2, aList.size());

		DialogFormat d = findDialog("dialog2.xml");
		assertNotNull(d);
		DialogVariables v = new DialogVariables();
		runAction(v, "dialog3.xml", "addpayment");

		aList = iResOp.getResAddPaymentList(getH(HOTEL), RESNAME);
		assertEquals(3, aList.size());

	}

}
