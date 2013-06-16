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
package com.gwthotel.hotel.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.gwthotel.admin.HotelId;
import com.gwthotel.admin.jpa.PropUtils;
import com.gwthotel.hotel.HUtils;
import com.gwthotel.hotel.IHotelProp;
import com.gwthotel.hotel.jpa.entities.EHotelDict;
import com.gwthotel.shared.IHotelConsts;
import com.gwthotel.shared.PropDescription;
import com.jython.ui.server.jpatrans.ITransactionContextFactory;
import com.jython.ui.server.jpatrans.JpaTransaction;
import com.jythonui.server.getmess.IGetLogMess;

public abstract class AbstractJpaCrud<T extends PropDescription, E extends EHotelDict>
        implements IHotelProp<T> {

    private final String[] queryMap;
    protected final ITransactionContextFactory eFactory;
    protected final IGetLogMess lMess;

    private final static int GETALLQUERY = 0;
    private final static int FINDELEMQUERY = 1;
    private final static int DELETEALLQUERY = 2;

    protected AbstractJpaCrud(String[] queryMap, ITransactionContextFactory eFactory,
            IGetLogMess lMess) {
        this.queryMap = queryMap;
        this.eFactory = eFactory;
        this.lMess = lMess;
    }

    abstract protected T toT(E sou, HotelId hotel);

    abstract protected E constructE(HotelId hotel);

    abstract protected void toE(E dest, T sou, HotelId hotel);

    abstract protected void beforedeleteAll(EntityManager em, HotelId hotel);

    abstract protected void beforedeleteElem(EntityManager em, HotelId hotel,
            E elem);

    protected abstract class doTransaction extends JpaTransaction {

        protected final HotelId hotel;

        protected doTransaction(HotelId hotel) {
            super(eFactory);
            this.hotel = hotel;
        }

        protected E getElem(EntityManager em, String name) {
            Query q = em.createNamedQuery(queryMap[FINDELEMQUERY]);
            q.setParameter(1, hotel.getId());
            q.setParameter(2, name);
            try {
                E pers = (E) q.getSingleResult();
                return pers;
            } catch (NoResultException e) {
                return null;
            }
        }

        protected E getElem(EntityManager em, T elem) {
            return getElem(em, elem.getName());
        }

    }

    protected void executeElemQuery(EntityManager em, HotelId hotel,
            String[] qList, E elem) {
        for (String query : qList) {
            Query q = em.createNamedQuery(query);
            q.setParameter(1, hotel.getId());
            q.setParameter(2, elem);
            q.executeUpdate();
        }

    }

    protected void executeHotelQuery(EntityManager em, HotelId hotel,
            String[] qList) {
        for (String query : qList) {
            Query q = em.createNamedQuery(query);
            q.setParameter(1, hotel.getId());
            q.executeUpdate();
        }

    }

    protected void toEProperties(String[] prop, E dest, T sou) {
        HUtils.toEProperties(lMess, prop, dest, sou);
    }

    protected void toTProperties(String[] prop, T dest, E sou) {
        HUtils.toTProperties(lMess, prop, dest, sou);
    }

    private class GetAllList extends doTransaction {

        private List<T> retList = new ArrayList<T>();

        GetAllList(HotelId hotel) {
            super(hotel);
        }

        @Override
        protected void dosth(EntityManager em) {
            Query q = em.createNamedQuery(queryMap[GETALLQUERY]);
            q.setParameter(1, hotel.getId());
            List<E> list = q.getResultList();
            for (E p : list) {
                T rec = toT(p, hotel);
                PropUtils.copyToProp(rec, p);
                rec.setAttr(IHotelConsts.HOTELPROP, hotel.getHotel());
                rec.setId(p.getId());
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

        AddElem(HotelId hotel, T elem) {
            super(hotel);
            this.elem = elem;
        }

        @Override
        protected void dosth(EntityManager em) {
            E pers = constructE(hotel);
            toE(pers, elem, hotel);
            PropUtils.copyToEDict(pers, elem);
            pers.setHotel(hotel.getId());
            em.persist(pers);
        }

    }

    @Override
    public void addElem(HotelId hotel, T elem) {
        AddElem command = new AddElem(hotel, elem);
        command.executeTran();
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
            toE(pers, elem, hotel);
            PropUtils.copyToEDict(pers, elem);
            pers.setHotel(hotel.getId());
            em.persist(pers);
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

    private class DeleteAll extends doTransaction {

        DeleteAll(HotelId hotel) {
            super(hotel);
        }

        @Override
        protected void dosth(EntityManager em) {
            beforedeleteAll(em, hotel);
            Query q = em.createNamedQuery(queryMap[DELETEALLQUERY]);
            q.setParameter(1, hotel.getId());
            q.executeUpdate();
        }
    }

    @Override
    public void deleteAll(HotelId hotel) {
        DeleteAll command = new DeleteAll(hotel);
        command.executeTran();
    }
}
