/*
 * Copyright 2016 stanislawbartkowski@gmail.com 
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
package com.jythonui.server.storage.suggest.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.google.inject.Inject;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.map.XMap;
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.UObjects;
import com.jythonui.server.Util;
import com.jythonui.server.registry.IStorageRegistry;
import com.jythonui.server.registry.IStorageRegistryFactory;
import com.jythonui.server.storage.suggest.ISuggestionStorage;
import com.jythonui.server.xml.IXMLToXMap;

public class SuggestionStorage implements ISuggestionStorage {

	private final IXMLToXMap ix;
	private final IStorageRegistry iReg;
	private final IStorageRegistry iRem;

	private final static String ROOT = "root";
	private final static String ELEM = "elem";
	private final char T = 'T';

	private class SMap extends XMap {
		private static final long serialVersionUID = 1L;
	}

	@Inject
	SuggestionStorage(IXMLToXMap ix, IStorageRegistryFactory iFactory) {
		this.ix = ix;
		iReg = iFactory.construct(ISharedConsts.SUGGESTIONREALM, true, true);
		iRem = iFactory.construct(ISharedConsts.REMEMBERREALM, true, true);
	}

	@Override
	public void saveSugestion(String key, String val, int size) {
		List<String> li = getSuggestion(key);
		// check if duplicate first already (most common case)
		if (!li.isEmpty())
			// do nothing more
			if (CUtil.EqNS(li.get(0), val))
				return;
		// remove duplicate
		for (String s : li)
			if (CUtil.EqNS(val, s)) {
				li.remove(s);
				break;
			}
		// insert at the beginning
		li.add(0, val);
		if (li.size() > size)
			// remove last
			li.remove(li.size() - 1);
		XMap x = new SMap();
		// create map preserving order
		for (int i = 0; i < li.size(); i++)
			x.setAttr(T + Integer.toString(i), li.get(i));
		String xml = ix.toXML(x, ROOT, ELEM);
		iReg.putEntry(key, UObjects.put(xml, key));
	}

	@Override
	public List<String> getSuggestion(String key) {
		byte[] o = iReg.getEntry(key);
		List<String> li = new ArrayList<String>();
		if (o == null)
			return li;
		SMap x = new SMap();
		String xml = (String) UObjects.get(o, key);
		ix.readXML(x, xml, ROOT, ELEM);
		List<Integer> order = new ArrayList<Integer>();
		Iterator<String> i = x.getKeys();
		while (i.hasNext())
			// remove T at the beginning
			order.add(Integer.parseInt(i.next().substring(1)));
		Collections.sort(order);
		for (Integer ii : order)
			li.add(x.getAttr(T + Integer.toString(ii)));
		return li;
	}

	@Override
	public void clearAll() {
		Util.removeStorageKeys(iReg);
		Util.removeStorageKeys(iRem);
	}

	@Override
	public void saveRemember(String key, String val) {
		iRem.putEntry(key, UObjects.put(val, key));
	}

	@Override
	public String getRemember(String key) {
		byte[] o = iRem.getEntry(key);
		if (o == null)
			return null;
		return (String) UObjects.get(o, key);

	}

	@Override
	public void removeRemember(String key) {
		iRem.removeEntry(key);
	}

}
