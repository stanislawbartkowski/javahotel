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
package com.gwthotel.hotel.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.gwthotel.admin.HotelId;
import com.gwthotel.admin.holder.HHolder;
import com.gwthotel.admin.jpa.PropUtils;
import com.gwthotel.hotel.HUtils;
import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.IHotelObjectGenSym;
import com.gwthotel.hotel.IHotelProp;
import com.gwthotel.hotel.jpa.entities.EHotelDict;
import com.gwthotel.hotel.jpa.entities.EHotelServices;
import com.gwthotel.shared.IHotelConsts;
import com.gwthotel.shared.PropDescription;
import com.jython.ui.server.jpatrans.ITransactionContextFactory;
import com.jython.ui.server.jpatrans.JpaTransaction;
import com.jythonui.server.BUtil;
import com.jythonui.server.UtilHelper;
import com.jythonui.server.getmess.IGetLogMess;

public abstract class AbstractJpaCrud<T extends PropDescription, E extends EHotelDict> extends UtilHelper 
        implements IHotelProp<T> {

    private final String[] queryMap;
    protected final ITransactionContextFactory eFactory;
    protected final IGetLogMess lMess;
    private final HotelObjects tObject;
    private final IHotelObjectGenSymFactory iGen;

    private final static int GETALLQUERY = 0;
    private final static int FINDELEMQUERY = 1;

    protected AbstractJpaCrud(String[] queryMap,
            ITransactionContextFactory eFactory, HotelObjects tObject,
            IHotelObjectGenSymFactory iGen) {
        this.queryMap = queryMap;
        this.eFactory = eFactory;
        this.lMess = HHolder.getHM();
        this.tObject = tObject;
        this.iGen = iGen;
    }

    abstract protected T toT(E sou, EntityManager em, HotelId hotel);

    abstract protected E constructE(EntityManager em, HotelId hotel);

    abstract protected void toE(E dest, T sou, EntityManager em, HotelId hotel);

    abstract protected void afterAddChange(EntityManager em, HotelId hotel,
            T prop, E elem, boolean add);

    abstract protected void beforedeleteElem(EntityManager em, HotelId hotel,
            E elem);

    protected abstract class doTransaction extends JpaTransaction {

        protected final HotelId hotel;

        protected doTransaction(HotelId hotel) {
            super(eFactory);
            this.hotel = hotel;
        }

        protected E getElem(EntityManager em, String name) {
            return JUtils.getElem(em, hotel, queryMap[FINDELEMQUERY], name);
        }

        protected E getElem(EntityManager em, T elem) {
            return getElem(em, elem.getName());
        }

        protected EHotelServices findService(EntityManager em, String s) {
            return JUtils.findService(em, hotel, s);
        }

    }

    protected void toEProperties(String[] prop, E dest, T sou) {
        HUtils.toEProperties(prop, dest, sou);
    }

    protected void toTProperties(String[] prop, T dest, E sou) {
        HUtils.toTProperties(prop, dest, sou);
    }

    private T createT(EntityManager em, E p, HotelId hotel) {
        T rec = toT(p, em, hotel);
        PropUtils.copyToProp(rec, p);
        rec.setAttr(IHotelConsts.HOTELPROP, hotel.getHotel());
        rec.setId(p.getId());
        return rec;
    }

    private class GetAllList extends doTransaction {

        private List<T> retList = new ArrayList<T>();

        GetAllList(HotelId hotel) {
            super(hotel);
        }

        @Override
        protected void dosth(EntityManager em) {
            Query q = JUtils.createHotelQuery(em, hotel, queryMap[GETALLQUERY]);
            @SuppressWarnings("unchecked")
            List<E> list = q.getResultList();
            for (E p : list) {
                T rec = createT(em, p, hotel);
                retList.add(rec);
            }
        }
    }

    @Override
    public List<T> getList(HotelId hotel) {
        GetAllList command = new GetAllList(hotel);
        command.executeTran();
        return command.retList;
    }

    private class AddElem extends doTransaction {

        private final T elem;
        T reselem;

        AddElem(HotelId hotel, T elem) {
            super(hotel);
            this.elem = elem;
        }

        @Override
        protected void dosth(EntityManager em) {
            E pers = constructE(em, hotel);
            IHotelObjectGenSym iGenSym = iGen.construct(em);
            iGenSym.genSym(hotel, elem, tObject);
            toE(pers, elem, em, hotel);
            JUtils.copyToEDict(hotel, pers, elem);
            BUtil.setCreateModif(hotel.getUserName(), pers, true);
            em.persist(pers);
            afterAddChange(em, hotel, elem, pers, true);
            reselem = toT(pers, em, hotel);
            PropUtils.copyToProp(reselem, pers);
        }

    }

    @Override
    public T addElem(HotelId hotel, T elem) {
        AddElem command = new AddElem(hotel, elem);
        command.executeTran();
        return command.reselem;
    }

    private class ChangeElem extends doTransaction {
        private final T elem;

        ChangeElem(HotelId hotel, T elem) {
            super(hotel);
            this.elem = elem;
        }

        @Override
        protected void dosth(EntityManager em) {
            E pers = getElem(em, elem);
            if (pers == null) // TODO: more verbose, not expected
                return;
            toE(pers, elem, em, hotel);
            PropUtils.copyToEDict(pers, elem);
            BUtil.setCreateModif(hotel.getUserName(), pers, false);
            pers.setHotel(hotel.getId());
            em.persist(pers);
            afterAddChange(em, hotel, elem, pers, false);
        }

    }

    @Override
    public void changeElem(HotelId hotel, T elem) {
        ChangeElem command = new ChangeElem(hotel, elem);
        command.executeTran();
    }

    private class DeleteElem extends doTransaction {
        private final T elem;

        DeleteElem(HotelId hotel, T elem) {
            super(hotel);
            this.elem = elem;
        }

        @Override
        protected void dosth(EntityManager em) {
            E pers = getElem(em, elem);
            if (pers == null) // TODO: more verbose, not expected
                return;
            beforedeleteElem(em, hotel, pers);
            em.remove(pers);
        }

    }

    @Override
    public void deleteElem(HotelId hotel, T elem) {
        DeleteElem command = new DeleteElem(hotel, elem);
        command.executeTran();
    }

    private class FindElem extends doTransaction {

        private final String name;
        T reselem = null;

        FindElem(HotelId hotel, String name) {
            super(hotel);
            this.name = name;
        }

        @Override
        protected void dosth(EntityManager em) {
            E pers = getElem(em, name);
            if (pers == null)
                return;
            reselem = createT(em, pers, hotel);
        }

    }

    @Override
    public T findElem(HotelId hotel, String name) {
        FindElem command = new FindElem(hotel, name);
        command.executeTran();
        return command.reselem;
    }

}
