/*
 * Copyright 2017 stanislawbartkowski@gmail.com  
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
package com.jythonui.server.xmltoxmap;

import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import com.gwtmodel.table.map.XMap;
import com.jamesmurty.utils.XMLBuilder;
import com.jythonui.server.UtilHelper;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.server.xml.IXMLHelper;
import com.jythonui.server.xml.IXMLToXMap;
import com.jythonui.server.xml.IXMapFactory;

public class XMLToXMap extends UtilHelper implements IXMLToXMap {

	private final IXMLHelper xHelper;

	@Inject
	public XMLToXMap(IXMLHelper xHelper) {
		this.xHelper = xHelper;
	}

	@Override
	public void readXML(final XMap dest, String xml, String rootTag, String elemTag) {

		IXMapFactory xFactory = new IXMapFactory() {

			@Override
			public XMap construct() {
				return dest;
			}

		};

		// result not important
		xHelper.getList(new String[] { null, rootTag, elemTag, xml }, null, xFactory);
	}

	@Override
	public String toXML(XMap sou, String rootTag, String elemTag) {
		try {
			XMLBuilder builder = XMLBuilder.create(rootTag).e(elemTag);
			Map<String, String> ma = sou.getMap();
			for (Entry<String, String> e : ma.entrySet()) {
				builder = builder.e(e.getKey()).t(e.getValue()).up();
			}
			return builder.up().asString();

		} catch (ParserConfigurationException | FactoryConfigurationError | TransformerException e) {
			errorLog(L().getMess(IErrorCode.ERRORCODE126, ILogMess.ERRORWHILETRANSFORMINGTOXML),(Exception) e);
		}
		return null;
	}

}
