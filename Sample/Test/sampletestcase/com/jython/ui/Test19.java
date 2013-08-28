/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.jythonui.server.Util;
import com.jythonui.server.holder.Holder;
import com.jythonui.shared.CheckList;
import com.jythonui.shared.CustomMessages;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.ListFormat;
import com.jythonui.shared.RequestContext;

public class Test19 extends TestHelper {
    
    private void setLocale(String loc) {
        RequestContext req = new RequestContext();
        req.setLocale(loc);
        Holder.setContext(req);
    }

    @Test
    public void test1() {
        String mess = appMess.getMessN("MESS1");
        System.out.println(mess);
        assertEquals("app message", mess);
        setLocale("xx");
        mess = appMess.getMessN("MESS1");
        System.out.println(mess);
        assertEquals("app message", mess);

        CustomMessages cmess = appMess.getCustomMess();
        String val = cmess.getAttr("MESSPL");
        System.out.println(val);
        assertEquals("english message", val);

        setLocale("pl");
        mess = appMess.getMessN("MESS1");
        System.out.println(mess);
        assertEquals("app message", mess);
        mess = appMess.getMessN("MESSPL");
        System.out.println(mess);
        assertEquals("pl message", mess);
        System.out.println(Util.getLocale());
        assertEquals("pl", Util.getLocale());

        cmess = appMess.getCustomMess();
        val = cmess.getAttr("MESSPL");
        System.out.println(val);
        assertEquals("pl message", val);
    }

    @Test
    public void test2() {
        DialogFormat d = findDialog("test46.xml");
        assertNotNull(d);
        ListFormat li = d.findList("lista");
        assertNotNull(li);
        FieldItem i = DialogFormat.findE(li.getColumns(), "id");
        assertNotNull(i);
        assertEquals("changepassword", i.getDefValue());
        i = DialogFormat.findE(li.getColumns(), "name");
        assertNotNull(i);
        assertNull(i.getDefValue());
    }

    @Test
    public void test3() {
        DialogFormat d = findDialog("test47.xml");
        assertNotNull(d);
        CheckList c = d.findCheckList("prices");
        assertNotNull(c);
        assertEquals("decimal", c.getAttr(ICommonConsts.TYPE));
        assertEquals("2", c.getAttr(ICommonConsts.AFTERDOT));
        assertTrue(c.isDecimal());
        assertFalse(c.isBoolean());

        try {
            d = findDialog("test48.xml");
            fail("Exception is expected here");
        } catch (Exception e) {
            System.out.println("OK, failure is expected");
        }
    }

}
