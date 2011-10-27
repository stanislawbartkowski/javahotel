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
package com.javahotel.db.commands.bookstate;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.javahotel.common.command.BookingStateType;
import com.javahotel.common.dateutil.CalendarTable;
import com.javahotel.common.dateutil.CalendarTable.PeriodType;
import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.dateutil.DateUtil;
import com.javahotel.common.rescache.ReadResParam;
import com.javahotel.common.toobject.BookingStateP;
import com.javahotel.common.toobject.ResDayObjectStateP;
import com.javahotel.common.util.GetMaxUtil;
import com.javahotel.db.context.ICommandContext;
import com.javahotel.db.copy.CommonCopyBean;
import com.javahotel.db.hotelbase.jpa.BookElem;
import com.javahotel.db.hotelbase.jpa.Booking;
import com.javahotel.db.hotelbase.jpa.BookingState;
import com.javahotel.db.hotelbase.jpa.PaymentRow;
import com.javahotel.db.hotelbase.queries.GetQueries;
import com.javahotel.db.jtypes.ToLD;
import com.javahotel.dbres.messid.IMessId;
import com.javahotel.dbres.resources.GetLMess;

/**
 * Class for getting actual list of res objects
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
        return p.getBookelem().getBooking().getName();
    }

    private static String logPaymentRow(PaymentRow po) {
        BookElem e = po.getBookelem();
        Booking bo = e.getBooking();
        String bName = bo.getName();
        String dFrom1 = DateFormatUtil.toS(po.getRowFrom());
        String dTo1 = DateFormatUtil.toS(po.getRowTo());
        String format = GetLMess.getM(IMessId.RESPAYMENTROW);
        return MessageFormat.format(format, bName, dFrom1, dTo1);
    }

    /**
     * Extracts from the list of PaymentRow only element related to the object
     * resName and only the last version (identified by number)
     * 
     * @param resName
     *            Object
     * @param col
     *            Input list of reservation
     * @return Output list filtered out
     */
    private static List<PaymentRow> getResCol(final String resName,
            final List<PaymentRow> col) {

        List<PaymentRow> out = new ArrayList<PaymentRow>();
        for (PaymentRow p : col) {
            if (rName(p).equals(resName)) {
                out.add(p);
            }
        }
        return out;
    }

    /**
     * Get list of all reservation related to given object between two dates
     * 
     * @param iC
     *            ICommandContextx
     * @param dFrom
     *            Beginning fo the period
     * @param dTo
     *            End of the period
     * @param oName
     *            Object given
     * @return List of all reservation (PaymentRows)
     */
    private static List<PaymentRow> getForRes(final ICommandContext iC,
            final Date dFrom, final Date dTo, final String oName) {
        List<PaymentRow> c = GetQueries.getPaymentForReservation(iC, dFrom,
                dTo, oName);
        Set<String> resName = getResName(c);
        List<PaymentRow> out = new ArrayList<PaymentRow>();
        // log
        String msg = iC.logEvent(IMessId.RESGETDATA, oName,
                DateFormatUtil.toS(dFrom), DateFormatUtil.toS(dTo));
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

    /**
     * Get reservation from one day
     * 
     * @param iC
     *            ICommandContext
     * @param d
     *            Day
     * @param col
     *            List of reservations
     * @return Reservation or null (if no reservation)
     */
    private static PaymentRow getForDay(ICommandContext iC, final Date d,
            final List<PaymentRow> col) {
        List<PaymentRow> out = new ArrayList<PaymentRow>();
        for (PaymentRow p : col) {

            int c = DateUtil.comparePeriod(d, p.getRowFrom(), p.getRowTo());
            if (c != 0) {
                continue;
            }
            // check if reservation cancelled or changed to checked-in
            Booking b = p.getBookelem().getBooking();
            List<BookingState> cP = b.getState();
            BookingState sta = GetMaxUtil.getLast(cP);
            if (sta == null) {
                iC.logFatal(IMessId.NULLSTATERES, b.getName());
            }
            BookingStateType s = sta.getBState();
            if (!s.isBooked()) {
                String msg = iC.logEvent(IMessId.RESOMMITEDBADSTATE, sta
                        .getBState().toString());
                iC.getLog().getL().fine(msg);
                continue;
            }
            out.add(p);
        }
        if (out.isEmpty()) {
            return null;
        }
        if (out.size() > 1) {
            // log info message
            for (PaymentRow pp : out) {
                String dateFrom = DateFormatUtil.toS(pp.getRowFrom());
                String dateTo = DateFormatUtil.toS(pp.getRowTo());
                String mess = iC.logEvent(IMessId.MORETHENONERESERVATION, pp
                        .getBookelem().getResObject(), dateFrom, dateTo, pp
                        .getBookelem().getBooking().getName());
                iC.getLog().getL().info(mess);
            }

        }
        PaymentRow out1 = out.get(0); // TODO: get first ?
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
            String rName = p.getBookelem().getBooking().getName();
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
            List<BookingState> cP = p.getBookelem().getBooking().getState();
            BookingState sta = GetMaxUtil.getLast(cP);

            BookingStateP stap = new BookingStateP();
            CommonCopyBean.copyB(iC, sta, stap);
            o.setLState(stap);
            res.add(o);
        }
    }

    /**
     * Get list of reservation
     * 
     * @param iC
     *            ICommandContext
     * @param resParam
     *            Parameters: list of object and periods
     * @param omitResName
     *            What should be omitted
     * @return List of reservation
     */
    public static List<ResDayObjectStateP> getRestState(
            final ICommandContext iC, final ReadResParam resParam,
            final String omitResName) {

        List<ResDayObjectStateP> rOut = new ArrayList<ResDayObjectStateP>();

        List<Date> dLine = CalendarTable.listOfDates(
                resParam.getPe().getFrom(), resParam.getPe().getTo(),
                PeriodType.byDay);
        for (String oName : resParam.getResList()) {
            addBookRes(iC, resParam.getPe().getFrom(),
                    resParam.getPe().getTo(), dLine, rOut, oName, omitResName);
        }
        return rOut;
    }
}
