/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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

import java.util.List;

import com.gwthotel.admin.gae.DictUtil;
import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.service.gae.entities.EHotelPriceElem;
import com.gwthotel.hotel.service.gae.entities.EHotelRoomServices;
import com.jython.serversecurity.cache.OObjectId;
import com.jython.ui.server.gae.crudimpl.CrudGaeAbstract;
import com.jython.ui.server.gae.security.entities.EObject;
import com.jython.ui.server.gae.security.entities.EObjectDict;
import com.jython.ui.server.gae.security.impl.EntUtil;
import com.jythonui.server.BUtil;
import com.jythonui.server.crud.ICrudObjectGenSym;
import com.jythonui.server.crud.IObjectCrud;
import com.jythonui.shared.PropDescription;

abstract public class HotelCrudGaeAbstract<T extends PropDescription, E extends EObjectDict>
        extends CrudGaeAbstract<T, E> implements IObjectCrud<T> {

    private final HotelObjects ho;

    protected HotelCrudGaeAbstract(Class<E> cl, HotelObjects ho,
            ICrudObjectGenSym iGen) {
        super(cl, ho.name(), iGen);
        this.ho = ho;
    }

    @Override
    protected E constructE() {
        Class cl = DictUtil.getClass(ho);
        return (E) BUtil.construct(cl);
    }

    protected class DeleteItem implements IDeleteElem {
        public List<EHotelPriceElem> pList = null;
        public List<EHotelRoomServices> sList = null;

        @Override
        public void remove() {
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

    protected EObject findEHotel(OObjectId hotel) {
        return EntUtil.findEOObject(hotel);
    }

    protected abstract void beforeDelete(DeleteItem i, EObject ho, E elem);

    @Override
    protected IDeleteElem beforeDelete(EObject ho, E elem) {
        final DeleteItem i = new DeleteItem();
        beforeDelete(i, ho, elem);
        return i;
    }

}
