package com.javahotel.db.hotelbase.queries;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import com.javahotel.common.command.DictType;
import com.javahotel.common.dateutil.DateUtil;
import com.javahotel.common.util.GetMaxUtil;
import com.javahotel.db.context.ICommandContext;
import com.javahotel.db.hotelbase.jpa.AdvancePayment;
import com.javahotel.db.hotelbase.jpa.Bill;
import com.javahotel.db.hotelbase.jpa.BookElem;
import com.javahotel.db.hotelbase.jpa.BookRecord;
import com.javahotel.db.hotelbase.jpa.Booking;
import com.javahotel.db.hotelbase.jpa.BookingState;
import com.javahotel.db.hotelbase.jpa.OfferPrice;
import com.javahotel.db.hotelbase.jpa.OfferSeason;
import com.javahotel.db.hotelbase.jpa.OfferSeasonPeriod;
import com.javahotel.db.hotelbase.jpa.ParamRegistry;
import com.javahotel.db.hotelbase.jpa.PaymentRow;
import com.javahotel.db.hotelbase.jpa.RHotel;
import com.javahotel.db.hotelbase.types.IHotelDictionary;
import com.javahotel.db.jtypes.IId;

public class GetQueries {

    private GetQueries() {

    }

    public static IHotelDictionary getD(final ICommandContext iC,
            final Class<?> cla, final RHotel hotel, final String name) {
        IHotelDictionary o = iC.getJpa().getOneWhereRecord(cla, "name", name,
                "hotelId", hotel.getId().getL());
        return o;
    }

    public static List<IId> getDList(final ICommandContext iC,
            final Class<?> cla, final DictType d, boolean all) {
        List<IId> col;
        switch (d) {
        case PriceListDict:
            List<OfferPrice> colx = iC.getJpa().getAllList(OfferPrice.class);
            col = new ArrayList<IId>();
            for (OfferPrice o : colx) {
                if (o.getSeason() == null) {
                    String s = MessageFormat.format(
                            "{0} OfferPrice object with null season field", o
                                    .getName());
                    iC.getLog().getL().log(Level.SEVERE, s);
                    if (all) {
                        col.add(o);
                    }
                    continue;
                }
                OfferSeason of = (OfferSeason) getD(iC, OfferSeason.class, iC
                        .getRHotel(), o.getSeason());
                if (of != null) {
                    col.add(o);
                }
            }
            break;
        default:
            col = iC.getJpa().getListQuery(cla, "hotelId",
                    iC.getRHotel().getId().getL());
            break;

        }
        return col;
    }

    public static List<IId> getDList(final ICommandContext iC,
            final Class<?> cla, final DictType d) {
        return getDList(iC, cla, d, false);
    }

    public static OfferPrice getOnePriceList(final ICommandContext iC,
            String seasonName, String oName) {
        // first search season
        OfferPrice op = iC.getJpa().getOneWhereRecord(OfferPrice.class,
                "season", seasonName, "name", oName, "hotelId",
                iC.getRHotel().getId().getL());
        return op;
    }

    public static int getNumber(final ICommandContext iC, final Class<?> cla) {
        List<?> col = iC.getJpa().getAllList(cla);
        return col.size();
    }

    public static OfferSeasonPeriod getSeasonPeriod(final ICommandContext iC,
            String seasonName, Long pId) {
        OfferSeason os = (OfferSeason) getD(iC, OfferSeason.class, iC
                .getRHotel(), seasonName);
        if (os == null) {
            return null;
        }
        for (OfferSeasonPeriod op : os.getPeriods()) {
            if (op.getPId().equals(pId)) {
                return op;
            }
        }
        return null;
    }

    public static List<PaymentRow> getPaymentForReservation(
            final ICommandContext iC, final Date dFrom, final Date dTo,
            final String oName) {
        List<PaymentRow> cc = new ArrayList<PaymentRow>();

        List<Booking> cB = iC.getJpa().getListQuery(Booking.class, "hotelId",
                iC.getRHotel().getId().getL());
        for (Booking b : cB) {
            List<BookRecord> cBo = b.getBookrecords();
            for (BookRecord bo : cBo) {
                List<BookElem> cE = bo.getBooklist();
                for (BookElem be : cE) {
                    if (!be.getResObject().equals(oName)) {
                        continue;
                    }
                    List<PaymentRow> cR = be.getPaymentrows();
                    for (PaymentRow p : cR) {

                        List<BookingState> cP = p.getBookelem().getBookrecord()
                                .getBooking().getState();
                        BookingState sta = GetMaxUtil.getLast(cP);
                        String ma = p.getBookelem().getBookrecord()
                                .getBooking().getName();
                        Date ddTo = p.getRowTo();
                        int co;
                        if (ddTo != null) {
                            co = DateUtil.compareDate(ddTo, dFrom);
                            if (co == -1) {
                                continue;
                            } // ddTo < dFrom
                        }
                        Date ddFrom = p.getRowFrom();
                        if (ddFrom != null) {
                            co = DateUtil.compareDate(ddFrom, dTo);
                            if (co == 1) {
                                continue;
                            } // ddFrom < dTo
                        }
                        cc.add(p);
                    }
                }
            }
        }

        return cc;
    }

    public static List<AdvancePayment> getValidationForHotel(
            final ICommandContext iC) {
        List<AdvancePayment> col = iC.getJpa().getAllList(AdvancePayment.class);
        List<AdvancePayment> out = new ArrayList<AdvancePayment>();
        String hot = iC.getHotel();
        for (AdvancePayment a : col) {
            // for some reason should - othwerwise is not read
            Bill bi = a.getBill();
            Bill bi1 = iC.getJpa().getRecord(Bill.class, bi.getId());
            Booking bo = bi1.getBooking();
            Booking bo1 = iC.getJpa().getRecord(Booking.class, bo.getId());
            // ------------------
            String ho = a.getBill().getBooking().getHotel().getName();
            if (!ho.equals(hot)) {
                continue;
            }
            out.add(a);
        }
        return out;
    }

    public static List<ParamRegistry> getRegistryEntries(
            final ICommandContext iC, final String keylike) {
        List<ParamRegistry> c = iC.getJpa().getListQuery(ParamRegistry.class,
                "hotelId", iC.getRHotel().getId().getL());
        List<ParamRegistry> out = new ArrayList<ParamRegistry>();
        for (ParamRegistry p : c) {
            String na = p.getName();
            if (na.indexOf(keylike) == 0) {
                out.add(p);
            }
        }
        return out;
    }

    public static int getDictNumber(final ICommandContext iC, final Class<?> cla) {
        List<IId> col = iC.getJpa().getListQuery(cla, "hotelId",
                iC.getRHotel().getId().getL());
        return col.size();
    }

}
