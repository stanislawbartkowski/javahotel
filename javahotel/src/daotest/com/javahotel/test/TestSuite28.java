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
package com.javahotel.test;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.BookingP;

public class TestSuite28 extends TestHelper {


	@Test
	public void Test1() {
		loginuser();
		BookingP bok = createB();
		bok.setValidationAmount(new BigDecimal(100));
		bok.setValidationDate(D("2008/03/07"));
		bok = getpersistName(DictType.BookingList, bok, "BOK0001");
		
        bok.setValidationAmount(new BigDecimal(200));
        bok.setValidationDate(D("2008/03/08"));
		bok = getpersistName(DictType.BookingList, bok, "BOK0002");

		bok.setValidationAmount(new BigDecimal(300));
		bok.setValidationDate(D("2008/03/09"));
		bok = getpersistName(DictType.BookingList, bok, "BOK0003");
		
		bok.setValidationAmount(new BigDecimal(400));
		bok.setValidationDate(D("2008/03/10"));
		bok = getpersistName(DictType.BookingList, bok, "BOK0004");

		
		CommandParam par = new CommandParam();
		par.setHotel(HOTEL1);
		par.setDateFrom(DateFormatUtil.toD("2008/03/07"));
		par.setDateTo(DateFormatUtil.toD("2008/03/09"));
		List<AbstractTo> res = list.getList(se, RType.DownPayments, par);
		assertEquals(3, res.size());
		
		par = new CommandParam();
		par.setHotel(HOTEL1);
		par.setDateFrom(DateFormatUtil.toD("2008/03/08"));
		res = list.getList(se, RType.DownPayments, par);
		assertEquals(3, res.size());

		par = new CommandParam();
		par.setHotel(HOTEL1);
		par.setDateTo(DateFormatUtil.toD("2008/03/08"));
		res = list.getList(se, RType.DownPayments, par);
		assertEquals(2, res.size());
	}

}
