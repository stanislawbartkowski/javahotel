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
package com.jython.ui.server.gae.crudimpl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.googlecode.objectify.VoidWork;
import com.jython.serversecurity.cache.OObjectId;
import com.jython.ui.server.gae.security.entities.EObject;
import com.jython.ui.server.gae.security.entities.EObjectDict;
import com.jython.ui.server.gae.security.impl.EntUtil;
import com.jythonui.server.BUtil;
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.UtilHelper;
import com.jythonui.server.crud.ICrudObjectGenSym;
import com.jythonui.server.crud.IObjectCrud;
import com.jythonui.shared.PropDescription;

abstract public class CrudGaeAbstract<T extends PropDescription, E extends EObjectDict> extends UtilHelper
		implements IObjectCrud<T> {

	private final Class<E> cl;
	private final String oName;
	private final ICrudObjectGenSym iGen;
	private final boolean orderlistById;

	protected interface IDeleteElem {
		void remove();
	}

	protected CrudGaeAbstract(Class<E> cl, String oName, ICrudObjectGenSym iGen, boolean orderlistById) {
		this.cl = cl;
		this.oName = oName;
		this.iGen = iGen;
		this.orderlistById = orderlistById;
	}

	protected CrudGaeAbstract(Class<E> cl, String oName, ICrudObjectGenSym iGen) {
		this(cl, oName, iGen, false);
	}

	protected EObject findEObject(OObjectId hotel) {
		return EntUtil.findEOObject(hotel);
	}

	protected abstract T constructProp(EObject ho, E e);

	protected abstract E constructE();

	protected abstract void toE(EObject ho, E e, T t);

	protected abstract IDeleteElem beforeDelete(EObject ho, E elem);

	private T toProp(E e, EObject ho) {
		T dest = constructProp(ho, e);
		EntUtil.toProp(dest, e);
		dest.setAttr(ISharedConsts.OBJECTPROP, ho.getName());
		return dest;
	}

	private void toEDict(E dest, T sou, EObject hotel) {
		EntUtil.toEDict(dest, sou);
		if (!dest.isObjectSet())
			dest.setObject(hotel);
		toE(hotel, dest, sou);
	}

	@Override
	public List<T> getList(OObjectId hotel) {
		EObject eh = findEObject(hotel);
		List<E> li = ofy().load().type(cl).ancestor(eh).list();
		List<T> outList = new ArrayList<T>();
		for (E e : li) {
			T p = toProp(e, eh);
			outList.add(p);
		}
		if (orderlistById)
			EntUtil.sortT(outList);
		return outList;
	}

	@Override
	public T addElem(OObjectId hotel, T elem) {
		final E e = constructE();
		iGen.genSym(hotel, elem, oName);
		EObject ho = findEObject(hotel);
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
		return EntUtil.findE(eh, name, cl);
	}

	private E findService(EObject eh, T serv) {
		return findE(eh, serv.getName());
	}

	@Override
	public void changeElem(OObjectId hotel, T elem) {
		EObject ho = findEObject(hotel);
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
		EObject ho = findEObject(hotel);
		final E serv = findService(ho, elem);
		if (serv == null) // TODO: more verbose log
			return;
		final IDeleteElem i = beforeDelete(ho, serv);
		ofy().transact(new VoidWork() {
			public void vrun() {
				ofy().delete().entity(serv).now();
				if (i != null)
					i.remove();
			}
		});
	}

	@Override
	public T findElem(OObjectId hotel, String name) {
		EObject ho = findEObject(hotel);
		E e = findE(ho, name);
		if (e == null)
			return null;
		return toProp(e, ho);
	}

	@Override
	public T findElemById(OObjectId hotel, Long id) {
		EObject eh = findEObject(hotel);
		E e = ofy().load().type(cl).parent(eh).id(id).now();
		return toProp(e, eh);
	}
}
