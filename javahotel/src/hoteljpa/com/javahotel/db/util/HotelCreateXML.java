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
package com.javahotel.db.util;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import com.gwtmodel.mapxml.CreateXML;
import com.gwtmodel.mapxml.IXMLTypeFactory;
import com.gwtmodel.table.mapxml.DataMapList;
import com.javahotel.db.context.ICommandContext;
import com.javahotel.dbres.resources.GetProp;
import com.javahotel.dbres.resources.IMess;

/**
 * @author hotel
 * 
 */
public class HotelCreateXML {

    private HotelCreateXML() {
    }


    /**
     * Create XML file using file fPattern as XML pattern to fill in
     * 
     * @param iC
     *            ICommandContext
     * @param fPattern
     *            File name of the xml file
     * @param xmlData
     *            Map of elements (XPat, Object) to put in
     * @return String valid xml file
     */
    public static String constructXMLFile(ICommandContext iC, String fPattern,
            DataMapList<?> d) {
        
        IXMLTypeFactory fa = new InvoiceXMLMapFactory();
        InputStream i = GetProp
                .getXMLFile(IMess.INVOICEXSDFILDER, fPattern);
        try {
            return CreateXML.constructXMLFile(fa, i, d);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
            iC.logFatalE(e);
        } catch (SAXException e) {
            e.printStackTrace();
            iC.logFatalE(e);
        } catch (IOException e) {
            e.printStackTrace();
            iC.logFatalE(e);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            iC.logFatalE(e);
        } catch (TransformerException e) {
            e.printStackTrace();
            iC.logFatalE(e);
        }
        return null;
    }
}
