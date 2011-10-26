package com.javahotel.db.copy;

import java.util.List;

import com.javahotel.common.command.BookingEnumTypes;
import com.javahotel.common.toobject.AddPaymentP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.OfferServicePriceP;
import com.javahotel.common.toobject.OfferSpecialPriceP;
import com.javahotel.common.toobject.ResObjectP;
import com.javahotel.common.toobject.RoomStandardP;
import com.javahotel.common.toobject.ServiceDictionaryP;
import com.javahotel.common.toobject.VatDictionaryP;
import com.javahotel.common.util.StringU;
import com.javahotel.db.context.ICommandContext;
import com.javahotel.db.hotelbase.jpa.OfferSeasonPeriod;
import com.javahotel.db.hotelbase.jpa.OfferServicePrice;
import com.javahotel.db.hotelbase.jpa.RoomFacilities;
import com.javahotel.db.hotelbase.jpa.RoomStandard;
import com.javahotel.db.hotelbase.jpa.ServiceDictionary;
import com.javahotel.db.hotelbase.jpa.VatDictionary;
import com.javahotel.db.hotelbase.queries.GetQueries;
import com.javahotel.db.jtypes.IId;
import com.javahotel.db.util.CommonHelper;
import com.javahotel.dbres.messid.IMessId;
import com.javahotel.dbres.resources.IMess;

public class BeanPrepareKeys {

    private BeanPrepareKeys() {
    }

    private static void checkD(final ICommandContext iC, Class<?> cla,
            String oName) {
        IId rs = CommonHelper.getA(iC, cla, oName);
        if (rs != null) {
            iC.getC().put(cla, oName, rs, false);
        }
    }

    private static void checkD(final ICommandContext iC, Class<?> cla,
            DictionaryP o) {
        checkD(iC, cla, o.getName());
    }

    private static void checkCollection(final ICommandContext iC, Class<?> cla,
            List<? extends DictionaryP> co) {
        for (DictionaryP o : co) {
            checkD(iC, cla, o);
        }
    }

    private static void prepare(final ICommandContext iC, ResObjectP o) {
        DictionaryP dict = o.getRStandard();
        checkD(iC, RoomStandard.class, dict);
        checkCollection(iC, RoomFacilities.class, o.getFacilities());
    }

    private static void prepare(final ICommandContext iC, OfferPriceP o) {
        List<OfferServicePriceP> col = o.getServiceprice();
        final String seasonName = o.getSeason();
        if (col != null) {
            for (OfferServicePriceP of : col) {
                checkD(iC, ServiceDictionary.class, of.getService());
                List<OfferSpecialPriceP> sCol = of.getSpecialprice();
                if (sCol != null) {
                    for (OfferSpecialPriceP sp : sCol) {
                        Long pId = sp.getSpecialperiod();
                        OfferSeasonPeriod se = GetQueries.getSeasonPeriod(iC,
                                seasonName, pId);
                        if (se != null) {
                            iC.getC().put(OfferServicePrice.class, seasonName,
                                    OfferSeasonPeriod.class, pId.toString(),
                                    se, false);
                        }
                    }
                }
            }
        }
    }

    public static void prepareA(ICommandContext iC, List<AddPaymentP> col) {
        if (col == null) {
            return;
        }
        for (AddPaymentP aa : col) {
            checkD(iC, ServiceDictionary.class, aa.getRService());
        }
    }

    private static void prepare(final ICommandContext iC, RoomStandardP o) {
        List<? extends DictionaryP> col = o.getServices();
        for (DictionaryP d : col) {
            ServiceDictionaryP dp = (ServiceDictionaryP) d;
            checkD(iC, ServiceDictionary.class, dp);
            VatDictionaryP va = dp.getVat();
            checkD(iC, VatDictionary.class, va);
        }
    }

    private static void prepare(final ICommandContext iC, BookingP o) {
        if (StringU.isEmpty(o.getState())) {
            iC.logFatal(IMessId.NULLSTATERESBOOKING);
        }
        String pId = IMess.BOOKINGPATTID;
        String patt = IMess.BOOKINGPATT;
        if (o.getBookingType() == BookingEnumTypes.Stay) {
            pId = IMess.STAYPATTID;
            patt = IMess.STAYPATT;
        }
        CopyHelper.setPattName(iC, o, pId, patt);

        // if (o.getBookrecords() != null) {
        // for (BookRecordP br : o.getBookrecords()) {
        // if (br.getSeqId() == null) {
        // int no = GetNextSym.NextIntValue(iC, IMess.BOOKRECORDSEQID);
        // br.setSeqId(new Integer(no));
        // }
        // }
        // }

        // List<BillP> col = o.getBill();
        // if (col != null) {
        // for (BillP p : col) {
        // prepareB(iC, p);
        // prepareA(iC, p.getAddpayments());
        // }
        // }

        prepareA(iC, o.getAddpayments());

    }

    // public static void prepareB(final ICommandContext iC, BillP sou) {
    // String pId = IMess.BILLPATTID;
    // String patt = IMess.BILLPATT;
    // CopyHelper.setPattName(iC, sou, pId, patt);
    // }

    private static void prepare(final ICommandContext iC, ServiceDictionaryP o) {
        VatDictionaryP va = o.getVat();
        checkD(iC, VatDictionary.class, va);
    }

    static public void prepareKeys(final ICommandContext iC, DictionaryP o) {

        if (o instanceof ResObjectP) {
            prepare(iC, (ResObjectP) o);
            return;
        }
        if (o instanceof OfferPriceP) {
            prepare(iC, (OfferPriceP) o);
            return;
        }

        if (o instanceof BookingP) {
            prepare(iC, (BookingP) o);
            return;
        }

        if (o instanceof ServiceDictionaryP) {
            prepare(iC, (ServiceDictionaryP) o);
            return;
        }

        if (o instanceof RoomStandardP) {
            prepare(iC, (RoomStandardP) o);
            return;
        }

    }

}
