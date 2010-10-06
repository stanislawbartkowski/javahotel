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
package com.javahotel.db.commands.bookstate;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.javahotel.common.dateutil.CalendarTable;
import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.dateutil.DateUtil;
import com.javahotel.common.dateutil.CalendarTable.PeriodType;
import com.javahotel.common.rescache.ReadResParam;
import com.javahotel.common.toobject.BookingStateP;
import com.javahotel.common.toobject.ResDayObjectStateP;
import com.javahotel.common.util.GetMaxUtil;
import com.javahotel.db.context.ICommandContext;
import com.javahotel.db.copy.CommonCopyBean;
import com.javahotel.db.hotelbase.jpa.BookElem;
import com.javahotel.db.hotelbase.jpa.BookRecord;
import com.javahotel.db.hotelbase.jpa.Booking;
import com.javahotel.db.hotelbase.jpa.BookingState;
import com.javahotel.db.hotelbase.jpa.PaymentRow;
import com.javahotel.db.hotelbase.queries.GetQueries;
import com.javahotel.db.jtypes.ToLD;
import com.javahotel.dbres.messid.IMessId;
import com.javahotel.dbres.resources.GetLMess;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class BookState {

	private BookState() {
	}

	private static Set<String> getResName(final List<PaymentRow> col) {
		Set<String> se = new HashSet<String>();
		for (PaymentRow p : col) {
			se.add(rName(p));
		}
		return se;
	}

	private static String rName(PaymentRow p) {
		return p.getBookelem().getBookrecord().getBooking().getName();
	}

	private static String logPaymentRow(PaymentRow po) {
		BookElem e = po.getBookelem();
		BookRecord re = e.getBookrecord();
		Booking bo = re.getBooking();
		String bName = bo.getName();
		String dFrom1 = DateFormatUtil.toS(po.getRowFrom());
		String dTo1 = DateFormatUtil.toS(po.getRowTo());
		String format = GetLMess.getM(IMessId.RESPAYMENTROW);
		return MessageFormat.format(format, bName, dFrom1, dTo1);
	}

	private static List<PaymentRow> getResCol(final String resName,
			final List<PaymentRow> col) {

		List<PaymentRow> out = new ArrayList<PaymentRow>();
		for (PaymentRow p : col) {
			if (rName(p).equals(resName)) {
				out.add(p);
			}
		}
		GetMaxUtil.IGetLp i = new GetMaxUtil.IGetLp() {

			public Integer getLp(Object o) {
				PaymentRow p = (PaymentRow) o;
				return p.getBookelem().getBookrecord().getLp();
			}
		};
		List<PaymentRow> out1 = GetMaxUtil.getListLp(out, i);
		return out1;
	}

	private static List<PaymentRow> getForRes(final ICommandContext iC,
			final Date dFrom, final Date dTo, final String oName) {
		List<PaymentRow> c = GetQueries.getPaymentForReservation(iC,
				dFrom, dTo, oName);
		Set<String> resName = getResName(c);
		List<PaymentRow> out = new ArrayList<PaymentRow>();
		// log
		String msg = iC.logEvent(IMessId.RESGETDATA, oName, DateFormatUtil
				.toS(dFrom), DateFormatUtil.toS(dTo));
		iC.getLog().getL().fine(msg);

		for (String rName : resName) {
			List<PaymentRow> o = getResCol(rName, c);
			out.addAll(o);
			// log
			for (PaymentRow po : o) {
				msg = iC.logEvent(IMessId.RESSTATERES, logPaymentRow(po));
				iC.getLog().getL().fine(msg);
			}
		}
		return out;
	}

	private static PaymentRow getForDay(ICommandContext iC, final Date d,
			final List<PaymentRow> col) {
		List<PaymentRow> out = new ArrayList<PaymentRow>();
		for (PaymentRow p : col) {
			
			int c = DateUtil.comparePeriod(d, p.getRowFrom(), p.getRowTo());
			if (c != 0) {
				continue;
			}
			out.add(p);
		}
		GetMaxUtil.IGetLp i = new GetMaxUtil.IGetLp() {

			public Integer getLp(Object o) {
				PaymentRow p = (PaymentRow) o;
				// IMPORTANT: getId, not getLp
				// Important: cannot assume that Id contains increasing values
				return p.getBookelem().getBookrecord().getSeqId();
			}
		};
		
		PaymentRow out1 = GetMaxUtil.getOneLp(out, i);
		return out1;

	}

	private static void addBookRes(final ICommandContext iC, final Date dFrom,
			final Date dTo, final List<Date> li,
			final List<ResDayObjectStateP> res, final String oName,
			final String omitResName) {
		List<PaymentRow> roc = getForRes(iC, dFrom, dTo, oName);
		for (final Date d : li) {
			PaymentRow p = getForDay(iC, d, roc);
			String logD = DateFormatUtil.toS(d);
			String msg;
			if (p == null) {
				msg = iC.logEvent(IMessId.RESOMITDATE, logD);
				iC.getLog().getL().fine(msg);
				continue;
			}
			msg = iC.logEvent(IMessId.RESADDBOOK, logD, logPaymentRow(p));
			iC.getLog().getL().fine(msg);
			String rName = p.getBookelem().getBookrecord().getBooking()
					.getName();
			if ((omitResName != null) && rName.equals(omitResName)) {
				msg = iC.logEvent(IMessId.RESOMITNOTEQUAL, rName, omitResName);
				iC.getLog().getL().fine(msg);
				continue;
			}
			ResDayObjectStateP o = new ResDayObjectStateP();
			o.setResObject(oName);
			o.setD(d);
			o.setPaymentRowId(ToLD.toLId(p.getId()));
			// // TODO:
			o.setLp(new Integer(0));
			o.setBookName(rName);
			// BookingStateP
			List<BookingState> cP = p.getBookelem().getBookrecord()
					.getBooking().getState();
			BookingState sta = GetMaxUtil.getLast(cP);
			if (sta == null) {
				iC.logFatal(IMessId.NULLSTATERES, rName);
			}
			switch (sta.getBState()) {
			case Confirmed:
			case WaitingForConfirmation:
			case Stay:
				break;
			default:
				msg = iC.logEvent(IMessId.RESOMMITEDBADSTATE, sta.getBState()
						.toString());
				iC.getLog().getL().fine(msg);
				continue;
			}

			BookingStateP stap = new BookingStateP();
			CommonCopyBean.copyB(iC, sta, stap);
			o.setLState(stap);
			res.add(o);
		}
	}

	public static List<ResDayObjectStateP> getRestState(
			final ICommandContext iC, final ReadResParam resParam,
			final String omitResName) {

		List<ResDayObjectStateP> rOut = new ArrayList<ResDayObjectStateP>();

		List<Date> dLine = CalendarTable.listOfDates(resParam.getPe()
				.getFrom(), resParam.getPe().getTo(), PeriodType.byDay);
		for (String oName : resParam.getResList()) {
			addBookRes(iC, resParam.getPe().getFrom(),
					resParam.getPe().getTo(), dLine, rOut, oName, omitResName);
		}
		return rOut;
	}
}
