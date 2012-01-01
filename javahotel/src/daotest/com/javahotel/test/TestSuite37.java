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
package com.javahotel.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.gwtmodel.mapxml.ChangeXMLToMap;
import com.gwtmodel.mapxml.CreateXML;
import com.gwtmodel.mapxml.IXMLTypeFactory;
import com.gwtmodel.mapxml.SimpleXMLTypeFactory;
import com.gwtmodel.mapxml.VerifyXML;
import com.gwtmodel.table.mapxml.DataMapList;
import com.javahotel.common.toobject.DContainer;
import com.javahotel.common.toobject.DMapContainerList;
import com.javahotel.db.util.InvoiceXMLMapFactory;
import com.javahotel.dbres.resources.GetProp;
import com.javahotel.dbres.resources.IMess;
import com.javahotel.dbutil.prop.ReadProperties;

/**
 * @author hotel
 * 
 */
public class TestSuite37 {

    private static final String INVOICENUMBER = "//InvoiceData/InvoiceNumber";
    private static final String INVOICEDATE = "//InvoiceData/InvoiceDate";
    private static final String INVOICETOTAL = "//Total/Total";
    private static final String PAYMENTMETHOD = "//Payment/PaymentMethod";
    private static final String PERSONTITLE = "//Person/Title";

    private static final String LINENO = "No";
    private static final String LINEDESC = "Description";

    private static final String PATTERNNAME = "Invoice.pattern";

    private void validate(String xml) throws SAXException, IOException {
        String fName = GetProp.getResourceName("xsd", IMess.INVOICEXSD);
        URL u = ReadProperties.getResourceURL(fName);
        StringReader s = new StringReader(xml);
        VerifyXML.verify(u, new StreamSource(s));
    }

    /*
     * Test scenario for testing XMLMap documents
     * Step 1: create Map object and generate xml file
     * Verification 1: Check if xml file contains value 
     * Verification 2: Run validate
     * Step 2: recreate Map object from xml file
     * Verification 3: Verify if Map object contains value
     */
    private void verify(String xId, String type, String val) throws Exception {
        // Step 1
        IXMLTypeFactory fa = new SimpleXMLTypeFactory();
        DataMapList<?> da = new DMapContainerList();
        da.getdFields().put(xId, fa.contruct(type, val));
        InputStream ii = GetProp.getXMLFile("xsd", PATTERNNAME);
        String xml = CreateXML.constructXMLFile(fa, ii, da);
        assertNotNull(xml);

        // Verification 1
        StringReader s = new StringReader(xml);
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
        Document doc = docBuilder.parse(new InputSource(s));
        XPathFactory xPathF = XPathFactory.newInstance();
        XPath xPath = xPathF.newXPath();
        XPathExpression xE = xPath.compile(xId);
        Object result = xE.evaluate(doc, XPathConstants.NODESET);
        NodeList nlist = (NodeList) result;
        assertEquals(1, nlist.getLength());
        for (int i = 0; i < nlist.getLength(); i++) {
            Node n = nlist.item(i);
            System.out.println(n.getTextContent());
            assertEquals(val, n.getTextContent());
        }

        // Verification 2
        validate(xml);
        // no exception
        // Step 2
        DataMapList d1 = new DMapContainerList();
        assertEquals(0, d1.getdLines().size());
        ii = GetProp.getXMLFile("xsd", PATTERNNAME);
        ChangeXMLToMap.constructMapFromXML(fa, d1, xml, ii);
        Object res = d1.getdFields().get(xId);
        // Verification 3
        assertEquals(val, fa.toS(type, res));

    }

    /*
     * Test case 1:  verify string value
     */
    @Test
    public void test1() throws Exception {
        verify(INVOICENUMBER, null, "2/22/66");
    }

    /*
     * Test case 2:  verify date value
     */
    @Test
    public void test2() throws Exception {
        verify(INVOICEDATE, IXMLTypeFactory.DATE, "2011-11-27");
    }

    /*
     * Test case 3: verify decimal value
     */
    @Test
    public void test3() throws Exception {
        verify(INVOICETOTAL, IXMLTypeFactory.DECIMAL, "123.45");
    }

    /*
     * Test case 4: verify lines
     * Step 1: create MAP document with one line
     * Step 2: create xml file
     * Verification 1: check if xml contains line value
     * Verification 2: run validation
     * Step 3: recreate map object
     * Verification 3: check if contains one line and value
     */
    @Test
    public void test4() throws Exception {
        // Step 1
        IXMLTypeFactory fa = new InvoiceXMLMapFactory();
        DataMapList<?> da = new DMapContainerList();
        DContainer li = (DContainer) da.addToLines();
        li.put(LINEDESC, "Purchase of something");
        InputStream ii = GetProp.getXMLFile("xsd", PATTERNNAME);
        // Step 2
        String xml = CreateXML.constructXMLFile(fa, ii, da);
        assertNotNull(xml);

        // Verification 1
        StringReader s = new StringReader(xml);
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
        Document doc = docBuilder.parse(new InputSource(s));
        XPathFactory xPathF = XPathFactory.newInstance();
        XPath xPath = xPathF.newXPath();
        XPathExpression xE = xPath.compile("//Lines/Line[1]/Description");
        Object result = xE.evaluate(doc, XPathConstants.NODESET);
        NodeList nlist = (NodeList) result;
        assertEquals(1, nlist.getLength());
        for (int i = 0; i < nlist.getLength(); i++) {
            Node n = nlist.item(i);
            System.out.println(n.getTextContent());
            assertEquals("Purchase of something", n.getTextContent());
        }

        // Verification 2
        validate(xml);

        // Step 3
        DMapContainerList d1 = new DMapContainerList();
        ii = GetProp.getXMLFile("xsd", PATTERNNAME);
        ChangeXMLToMap.constructMapFromXML(fa, d1, xml, ii);
        // Verification 3
        assertEquals(1, d1.getdLines().size());
        for (DContainer ma : d1.getdLines()) {
            String desc = (String) ma.get(LINEDESC);
            assertEquals("Purchase of something", desc);
        }
    }

    /*
     * Test case 5: check bulk number of lines
     * Step 1: prepare map object with 50 lines
     * Step 2: create xml file
     * Verification 1: run validate
     * Step 3: recreate map object
     * Verification 2: check if object contain 50 lines
     */
    @Test
    public void test5() throws Exception {
        // Step 1
        IXMLTypeFactory fa = new InvoiceXMLMapFactory();
        DMapContainerList da = new DMapContainerList();
        for (int i = 0; i < 50; i++) {
            DContainer li = da.addToLines();
            li.put(LINEDESC, "Purchase of something");
            li.put(LINENO, new Long(i));
        }
        InputStream ii = GetProp.getXMLFile("xsd", PATTERNNAME);
        // Step 2
        String xml = CreateXML.constructXMLFile(fa, ii, da);
        assertNotNull(xml);

        // Verification 1
        validate(xml);

        // Step 3
        DMapContainerList d1 = new DMapContainerList();
        ii = GetProp.getXMLFile("xsd", PATTERNNAME);
        ChangeXMLToMap.constructMapFromXML(fa, d1, xml, ii);
        // Verification 2
        assertEquals(50, d1.getdLines().size());
        int i = 0;
        for (DContainer ma : d1.getdLines()) {
            String desc = (String) ma.get(LINEDESC);
            Long in = (Long) ma.get(LINENO);
            assertEquals("Purchase of something", desc);
            assertEquals(i, in.longValue());
            i++;
        }
    }
    
    /*
     * Test case 6:  verify payment method
     */
    @Test
    public void test6() throws Exception {
        verify(PAYMENTMETHOD, InvoiceXMLMapFactory.PAYMENT, "CreditCard");
    }

    /*
     * Test case 7:  verify person title
     */
    @Test
    public void test7() throws Exception {
        verify(PERSONTITLE, InvoiceXMLMapFactory.TITLE, "Mrs");
    }

}
