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
package com.gwthotel.admin.xmlhelper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.gwthotel.mess.IHError;
import com.gwthotel.mess.IHMess;
import com.gwthotel.shared.PropDescription;
import com.jython.ui.shared.SaxUtil;
import com.jython.ui.shared.UtilHelper;
import com.jython.ui.shared.resource.IReadResource;
import com.jython.ui.shared.resource.IReadResourceFactory;
import com.jythonui.server.getmess.IGetLogMess;

public abstract class ReadXMLHelper<T extends PropDescription> extends
        UtilHelper {

    private final IGetLogMess lMess;
    private final String[] constList;
    private final String[] tagList;
    private List<T> rList = null;
    private final IReadResource iRead;

    private static final int XMLFILENAME = 0;
    private static final int ALLTAG = 1;
    private static final int ONETAG = 2;
    private static final int XMLSTRING = 3;

    protected abstract T constructT();

    protected ReadXMLHelper(IGetLogMess lMess, String[] constList,
            String[] tagList, IReadResourceFactory iFactory) {
        this.lMess = lMess;
        this.constList = constList;
        this.tagList = tagList;
        this.iRead = iFactory.constructLoader(ReadXMLHelper.class.getClassLoader());
    }

    private URL getURL() {
//        URL ur = ReadXMLHelper.class.getClassLoader().getResource(
//                ISharedConsts.RESOURCES + "/" + constList[XMLFILENAME]);
//        return ur;
        return iRead.getRes(constList[XMLFILENAME]);
    }

    private InputSource getXML() throws IOException {
        if (constList.length == 3) {
//            InputStream s = new FileInputStream(getURL().getFile());
            InputStream s = getURL().openStream();
            return new InputSource(s);
        } else {
            String xml = constList[XMLSTRING];
            return new InputSource(new StringReader(xml));

        }
    }

    private class MyHandler extends DefaultHandler {

        private List<T> rrList = new ArrayList<T>();
        private boolean started = false;
        private T role = null;
        private StringBuffer buf;

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
                role = constructT();
                SaxUtil.readAttr(role.getMap(), attributes, tagList);
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
            SaxUtil.readVal(role.getMap(), qName, tagList, buf);
        }

        @Override
        public void characters(char ch[], int start, int length)
                throws SAXException {
            buf.append(ch, start, length);
        }
    }

    private List<T> readRoles() throws ParserConfigurationException,
            SAXException, FileNotFoundException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser;
        saxParser = factory.newSAXParser();
        MyHandler ma = new MyHandler();
        saxParser.parse(getXML(), ma);
        return ma.rrList;
    }

    public List<T> getList() {
        if (rList == null) {
            try {
                rList = readRoles();
            } catch (ParserConfigurationException | SAXException | IOException e) {
                errorLog(lMess.getMess(IHError.HERROR001,
                        IHMess.READROLESERROR, constList[ALLTAG]), e);
            }
        }
        // replace names
        return rList;
    }

}
