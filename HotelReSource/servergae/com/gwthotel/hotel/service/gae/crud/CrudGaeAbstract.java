/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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

import com.googlecode.objectify.VoidWork;
import com.gwthotel.admin.gae.DictUtil;
import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.IHotelObjectGenSym;
import com.gwthotel.hotel.IHotelProp;
import com.gwthotel.hotel.service.gae.entities.EHotelDict;
import com.gwthotel.hotel.service.gae.entities.EHotelPriceElem;
import com.gwthotel.hotel.service.gae.entities.EHotelRoomServices;
import com.gwthotel.shared.IHotelConsts;
import com.gwthotel.shared.PropDescription;
import com.jython.serversecurity.OObjectId;
import com.jython.ui.server.gae.security.entities.EObject;
import com.jython.ui.server.gae.security.impl.EntUtil;
import com.jythonui.server.BUtil;
import com.jythonui.server.getmess.IGetLogMess;

abstract public class CrudGaeAbstract<T extends PropDescription, E extends EHotelDict>
        implements IHotelProp<T> {

    protected final IGetLogMess lMess;
    private final Class<E> cl;
    private final IHotelObjectGenSym iGen;
    private final HotelObjects o;

    protected CrudGaeAbstract(IGetLogMess lMess, Class<E> cl, HotelObjects ho,
            IHotelObjectGenSym iGen) {
        this.lMess = lMess;
        this.cl = cl;
        this.o = ho;
        this.iGen = iGen;
    }

    protected EObject findEHotel(OObjectId hotel) {
        return DictUtil.findEHotel(lMess, hotel);
    }

    protected abstract T constructProp(EObject ho, E e);

    protected abstract E constructE();

    protected abstract void toE(EObject ho, E e, T t);

    protected class DeleteItem {
        public List<EHotelPriceElem> pList = null;
        public List<EHotelRoomServices> sList = null;

        void removeItems() {
            if (pList != null)
                ofy().delete().entities(pList);
            if (sList != null)
                ofy().delete().entities(sList);
        }

        public void readAllRoomServices(EObject eh) {
            sList = ofy().load().type(EHotelRoomServices.class).ancestor(eh)
                    .list();
        }

        public void readAllPriceElems(EObject eh) {
            pList = ofy().load().type(EHotelPriceElem.class).ancestor(eh)
                    .list();
        }
    }

    protected abstract void beforeDelete(DeleteItem i, EObject ho, E elem);

    private T toProp(E e, EObject ho) {
        T dest = constructProp(ho, e);
        EntUtil.toProp(dest, e);
        dest.setAttr(IHotelConsts.HOTELPROP, ho.getName());
        return dest;
    }

    private void toEDict(E dest, T sou, EObject hotel) {
        EntUtil.toEDict(dest, sou);
        if (!dest.isHotelSet())
            dest.setHotel(hotel);
        toE(hotel, dest, sou);
    }

    @Override
    public List<T> getList(OObjectId hotel) {
        EObject eh = findEHotel(hotel);
        List<E> li = ofy().load().type(cl).ancestor(eh).list();
        List<T> outList = new ArrayList<T>();
        for (E e : li) {
            T p = toProp(e, eh);
            outList.add(p);
        }
        return outList;
    }

    @Override
    public T addElem(OObjectId hotel, T elem) {
        final E e = constructE();
        iGen.genSym(hotel, elem, o);
        EObject ho = findEHotel(hotel);
        toEDict(e, elem, ho);
        BUtil.setCreateModif(hotel.getUserName(), e, true);
        ofy().transact(new VoidWork() {
            public void vrun() {
                ofy().save().entity(e).now();
            }
        });
        E e1 = findE(ho, elem.getName());
        return toProp(e1, ho);
    }

    private E findE(EObject eh, String name) {
        return DictUtil.findE(eh, name, cl);
    }

    private E findService(EObject eh, T serv) {
        return findE(eh, serv.getName());
    }

    @Override
    public void changeElem(OObjectId hotel, T elem) {
        EObject ho = findEHotel(hotel);
        final E serv = findService(ho, elem);
        if (serv == null) // TODO: more verbose log
            return;
        toEDict(serv, elem, ho);
        BUtil.setCreateModif(hotel.getUserName(), serv, false);
        ofy().transact(new VoidWork() {
            public void vrun() {
                ofy().save().entity(serv).now();
            }
        });
    }

    @Override
    public void deleteElem(OObjectId hotel, T elem) {
        EObject ho = findEHotel(hotel);
        final E serv = findService(ho, elem);
        if (serv == null) // TODO: more verbose log
            return;
        final DeleteItem i = new DeleteItem();
        beforeDelete(i, ho, serv);
        ofy().transact(new VoidWork() {
            public void vrun() {
                ofy().delete().entity(serv).now();
                i.removeItems();
            }
        });
    }

    @Override
    public T findElem(OObjectId hotel, String name) {
        EObject ho = findEHotel(hotel);
        E e = findE(ho, name);
        if (e == null)
            return null;
        return toProp(e, ho);
    }

    @Override
    public T findElemById(OObjectId hotel, Long id) {
        EObject eh = findEHotel(hotel);
        E e = ofy().load().type(cl).parent(eh).id(id).now();
        return toProp(e, eh);
    }
}
