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

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.gwtmodel.table.binder.BinderWidget;

class BinderReader {

	private BinderReader() {

	}

	static void extractStyle(BinderWidget w) throws ParserConfigurationException, SAXException, IOException {
		ExtractStyle.getStyle(w);
		// recursive
		for (BinderWidget b : w.getwList())
			extractStyle(b);
	}

	static BinderWidget parseBinder(InputStream sou) throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		// important, namespace
		factory.setNamespaceAware(true);
		SAXParser saxParser = factory.newSAXParser();
		BinderHandler ma = new BinderHandler();
		saxParser.parse(sou, ma);
		extractStyle(ma.parsed);
		return ma.parsed;
	}

}
