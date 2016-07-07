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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.gwtmodel.table.binder.BinderWidget;

class CssExtractorHandler extends DefaultHandler {

	BinderWidget.StyleClass sTyle = new BinderWidget.StyleClass();

	private final StringBuffer buf = new StringBuffer();

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		for (int i = 0; i < attributes.getLength(); i++) {
			String key = attributes.getLocalName(i);
			String val = attributes.getValue(i);
			sTyle.setAttr(key, val);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qqName) throws SAXException {
		sTyle.setContent(buf.toString());
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		buf.append(ch, start, length);
	}

}
