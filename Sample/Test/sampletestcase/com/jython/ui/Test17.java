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
package com.jython.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.jythonui.shared.DialogInfo;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldValue;

public class Test17 extends TestHelper {

    @Test
    public void test1() {
        String t = authenticateToken(realmIni, "darkhelmet",
                "ludicrousspeed");
        assertNotNull(t);
        DialogInfo i = findDialog(t, "test41.xml");
        assertNotNull(i);
        assertEquals(0, i.getDialog().getCheckList().size());
//        DialSecurityInfo elem = i.getSecurity().getListSecur().get("lista");
//        assertNotNull(elem);
//        assertFalse(elem.getFieldSec().get("id").contains("hidden"));
        assertFalse(listContainsAttr(i, "lista","id", "hidden"));

        String t1 = authenticateToken(realmIni, "guest", "guest");
        assertNotNull(t1);
        i = findDialog(t1, "test41.xml");
        assertNotNull(i);
//        elem = i.getSecurity().getListSecur().get("lista");
//        assertNotNull(elem);
        assertTrue(listContainsAttr(i, "lista","id", "hidden"));
//        assertTrue(elem.getFieldSec().get("id").contains("hidden"));
        iSec.logout(t);
        iSec.logout(t1);

    }

    @Test
    public void test2() {
        String t = authenticateToken(realmIni, "darkhelmet",
                "ludicrousspeed");
        assertNotNull(t);
        DialogVariables v = new DialogVariables();
        runAction(t,v, "test42.xml", "testsecu");
        FieldValue val = v.getValue("OK");
        assertTrue(val.getValueB());
        iSec.logout(t);
    }

    @Test
    public void test3() {
        String t = authenticateToken(realmIni, "darkhelmet",
                "ludicrousspeed");
        assertNotNull(t);
        DialogInfo i = findDialog(t, "test43.xml");
        assertNotNull(i);
//        assertFalse(i.getSecurity().getButtSec().get("ID").contains("hidden"));
//        assertFalse(i.getSecurity().getButtSec().get("ID1").contains("hidden"));
//        assertFalse(i.getSecurity().getButtSec().get("ID1")
//                .contains("readonly"));
        assertFalse(leftbuttContainsAttr(i, "ID", "hidden"));
        assertFalse(leftbuttContainsAttr(i, "ID1", "hidden"));
        assertFalse(leftbuttContainsAttr(i, "ID1", "readonly"));

        iSec.logout(t);

        t = authenticateToken(realmIni, "lonestarr", "vespa");
        assertNotNull(t);
        i = findDialog(t, "test43.xml");
//        assertTrue(i.getSecurity().getButtSec().get("ID").contains("hidden"));
//        assertFalse(i.getSecurity().getButtSec().get("ID1").contains("hidden"));
//        assertTrue(i.getSecurity().getButtSec().get("ID1").contains("readonly"));
        assertTrue(leftbuttContainsAttr(i, "ID", "hidden"));
        assertFalse(leftbuttContainsAttr(i, "ID1", "hidden"));
        assertTrue(leftbuttContainsAttr(i, "ID1", "readonly"));

        iSec.logout(t);

    }

    @Test
    public void test4() {
        String t = authenticateToken(realmIni, "darkhelmet",
                "ludicrousspeed");
        assertNotNull(t);
        DialogInfo i = findDialog(t, "test44.xml");
        assertNotNull(i);
//        DialSecurityInfo sI = i.getSecurity().getListSecur().get("list");
//        assertNotNull(sI);
//        ListFormat li = i.getDialog().findList("list");
//        assertFalse(sI.isFieldHidden(DialogFormat.findE(li.getColumns(), "id")));
        assertFalse(listContainsAttr(i, "list","id", "hidden"));

        iSec.logout(t);

        t = authenticateToken(realmIni, "lonestarr", "vespa");
        assertNotNull(t);
        i = findDialog(t, "test44.xml");
        assertNotNull(i);
//        sI = i.getSecurity().getListSecur().get("list");
//        assertNotNull(sI);
//        li = i.getDialog().findList("list");
        assertTrue(listContainsAttr(i, "list","id", "hidden"));
//        assertTrue(sI.isFieldHidden(DialogFormat.findE(li.getColumns(), "id")));
        iSec.logout(t);
    }

}
