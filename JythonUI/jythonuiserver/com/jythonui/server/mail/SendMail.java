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
package com.jythonui.server.mail;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.event.TransportEvent;
import javax.mail.event.TransportListener;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.jythonui.server.UtilHelper;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.holder.SHolder;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;

/**
 * 
 * @author perseus
 */
public class SendMail extends UtilHelper {

    private SendMail() {
    }
    
    private static IGetLogMess L() {
        return SHolder.getM();
    }

    private static class T implements TransportListener {

        private String errMess = null;

        public void messageDelivered(TransportEvent e) {
        }

        public void messageNotDelivered(TransportEvent e) {
            errMess = L().getMessN(ILogMess.MAILNOTDELIVERED);
        }

        public void messagePartiallyDelivered(TransportEvent e) {
            errMess = L().getMessN(ILogMess.MAILDELIVEREDONLYTOSOME);
        }

        /**
         * @return the errMess
         */
        public String getErrMess() {
            return errMess;
        }
    }

    private static void postmail(Session session, T li, boolean text,
            String recipients[], String subject, String message, String from)
            throws MessagingException {

        // create a message
        Message msg = new MimeMessage(session);
            
        // set the from and to address
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

        Transport.send(msg);
    }

    public static String postMail(Session session, boolean text,
            String recipients[], String subject, String message, String from) {
        T li = new T();
        try {
            postmail(session, li, text, recipients, subject, message, from);
        } catch (MessagingException ex) {
            severe(L().getMess(IErrorCode.ERRORCODE103, ILogMess.MAILDELIVERERROR));
            return ex.getMessage();
        } catch (Exception e) {
            severe(L().getMess(IErrorCode.ERRORCODE104, ILogMess.MAILDELIVERERROR));
            return e.getMessage();
        }
        if (li.getErrMess() != null) {
            return li.getErrMess();
        }
        return null;
    }

}
