/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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
import java.io.StringReader;
import java.net.URL;

import javax.xml.transform.stream.StreamSource;

import org.xml.sax.SAXException;

import com.gwtmodel.mapxml.VerifyXML;
import com.javahotel.db.context.ICommandContext;
import com.javahotel.dbres.resources.GetProp;
import com.javahotel.dbres.resources.IMess;
import com.javahotel.dbutil.prop.ReadProperties;

/**
 * @author hotel
 * 
 */
public class HotelVerifyXML {

    private HotelVerifyXML() {

    }

    public static boolean verify(ICommandContext iC, String souXML,
            String xsdFile) {
        String fName = GetProp.getResourceName(IMess.INVOICEXSDFILDER, xsdFile);
        URL u = ReadProperties.getResourceURL(fName);
        StringReader s = new StringReader(souXML);
        try {
            VerifyXML.verify(u, new StreamSource(s));
            return true;
        } catch (SAXException e) {
            e.printStackTrace();
            iC.logFatalE(e);
        } catch (IOException e) {
            e.printStackTrace();
            iC.logFatalE(e);
        }
        return false;
    }

}
