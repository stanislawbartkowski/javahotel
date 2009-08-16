package com.javahotel.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.AdvancePaymentP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.DownPaymentP;
import com.javahotel.types.LId;

public class TestSuite28 extends TestHelper {


	@Test
	public void Test1() {
		loginuser();
		BookingP bok = createB();
		Collection<AdvancePaymentP> col = new ArrayList<AdvancePaymentP>();
		AdvancePaymentP va = new AdvancePaymentP();
		va.setLp(new Integer(1));
		va.setAmount(new BigDecimal(100));
		va.setValidationDate(D("2008/03/07"));
		col.add(va);
		setVal(bok, col);
		bok = getpersistName(DictType.BookingList, bok, "BOK0001");
		
		col = new ArrayList<AdvancePaymentP>();
		va = new AdvancePaymentP();
		va.setLp(new Integer(2));
		va.setAmount(new BigDecimal(200));
		va.setValidationDate(D("2008/03/08"));
		col.add(va);
		setVal(bok, col);
		bok = getpersistName(DictType.BookingList, bok, "BOK0002");

		col = new ArrayList<AdvancePaymentP>();
		va = new AdvancePaymentP();
		va.setLp(new Integer(3));
		va.setAmount(new BigDecimal(300));
		va.setValidationDate(D("2008/03/09"));
		col.add(va);
		setVal(bok, col);
		bok = getpersistName(DictType.BookingList, bok, "BOK0003");
		
		col = new ArrayList<AdvancePaymentP>();
		va = new AdvancePaymentP();
		va.setLp(new Integer(4));
		va.setAmount(new BigDecimal(400));
		va.setValidationDate(D("2008/03/10"));
		col.add(va);
		setVal(bok, col);
		bok = getpersistName(DictType.BookingList, bok, "BOK0004");

		
		CommandParam par = new CommandParam();
		par.setHotel(HOTEL1);
		par.setDateFrom(DateFormatUtil.toD("2008/03/07"));
		par.setDateTo(DateFormatUtil.toD("2008/03/09"));
		Collection<AbstractTo> res = list.getList(se, RType.DownPayments, par);
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
