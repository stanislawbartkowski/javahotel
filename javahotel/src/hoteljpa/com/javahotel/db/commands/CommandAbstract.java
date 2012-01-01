/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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
package com.javahotel.db.commands;

import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gwtmodel.table.common.CUtil;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.util.GetMaxUtil;
import com.javahotel.db.command.CommandTemplate;
import com.javahotel.db.copy.CommonCopyBean;
import com.javahotel.db.copy.CopyHelper;
import com.javahotel.db.hotelbase.jpa.Booking;
import com.javahotel.db.hotelbase.jpa.Customer;
import com.javahotel.db.hotelbase.jpa.OfferPrice;
import com.javahotel.db.hotelbase.jpa.RHotel;
import com.javahotel.db.hotelbase.jpa.ServiceDictionary;
import com.javahotel.db.hotelbase.types.IHotelDictionary;
import com.javahotel.db.hotelbase.types.IPureDictionary;
import com.javahotel.db.jtypes.ToLD;
import com.javahotel.db.util.CommonHelper;
import com.javahotel.dbjpa.copybean.GetFieldHelper;
import com.javahotel.dbjpa.ejb3.JpaEntity;
import com.javahotel.dbjpa.xmlbean.ReadFromXML;
import com.javahotel.dbres.exceptions.HotelException;
import com.javahotel.dbres.log.HLog;
import com.javahotel.dbres.messid.IMessId;
import com.javahotel.dbres.resources.GetProp;
import com.javahotel.dbutil.container.ContainerInfo;
import com.javahotel.remoteinterfaces.HotelT;
import com.javahotel.remoteinterfaces.SessionT;
import com.javahotel.types.INumerable;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public abstract class CommandAbstract extends CommandTemplate {

    private final HotelT ho; // to pass parameter to prevRun

    @Override
    protected void prevRun() {
        setCo(iC.getSession(), ho); // use ho
    }

    private void setCo(final SessionT sessionId, final HotelT hotel) {
        JpaEntity jpa = HotelHelper.getJPA(sessionId, hotel);
        String hote = hotel.getName();
        iC.setJpa(jpa); // before setting ho
        iC.setHotel(hote);
    }

    CommandAbstract(final SessionT sessionId, boolean starttra,
            final HotelT hotel) {
        super(sessionId, false, !ContainerInfo.TransactionContainer(), starttra);
        this.ho = hotel;
    }

    // admin constructor
    CommandAbstract(final SessionT sessionId, final HotelT hotel) {
        super(sessionId, true, !ContainerInfo.TransactionContainer(), true);
        this.ho = hotel;
    }

    protected <T> List<T> readDefault(final Class<?> cla, final String tagProp,
            final String xmlProp) {
        String tagName = GetProp.getProp(tagProp);
        InputStream i = GetProp.getXMLFile(xmlProp);
        String rootTag = GetProp.getRootTagName(tagProp);
        List<T> col = (List<T>) ReadFromXML.readCol(i, cla, tagName, rootTag,
                HLog.getL());

        RHotel r = CommonHelper.getH(iC, new HotelT(iC.getHotel()));
        for (T o : col) {
            IHotelDictionary a = (IHotelDictionary) o;
            a.setHotel(r);
        }
        CommonHelper.addTraList(iC, col);
        return col;
    }

    private OfferPrice getOneOffer(final DictionaryP a,
            final String seasonname, final String name) {
        return CommonHelper.getOneOffer(iC, seasonname, name);
    }

    protected class GetObjectRes {

        private Class<?> cla;
        private IPureDictionary o;
        private boolean created = false;

        public void refresh() {
            if (created) {
                return;
            }
            o = iC.getJpa().getRecord(cla, o.getId());
        }

        public IPureDictionary getO() {
            return o;
        }
    }

    protected GetObjectRes getObject(final DictType d, final DictionaryP a) {
        GetObjectRes res = new GetObjectRes();
        String na = a.getName();
        res.cla = ObjectFactory.getC(d);
        if (res.cla == Customer.class) {
            CustomerP cP = (CustomerP) a;
            if (cP.getId() != null) {
                res.o = iC.getJpa().getRecord(Customer.class, cP.getId());
            }
        } else if (res.cla == OfferPrice.class) {
            OfferPriceP oP = (OfferPriceP) a;
            res.o = getOneOffer(a, oP.getSeason(), na);
        }
        if ((res.o == null) && !CUtil.EmptyS(na)) {
            res.o = CommonHelper.getA(iC, res.cla, na);
            res.created = true;
        }
        if (res.o == null) {
            res.o = ObjectFactory.constructADict(d);
            res.created = true;
        }
        return res;
    }

    protected Booking getBook(final String resName) {
        DictionaryP dP = new DictionaryP();
        dP.setName(resName);
        dP.setHotel(iC.getHotel());
        GetObjectRes res = getObject(DictType.BookingList, dP);
        // do not return null
        if (res.getO().getName() == null) {
            iC.logFatal(IMessId.CANNOTFINDBOOKING, resName);
        }
        Booking b = (Booking) res.getO();
        return b;
    }

    protected void getRet(final ReturnPersist re, final IPureDictionary o) {
        if (o != null) {
            re.setId(ToLD.toLId(o.getId()));
            re.setIdName(o.getName());
        }
    }

    /**
     * Create set of booking services and keep it in the cache
     */
    protected void createSetOfBookingServices() {
        // read all services and create a set of services related to booking
        List<ServiceDictionary> colx = iC.getJpa().getAllList(
                ServiceDictionary.class);
        Set<String> booking = new HashSet<String>();
        for (ServiceDictionary c : colx) {
            if (c.getServType().isBooking()) {
                booking.add(c.getName());
            }
        }
        // store result in the cache
        iC.getC().setBookingServices(booking);
    }

    protected void setCol(final Object p, final List<? extends INumerable> col,
            final INumerable sou, final Class<?> cla, final String name,
            final Class<?> mecl) {

        INumerable dest;
        try {
            dest = (INumerable) cla.newInstance();
        } catch (InstantiationException ex) {
            throw new HotelException(ex);
        } catch (IllegalAccessException ex) {
            throw new HotelException(ex);
        }
        CommonCopyBean.copyB(iC, sou, dest);
        // dest.setLp(lp);
        GetMaxUtil.setNextLp(col, dest);
        GetFieldHelper.setterVal(dest, p, name, mecl, iC.getLog());
        CopyHelper.checkPersonDateOp(iC, dest);
        ((List) col).add(dest);
    }

}
