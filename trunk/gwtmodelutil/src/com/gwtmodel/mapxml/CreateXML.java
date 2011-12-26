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
 * @author hotel
 * 
 */
public class CreateXML {

    private CreateXML() {
    }

    private static void removeAttrs(Document doc, String attr)
            throws XPathExpressionException {
        // remove type attributes
        XPathFactory xPathF = XPathFactory.newInstance();
        XPath xPath = xPathF.newXPath();
        XPathExpression xPathE = xPath.compile("//*[@" + attr + "]");
        Object res = xPathE.evaluate(doc, XPathConstants.NODESET);
        NodeList nlist = (NodeList) res;
        for (int i = 0; i < nlist.getLength(); i++) {
            Node n = nlist.item(i);
            NamedNodeMap map = n.getAttributes();
            map.removeNamedItem(attr);
        }

    }

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

    private static void modifyNode(IXMLTypeFactory fa, String prefix,
            Document doc, IDataContainer d) throws XPathExpressionException {
        for (String e : d.getKeys()) {
            String expression = e;
            String ee;
            if (prefix != null) {
                ee = prefix + expression;
            } else {
                ee = expression;
            }
            Node n = findSingleNode(doc, ee);
            String attr = null;
            if (n.hasAttributes()) {
                Node a = n.getAttributes().getNamedItem(IXMLTypeFactory.TYPE);
                if (a != null) {
                    attr = a.getTextContent();
                }
                Node nilnamed = n.getAttributes().getNamedItemNS(
                        XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, "nil");
                if (nilnamed != null) {
                    n.getAttributes().removeNamedItemNS(
                            XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, "nil");
                }
                Node nil = n.getAttributes().getNamedItem("xsi:nil");
                if (nil != null) {
                    n.getAttributes().removeNamedItem("xsi:nil");
                }
            }
            n.setTextContent(fa.toS(attr, d.get(e)));
        }

    }

    public static String constructXMLFile(IXMLTypeFactory fa,
            InputStream fPattern, DataMapList d) throws SAXException,
            IOException, ParserConfigurationException,
            XPathExpressionException, TransformerException {
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
        Document doc = docBuilder.parse(fPattern);

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
        if (!d.getdLines().isEmpty()) {
            for (int i = 0; i < d.getdLines().size(); i++) {
                Node newChild = child.cloneNode(true);
                n.appendChild(newChild);
                String prefix = fa.getLinesTag() + "/" + fa.getLineTag() + "["
                        + (i + 1) + "]/";
                modifyNode(fa, prefix, doc, (IDataContainer) d.getdLines().get(i));
            }
        }

        modifyNode(fa, null, doc, d.getdFields());

        // remove type attributes
        removeAttrs(doc, IXMLTypeFactory.TYPE);

        DOMSource sou = new DOMSource(doc);
        TransformerFactory factory = TransformerFactory.newInstance();
        StringWriter writer = null;
        Transformer transformer = factory.newTransformer();
        writer = new StringWriter();
        Result result = new StreamResult(writer);
        transformer.transform(sou, result);
        writer.close();
        return writer.toString();
    }

}
