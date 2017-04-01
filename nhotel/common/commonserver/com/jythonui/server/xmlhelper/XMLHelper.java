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
package com.jythonui.server.xmlhelper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.google.inject.Inject;
import com.gwtmodel.table.map.XMap;
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.SaxUtil;
import com.jythonui.server.UtilHelper;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.server.resource.IReadResource;
import com.jythonui.server.resource.IReadResourceFactory;
import com.jythonui.server.xml.IXMLHelper;
import com.jythonui.server.xml.IXMapFactory;

public class XMLHelper extends UtilHelper implements IXMLHelper {

    private final IGetLogMess lMess;
    private final IReadResource iRead;

    private static final int XMLFILENAME = 0;
    private static final int ALLTAG = 1;
    private static final int ONETAG = 2;
    private static final int XMLSTRING = 3;

    @Inject
    public XMLHelper(@Named(ISharedConsts.JYTHONMESSSERVER) IGetLogMess lMess,
            IReadResourceFactory iFactory) {
        this.lMess = lMess;
        this.iRead = iFactory.constructLoader(XMLHelper.class.getClassLoader());
    }

    private URL getURL(String[] constList) {
        return iRead.getRes(constList[XMLFILENAME]);
    }

    private InputSource getXML(String[] constList) throws IOException {
        if (constList.length == 3) {
            InputStream s = getURL(constList).openStream();
            return new InputSource(s);
        } else {
            String xml = constList[XMLSTRING];
            return new InputSource(new StringReader(xml));

        }
    }

    private class MyHandler extends DefaultHandler {

        private List<XMap> rrList = new ArrayList<XMap>();
        private boolean started = false;
        private XMap role = null;
        private StringBuffer buf;

        private final String[] constList;
        private final String[] tagList;
        private final IXMapFactory xFactory;

        MyHandler(String[] constList, String[] tagList, IXMapFactory xFactory) {
            this.constList = constList;
            this.tagList = tagList;
            this.xFactory = xFactory;
        }

        @Override
        public void startElement(String uri, String localName, String qName,
                Attributes attributes) throws SAXException {
            buf = new StringBuffer();
            if (qName.equals(constList[ALLTAG])) {
                started = true;
                return;
            }
            if (!started)
                return;
            if (qName.equals(constList[ONETAG])) {
                role = xFactory.construct();
                SaxUtil.readAttr(role.getMap(), attributes, tagList, null);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            if (!started)
                return;
            if (qName.equals(constList[ONETAG])) {
                rrList.add(role);
                role = null;
                return;
            }
            if (role == null)
                return;
            SaxUtil.readVal(role.getMap(), qName, tagList, buf, null);
        }

        @Override
        public void characters(char ch[], int start, int length)
                throws SAXException {
            buf.append(ch, start, length);
        }
    }

    private List<XMap> readRoles(String[] constList, String[] tagList,
            IXMapFactory xFactory) throws ParserConfigurationException,
            SAXException, FileNotFoundException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser;
        saxParser = factory.newSAXParser();
        MyHandler ma = new MyHandler(constList, tagList, xFactory);
        saxParser.parse(getXML(constList), ma);
        return ma.rrList;
    }

    public List<? extends XMap> getList(String[] constList, String[] tagList,
            IXMapFactory xFactory) {
        try {
            return readRoles(constList, tagList, xFactory);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            errorLog(lMess.getMess(IErrorCode.ERRORCODE81,
                    ILogMess.XMLREADERROR, constList[ALLTAG]), e);
        }
        return null;
    }

}
