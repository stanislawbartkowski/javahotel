/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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
package com.gwthotel.admin.roles;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.gwthotel.admin.IGetHotelRoles;
import com.gwthotel.admin.Role;
import com.gwthotel.mess.IHError;
import com.gwthotel.mess.IHMess;
import com.gwthotel.shared.IHotelConsts;
import com.jythonui.server.SaxUtil;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.JythonUIFatal;

public class GetHotelRoles implements IGetHotelRoles {

    private final IGetLogMess lMess;

    static final private Logger log = Logger.getLogger(GetHotelRoles.class
            .getName());

    static private void error(String mess, Throwable e) {
        log.log(Level.SEVERE, mess);
        throw new JythonUIFatal(mess, e);
    }

    @Inject
    public GetHotelRoles(@Named(IHotelConsts.MESSNAMED) IGetLogMess lMess) {
        this.lMess = lMess;
    }

    private List<Role> rList = null;

    private static final String ROLES = "roles//roles.xml";
    private static final String ROLESTAG = "roles";
    private static final String ROLETAG = "role";

    static private URL getURL() {
        URL ur = GetHotelRoles.class.getClassLoader().getResource(
                ICommonConsts.RESOURCES + "/" + ROLES);
        return ur;
    }

    static private InputStream getXML() throws FileNotFoundException {
        InputStream s = new FileInputStream(getURL().getFile());
        return s;
    }

    private static class MyHandler extends DefaultHandler {

        private List<Role> rrList = new ArrayList<Role>();
        private String[] tagList = new String[] { IHotelConsts.NAME,
                IHotelConsts.DESCRIPTION };
        private boolean started = false;
        private Role role = null;
        private StringBuffer buf;

        @Override
        public void startElement(String uri, String localName, String qName,
                Attributes attributes) throws SAXException {
            buf = new StringBuffer();
            if (qName.equals(ROLESTAG)) {
                started = true;
                return;
            }
            if (!started)
                return;
            if (qName.equals(ROLETAG)) {
                role = new Role();
                SaxUtil.readAttr(role, attributes, tagList);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            if (!started)
                return;
            if (qName.equals(ROLETAG)) {
                rrList.add(role);
                role = null;
                return;
            }
            SaxUtil.readVal(role, qName, tagList, buf);
        }

        @Override
        public void characters(char ch[], int start, int length)
                throws SAXException {
            buf.append(ch, start, length);
        }
    }

    private static List<Role> readRoles() throws ParserConfigurationException,
            SAXException, FileNotFoundException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser;
        saxParser = factory.newSAXParser();
        MyHandler ma = new MyHandler();
        saxParser.parse(getXML(), ma);
        return ma.rrList;
    }

    @Override
    public List<Role> getList() {
        if (rList == null) {
            try {
                rList = readRoles();                
            } catch (ParserConfigurationException | SAXException | IOException e) {
                error(lMess.getMess(IHError.HERROR001, IHMess.READROLESERROR,
                        ROLES), e);
            }
        }
        // replace names
        return rList;
    }

}
