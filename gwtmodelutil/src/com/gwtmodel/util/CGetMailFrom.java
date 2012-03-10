/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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

import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.mailcommon.CMailToSend;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;

/**
 *
 * @author perseus
 */
public class CGetMailFrom {

    public static class CGetMailFromException extends Exception {

        CGetMailFromException(Throwable t) {
            super(t);
        }
    }
    public static String HOST = "mail.in.host";
    public static String USER = "mail.in.user";
    public static String PASSWORD = "mail.in.password";
    public static String MAILDEBUG = "mail.debug";
    public static String MAILNUMB = "mail.in.numb";

    private CGetMailFrom() {
    }

    private static List<CMailToSend> ingetMail(Properties properties) throws NoSuchProviderException, MessagingException, IOException {

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

        //Connect to the current host using the specified username and password.
        store.connect(host, user, password);

        //Create a Folder object corresponding to the given name.
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

    public static List<CMailToSend> getMail(Properties properties) throws CGetMailFromException {
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

    public static List<CMailToSend> getMail(Map<String, String> ma) throws CGetMailFromException {
        Properties prop = FileUtil.MapToProperty(ma);
        return getMail(prop);
    }
}
