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
package com.javahotel.dbjpa.xmlbean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.javahotel.dbjpa.copybean.AbstractGetField;
import com.javahotel.dbjpa.copybean.CopyBean;
import com.javahotel.dbutil.log.GetLogger;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class ReadBeanHandler extends DefaultHandler {

	private class setField extends AbstractGetField {

		private String ch;

		@Override
		protected Object getVal(Object sou, Object dest, Method m)
				throws IllegalAccessException, IllegalArgumentException,
				InvocationTargetException {
			Class<?> cla = m.getReturnType();
			if (cla == Boolean.TYPE) {
				Boolean b = new Boolean(ch);
				return b;
			}
			if (cla == BigDecimal.class) {
				BigDecimal bg = new BigDecimal(ch);
				return bg;
			}
			return ch;
		}

	}

	private final List<Object> out = new ArrayList<Object>();
	private Object o;
	private final Class cla;
	private final String tagName;
	private final String rootName;
	private final GetLogger log;
	private String chars;
	private final setField sF = new setField();

	List<Object> getRes() {
		return out;
	}

	ReadBeanHandler(Class cla, GetLogger log, String tagName, String rootName) {
		this.cla = cla;
		this.tagName = tagName;
		this.rootName = rootName;
		this.log = log;
	}

	@Override
	public void startElement(final String namespaceURI, final String localName,
			String qName, Attributes attr) throws SAXException {
		if (qName.equals(tagName)) {
			o = CopyBean.createI(cla, log);
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
		try {
			if (qName.equals(tagName)) {
				out.add(o);
				return;
			}
			if (qName.equals(rootName)) {
				return;
			}
			sF.ch = chars;
			sF.setField(qName, o, o);
		} catch (NoSuchMethodException ex) {
			log.getL().log(Level.SEVERE,"",ex);
		} catch (IllegalAccessException ex) {
			log.getL().log(Level.SEVERE,"",ex);
		} catch (IllegalArgumentException ex) {
			log.getL().log(Level.SEVERE,"",ex);
		} catch (InvocationTargetException ex) {
			log.getL().log(Level.SEVERE,"",ex);
		}

	}
}
