/*
 * Copyright 2016 stanislawbartkowski@gmail.com 
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
package com.jythonui.server.defa;

import java.io.IOException;
import java.util.Properties;

import javax.inject.Named;
import javax.inject.Provider;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import com.google.inject.Inject;
import com.jythonui.server.IJythonUIServerProperties;
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.ReadUTF8Properties;
import com.jythonui.server.UtilHelper;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;

public class JavaMailSessionProvider extends UtilHelper implements
        Provider<Session> {

    private final IJythonUIServerProperties iProp;
    private final IGetLogMess gMess;

    @Inject
    public JavaMailSessionProvider(
            @Named(ISharedConsts.JYTHONMESSSERVER) IGetLogMess gMess,
            IJythonUIServerProperties iProp) {
        this.gMess = gMess;
        this.iProp = iProp;
    }

    @Override
    public Session get() {
        if (iProp.getSendMailPropertiesFile() == null) {
            errorMess(gMess, IErrorCode.ERRORCODE117,
                    ILogMess.MAILBOXPROPERTYNOTDEFINED);
            return null;
        }
        try {
            Properties prop = ReadUTF8Properties.readProperties(iProp
                    .getSendMailPropertiesFile().openStream());
            final String user = prop.getProperty("mail.smtp.user");
            final String password = prop.getProperty("mail.smtp.password");
            return Session.getInstance(prop, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(user, password);
                }
            });
        } catch (IOException e) {
            errorMess(gMess, IErrorCode.ERRORCODE105,
                    ILogMess.MAILERRORPROPERTIES, e);
            return null;
        }
    }

}
