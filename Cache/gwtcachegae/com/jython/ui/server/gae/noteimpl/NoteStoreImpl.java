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
package com.jython.ui.server.gae.noteimpl;

import javax.inject.Inject;

import com.googlecode.objectify.ObjectifyService;
import com.jython.ui.server.gae.crudimpl.CrudGaeAbstract;
import com.jython.ui.server.gae.security.entities.EMailAttach;
import com.jython.ui.server.gae.security.entities.EMailNote;
import com.jython.ui.server.gae.security.entities.EObject;
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.crud.ICrudObjectGenSym;
import com.jythonui.server.mail.INoteStorage;
import com.jythonui.server.mail.Note;
import com.jythonui.server.mail.NoteAttach;
import com.jythonui.server.newblob.IAddNewBlob;
import com.jythonui.server.storage.blob.IBlobHandler;

public class NoteStoreImpl extends CrudGaeAbstract<Note, EMailNote> implements
        INoteStorage {

    static {
        ObjectifyService.register(EMailNote.class);
    }

    private final IAddNewBlob iAdd;
    private final IBlobHandler hBlob;

    @Inject
    public NoteStoreImpl(ICrudObjectGenSym iGen, IAddNewBlob iAdd,
            IBlobHandler hBlob) {
        super(EMailNote.class, EMailNote.class.getSimpleName(), iGen);
        this.iAdd = iAdd;
        this.hBlob = hBlob;
    }

    private String createRealm(String name, EObject ho) {
        String realM = ho.getName() + "-" + ISharedConsts.MAILNOTEREALM + "-"
                + name;
        return realM;
    }

    @Override
    protected Note constructProp(EObject ho, EMailNote e) {
        Note no = new Note();
        no.setContent(e.getContent());
        no.setDescription(e.getDescription());
        no.setName(e.getName());
        no.setId(e.getId());
        no.setSendResult(e.getSendRes());
        no.setFrom(e.getFrom());
        no.setText(e.isText());
        no.getRecipientsList().addAll(e.getRecipientsList());
        String realM = createRealm(e.getName(), ho);
        for (EMailAttach a : e.getaList()) {
            NoteAttach ata = new NoteAttach();
            ata.setRealm(realM);
            ata.setBlobKey(a.getBlobId());
            ata.setFileName(a.getFileName());
            no.getaList().add(ata);
        }
        return no;
    }

    @Override
    protected EMailNote constructE() {
        return new EMailNote();
    }

    @Override
    protected void toE(EObject ho, EMailNote e, Note t) {
        String realM = createRealm(e.getName(), ho);
        e.setDescription(t.getName());
        e.setName(t.getName());
        e.setContent(t.getContent());
        e.setText(t.isText());
        e.setSendRes(t.getSendResult());
        e.getRecipientsList().addAll(t.getRecipientsList());
        e.setFrom(t.getFrom());
        for (NoteAttach a : t.getaList()) {
            EMailAttach ata = new EMailAttach();
            if (!realM.equals(a.getRealm())) {
                byte[] val = hBlob.findBlob(a.getRealm(), a.getBlobKey());
                String key = iAdd.addNewBlob(realM, t.getName(), val);
                ata.setBlobId(key);
            } else
                ata.setBlobId(a.getBlobKey());
            ata.setFileName(a.getFileName());
            e.getaList().add(ata);
        }
    }

    @Override
    protected void beforeDelete(EObject ho, EMailNote elem) {
        String realM = createRealm(elem.getName(), ho);
        for (EMailAttach a : elem.getaList())
            hBlob.removeBlob(realM, a.getBlobId());
    }

}
