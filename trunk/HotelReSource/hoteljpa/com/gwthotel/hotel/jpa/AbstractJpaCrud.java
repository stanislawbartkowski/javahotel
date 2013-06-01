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
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.gwthotel.admin.jpa.JpaTransaction;
import com.gwthotel.hotel.IHotelProp;
import com.gwthotel.hotel.jpa.entities.EHotelDict;
import com.gwthotel.shared.IHotelConsts;
import com.gwthotel.shared.PropDescription;
import com.jythonui.server.getmess.IGetLogMess;

public abstract class AbstractJpaCrud<T extends PropDescription, E extends EHotelDict>
        implements IHotelProp<T> {

    private final String[] queryMap;
    private final EntityManagerFactory eFactory;
    private final IGetLogMess lMess;

    private final static int GETALLQUERY = 0;
    private final static int FINDELEMQUERY = 1;
    private final static int DELETEALLQUERY = 2;

    protected AbstractJpaCrud(String[] queryMap, EntityManagerFactory eFactory,
            IGetLogMess lMess) {
        this.queryMap = queryMap;
        this.eFactory = eFactory;
        this.lMess = lMess;
    }

    abstract protected T toT(E sou);

    abstract protected E constructE();

    abstract protected void toE(E dest, T sou);

    abstract protected void beforedeleteAll(EntityManager em, String hotel);

    abstract protected void beforedeleteElem(EntityManager em, String hotel, E elem);

    private abstract class doTransaction extends JpaTransaction {

        protected final String hotel;

        doTransaction(String hotel) {
            super(eFactory, lMess);
            this.hotel = hotel;
        }

        protected E getElem(EntityManager em, T elem) {
            Query q = em.createNamedQuery(queryMap[FINDELEMQUERY]);
            q.setParameter(1, hotel);
            q.setParameter(2, elem.getName());
            try {
                E pers = (E) q.getSingleResult();
                return pers;
            } catch (NoResultException e) {
                return null;
            }
        }

    }

    private class GetAllList extends doTransaction {

        private List<T> retList = new ArrayList<T>();

        GetAllList(String hotel) {
            super(hotel);
        }

        @Override
        protected void dosth(EntityManager em) {
            Query q = em.createNamedQuery(queryMap[GETALLQUERY]);
            q.setParameter(1, hotel);
            List<E> list = q.getResultList();
            for (E p : list) {
                T rec = toT(p);
                rec.setAttr(IHotelConsts.HOTELPROP, p.getHotel());
                rec.setAttrLong(IHotelConsts.ID, p.getId());
                retList.add(rec);
            }
        }
    }

    @Override
    public List<T> getList(String hotel) {
        GetAllList command = new GetAllList(hotel);
        command.executeTran();
        return command.retList;
    }

    private class AddElem extends doTransaction {

        private final T elem;

        AddElem(String hotel, T elem) {
            super(hotel);
            this.elem = elem;
        }

        @Override
        protected void dosth(EntityManager em) {
            E pers = constructE();
            toE(pers, elem);
            pers.setHotel(hotel);
            em.persist(pers);
        }

    }

    @Override
    public void addElem(String hotel, T elem) {
        AddElem command = new AddElem(hotel, elem);
        command.executeTran();
    }

    private class ChangeElem extends doTransaction {
        private final T elem;

        ChangeElem(String hotel, T elem) {
            super(hotel);
            this.elem = elem;
        }

        @Override
        protected void dosth(EntityManager em) {
            E pers = getElem(em, elem);
            if (pers == null) // TODO: more verbose, not expected
                return;
            toE(pers, elem);
            pers.setHotel(hotel);
            em.persist(pers);
        }

    }

    @Override
    public void changeElem(String hotel, T elem) {
        ChangeElem command = new ChangeElem(hotel, elem);
        command.executeTran();
    }

    private class DeleteElem extends doTransaction {
        private final T elem;

        DeleteElem(String hotel, T elem) {
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
    public void deleteElem(String hotel, T elem) {
        DeleteElem command = new DeleteElem(hotel, elem);
        command.executeTran();
    }

    private class DeleteAll extends doTransaction {

        DeleteAll(String hotel) {
            super(hotel);
        }

        @Override
        protected void dosth(EntityManager em) {
            beforedeleteAll(em, hotel);
            Query q = em.createNamedQuery(queryMap[DELETEALLQUERY]);
            q.setParameter(1, hotel);
            q.executeUpdate();
        }
    }

    @Override
    public void deleteAll(String hotel) {
        DeleteAll command = new DeleteAll(hotel);
        command.executeTran();
    }
}
