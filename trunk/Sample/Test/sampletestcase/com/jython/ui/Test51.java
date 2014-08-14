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
package com.jython.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.gwtmodel.table.common.TT;
import com.jythonui.server.IMailSend;
import com.jythonui.server.holder.Holder;
import com.jythonui.server.security.token.ICustomSecurity;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldItem;

public class Test51 extends TestHelper {

    @Test
    public void test1() {

        DialogFormat d = findDialog("test96.xml");
        assertNotNull(d);
        FieldItem v = d.findFieldItem("email");
        assertEquals(TT.STRING, v.getFieldType());
        assertTrue(v.isEmailType());
    }

    @Test
    public void test2() {
        IMailSend iiMail = Holder.getMail();
        String res = iiMail.postMail(true,
                new String[] { "stanislawbartkowski@gmail.com" }, "hello",
                "my first note", "hello.x", null);
        System.out.println(res);
        assertNull(res);
    }

    @Test
    public void test3() {
        DialogFormat d = findDialog("test97.xml");
        assertNotNull(d);
        DialogVariables v = new DialogVariables();
        runAction(v, "test97.xml", "sendmail");
        assertOK(v);
    }

    @Test
    public void test4() throws IOException {
        IMailSend iiMail = Holder.getMail();
        URL u = iResServer.getResource().getRes("testdata/attach.txt");
        assertNotNull(u);
        int i = 0;
        InputStream is = u.openStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] a = new byte[100];
        int no = is.read(a);
        while (no != -1) {
            out.write(a, 0, no);
            no = is.read(a);
        }
        String bKey = iAddBlob.addNewBlob("PDFTEMP", "ATTACH",
                out.toByteArray());
        assertNotNull(bKey);
        IMailSend.AttachElem atta = new IMailSend.AttachElem("PDFTEMP", bKey,
                "attach.txt");
        List<IMailSend.AttachElem> aList = new ArrayList<IMailSend.AttachElem>();
        aList.add(atta);
        String res = iiMail.postMail(true,
                new String[] { "stanislawbartkowski@gmail.com" },
                "Note with single attachment",
                "This is my first not with one attachment", "hello.x", aList);
        System.out.println(res);
        assertNull(res);
    }

    @Test
    public void test5() {
        DialogVariables v = new DialogVariables();
        runAction(v, "test97.xml", "sendmailattachment");
        assertOK(v);
    }

    @Test
    public void test6() {
        DialogVariables v = new DialogVariables();
        runAction(v, "test97.xml", "sendmailattachment3");
        assertOK(v);
    }

    @Test
    public void test7() {
        ICustomSecurity cu = getPersonSec();
        String t = iSec.authenticateToken(realmIni, "guest", "guest", cu);
        assertNotNull(t);

        DialogVariables v = new DialogVariables();
        runAction(t, v, "test97.xml", "createbloblist");
        assertOK(v);
        v = new DialogVariables();
        runAction(t, v, "test97.xml", "sendbloblist");
        assertOK(v);
    }

}
