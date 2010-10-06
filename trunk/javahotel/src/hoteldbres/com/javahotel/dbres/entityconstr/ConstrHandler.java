/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
package com.javahotel.dbres.entityconstr;

import java.lang.reflect.InvocationTargetException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.javahotel.dbutil.log.GetLogger;

class ConstrHandler extends DefaultHandler {

	private final EntityConstrData da;
	private String komname;
	private String symname;
	private String classname;
	private String viewname;
	private String chars;

	private final GetLogger log;
	private final static String DEFAULT = "default";
	private final static String CLASS = "class";
	private final static String SYM = "sym";
	private final static String KOM = "kom";
	private final static String ENTITY = "entity";
	private final static String VIEWNAME = "vname";

	private void init() {
		komname = null;
		symname = null;
		classname = null;
		viewname = null;
	}

	ConstrHandler(EntityConstrData da, GetLogger log) {
		this.da = da;
		this.log = log;
		init();
		chars = "";
	}

	@Override
	public void startElement(final String namespaceURI, final String localName,
			String qName, Attributes attr) throws SAXException {
		if (qName.equals(ENTITY)) {
			init();
		}
		chars = "";
	}

	@Override
	public void characters(char ch[], int start, int length)
			throws SAXException {
		String s = new String(ch, start, length).trim();
		// next chunk
		chars += s;
	}

	@Override
	public void endElement(final String namespaceURI, final String localName,
			String qName) throws SAXException {
		if (qName.equals(ENTITY)) {
			EntityConstr ec = new EntityConstr(komname, symname, viewname);
			if (classname != null) {
				if (classname.equals(DEFAULT)) {
					da.setDefa(ec);
				} else {
					da.addConstr(classname, ec);
				}
			}
			return;
		}
		if (qName.equals(CLASS)) {
			classname = chars;
		}
		if (qName.equals(SYM)) {
			symname = chars;
		}
		if (qName.equals(VIEWNAME)) {
			viewname = chars;
		}
		if (qName.equals(KOM)) {
			komname = chars;
		}
	}
}
