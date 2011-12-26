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
import java.io.StringReader;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.mapxml.DataMapList;
import com.gwtmodel.table.mapxml.IDataContainer;

/**
 * @author hotel Utility class - changes XML file into DataMapList map
 */
public class ChangeXMLToMap {

    private ChangeXMLToMap() {

    }

    /**
     * Implementation of SAX handler
     * 
     * @author hotel
     * 
     */
    private static class MyHandler extends DefaultHandler {

        private StringBuffer buf;
        /** stack of nested XML tags. */
        private final Stack<String> st = new Stack<String>();
        private final DataMapList<?> d;
        private final IXMLTypeFactory fa;
        private final Document pattDoc;
        private IDataContainer elem = null;
        /** if true then 'lines' section is scanned now. */
        private boolean lines = false;

        MyHandler(IXMLTypeFactory fa, DataMapList<?> d, Document pattDoc) {
            this.d = d;
            this.fa = fa;
            this.pattDoc = pattDoc;
        }

        /**
         * Create XPath pointer basing on current tag nesting as //root
         * tag/tag/....
         * 
         * @return XPath pointer
         */
        private String toPath() {
            String xp = "/";
            boolean first = true;
            for (String s : st) {
                if (!first) {
                    xp = xp + "/" + s;
                }
                first = false;
            }
            return xp;
        }

        @Override
        public void startElement(String uri, String localName, String qName,
                Attributes attributes) throws SAXException {
            // push tag to stack
            st.push(qName);
            buf = new StringBuffer();
            String tag = toPath();
            if (fa.getLinesTag().equals(tag)) {
                lines = true;
            }
            // if 'lines' section and the first tag add next line
            if (lines & fa.getLineTag().equals(qName)) {
                elem = d.addToLines();
            }
        }

        /**
         * Finds type attribute in pattern XML
         * 
         * @return Type string or null meaning no type is defined (default
         *         string)
         */
        private String getType() {
            XPathFactory xPathF = XPathFactory.newInstance();
            XPath xPath = xPathF.newXPath();
            XPathExpression xPathE;
            Object res = null;
            try {
                xPathE = xPath.compile(toPath() + "[@" + IXMLTypeFactory.TYPE
                        + "]");
                res = xPathE.evaluate(pattDoc, XPathConstants.NODESET);
            } catch (XPathExpressionException e) {
                e.printStackTrace();
                return null;
            }
            NodeList nlist = (NodeList) res;
            for (int i = 0; i < nlist.getLength(); i++) {
                Node n = nlist.item(i);
                NamedNodeMap map = n.getAttributes();
                Node atr = map.getNamedItem(IXMLTypeFactory.TYPE);
                return atr.getTextContent();
            }
            return null;

        }

        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            String val = buf.toString().trim();
            String tag = toPath();
            if (lines && fa.getLineTag().equals(qName)) {
                elem = null;
            } else if (lines && fa.getLinesTag().equals(tag)) {
                // end of 'lines' section
                lines = false;
            } else if (!CUtil.EmptyS(val)) {
                String attr = getType();
                Object o = fa.contruct(attr, val);
                if (lines && elem != null) {
                    elem.put(qName, o);
                } else {
                    d.getdFields().put(toPath(), o);
                }
            }
            buf = new StringBuffer();
            // delete element from tag stack
            st.pop();
        }

        @Override
        public void characters(char ch[], int start, int length)
                throws SAXException {
            buf.append(ch, start, length);
        }
    }

    /**
     * Only one public method, construct DataMapList map from XML file. In case
     * of error throw exception
     * 
     * @param fa
     *            IXMLTypeFactory
     * @param d
     *            DataMapList to be filled
     * @param sXML
     *            Source XML (as string)
     * @param fPattern
     *            InputStream with pattern XML
     * 
     * @throws ParserConfigurationException
     *             Exception in case of error
     * @throws SAXException
     * @throws IOException
     */
    public static void constructMapFromXML(IXMLTypeFactory fa,
            DataMapList<?> d, String sXML, InputStream fPattern)
            throws ParserConfigurationException, SAXException, IOException {

        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
        Document doc = docBuilder.parse(fPattern);
        StringReader s = new StringReader(sXML);
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        saxParser.parse(new InputSource(s), new MyHandler(fa, d, doc));
    }

}
