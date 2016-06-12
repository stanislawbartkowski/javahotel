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
package com.jythonui.server.binder;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.gwtmodel.table.common.CUtil;
import com.jamesmurty.utils.XMLBuilder;
import com.jythonui.server.IConsts;
import com.jythonui.shared.binder.BinderWidget;
import com.jythonui.shared.binder.WidgetTypes;

class BinderHandler extends DefaultHandler {

	private class Tag {
		final BinderWidget b = new BinderWidget();
		XMLBuilder builder = null;
		Stack<StringBuffer> buf = new Stack<StringBuffer>();

		Tag(WidgetTypes w, Attributes attr) {
			b.setType(w);
			buf.push(new StringBuffer());
			for (int i = 0; i < attr.getLength(); i++) {
				String key = attr.getLocalName(i);
				String val = attr.getValue(i);
				b.getAttrs().put(key, val);
			}
		}
	}

	private Stack<Tag> sta = new Stack<Tag>();

	private static final Map<String, WidgetTypes> toW = new HashMap<String, WidgetTypes>();

	private static Random ra = new Random();

	BinderWidget parsed;

	static {
		toW.put(IConsts.HTMLPANELWIDGET, WidgetTypes.HTMLPanel);
		toW.put(IConsts.LABELWIDGET, WidgetTypes.Label);
		toW.put(IConsts.BUTTONWIDGET, WidgetTypes.Button);
		toW.put(IConsts.UIBINDER, WidgetTypes.UiBinder);
	}

	private static String genID() {
		return "binder-" + ra.nextInt();

	}

	private static boolean isNameSpace(String uri) {
		return IConsts.BINDERNAMESPACE.equals(uri);
	}

	private static WidgetTypes toWi(String uri, String localName) {
		if (!isNameSpace(uri))
			return null;
		return toW.get(localName);
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		// namespace aware, get localName;
		WidgetTypes w = toWi(uri, localName);
		if (w != null) {
			sta.push(new Tag(w, attributes));
			return;
		}
		if (isNameSpace(uri) && CUtil.EqNS(IConsts.OWIDGET,localName)) {
			return;
		}
		Tag ta = sta.peek();
		ta.buf.push(new StringBuffer());
		if (ta.builder == null)
			try {
				ta.builder = XMLBuilder.create(localName);
			} catch (ParserConfigurationException | FactoryConfigurationError e) {
				throw new SAXException(e.getLocalizedMessage());
			}
		else
			ta.builder = ta.builder.e(localName);
		for (int i = 0; i < attributes.getLength(); i++) {
			String key = attributes.getLocalName(i);
			String val = attributes.getValue(i);
			ta.builder = ta.builder.a(key, val);
		}
	}
	

	@Override
	public void endElement(String uri, String localName, String qqName) throws SAXException {
		WidgetTypes w = toWi(uri, localName);
		try {
			if (w != null) {
				Tag ta = sta.pop();
				String c1 = ta.builder == null ? null : ta.builder.asString();
				StringBuffer b = ta.buf.pop();
				String c2 = b.length() == 0 ? null : b.toString();
				ta.b.setContentHtml(CUtil.concatSP(c1, c2));
				if (sta.isEmpty())
					parsed = ta.b;
				else
					sta.peek().b.getwList().add(ta.b);
				return;
			}
			// ignore ui:o
			if (isNameSpace(uri) && CUtil.EqNS(IConsts.OWIDGET,localName)) {
				return;
			}
			Tag ta = sta.peek();
			StringBuffer bu = ta.buf.pop();
			ta.builder = ta.builder.t(bu.toString()).up();
		} catch (TransformerException e) {
			new SAXException(e.getMessage());
		}
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		sta.peek().buf.peek().append(ch, start, length);
	}

}
