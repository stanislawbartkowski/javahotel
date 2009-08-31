/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
