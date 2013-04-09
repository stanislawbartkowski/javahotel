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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.jythonui.shared.DialSecurityInfo;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogInfo;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldValue;
import com.jythonui.shared.ListFormat;

public class Test17 extends TestHelper {

    @Test
    public void test1() {
        String t = iSec.authenticateToken(realmIni, "darkhelmet",
                "ludicrousspeed");
        assertNotNull(t);
        DialogInfo i = iServer.findDialog(t, "test41.xml");
        assertNotNull(i);
        DialSecurityInfo elem = i.getSecurity().getlSecur().get("lista");
        assertNotNull(elem);
        assertFalse(elem.getFieldSec().get("id")
                .contains("hidden"));

        String t1 = iSec.authenticateToken(realmIni, "guest", "guest");
        assertNotNull(t1);
        i = iServer.findDialog(t1, "test41.xml");
        assertNotNull(i);
        elem = i.getSecurity().getlSecur().get("lista");
        assertNotNull(elem);
        assertTrue(elem.getFieldSec().get("id")
                .contains("hidden"));
        iSec.logout(t);
        iSec.logout(t1);

    }

    @Test
    public void test2() {
        String t = iSec.authenticateToken(realmIni, "darkhelmet",
                "ludicrousspeed");
        assertNotNull(t);
        DialogVariables v = new DialogVariables();
        v.setSecurityToken(t);
        iServer.runAction(v, "test42.xml", "testsecu");
        FieldValue val = v.getValue("OK");
        assertTrue(val.getValueB());
        iSec.logout(t);
    }
    
    @Test
    public void test3() {
        String t = iSec.authenticateToken(realmIni, "darkhelmet",
                "ludicrousspeed");
        assertNotNull(t);
        DialogInfo i = iServer.findDialog(t, "test43.xml");
        assertNotNull(i);
        assertFalse(i.getSecurity().getButtSec().get("ID")
                .contains("hidden"));
        assertFalse(i.getSecurity().getButtSec().get("ID1")
                .contains("hidden"));
        assertFalse(i.getSecurity().getButtSec().get("ID1")
                .contains("readonly"));

        iSec.logout(t);
        
        t = iSec.authenticateToken(realmIni, "lonestarr", "vespa");
        assertNotNull(t);
        i = iServer.findDialog(t, "test43.xml");
        assertTrue(i.getSecurity().getButtSec().get("ID")
                .contains("hidden"));
        assertFalse(i.getSecurity().getButtSec().get("ID1")
                .contains("hidden"));
        assertTrue(i.getSecurity().getButtSec().get("ID1")
                .contains("readonly"));
        
        iSec.logout(t);

    }
    
    @Test
    public void test4() {
        String t = iSec.authenticateToken(realmIni, "darkhelmet",
                "ludicrousspeed");
        assertNotNull(t);
        DialogInfo i = iServer.findDialog(t, "test44.xml");
        assertNotNull(i);
        DialSecurityInfo sI = i.getSecurity().getListSecur().get("list");
        assertNotNull(sI);
        ListFormat li = i.getDialog().findList("list");
        assertFalse(sI.isFieldHidden(DialogFormat.findE(li.getColumns(),"id")));
        iSec.logout(t);
        
        t = iSec.authenticateToken(realmIni, "lonestarr", "vespa");
        assertNotNull(t);
        i = iServer.findDialog(t, "test44.xml");
        assertNotNull(i);
        sI = i.getSecurity().getListSecur().get("list");
        assertNotNull(sI);
        li = i.getDialog().findList("list");
        assertTrue(sI.isFieldHidden(DialogFormat.findE(li.getColumns(),"id")));
        iSec.logout(t);
    }

}
