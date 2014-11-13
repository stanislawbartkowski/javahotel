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
package com.jythonui.server.mailsave.impl;

import java.util.List;

import com.google.inject.Inject;
import com.jythonui.server.IMailSend;
import com.jythonui.server.IMailSend.AttachElem;
import com.jythonui.server.IMailSendSave;
import com.jythonui.server.holder.Holder;
import com.jythonui.server.mail.INoteStorage;
import com.jythonui.server.mail.Note;
import com.jythonui.server.mail.NoteAttach;

public class MailSaveImpl implements IMailSendSave {

    private final IMailSend iSend;
    private final INoteStorage iStor;

    @Inject
    public MailSaveImpl(IMailSend iSend, INoteStorage iStor) {
        this.iSend = iSend;
        this.iStor = iStor;
    }

    @Override
    public Note postMail(boolean text, String[] recipients, String subject,
            String message, String from, List<AttachElem> aList) {
        String res = iSend.postMail(text, recipients, subject, message, from,
                aList);
        Note no = new Note();
        no.setDescription(subject);
        no.setFrom(from);
        for (String r : recipients)
            no.getRecipientsList().add(r);
        no.setText(text);
        no.setContent(message);
        if (aList != null)
            for (AttachElem a : aList) {
                NoteAttach ata = new NoteAttach();
                ata.setRealm(a.getRealM());
                ata.setBlobKey(a.getBlobId());
                ata.setFileName(a.getFileName());
                no.getaList().add(ata);
            }
        if (res != null)
            no.setSendResult(res);
        // important assignment : otherwise EJB does not work
        no = iStor.addElem(Holder.getO(), no);
        return no;
    }

}
