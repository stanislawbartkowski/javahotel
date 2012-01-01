/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.mapxml;

import java.io.IOException;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

/**
 * @author hotel Utility class for XML validation. In case of error throws
 *         exception
 */
public class VerifyXML {

    private VerifyXML() {
    }

    /**
     * 
     * @param xsdFile
     *            URL of xsd file (XML schema)
     * @param sou
     *            Source XML file (in shape of StreamSource)
     * 
     * @throws SAXException
     * @throws IOException
     */
    public static void verify(URL xsdFile, StreamSource sou)
            throws SAXException, IOException {
        SchemaFactory fa = SchemaFactory
                .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema se = fa.newSchema(xsdFile);
        Validator validator = se.newValidator();
        validator.validate(sou);
    }

}
