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
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.gwtmodel.table.mapxml.DataMapList;
import com.gwtmodel.table.mapxml.IDataContainer;

/**
 * @author hotel Utility class for transforming data map to XML file
 */
public class CreateXML {

    private CreateXML() {
    }

    /**
     * Remove attribute form Document
     * 
     * @param doc
     *            Document
     * @param attr
     *            Attribute name to be removed
     * @throws XPathExpressionException
     */
    private static void removeAttrs(Document doc, String attr)
            throws XPathExpressionException {
        // remove type attributes
        XPathFactory xPathF = XPathFactory.newInstance();
        XPath xPath = xPathF.newXPath();
        // Create XPath selector to be removed
        XPathExpression xPathE = xPath.compile("//*[@" + attr + "]");
        Object res = xPathE.evaluate(doc, XPathConstants.NODESET);
        NodeList nlist = (NodeList) res;
        for (int i = 0; i < nlist.getLength(); i++) {
            Node n = nlist.item(i);
            NamedNodeMap map = n.getAttributes();
            // remove
            map.removeNamedItem(attr);
        }

    }

    /**
     * Find single element in the Document or null if not exist
     * 
     * @param doc
     *            Document
     * @param expre
     *            XPath selector
     * @return Node (or null)
     * @throws XPathExpressionException
     */
    private static Node findSingleNode(Document doc, String expre)
            throws XPathExpressionException {
        XPathFactory xPathF = XPathFactory.newInstance();
        XPath xPath = xPathF.newXPath();
        XPathExpression xPathE = xPath.compile(expre);
        Object result = xPathE.evaluate(doc, XPathConstants.NODESET);
        NodeList nlist = (NodeList) result;
        if (nlist.getLength() == 0) {
            return null;
        }
        return nlist.item(0);
    }

    /**
     * Modify node in Document by adding node text to set of elements
     * 
     * @param fa
     *            IXMLTypeFactory
     * @param prefix
     *            Prefix for all elements to be modified (or null)
     * @param doc
     *            Document
     * @param d
     *            IDataContainer with data to be inserted
     * @throws XPathExpressionException
     */
    private static void modifyNode(IXMLTypeFactory fa, String prefix,
            Document doc, IDataContainer d) throws XPathExpressionException {
        for (String e : d.getKeys()) {
            // scans through all values in IDataContainer
            String expression = e;
            String ee;
            if (prefix != null) {
                ee = prefix + expression;
            } else {
                ee = expression;
            }
            // Find element (important: element should exist !)
            Node n = findSingleNode(doc, ee);
            String attr = null;
            if (n.hasAttributes()) {
                // two attributes are modified: 'nil' and 'type'
                Node a = n.getAttributes().getNamedItem(IXMLTypeFactory.TYPE);
                // get type attributes
                if (a != null) {
                    attr = a.getTextContent();
                }
                // look for 'nil' attribute
                Node nilnamed = n.getAttributes().getNamedItemNS(
                        XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, "nil");
                // if 'nil' exists then remove
                if (nilnamed != null) {
                    n.getAttributes().removeNamedItemNS(
                            XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, "nil");
                }
                Node nil = n.getAttributes().getNamedItem("xsi:nil");
                if (nil != null) {
                    n.getAttributes().removeNamedItem("xsi:nil");
                }
            }
            // insert value into element text
            n.setTextContent(fa.toS(attr, d.get(e)));
        }

    }

    /**
     * Only one public method, returns XML string basing on DataMapList data
     * 
     * @param fa
     *            IMLTypeFactopry
     * @param fPattern
     *            Pattern XML (in shape of InputStream)
     * @param d
     *            DataMapList
     * @return XML string
     * 
     * 
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws XPathExpressionException
     * @throws TransformerException
     */
    public static String constructXMLFile(IXMLTypeFactory fa,
            InputStream fPattern, DataMapList<?> d) throws SAXException,
            IOException, ParserConfigurationException,
            XPathExpressionException, TransformerException {
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
        // create Document from pattern XML
        Document doc = docBuilder.parse(fPattern);

        // remove at the beginning 'lines' section, will be created later
        Node n = findSingleNode(doc, fa.getLinesTag());
        NodeList nChild = n.getChildNodes();
        Node child = null;
        for (int k = 0; k < nChild.getLength(); k++) {
            child = nChild.item(k);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                n.removeChild(child);
                break;
            }
        }

        // create 'lines' section (if exists in source DataMapList
        if (!d.getdLines().isEmpty()) {
            for (int i = 0; i < d.getdLines().size(); i++) {
                Node newChild = child.cloneNode(true);
                n.appendChild(newChild);
                String prefix = fa.getLinesTag() + "/" + fa.getLineTag() + "["
                        + (i + 1) + "]/";
                // create single line and add to 'lines' section
                modifyNode(fa, prefix, doc,
                        (IDataContainer) d.getdLines().get(i));
            }
        }

        // create main body of the XML
        modifyNode(fa, null, doc, d.getdFields());

        // remove type attributes
        removeAttrs(doc, IXMLTypeFactory.TYPE);

        // transform Document to string
        DOMSource sou = new DOMSource(doc);
        TransformerFactory factory = TransformerFactory.newInstance();
        StringWriter writer = null;
        Transformer transformer = factory.newTransformer();
        writer = new StringWriter();
        Result result = new StreamResult(writer);
        transformer.transform(sou, result);
        writer.close();
        // return result
        return writer.toString();
    }

}
