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
package com.jython.jpautil.crudimpl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.jython.jpautil.JpaUtils;
import com.jython.jpautil.crudimpl.gensym.IJpaObjectGenSymFactory;
import com.jython.serversecurity.cache.OObjectId;
import com.jython.serversecurity.jpa.PropUtils;
import com.jython.serversecurity.jpa.entities.EObjectDict;
import com.jython.ui.server.jpatrans.ITransactionContextFactory;
import com.jython.ui.server.jpatrans.JpaTransaction;
import com.jythonui.server.BUtil;
import com.jythonui.server.RUtils;
import com.jythonui.server.UtilHelper;
import com.jythonui.server.crud.ICrudObjectGenSym;
import com.jythonui.server.crud.IObjectCrud;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.shared.PropDescription;

public abstract class AbstractJpaCrud<T extends PropDescription, E extends EObjectDict>
        extends UtilHelper implements IObjectCrud<T> {

    private final String[] queryMap;
    protected final ITransactionContextFactory eFactory;
    private final String oName;
    private final Class<E> cl;
    private final IJpaObjectGenSymFactory iGenFactory;

    private final static int GETALLQUERY = 0;
    private final static int FINDELEMQUERY = 1;

    protected AbstractJpaCrud(String[] queryMap,
            IJpaObjectGenSymFactory iGenFactory, String oName,
            ITransactionContextFactory eFactory, Class<E> cl) {
        this.queryMap = queryMap;
        this.eFactory = eFactory;
        this.cl = cl;
        this.iGenFactory = iGenFactory;
        this.oName = oName;
    }

    abstract protected T toT(E sou, EntityManager em, OObjectId hotel);

    abstract protected E constructE(EntityManager em, OObjectId hotel);

    abstract protected void toE(E dest, T sou, EntityManager em, OObjectId hotel);

    abstract protected void afterAddChange(EntityManager em, OObjectId hotel,
            T prop, E elem, boolean add);

    abstract protected void beforedeleteElem(EntityManager em, OObjectId hotel,
            E elem);

    protected abstract class doTransaction extends JpaTransaction {

        protected final OObjectId hotel;

        protected doTransaction(OObjectId hotel) {
            super(eFactory);
            this.hotel = hotel;
        }

        protected E getElem(EntityManager em, String name) {
            return JpaUtils.getElem(em, hotel, queryMap[FINDELEMQUERY], name);
        }

        protected E getElem(EntityManager em, T elem) {
            return getElem(em, elem.getName());
        }

    }

    private void testObjectNull(OObjectId hotel) {
        if (hotel.getId() != null)
            return;
        errorMess(L(), IErrorCode.ERRORCODE115, ILogMess.OBKJECTIDCANNOTBENULL,hotel.getUserName(),hotel.getObject(),hotel.getInstanceId().getInstanceName());
    }

    protected void toEProperties(String[] prop, E dest, T sou) {
        RUtils.toEProperties(prop, dest, sou);
    }

    protected void toTProperties(String[] prop, T dest, E sou) {
        RUtils.toTProperties(prop, dest, sou);
    }

    private T createT(EntityManager em, E p, OObjectId hotel) {
        T rec = toT(p, em, hotel);
        PropUtils.copyToProp(rec, p);
        rec.setOOBjectId(hotel.getObject());
        rec.setId(p.getId());
        return rec;
    }

    private class GetAllList extends doTransaction {

        private List<T> retList = new ArrayList<T>();

        GetAllList(OObjectId hotel) {
            super(hotel);
        }

        @Override
        protected void dosth(EntityManager em) {
            Query q = JpaUtils.createObjectIdQuery(em, hotel,
                    queryMap[GETALLQUERY]);
            @SuppressWarnings("unchecked")
            List<E> list = q.getResultList();
            for (E p : list) {
                T rec = createT(em, p, hotel);
                retList.add(rec);
            }
        }
    }

    @Override
    public List<T> getList(OObjectId hotel) {
        GetAllList command = new GetAllList(hotel);
        command.executeTran();
        return command.retList;
    }

    private class AddElem extends doTransaction {

        private final T elem;
        T reselem;
        E pers;

        AddElem(OObjectId hotel, T elem) {
            super(hotel);
            this.elem = elem;
        }

        @Override
        protected void dosth(EntityManager em) {
            pers = constructE(em, hotel);

            ICrudObjectGenSym iGenSym = iGenFactory.construct(em);
            iGenSym.genSym(hotel, elem, oName);

            toE(pers, elem, em, hotel);
            JpaUtils.copyToEDict(hotel, pers, elem);
            BUtil.setCreateModif(hotel.getUserName(), pers, true);
            em.persist(pers);
            // id is not set at this moment (after commit)
            afterAddChange(em, hotel, elem, pers, true);
            reselem = toT(pers, em, hotel);
            PropUtils.copyToProp(reselem, pers);
        }

    }

    @Override
    public T addElem(OObjectId hotel, T elem) {
        testObjectNull(hotel);
        AddElem command = new AddElem(hotel, elem);
        command.executeTran();
        // id is set only after commit
        command.reselem.setId(command.pers.getId());
        return command.reselem;
    }

    private class ChangeElem extends doTransaction {
        private final T elem;

        ChangeElem(OObjectId hotel, T elem) {
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
            pers.setOObject(hotel.getId());
            em.persist(pers);
            afterAddChange(em, hotel, elem, pers, false);
        }

    }

    @Override
    public void changeElem(OObjectId hotel, T elem) {
        ChangeElem command = new ChangeElem(hotel, elem);
        command.executeTran();
    }

    private class DeleteElem extends doTransaction {
        private final T elem;

        DeleteElem(OObjectId hotel, T elem) {
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
    public void deleteElem(OObjectId hotel, T elem) {
        DeleteElem command = new DeleteElem(hotel, elem);
        command.executeTran();
    }

    private class FindElem extends doTransaction {

        private final String name;
        T reselem = null;

        FindElem(OObjectId hotel, String name) {
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
    public T findElem(OObjectId hotel, String name) {
        FindElem command = new FindElem(hotel, name);
        command.executeTran();
        return command.reselem;
    }

    private class FindElemId extends doTransaction {

        private final Long id;
        T reselem = null;

        FindElemId(OObjectId hotel, Long id) {
            super(hotel);
            this.id = id;
        }

        @Override
        protected void dosth(EntityManager em) {
            E pers = em.find(cl, id);
            if (pers == null)
                return;
            reselem = createT(em, pers, hotel);
        }

    }

    @Override
    public T findElemById(OObjectId hotel, Long id) {
        FindElemId command = new FindElemId(hotel, id);
        command.executeTran();
        return command.reselem;
    }

}
