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

import org.xml.sax.SAXException;

import com.gwtmodel.mapxml.ChangeXMLToMap;
import com.gwtmodel.mapxml.IXMLTypeFactory;
import com.gwtmodel.table.mapxml.DataMapList;
import com.javahotel.db.context.ICommandContext;
import com.javahotel.dbres.resources.GetProp;
import com.javahotel.dbres.resources.IMess;

/**
 * @author hotel
 * 
 */
public class HotelChangeXMLToMap {

    private HotelChangeXMLToMap() {
    }

    public static void constructMapFromXML(ICommandContext iC, DataMapList<?> d,
            String sXML) {
        IXMLTypeFactory fa = new InvoiceXMLMapFactory();
        InputStream ii = GetProp.getXMLFile(IMess.INVOICEXSDFILDER,
                IMess.INVOICEPATTERN);
        try {
            ChangeXMLToMap.constructMapFromXML(fa, d, sXML, ii);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            iC.logFatalE(e);
            return;
        } catch (SAXException e) {
            e.printStackTrace();
            iC.logFatalE(e);
            return;
        } catch (IOException e) {
            e.printStackTrace();
            iC.logFatalE(e);
            return;
        }
    }

}
