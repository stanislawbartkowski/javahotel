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

import org.junit.Test;

import com.jythonui.server.IMailGet;
import com.jythonui.server.holder.Holder;
import com.jythonui.shared.DialogVariables;

public class Test52 extends TestHelper {

    @Test
    public void test1() {
        IMailGet iiMail = Holder.getGetMail();
        IMailGet.IResMail res = iiMail.getMail(-1, -1);
        System.out.println(res.getNo());
        // assertTrue(res.getNo() > 1);
        res = iiMail.getMail(-1, 0);
        for (IMailGet.IMailNote no : res.getList()) {
            System.out.println(no.getHeader());
            // System.out.println(no.getContent());
        }
        System.out.println("-----------------");
        res = iiMail.getMail(0, 1);
        for (IMailGet.IMailNote no : res.getList()) {
            System.out.println(no.getHeader());
            // System.out.println(no.getContent());
        }
    }

    @Test
    public void test2() {
        DialogVariables v = new DialogVariables();
        runAction(v, "test97.xml", "getmailno");
        assertOK(v);
    }

    @Test
    public void test3() {
        DialogVariables v = new DialogVariables();
        runAction(v, "test97.xml", "getlist");
        assertOK(v);
    }

}
