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
package com.jythonui.server.xmlmap;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.DateFormat;
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.IXMLToMap;
import com.jythonui.server.UtilHelper;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;

public class XMLMap extends UtilHelper implements IXMLToMap {

    private final IGetLogMess gMess;

    @Inject
    public XMLMap(@Named(ISharedConsts.JYTHONMESSSERVER) IGetLogMess gMess) {
        this.gMess = gMess;
    }

    private class Result implements IXMLToMap.IMapResult {

        private final Map<String, Object> rMap = new HashMap<String, Object>();
        private final List<Map<String, Object>> lMap = new ArrayList<Map<String, Object>>();

        @Override
        public Map<String, Object> getMap() {
            return rMap;
        }

        @Override
        public List<Map<String, Object>> getList() {
            return lMap;
        }

    }

    private class MyHandler extends DefaultHandler {
        private final Result res = new Result();
        private boolean listnow = false;
        private StringBuffer buf;
        private String type;
        private Map<String, Object> curr = res.rMap;

        @Override
        public void startElement(String uri, String localName, String qName,
                Attributes attributes) throws SAXException {
            buf = new StringBuffer();
            type = attributes.getValue(IMapValues.TYPE);
            if (qName.equals(IMapValues.LIST))
                listnow = true;
            if (qName.equals(IMapValues.ELEM) && listnow)
                curr = new HashMap<String, Object>();

        }

        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            if (qName.equals(IMapValues.ELEM)) {
                if (listnow)
                    res.lMap.add(curr);
                return;
            }
            if (qName.equals(IMapValues.ROOT))
                return;
            if (qName.equals(IMapValues.LIST))
                return;

            String value = buf.toString();
            Object v = value;
            if (CUtil.EqNS(type, IMapValues.BOOL)) {
                Boolean b;
                if (value.equals("1"))
                    b = new Boolean(true);
                else
                    b = new Boolean(false);
                v = b;
            } else if (CUtil.EmptyS(value)) {
                curr.put(qName, null);
                return;
            }
            if (CUtil.EqNS(type, IMapValues.LONG)) {
                Long l = Long.parseLong(value);
                v = l;
            }
            if (CUtil.EqNS(type, IMapValues.DECIMAL)) {
                Double d = Double.parseDouble(value);
                v = d;
            }
            if (CUtil.EqNS(type, IMapValues.DATE)) {
                Date d = DateFormat.toD(value, false);
                if (d == null)
                    // try to recognize date in format YYYY-MM-DD also
                    d = DateFormat.toD(value.replace('-', '/'), false);
                v = d;
            }
            curr.put(qName, v);
        }

        @Override
        public void characters(char ch[], int start, int length)
                throws SAXException {
            buf.append(ch, start, length);
        }

    }

    @Override
    public IMapResult getMap(String xml) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser;
        MyHandler ha = new MyHandler();
        try {
            saxParser = factory.newSAXParser();
            InputSource sou = new InputSource(new StringReader(xml));
            saxParser.parse(sou, ha);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            String mess = gMess.getMess(IErrorCode.ERRORCODE73,
                    ILogMess.ERRORWHILEREADINGXML);
            errorLog(mess, e);
            return null;
        }
        return ha.res;
    }
}
