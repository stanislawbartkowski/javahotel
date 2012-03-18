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

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.event.TransportEvent;
import javax.mail.event.TransportListener;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * 
 * @author perseus
 */
public class SendMail {

    public static final String PROTOCOL = "mail.transport.protocol";
    public static final String USER = "mail.smtp.user";
    public static final String PASSWORD = "mail.smtp.password";
    public static final String HOST = "mail.smtp.host";
    public static final String MAIL_FROM = "mail.from";
    public static final String PROP_FILE_NAME = "property.file.name";
    private static final String GAEMAIL = "gae";
    public static final String MAILDEBUG = "mail.debug";

    private SendMail() {
    }

    private static class T implements TransportListener {

        private String errMess = null;

        public void messageDelivered(TransportEvent e) {
        }

        public void messageNotDelivered(TransportEvent e) {
            errMess = "Poczta nie dostarczona do skrzynki";
        }

        public void messagePartiallyDelivered(TransportEvent e) {
            errMess = "Poczta dostarczona do niektórych odbiorców";
        }

        /**
         * @return the errMess
         */
        public String getErrMess() {
            return errMess;
        }
    }

    private static void postmail(T li, boolean text, Properties props,
            String recipients[], String subject, String message, String from)
            throws MessagingException {

        // create some properties and get the default Session
        Session session = Session.getDefaultInstance(props, null);
        String debug = props.getProperty(MAILDEBUG);

        if (debug != null) {
            session.setDebug(true);
        }

        // create a message
        Message msg = new MimeMessage(session);

        // set the from and to address
        if (from == null) {
            from = props.getProperty(MAIL_FROM);
        }
        InternetAddress addressFrom = new InternetAddress(from);
        msg.setFrom(addressFrom);

        InternetAddress[] addressTo = new InternetAddress[recipients.length];
        for (int i = 0; i < recipients.length; i++) {
            addressTo[i] = new InternetAddress(recipients[i]);
        }
        msg.setRecipients(Message.RecipientType.TO, addressTo);

        // Setting the Subject and Content Type
        msg.setSubject(subject);
        if (text) {
            msg.setContent(message, "text/plain;charset=UTF-8");
        } else {
            msg.setContent(message, "text/html;charset=UTF-8");
        }

        String protocol = props.getProperty(PROTOCOL);
        if (protocol.equalsIgnoreCase(GAEMAIL)) {
            Transport.send(msg);
        } else {

            Transport transport = session.getTransport(props
                    .getProperty(PROTOCOL));
            transport.connect(props.getProperty(HOST), props.getProperty(USER),
                    props.getProperty(PASSWORD));
            transport.addTransportListener(li);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
        }
    }

    public static String postMail(boolean text, Properties props,
            String recipients[], String subject, String message, String from) {
        T li = new T();
        try {
            postmail(li, text, props, recipients, subject, message, from);
        } catch (MessagingException ex) {
            // PerseusJavaUtil.getLog().log(Level.SEVERE, null, ex);
            return ex.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
        if (li.getErrMess() != null) {
            return li.getErrMess();
        }
        return null;
    }

    public static String postMail(boolean text, Properties props,
            String recipient, String subject, String message, String from) {
        return postMail(text, props, new String[] { recipient }, subject,
                message, from);
    }
}
