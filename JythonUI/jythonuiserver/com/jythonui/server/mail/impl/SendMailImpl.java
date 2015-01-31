/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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

import java.util.ArrayList;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.mail.Session;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.jythonui.server.IConsts;
import com.jythonui.server.IMailSend;
import com.jythonui.server.mail.SendMail;
import com.jythonui.server.storage.blob.IBlobHandler;

public class SendMailImpl implements IMailSend {

    private final Session sendsession;
    private final IBlobHandler iBlob;

    @Inject
    public SendMailImpl(@Named(IConsts.SENDMAIL) Session sendsession,
            IBlobHandler iBlob) {
        this.sendsession = sendsession;
        this.iBlob = iBlob;
    }

    private class Attach implements SendMail.IAttachment {

        private final AttachElem a;
        private final MimetypesFileTypeMap mim = new MimetypesFileTypeMap();

        Attach(AttachElem a) {
            this.a = a;
        }

        @Override
        public String getFileName() {
            return a.getFileName();
        }

        @Override
        public byte[] getAttach() {
            return iBlob.findBlob(a.getRealM(), a.getBlobId());
        }

        @Override
        public String getContentType() {
            return mim.getContentType(getFileName());
        }

    }

    @Override
    public String postMail(boolean text, String[] recipients, String subject,
            String message, String from, List<AttachElem> aList) {
        List<SendMail.IAttachment> aaList = null;
        if (aList != null && !aList.isEmpty()) {
            aaList = new ArrayList<SendMail.IAttachment>();
            for (AttachElem a : aList)
                aaList.add(new Attach(a));
        }
        return SendMail.postMail(sendsession, text, recipients, subject,
                message, from, aaList);
    }

}
