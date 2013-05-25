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
package com.gwthotel.hotel.service.gae.crud;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.googlecode.objectify.LoadResult;
import com.googlecode.objectify.VoidWork;
import com.gwthotel.admin.gae.DictUtil;
import com.gwthotel.admin.gae.entities.EHotel;
import com.gwthotel.hotel.IHotelProp;
import com.gwthotel.hotel.service.gae.entities.EHotelDict;
import com.gwthotel.mess.IHError;
import com.gwthotel.mess.IHMess;
import com.gwthotel.shared.IHotelConsts;
import com.gwthotel.shared.PropDescription;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.shared.JythonUIFatal;

abstract public class CrudGaeAbstract<T extends PropDescription, E extends EHotelDict>
        implements IHotelProp<T> {

    private final IGetLogMess lMess;
    private final Class<E> cl;

    protected CrudGaeAbstract(IGetLogMess lMess, Class<E> cl) {
        this.lMess = lMess;
        this.cl = cl;
    }

    private static final Logger log = Logger.getLogger(CrudGaeAbstract.class
            .getName());

    private void setFailure(String mess) {
        log.severe(mess);
        throw new JythonUIFatal(mess);
    }

    private EHotel findEHotel(String hotel) {
        EHotel ho = DictUtil.findHotel(hotel);
        if (ho == null) {
            String mess = lMess.getMess(IHError.HERROR005,
                    IHMess.HOTELNAMENOTFOUND, hotel);
            setFailure(mess);
        }
        return ho;
    }

    protected abstract T constructProp(E e);

    protected abstract E constructE();

    protected abstract void toE(E e, T t);

    private T toProp(E e, String hotel) {
        T dest = constructProp(e);
        DictUtil.toProp(dest, e);
        dest.setAttr(IHotelConsts.HOTELPROP, hotel);
        return dest;
    }

    private void toEDict(E dest, T sou, String hotel) {
        DictUtil.toEDict(dest, sou);
        if (!dest.isHotelSet())
            dest.setHotel(findEHotel(hotel));
        toE(dest, sou);
    }

    @Override
    public List<T> getList(String hotel) {
        EHotel ho = findEHotel(hotel);
        List<E> li = ofy().load().type(cl).ancestor(ho).list();
        List<T> outList = new ArrayList<T>();
        for (E e : li) {
            T p = toProp(e, hotel);
            outList.add(p);
        }
        return outList;
    }

    @Override
    public void addElem(String hotel, T elem) {
        final E e = constructE();
        toEDict(e, elem, hotel);
        ofy().transact(new VoidWork() {
            public void vrun() {
                ofy().save().entity(e).now();
            }
        });

    }

    private E findService(String hotel, T serv) {
        EHotel ho = findEHotel(hotel);
        LoadResult<E> p = ofy().load().type(cl).ancestor(ho)
                .filter("name == ", serv.getName()).first();
        if (p == null) {
            return null;
        }
        return p.now();
    }

    @Override
    public void changeElem(String hotel, T elem) {
        final E serv = findService(hotel, elem);
        if (serv == null) // TODO: more verbose log
            return;
        toEDict(serv, elem, hotel);
        ofy().transact(new VoidWork() {
            public void vrun() {
                ofy().save().entity(serv).now();
            }
        });
    }

    @Override
    public void deleteElem(String hotel, T elem) {
        final E serv = findService(hotel, elem);
        if (serv == null) // TODO: more verbose log
            return;
        ofy().transact(new VoidWork() {
            public void vrun() {
                ofy().delete().entity(serv).now();
            }
        });
    }

    @Override
    public void deleteAll(final String hotel) {
        final EHotel ho = findEHotel(hotel);
        ofy().transact(new VoidWork() {
            public void vrun() {
                final List<E> li = ofy().load().type(cl).ancestor(ho).list();
                ofy().delete().entities(li);
            }
        });

    }

}
