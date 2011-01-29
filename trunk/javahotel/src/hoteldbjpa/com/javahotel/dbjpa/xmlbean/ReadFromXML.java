/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.javahotel.dbutil.log.GetLogger;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class ReadFromXML {

	public static List<Object> readCol(InputStream i, Class<?> cla,
			final String tagName, String rootName, GetLogger log) {
		try {
			XMLReader xr = XMLReaderFactory.createXMLReader();
			ReadBeanHandler handler = new ReadBeanHandler(cla, log, tagName,
					rootName);
			xr.setContentHandler(handler);
			xr.parse(new InputSource(i));
			return handler.getRes();
		} catch (IOException ex) {
			log.getL().log(Level.SEVERE,"",ex);
		} catch (SAXException ex) {
			log.getL().log(Level.SEVERE,"",ex);
		}
		return null;
	}
}
