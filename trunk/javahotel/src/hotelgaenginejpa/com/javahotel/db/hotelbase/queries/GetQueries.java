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
package com.javahotel.db.hotelbase.queries;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import com.gwtmodel.table.common.dateutil.DateUtil;
import com.javahotel.common.command.BookingEnumTypes;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.util.GetMaxUtil;
import com.javahotel.db.context.ICommandContext;
import com.javahotel.db.hotelbase.jpa.BookElem;
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
            final Class<?> cla, final DictType d, boolean all,
            CommandParam param) {
        List<IId> col;
        switch (d) {
        case PriceListDict:
            List<OfferPrice> colx = iC.getJpa().getAllList(OfferPrice.class);
            col = new ArrayList<IId>();
            for (OfferPrice o : colx) {
                if (o.getSeason() == null) {
                    String s = MessageFormat.format(
                            "{0} OfferPrice object with null season field",
                            o.getName());
                    iC.getLog().getL().log(Level.SEVERE, s);
                    if (all) {
                        col.add(o);
                    }
                    continue;
                }
                OfferSeason of = (OfferSeason) getD(iC, OfferSeason.class,
                        iC.getRHotel(), o.getSeason());
                if (of != null) {
                    col.add(o);
                }
            }
            break;
        case InvoiceList:
            if (param == null || param.getBookingId() == null) {
                col = iC.getJpa().getListQuery(cla, "hotelId",
                        iC.getRHotel().getId().getL());
                break;
            }
            col = iC.getJpa().getListQuery(cla, "hotelId",
                    iC.getRHotel().getId().getL(), "bookingId",
                    param.getBookingId().getId());
            break;
        default:
            col = iC.getJpa().getListQuery(cla, "hotelId",
                    iC.getRHotel().getId().getL());
            break;

        }
        return col;
    }

    public static List<IId> getDList(final ICommandContext iC,
            final Class<?> cla, final DictType d, CommandParam param) {
        return getDList(iC, cla, d, false, param);
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
        OfferSeason os = (OfferSeason) getD(iC, OfferSeason.class,
                iC.getRHotel(), seasonName);
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

    /**
     * Get the list of PaymentRow (reservation) related to the given object
     * 
     * @param iC
     *            ICommandContext
     * @param dFrom
     *            The beginning of the period
     * @param dTo
     *            The end of the period
     * @param oName
     *            Object
     * @return List of reservation
     */
    public static List<PaymentRow> getPaymentForReservation(
            final ICommandContext iC, final Date dFrom, final Date dTo,
            final String oName) {
        List<PaymentRow> cc = new ArrayList<PaymentRow>();

        List<Booking> cB = iC.getJpa().getListQuery(Booking.class, "hotelId",
                iC.getRHotel().getId().getL());
        for (Booking b : cB) {
            List<BookElem> cE = b.getBooklist();
            for (BookElem be : cE) {
                // only oName object
                if (!be.getResObject().equals(oName)) {
                    continue;
                }
                // only BookElem related to reservation
                // ignore all other BookElems
                String service = be.getService();
                if (!iC.getC().getBookingServices().contains(service)) {
                    continue;
                }
                List<PaymentRow> cR = be.getPaymentrows();
                for (PaymentRow p : cR) {

                    List<BookingState> cP = p.getBookelem().getBooking()
                            .getState();
                    @SuppressWarnings("unused")
                    BookingState sta = GetMaxUtil.getLast(cP);
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

        return cc;
    }

    public static List<Booking> getValidationForHotel(final ICommandContext iC) {
        List<Booking> col = iC.getJpa().getAllList(Booking.class);
        List<Booking> out = new ArrayList<Booking>();
        String hot = iC.getHotel();
        for (Booking bo : col) {
            String ho = bo.getHotel().getName();
            if (!ho.equals(hot)) {
                continue;
            }
            if (bo.getBookingType() == BookingEnumTypes.Stay) {
                continue;
            }
            List<BookingState> cP = bo.getState();
            BookingState sta = GetMaxUtil.getLast(cP);
            if (!sta.getBState().isBooked()) {
                continue;
            }
            if (bo.getValidationAmount() == null) {
                continue;
            }
            out.add(bo);
        }
        return out;
    }

    /**
     * Gets the list of ParamRegistries entry like keyLike
     * 
     * @param iC
     *            ICommandContext
     * @param keylike
     *            Like pattern
     * @return List of ParamRegistry
     */
    public static List<ParamRegistry> getRegistryEntries(
            final ICommandContext iC, final String keylike) {

        // check if the list of all entries already stored in the cache
        List<ParamRegistry> c = iC.getC().getCacheList();
        if (c == null) {
            // if not then read it from the storage and save in the cache
            c = iC.getJpa().getListQuery(ParamRegistry.class, "hotelId",
                    iC.getRHotel().getId().getL());
            iC.getC().setCacheList(c);
        }
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
