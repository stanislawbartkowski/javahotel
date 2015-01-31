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
package com.gwthotel.hotel.jpa;

import javax.persistence.EntityManager;

import com.gwthotel.admin.holder.HHolder;
import com.gwthotel.hotel.HotelObjects;
import com.jython.jpautil.crudimpl.AbstractJpaCrud;
import com.jython.jpautil.crudimpl.gensym.IJpaObjectGenSymFactory;
import com.jython.serversecurity.cache.OObjectId;
import com.jython.serversecurity.jpa.entities.EObjectDict;
import com.jython.ui.server.jpatrans.ITransactionContextFactory;
import com.jythonui.server.crud.IObjectCrud;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.shared.PropDescription;

public abstract class HotelAbstractJpaCrud<T extends PropDescription, E extends EObjectDict>
        extends AbstractJpaCrud<T, E> implements IObjectCrud<T> {

    protected final IGetLogMess hMess;
    private final HotelObjects hO;

    protected HotelAbstractJpaCrud(String[] queryMap,
            ITransactionContextFactory eFactory, HotelObjects tObject,
            IJpaObjectGenSymFactory iGenFactory, Class<E> cl) {
        super(queryMap, iGenFactory, tObject.name(), eFactory, cl);
        this.hMess = HHolder.getHM();
        this.hO = tObject;
    }
    
    @Override
    protected E constructE(EntityManager em, OObjectId hotel) {
        return (E) JUtils.create(hO);
    }


}
