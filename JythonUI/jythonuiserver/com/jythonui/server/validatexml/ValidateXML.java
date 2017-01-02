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
package com.jythonui.server.validatexml;

import java.io.IOException;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

/*
import org.apache.xerces.jaxp.validation.XMLSchema11Factory;
*/

import com.jythonui.server.IValidateXML;

public class ValidateXML implements IValidateXML {

	private static SchemaFactory construct() {
		return SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	}

	/*
	 * private static SchemaFactory construct11() { return
	 * XMLSchema11Factory.newInstance("http://www.w3.org/XML/XMLSchema/v1.1"); }
	 */

	@Override
	public void validate(Source sou, URL... xsdFiles) throws SAXException, IOException {
		SchemaFactory fa = construct();
		Source[] xsda = new Source[xsdFiles.length];
		for (int i = 0; i < xsdFiles.length; i++)
			xsda[i] = new StreamSource(xsdFiles[i].openStream());
		Schema se = fa.newSchema(xsda);
		Validator validator = se.newValidator();
		validator.validate(sou);
	}

}
