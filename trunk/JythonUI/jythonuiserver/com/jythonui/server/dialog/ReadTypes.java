/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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
package com.jythonui.server.dialog;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.jython.ui.shared.SaxUtil;
import com.jythonui.shared.ElemDescription;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.TypedefDescr;
import com.jythonui.shared.TypesDescr;

class ReadTypes {

    private static class MyHandler extends DefaultHandler {

        /** DialogFormat class being built. */
        private TypesDescr dTypes = null;
        private TypedefDescr aType = null;
        private ElemDescription bDescr = null;
        private String tagElem[] = { ICommonConsts.ID,
                ICommonConsts.DISPLAYNAME, ICommonConsts.IMPORT,
                ICommonConsts.METHOD, ICommonConsts.TYPE, ICommonConsts.COMBOID };
        private StringBuffer buf;
        private String colElem[] = { ICommonConsts.ID,
                ICommonConsts.DISPLAYNAME };
        private String[] aElem;

        @Override
        public void startElement(String uri, String localName, String qName,
                Attributes attributes) throws SAXException {
            buf = new StringBuffer();
            boolean getAttribute = false;
            if (qName.equals(ICommonConsts.TYPEDEFS)) {
                dTypes = new TypesDescr();
                bDescr = dTypes;
                aElem = tagElem;
                getAttribute = true;
            }
            if (qName.equals(ICommonConsts.TYPEDEF)) {
                aType = new TypedefDescr();
                bDescr = aType;
                aElem = tagElem;
                getAttribute = true;
            }
            if (qName.equals(ICommonConsts.COLUMN)) {
                bDescr = new FieldItem();
                aElem = colElem;
                getAttribute = true;
            }
            if (getAttribute) {
                SaxUtil.readAttr(bDescr.getMap(), attributes, aElem);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            if (bDescr == null) {
                return;
            }
            if (qName.equals(ICommonConsts.TYPEDEF)) {
                List<TypedefDescr> tList = dTypes.getTypeList();
                tList.add(aType);
                return;
            }
            if (qName.equals(ICommonConsts.COLUMN)) {
                aType.getColumns().add((FieldItem) bDescr);
                return;
            }
            SaxUtil.readVal(bDescr.getMap(), qName, aElem, buf);
        }

        @Override
        public void characters(char ch[], int start, int length)
                throws SAXException {
            buf.append(ch, start, length);
        }
    }

    static TypesDescr parseDocument(InputStream sou)
            throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser;
        saxParser = factory.newSAXParser();
        MyHandler ma = new MyHandler();
        saxParser.parse(sou, ma);
        return ma.dTypes;
    }

}
