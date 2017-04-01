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
package com.jythonui.server.mail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;

import com.jythonui.server.IMailGet;
import com.jythonui.server.UtilHelper;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;

public class GetMail extends UtilHelper {

    private GetMail() {

    }

    private static class ResMail implements IMailGet.IResMail {

        private String errMess;
        private int no;
        private List<IMailGet.IMailNote> list;

        @Override
        public String getErrMess() {
            return errMess;
        }

        @Override
        public int getNo() {
            return no;
        }

        @Override
        public List<IMailGet.IMailNote> getList() {
            return list;
        }

    }

    private static class CMailToSend implements IMailGet.IMailNote {

        final private String header;
        final private String content;
        final private String from;
        final private boolean text;
        final private boolean isSeen;
        final private Date sentDate;
        final private String person;

        CMailToSend(String header, String content, String from, boolean text,
                boolean isSeen, Date sentDate, String person) {
            this.header = header;
            this.content = content;
            this.from = from;
            this.text = text;
            this.isSeen = isSeen;
            this.sentDate = sentDate;
            this.person = person;
        }

        @Override
        public String getHeader() {
            return header;
        }

        @Override
        public String getContent() {
            return content;
        }

        @Override
        public String getFrom() {
            return from;
        }

        @Override
        public boolean isText() {
            return text;
        }

        @Override
        public boolean isIsSeen() {
            return isSeen;
        }

        @Override
        public Date getSentDate() {
            return sentDate;
        }

        @Override
        public String getPerson() {
            return person;
        }

    }

    private static IMailGet.IResMail ingetMail(Session session, int fromN,
            int toN) throws MessagingException, IOException {

        ResMail res = new ResMail();
        res.list = new ArrayList<IMailGet.IMailNote>();

        Properties prop = session.getProperties();

        if (prop.getProperty("debug") != null)
            session.setDebug(true);

        Store store = session.getStore();
        String host = prop.getProperty("host");
        String user = prop.getProperty("user");
        String password = prop.getProperty("password");

        // Connect to the current host using the specified username and
        // password.
        store.connect(host, user, password);

        // Create a Folder object corresponding to the given name.
        Folder folder = store.getFolder("inbox");

        // Open the Folder.
        folder.open(Folder.READ_ONLY);

        Message[] message;
        res.no = folder.getMessageCount();
        if (toN == -1)
            return res;
        if (fromN == -1)
            message = folder.getMessages();
        else
            message = folder.getMessages(res.no - toN, res.no - fromN);

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

            // extract address part only without personal name
            Address from = me.getFrom()[0];
            InternetAddress f = (InternetAddress) from;
            boolean isSeen = me.isSet(Flags.Flag.SEEN);
            CMailToSend ma = new CMailToSend(me.getSubject(), s,
                    f.getAddress(), !textIsHtml, isSeen, me.getSentDate(),
                    f.getPersonal());
            res.list.add(0, ma);
            // res.list.add(ma);
        }
        folder.close(true);
        store.close();
        return res;
        // return liM;
    }

    public static IMailGet.IResMail getMail(Session session, int fromN, int toN) {
        try {
            return ingetMail(session, fromN, toN);
        } catch (MessagingException | IOException e) {
            severe(L().getMess(IErrorCode.ERRORCODE107,
                    ILogMess.ERRORWHILEREADMAILLIST), e);
            ResMail res = new ResMail();
            res.errMess = e.getMessage();
            return res;
        }
    }

}
