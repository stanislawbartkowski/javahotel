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
package com.jython.ui.server.jpanote;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.gwtmodel.table.common.CUtil;
import com.jython.jpautil.crudimpl.AbstractJpaCrud;
import com.jython.jpautil.crudimpl.gensym.IJpaObjectGenSymFactory;
import com.jython.serversecurity.cache.OObjectId;
import com.jython.ui.server.jpastoragekey.entity.EMailAttach;
import com.jython.ui.server.jpastoragekey.entity.EMailNote;
import com.jython.ui.server.jpatrans.ITransactionContextFactory;
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.server.mail.INoteStorage;
import com.jythonui.server.mail.Note;
import com.jythonui.server.mail.NoteAttach;
import com.jythonui.server.newblob.IAddNewBlob;
import com.jythonui.server.storage.blob.IBlobHandler;

public class JpaNoteStorage extends AbstractJpaCrud<Note, EMailNote> implements
        INoteStorage {

    private final IAddNewBlob iAdd;
    private final IBlobHandler hBlob;

    private final static int MAXSIZE = 255;

    @Inject
    public JpaNoteStorage(ITransactionContextFactory eFactory,
            IJpaObjectGenSymFactory iGenFactory, IAddNewBlob iAdd,
            IBlobHandler hBlob) {
        super(new String[] { "findAllMails", "findOneMail" }, iGenFactory,
                EMailNote.class.getSimpleName(), eFactory, EMailNote.class);
        this.iAdd = iAdd;
        this.hBlob = hBlob;
    }

    @Override
    protected Note toT(EMailNote sou, EntityManager em, OObjectId hotel) {
        Note no = new Note();
        no.setContent(sou.getContent());
        no.setFrom(sou.getMailFrom());
        no.getRecipientsList().addAll(sou.getRecipientsList());
        no.setText(sou.isText());
        no.setSendResult(sou.getSendRes());
        String realM = createRealm(sou.getName(), hotel);
        for (EMailAttach a : sou.getaList()) {
            NoteAttach aa = new NoteAttach();
            aa.setRealm(realM);
            aa.setBlobKey(a.getBlobKey());
            aa.setFileName(a.getFileName());
            no.getaList().add(aa);
        }
        return no;
    }

    @Override
    protected EMailNote constructE(EntityManager em, OObjectId hotel) {
        return new EMailNote();
    }

    @Override
    protected void toE(EMailNote dest, Note sou, EntityManager em,
            OObjectId hotel) {
        if (!CUtil.EmptyS(sou.getContent())
                && sou.getContent().length() >= ISharedConsts.MAILCONTENTSIZE)
            errorMess(L(), IErrorCode.ERRORCODE118,
                    ILogMess.MAILCONTENTTOOLONG,
                    CUtil.NumbToS(sou.getContent().length()),
                    CUtil.NumbToS(ISharedConsts.MAILCONTENTSIZE));

        dest.setContent(sou.getContent());
        dest.setMailFrom(sou.getFrom());
        String res = sou.getSendResult();
        if (!CUtil.EmptyS(res)) {
            if (res.length() > MAXSIZE)
                res = res.substring(0, MAXSIZE);
            dest.setSendRes(res);
        }
        dest.setText(sou.isText());
        dest.getRecipientsList().addAll(sou.getRecipientsList());
    }

    private String createRealm(String name, OObjectId hotel) {
        String realM = hotel.getObject() + "-" + ISharedConsts.MAILNOTEREALM
                + "-" + name;
        return realM;
    }

    @Override
    protected void afterAddChange(EntityManager em, OObjectId hotel, Note prop,
            EMailNote elem, boolean add) {

        String realM = createRealm(prop.getName(), hotel);
        elem.getaList().clear();
        for (NoteAttach a : prop.getaList()) {
            String key = a.getBlobKey();
            if (!CUtil.EqNS(realM, a.getRealm())) {
                byte[] atta = hBlob.findBlob(a.getRealm(), a.getBlobKey());
                // TODO: not under transaction !!!
                // improvement is needed
                key = iAdd.addNewBlob(realM, prop.getName(), atta);
            }
            EMailAttach aa = new EMailAttach();
            aa.setBlobKey(key);
            aa.setFileName(a.getFileName());
            elem.getaList().add(aa);
        }

    }

    @Override
    protected void beforedeleteElem(EntityManager em, OObjectId hotel,
            EMailNote elem) {
        String realM = createRealm(elem.getName(), hotel);
        for (EMailAttach a : elem.getaList())
            hBlob.removeBlob(realM, a.getBlobKey());
    }

}
