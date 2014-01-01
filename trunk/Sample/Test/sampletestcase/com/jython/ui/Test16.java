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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.jythonui.server.holder.Holder;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogInfo;

public class Test16 extends TestHelper {

    @Test
    public void test1() {
        Holder.setAuth(false);
        DialogFormat d = findDialog("test38.xml");
        assertNotNull(d);
        Holder.setAuth(true);
        // security enabled : null is expected
        d = findDialog("test38.xml");
        assertNull(d);
    }

    @Test
    public void test2() {
        String t = authenticateToken(realmIni, "darkhelmet",
                "ludicrousspeed");
        assertNotNull(t);
        Holder.setAuth(true);
        DialogInfo i = findDialog(t, "test38.xml");
        assertNotNull(i);
        assertEquals(1, i.getSecurity().getFieldSec().get("glob").size());
        assertEquals(3, i.getSecurity().getFieldSec().get("globenum").size());
        assertEquals(0, i.getSecurity().getButtSec().size());
    }

    @Test
    public void test3() {
        String t = authenticateToken(realmIni, "darkhelmet",
                "ludicrousspeed");
        assertNotNull(t);
        DialogInfo i = findDialog(t, "test39.xml");
        assertNotNull(i);
        // before eyes of darkhelmet not hidden
        assertFalse(i.getSecurity().getFieldSec().get("globenum")
                .contains("hidden"));
        assertFalse(i.getSecurity().isFieldHidden(
                i.getDialog().findFieldItem("globenum")));
        iSec.logout(t);
        t = authenticateToken(realmIni, "lonestarr", "vespa");
        assertNotNull(t);
        i = findDialog(t, "test39.xml");
        assertNotNull(i);
        // hidden before other mortals
        assertTrue(i.getSecurity().getFieldSec().get("globenum")
                .contains("hidden"));
        assertTrue(i.getSecurity().isFieldHidden(
                i.getDialog().findFieldItem("globenum")));
        iSec.logout(t);
    }

    @Test
    public void test4() {
        String t = authenticateToken(realmIni, "darkhelmet",
                "ludicrousspeed");
        assertNotNull(t);
        DialogInfo i = findDialog(t, "test40.xml");
        assertNotNull(i);
        // before darkhelmet neither hidden nor readonly
        assertFalse(i.getSecurity().getButtSec().get("ID").contains("hidden"));
        assertFalse(i.getSecurity().isButtHidden(
                DialogFormat.findE(i.getDialog().getButtonList(), "ID")));
        assertFalse(i.getSecurity().getButtSec().get("ID").contains("readonly"));
        assertFalse(i.getSecurity().isButtReadOnly(
                DialogFormat.findE(i.getDialog().getButtonList(), "ID")));
        iSec.logout(t);
        //
        t = authenticateToken(realmIni, "lonestarr", "vespa");
        assertNotNull(t);
        i = findDialog(t, "test40.xml");
        assertNotNull(i);
        // lonestarr : hidden but readonly
        assertFalse(i.getSecurity().getButtSec().get("ID").contains("hidden"));
        assertFalse(i.getSecurity().isButtHidden(
                DialogFormat.findE(i.getDialog().getButtonList(), "ID")));
        assertTrue(i.getSecurity().getButtSec().get("ID").contains("readonly"));
        assertTrue(i.getSecurity().isButtReadOnly(
                DialogFormat.findE(i.getDialog().getButtonList(), "ID")));
        iSec.logout(t);
        iSec.logout(t);
        //
        t = authenticateToken(realmIni, "guest", "guest");
        assertNotNull(t);
        i = findDialog(t, "test40.xml");
        assertNotNull(i);
        // other hidden and not readonly
        assertTrue(i.getSecurity().getButtSec().get("ID").contains("hidden"));
        assertTrue(i.getSecurity().isButtHidden(
                DialogFormat.findE(i.getDialog().getButtonList(), "ID")));
        assertFalse(i.getSecurity().getButtSec().get("ID").contains("readonly"));
        assertFalse(i.getSecurity().isButtReadOnly(
                DialogFormat.findE(i.getDialog().getButtonList(), "ID")));
    }

}
