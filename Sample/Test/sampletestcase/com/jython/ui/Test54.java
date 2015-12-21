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
package com.jython.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.jython.serversecurity.cache.OObjectId;
import com.jythonui.server.IMailSendSave;
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.holder.Holder;
import com.jythonui.server.mail.Note;
import com.jythonui.server.security.token.ICustomSecurity;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.RequestContext;

public class Test54 extends TestHelper {

//    @Before
//    public void setUp() {
//        super.setUp();
//        createObjects();
//    }

    private OObjectId getP() {
        return iGetI.getOObject(TESTINSTANCE, ISharedConsts.SINGLEOBJECTHOLDER,
                "user");
    }

    @Test
    public void test1() {
        ICustomSecurity cu = getPersonSec();
        String t = iSec.authenticateToken(realmIni, "guest", "guest", cu);
        assertNotNull(t);
        RequestContext req = new RequestContext();
        req.setToken(t);
        Holder.setContext(req);

        IMailSendSave iiMail = Holder.getSaveMail();
        String res = iiMail.postMail(true,
                new String[] { "stanislawbartkowski@gmail.com" }, "hello",
                "my first note", "hello.x", null).getSendResult();
        System.out.println(res);
        assertNull(res);
        List<Note> nList = iNoteStorage.getList(getP());
        assertEquals(1, nList.size());
        Note no = nList.get(0);
        assertTrue(no.isText());
        assertEquals("stanislawbartkowski@gmail.com", no.getRecipient());
    }

    @Test
    public void test2() {
        ICustomSecurity cu = getPersonSec();
        String t = iSec.authenticateToken(realmIni, "guest", "guest", cu);
        assertNotNull(t);
        DialogVariables v = new DialogVariables();
        runAction(t, v, "test97.xml", "setxmail");
        assertOK(v);
        v = new DialogVariables();
        runAction(t, v, "test97.xml", "readxmail");
        assertOK(v);
        v = new DialogVariables();
        runAction(t, v, "test97.xml", "removexmail");
        assertOK(v);
    }

}
