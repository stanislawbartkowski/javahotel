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

import com.googlecode.objectify.LoadResult;
import com.googlecode.objectify.VoidWork;
import com.gwthotel.admin.gae.DictUtil;
import com.gwthotel.admin.gae.entities.EHotel;
import com.gwthotel.hotel.IHotelProp;
import com.gwthotel.hotel.service.gae.entities.EHotelDict;
import com.gwthotel.hotel.service.gae.entities.EHotelPriceElem;
import com.gwthotel.hotel.service.gae.entities.EHotelRoomServices;
import com.gwthotel.shared.IHotelConsts;
import com.gwthotel.shared.PropDescription;
import com.jythonui.server.getmess.IGetLogMess;

abstract public class CrudGaeAbstract<T extends PropDescription, E extends EHotelDict>
        implements IHotelProp<T> {

    protected final IGetLogMess lMess;
    private final Class<E> cl;

    protected CrudGaeAbstract(IGetLogMess lMess, Class<E> cl) {
        this.lMess = lMess;
        this.cl = cl;
    }

    private EHotel findEHotel(String hotel) {
        return DictUtil.findEHotel(lMess, hotel);
    }

    protected abstract T constructProp(E e);

    protected abstract E constructE();

    protected abstract void toE(E e, T t);

    protected class DeleteItem {
        public List<EHotelPriceElem> pList = null;
        public List<EHotelRoomServices> sList = null;

        void removeItems() {
            if (pList != null) ofy().delete().entities(pList);
            if (sList != null) ofy().delete().entities(sList);
        }
        
        public void readAllRoomServices(EHotel ho) {
            sList = ofy().load().type(EHotelRoomServices.class).ancestor(ho).list();
        }
        
        public void readAllPriceElems(EHotel ho) {
            pList = ofy().load().type(EHotelPriceElem.class).ancestor(ho).list();
        }
    }

    protected abstract void beforeDelete(DeleteItem i,EHotel ho, E elem);

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
        final EHotel ho = findEHotel(hotel);
        final E serv = findService(hotel, elem);
        if (serv == null) // TODO: more verbose log
            return;
        final DeleteItem i = new DeleteItem();
        beforeDelete(i,ho,serv);
        ofy().transact(new VoidWork() {
            public void vrun() {
                ofy().delete().entity(serv).now();
                i.removeItems();
            }
        });
    }

    @Override
    public void deleteAll(final String hotel) {
        final EHotel ho = findEHotel(hotel);
        final DeleteItem i = new DeleteItem();
        beforeDelete(i,ho,null);
        ofy().transact(new VoidWork() {
            public void vrun() {
                List<E> li = ofy().load().type(cl).ancestor(ho).list();
                ofy().delete().entities(li);
                i.removeItems();
            }
        });

    }

}
