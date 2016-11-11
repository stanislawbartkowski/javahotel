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

import java.util.Random;
import java.util.Stack;
import java.util.logging.Logger;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.gwtmodel.table.binder.BinderWidget;
import com.gwtmodel.table.binder.WidgetTypes;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.shared.JythonUIFatal;
import com.jamesmurty.utils.XMLBuilder;
import com.jythonui.server.IBinderUIStyle;
import com.jythonui.server.IConsts;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.holder.SHolder;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.shared.ICommonConsts;

class BinderHandler extends DefaultHandler {

	private static final String root = "root";

	static final private Logger log = Logger.getLogger(BinderHandler.class.getName());

	private class Tag {
		final BinderWidget b = new BinderWidget();
		XMLBuilder builder;
		Stack<StringBuffer> buf = new Stack<StringBuffer>();

		Tag(WidgetTypes w, Attributes attr)
				throws ParserConfigurationException, FactoryConfigurationError, SAXException {
			b.setType(w);
			buf.push(new StringBuffer());
			for (int i = 0; i < attr.getLength(); i++) {
				String key = attr.getLocalName(i);
				String val = null;
				try {
					val = resC.fixAttrValue(attr.getValue(i));
				} catch (JythonUIFatal e) {
					// not happy, exception in constructor
					throw new SAXException(e.getMessage());
				}
				b.setAttr(key, val);
			}
			builder = XMLBuilder.create(root);
		}
	}

	private Stack<Tag> sta = new Stack<Tag>();
	private StringBuffer charb = null;

	private static Random ra = new Random();

	BinderWidget parsed;
	
	private final IBinderUIStyle resC = SHolder.getiStyleBinderFactory().construct();

	private static String genId() {
		return "binder-" + ra.nextInt();

	}

	private static void throwE(String errC, String errM, String... pars) throws SAXException {
		String mess = L().getMess(errC, errM, pars);
		log.severe(mess);
		throw new SAXException(mess);
	}

	private static boolean isNameSpace(String uri) {
		return IConsts.BINDERNAMESPACE.equals(uri);
	}

	private static IGetLogMess L() {
		return SHolder.getM();
	}

	private static WidgetTypes toWi(String uri, String localName) throws SAXException {

		if (!isNameSpace(uri))
			return null;
		if (isSpec(uri, localName))
			return null;
		try {
			return WidgetTypes.valueOf(localName);
		} catch (IllegalArgumentException e) {
			throwE(IErrorCode.ERRORCODE129, ILogMess.WIDGETYPESNOTRECOGNIZED, localName);
			return null;
		}
	}

	private static boolean isSpecO(String uri, String localName) {
		return isNameSpace(uri) && CUtil.EqNS(IConsts.OWIDGET, localName);
	}

	private static boolean isSpecStyle(String uri, String localName) {
		return isNameSpace(uri) && CUtil.EqNS(IConsts.STYLEW, localName);
	}

	private static boolean isSpec(String uri, String localName) {
		return isSpecO(uri, localName) || isSpecStyle(uri, localName);
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		// namespace aware, get localName;
		charb = new StringBuffer();
		WidgetTypes w = toWi(uri, localName);
		if (w != null) {
			try {
				sta.push(new Tag(w, attributes));
			} catch (ParserConfigurationException | FactoryConfigurationError e) {
				throw new SAXException(e.getLocalizedMessage());
			}
			return;
		}
		if (isSpecStyle(uri, localName)) {
			if (sta.isEmpty() || sta.peek().b.getType() != WidgetTypes.UiBinder)
				throwE(IErrorCode.ERRORCODE132, ILogMess.UISTYLEUIBINDER, qName, WidgetTypes.UiBinder.name());
		}
		if (isSpec(uri, localName))
			return;
		Tag ta = sta.peek();
		ta.buf.push(new StringBuffer());
		ta.builder = ta.builder.e(localName);
		for (int i = 0; i < attributes.getLength(); i++) {
			String key = attributes.getLocalName(i);
			// replace resource
			String val = null;
			try {
				val = resC.fixAttrValue(attributes.getValue(i));
			} catch (JythonUIFatal e) {
				throw new SAXException(e.getMessage());
			}
			ta.builder = ta.builder.a(key, val);
			if (key.equals(ICommonConsts.ID) && val.equals(ICommonConsts.DROPMENUID))
				ta.b.setIdDropId(true);
			if (key.equals(IConsts.HTMLCLASS) && val.contains(ICommonConsts.DROPDOWNCONTENT))
				ta.b.setClassDropDownContent(true);
		}
	}

	private static String getS(XMLBuilder builder) throws TransformerException {
		String s = builder.asString().replaceAll("<" + root + ">", "").replaceAll("</" + root + ">", "")
				.replaceAll("<" + root + "/>", "");
		// remove <root> and </root>
		return s;
	}

	@Override
	public void endElement(String uri, String localName, String qqName) throws SAXException {
		WidgetTypes w = toWi(uri, localName);
		try {
			if (w != null) {
				// System.out.println(localName);
				Tag ta = sta.pop();
				String c1 = getS(ta.builder);
				StringBuffer b = ta.buf.pop();
				String c2 = b.length() == 0 ? null : b.toString();
				ta.b.setContentHtml(CUtil.concatSP(c1, c2));
				// System.out.println(ta.b.getContentHtml());
				if (sta.isEmpty())
					parsed = ta.b;
				else {
					// add to list
					sta.peek().b.getwList().add(ta.b);
					// create div id
					// System.out.println(sta.peek().builder.asString());
					String id = genId();
					sta.peek().builder = sta.peek().builder.e("div").a("id", id).t("to remove").up();
					ta.b.setId(id);
					// System.out.println(sta.peek().builder.asString());
				}
				return;
			}
			if (isSpecStyle(uri, localName)) {
				String style = resC.parseStyle(charb.toString());
				BinderWidget.StyleClass sTyle = new BinderWidget.StyleClass();
				sTyle.setContent(style);
				// clear
				sta.peek().b.getStyleList().add(sTyle);
				return;
			}
			// add to contenthtlm if not ui:style
			if (sta.peek().b.getType() == WidgetTypes.UiBinder)
				sta.peek().buf.peek().append(charb);
			// ignore ui:o
			if (isSpec(uri, localName))
				return;
			Tag ta = sta.peek();
			StringBuffer bu = ta.buf.pop();
			ta.builder = ta.builder.t(bu.toString()).up();
			// System.out.println(localName);
			// System.out.println(ta.builder.asString());
		} catch (TransformerException e) {
			throw new SAXException(e.getMessage());
		}
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		if (sta.peek().b.getType() != WidgetTypes.UiBinder)
			sta.peek().buf.peek().append(ch, start, length);
		charb.append(ch, start, length);
	}

}
