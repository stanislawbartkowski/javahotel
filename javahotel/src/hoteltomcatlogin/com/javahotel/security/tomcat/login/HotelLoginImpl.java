/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
package com.javahotel.security.tomcat.login;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.logging.Level;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.javahotel.dbres.exceptions.HotelException;
import com.javahotel.dbres.log.HLog;
import com.javahotel.dbres.resources.GetMess;
import com.javahotel.dbres.resources.GetProp;
import com.javahotel.dbres.resources.IMess;
import com.javahotel.dbutil.prop.ReadProperties;
import com.javahotel.remoteinterfaces.PasswordT;
import com.javahotel.security.login.HotelLoginP;
import com.javahotel.security.login.IHotelLogin;
import com.javahotel.security.login.IHotelLoginJDBC;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class HotelLoginImpl implements IHotelLogin {

    private IHotelLoginJDBC jlogin;

    private String passwordFile() {
        // is defined also for JBoss
        String classPath = System.getProperty("CATALINA_BASE");
        if (classPath == null) {
            classPath = System.getProperty("catalina.base");
        }
        if (classPath == null) {
            return null;
        }
        String co = classPath + File.separator + "conf" + File.separator
                + "hoteladmin.xml";
        return co;
    }

    private class passH extends DefaultHandler {

        private String chS = "";
        private boolean okU = false;
        private boolean okUser = false;
        private final String user;
        private final PasswordT pass;

        passH(final String p1, final PasswordT p2) {
            super();
            user = p1;
            pass = p2;
        }

        @Override
        public void characters(final char[] ch, final int start,
                final int length) {
            for (int i = 0; i < length; i++) {
                char cha = ch[start + i];
                if (Character.isWhitespace(cha)) {
                    continue;
                }
                chS += cha;
            }
        }

        @Override
        public void endElement(final String uri, final String localName,
                final String qName) {
            if (qName.equals(IMess.userTAG)) {
                if (chS.equals(user)) {
                    okU = true;
                    chS = "";
                    return;
                }
                okU = false;
            }
            if (qName.equals(IMess.passwdTAG)) {
                if (okU && chS.equals(pass.getPassword())) {
                    okUser = true;
                }
            }
            chS = "";
        }
    }

    public HotelLoginP loginadmin(final String puser,
            final PasswordT ppassword, final Map<String, String> prop) {
        try {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();
            passH pa = new passH(puser, ppassword);
            String fName = passwordFile();
            if (fName == null) {
                InputStream iS = ReadProperties.getInputStream(GetProp
                        .getResourceName("hoteladmin.xml"));
                saxParser.parse(iS, pa);
            } else {
                File f = new File(fName);
                saxParser.parse(f, pa);
            }
            if (pa.okUser) {
                return new HotelLoginP(puser, true);
            }
            throw new HotelException(GetMess.getM(IMess.USERPASSWORDNOTVALID));
        } catch (IOException ex) {
            HLog.getLo().log(Level.SEVERE, "", ex);
            throw new HotelException(ex);
        } catch (ParserConfigurationException ex) {
            HLog.getLo().log(Level.SEVERE, "", ex);
            throw new HotelException(ex);
        } catch (SAXException ex) {
            HLog.getLo().log(Level.SEVERE, "", ex);
            throw new HotelException(ex);
        }
    }

    public HotelLoginP loginuser(final String user, final PasswordT password,
            final Map<String, String> p) {
        return jlogin.loginuser(user, password, p);

    }

    public void setLoginJDBC(final IHotelLoginJDBC i) {
        jlogin = i;
    }
}
