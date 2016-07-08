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
import com.jamesmurty.utils.XMLBuilder;
import com.jythonui.server.IConsts;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.holder.SHolder;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;

class BinderHandler extends DefaultHandler {

	private static final String root = "root";

	static final private Logger log = Logger.getLogger(BinderHandler.class.getName());

	private class Tag {
		final BinderWidget b = new BinderWidget();
		XMLBuilder builder;
		Stack<StringBuffer> buf = new Stack<StringBuffer>();

		Tag(WidgetTypes w, Attributes attr) throws ParserConfigurationException, FactoryConfigurationError {
			b.setType(w);
			buf.push(new StringBuffer());
			for (int i = 0; i < attr.getLength(); i++) {
				String key = attr.getLocalName(i);
				String val = attr.getValue(i);
				b.setAttr(key, val);
			}
			builder = XMLBuilder.create(root);
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
		toW.put(IConsts.IRONICON, WidgetTypes.IronIcon);
		toW.put(IConsts.PAPERICONITEM, WidgetTypes.PaperIconItem);
		toW.put(IConsts.PAPERBUTTON, WidgetTypes.PaperButton);
		toW.put(IConsts.PAPERHEADERPANEL, WidgetTypes.PaperHeaderPanel);
		toW.put(IConsts.PAPERTOOLBAR, WidgetTypes.PaperToolBar);
		toW.put(IConsts.IMAGE, WidgetTypes.Image);
		toW.put(IConsts.PAPERICONBUTTON, WidgetTypes.PaperIconButton);
		toW.put(IConsts.PAPERDRAWERPANEL, WidgetTypes.PaperDrawerPanel);
		toW.put(IConsts.PAPERCHECKBOX, WidgetTypes.PaperCheckbox);
		toW.put(IConsts.PAPERDIALOG, WidgetTypes.PaperDialog);
		toW.put(IConsts.PAPERDIALOGSCROLLABLE, WidgetTypes.PaperDialogScrollable);
		toW.put(IConsts.PAPERDROPDOWNMENU, WidgetTypes.PaperDropDownMenu);
		toW.put(IConsts.PAPERMENU, WidgetTypes.PaperMenu);
		toW.put(IConsts.PAPERTABS, WidgetTypes.PaperTabs);
	}

	private static String genId() {
		return "binder-" + ra.nextInt();

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
		WidgetTypes w = toW.get(localName);
		if (w != null)
			return w;
		String mess = L().getMess(IErrorCode.ERRORCODE129, ILogMess.WIDGETYPESNOTRECOGNIZED, localName);
		log.severe(mess);
		throw new SAXException(mess);
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		// namespace aware, get localName;
		WidgetTypes w = toWi(uri, localName);
		if (w != null) {
			try {
				sta.push(new Tag(w, attributes));
			} catch (ParserConfigurationException | FactoryConfigurationError e) {
				throw new SAXException(e.getLocalizedMessage());
			}
			return;
		}
		if (isNameSpace(uri) && CUtil.EqNS(IConsts.OWIDGET, localName)) {
			return;
		}
		Tag ta = sta.peek();
		ta.buf.push(new StringBuffer());
		ta.builder = ta.builder.e(localName);
		for (int i = 0; i < attributes.getLength(); i++) {
			String key = attributes.getLocalName(i);
			String val = attributes.getValue(i);
			ta.builder = ta.builder.a(key, val);
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
			// ignore ui:o
			if (isNameSpace(uri) && CUtil.EqNS(IConsts.OWIDGET, localName)) {
				return;
			}
			Tag ta = sta.peek();
			StringBuffer bu = ta.buf.pop();
			ta.builder = ta.builder.t(bu.toString()).up();
			// System.out.println(localName);
			// System.out.println(ta.builder.asString());
		} catch (TransformerException e) {
			new SAXException(e.getMessage());
		}
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		sta.peek().buf.peek().append(ch, start, length);
	}

}
