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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.jythonui.shared.DialSecurityInfo;
import com.jythonui.shared.DialogInfo;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldValue;

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
        assertEquals(0, elem.getButtonAccess().size());
        assertEquals(0, elem.getButtonReadOnly().size());
        assertEquals(2, elem.getFieldAccess().size());
        assertEquals(0, elem.getFieldReadOnly().size());

        String t1 = iSec.authenticateToken(realmIni, "guest", "guest");
        assertNotNull(t1);
        i = iServer.findDialog(t1, "test41.xml");
        assertNotNull(i);
        elem = i.getSecurity().getlSecur().get("lista");
        assertNotNull(elem);
        assertEquals(0, elem.getButtonAccess().size());
        assertEquals(0, elem.getButtonReadOnly().size());
        assertEquals(1, elem.getFieldAccess().size());
        assertEquals(0, elem.getFieldReadOnly().size());
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
        assertEquals(3,i.getDialog().getLeftButtonList().size());
        assertEquals(3, i.getSecurity().getButtonAccess().size());
        assertEquals(0, i.getSecurity().getButtonReadOnly().size());        
        iSec.logout(t);
        
        t = iSec.authenticateToken(realmIni, "lonestarr", "vespa");
        assertNotNull(t);
        i = iServer.findDialog(t, "test43.xml");
        assertEquals(1, i.getSecurity().getButtonAccess().size());
        assertEquals(1, i.getSecurity().getButtonReadOnly().size());        
        iSec.logout(t);

    }

}
