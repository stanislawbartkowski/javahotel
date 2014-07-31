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
package com.gwtmodel.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;

import com.gwtmodel.table.common.CUtil;

/**
 * 
 * @author perseus
 */

// TODO: remove later

class CGetMailFrom {

    static class CMailToSend implements Serializable {

        private static final long serialVersionUID = 1L;

        private Map<String, String> box;
        private String boxName;
        private String header;
        private String content;
        private String to;
        private String from;
        private boolean text;
        private boolean isSeen;
        private Date sentDate;
        private String person;

        public void setBoxName(String boxName) {
            this.boxName = boxName;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public void setText(boolean text) {
            this.text = text;
        }

        /**
         * @return the box
         */
        public Map<String, String> getBox() {
            return box;
        }

        /**
         * @return the header
         */
        public String getHeader() {
            return header;
        }

        /**
         * @return the content
         */
        public String getContent() {
            return content;
        }

        /**
         * @return the to
         */
        public String getTo() {
            return to;
        }

        /**
         * @return the from
         */
        public String getFrom() {
            return from;
        }

        /**
         * @return the text
         */
        public boolean isText() {
            return text;
        }

        /**
         * @return the boxName
         */
        public String getBoxName() {
            return boxName;
        }

        /**
         * @param box
         *            the box to set
         */
        public void setBox(Map<String, String> box) {
            this.box = box;
        }

        /**
         * @return the isSeen
         */
        public boolean isIsSeen() {
            return isSeen;
        }

        /**
         * @param isSeen
         *            the isSeen to set
         */
        public void setIsSeen(boolean isSeen) {
            this.isSeen = isSeen;
        }

        /**
         * @return the sentDate
         */
        public Date getSentDate() {
            return sentDate;
        }

        /**
         * @param sentDate
         *            the sentDate to set
         */
        public void setSentDate(Date sentDate) {
            this.sentDate = sentDate;
        }

        /**
         * @return the person
         */
        public String getPerson() {
            return person;
        }

        /**
         * @param person
         *            the person to set
         */
        public void setPerson(String person) {
            this.person = person;
        }

    }

    @SuppressWarnings("serial")
    public static class CGetMailFromException extends Exception {

        CGetMailFromException(Throwable t) {
            super(t);
        }
    }

    public static final String HOST = "mail.in.host";
    public static final String USER = "mail.in.user";
    public static final String PASSWORD = "mail.in.password";
    public static final String MAILDEBUG = "mail.debug";
    public static final String MAILNUMB = "mail.in.numb";

    private CGetMailFrom() {
    }

    private static List<CMailToSend> ingetMail(Properties properties)
            throws NoSuchProviderException, MessagingException, IOException {

        List<CMailToSend> liM = new ArrayList<CMailToSend>();
        String host = properties.getProperty(HOST);
        String user = properties.getProperty(USER);
        String password = properties.getProperty(PASSWORD);
        String debug = properties.getProperty(MAILDEBUG);
        String numb = properties.getProperty(MAILNUMB);

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);
        if (debug != null) {
            session.setDebug(true);
        }

        Store store = session.getStore();

        // Connect to the current host using the specified username and
        // password.
        store.connect(host, user, password);

        // Create a Folder object corresponding to the given name.
        Folder folder = store.getFolder("inbox");

        // Open the Folder.
        folder.open(Folder.READ_ONLY);

        Message[] message;
        if (numb == null) {
            message = folder.getMessages();
        } else {
            int n = CUtil.getInteger(numb);
            int c = folder.getMessageCount();
            if (c <= n) {
                message = folder.getMessages();
            } else {
                message = folder.getMessages(c - n, c);
            }
        }

        // Display message.
        for (Message me : message) {

            String s = null;
            boolean textIsHtml = false;
            if (me.isMimeType("text/*")) {
                s = (String) me.getContent();
                textIsHtml = me.isMimeType("text/html");
            } else if (me.isMimeType("multipart/alternative")) {
                // prefer html text over plain text
                Multipart mp = (Multipart) me.getContent();
                for (int i = 0; i < mp.getCount(); i++) {
                    Part bp = mp.getBodyPart(i);
                    textIsHtml = bp.isMimeType("text/html");
                    if (bp.isMimeType("text/plain")) {
                        if (s == null) {
                            s = (String) bp.getContent();
                            continue;
                        }
                        if (bp.isMimeType("text/html")) {
                            s = (String) bp.getContent();
                            continue;
                        }
                    }
                }

            }
            CMailToSend ma = new CMailToSend();

            // extract address part only without personal name
            Address from = me.getFrom()[0];
            InternetAddress f = (InternetAddress) from;
            ma.setFrom(f.getAddress());
            ma.setPerson(f.getPersonal());

            ma.setHeader(me.getSubject());
            ma.setSentDate(me.getSentDate());
            ma.setText(!textIsHtml);
            ma.setContent(s);
            boolean isSeen = me.isSet(Flags.Flag.SEEN);
            ma.setIsSeen(isSeen);
            liM.add(0, ma);
        }
        folder.close(true);
        store.close();
        return liM;
    }

    public static List<CMailToSend> getMail(Properties properties)
            throws CGetMailFromException {
        try {
            List<CMailToSend> mLi = ingetMail(properties);
            return mLi;
        } catch (NoSuchProviderException e) {
            throw new CGetMailFromException(e);
        } catch (MessagingException e) {
            throw new CGetMailFromException(e);
        } catch (IOException e) {
            throw new CGetMailFromException(e);
        }
    }

    public static List<CMailToSend> getMail(Map<String, String> ma)
            throws CGetMailFromException {
        Properties prop = FileUtil.MapToProperty(ma);
        return getMail(prop);
    }
}
