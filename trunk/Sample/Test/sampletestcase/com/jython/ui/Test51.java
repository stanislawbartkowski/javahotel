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

import org.junit.Test;

import com.gwtmodel.table.common.TT;
import com.jythonui.server.IMailGet;
import com.jythonui.server.IMailSend;
import com.jythonui.server.holder.Holder;
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
                "my first note", "hello.x");
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
    public void test4() {
        IMailGet iiMail = Holder.getGetMail();
        IMailGet.IResMail res = iiMail.getMail(-1, -1);
        System.out.println(res.getNo());
//        assertTrue(res.getNo() > 1);
        res = iiMail.getMail(-1, 0);
        for (IMailGet.IMailNote no : res.getList()) {
            System.out.println(no.getHeader());
        }
     }
    

}
