/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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
package com.gwthotel.admintest.suite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.gwthotel.hotel.bill.CustomerBill;
import com.jythonui.server.security.token.ICustomSecurity;
import com.jythonui.shared.DialogVariables;

public class Test23 extends TestHelper {

    @Before
    public void before() {
        super.before();
        setUserPassword();
    }

    @Test
    public void test1() {
        ICustomSecurity cu = getSec(HOTEL);
        String token = iSec.authenticateToken(realM, "user", "secret", cu);
        assertNotNull(token);

        CustomerBill b = createP();
        DialogVariables v = new DialogVariables();
        v.setValueS("billno", b.getName());
        runAction(token, v, "dialog4.xml", "invoicexmlforbill");
        assertTrue(v.getValue("OK").getValueB());
        v = new DialogVariables();
        v.setValueS("billno", b.getName());
        runAction(token, v, "dialog4.xml", "invoicehtmlforbill");
        assertTrue(v.getValue("OK").getValueB());
        v = new DialogVariables();
        v.setValueS("billno", b.getName());
        runAction(token, v, "dialog4.xml", "invoicepdfforbill");
        assertTrue(v.getValue("OK").getValueB());
    }

    @Test
    public void test2() throws ParserConfigurationException, SAXException,
            IOException, XPathExpressionException {
        ICustomSecurity cu = getSec(HOTEL);
        String token = iSec.authenticateToken(realM, "user", "secret", cu);
        assertNotNull(token);

        CustomerBill b = createP();
        DialogVariables v = new DialogVariables();
        v.setValueS("billno", b.getName());
        runAction(token, v, "dialog4.xml", "invoicexmlforbill");
        assertTrue(v.getValue("OK").getValueB());
        String xml = v.getValueS("xml");
        assertNotNull(xml);
        System.out.println(xml);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(xml)));
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();
        XPathExpression expr = xpath.compile("/root/elem/country");
        NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        assertNotNull(nl);
        System.out.println(nl);
        Node no = nl.item(0);
        String s = no.getTextContent();
        System.out.println(s);
        assertEquals(s, "United Kingdom");
        int i = 0;

    }

}
