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
package com.jythonui.server.mail.impl;

import javax.mail.Session;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.jythonui.server.IConsts;
import com.jythonui.server.IMailSend;
import com.jythonui.server.mail.GetMail;
import com.jythonui.server.mail.SendMail;

public class SendMailImpl implements IMailSend {

    private final Session sendsession;

    @Inject
    public SendMailImpl(@Named(IConsts.SENDMAIL) Session sendsession) {
        this.sendsession = sendsession;
    }

    @Override
    public String postMail(boolean text, String[] recipients, String subject,
            String message, String from) {
        return SendMail.postMail(sendsession, text, recipients, subject,
                message, from);
    }

}
