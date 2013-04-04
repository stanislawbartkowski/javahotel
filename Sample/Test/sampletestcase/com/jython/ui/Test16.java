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

import static org.junit.Assert.*;

import org.junit.Test;

import com.jythonui.server.holder.Holder;
import com.jythonui.shared.DialSecurityInfo;
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
        String t = iSec.authenticateToken(realmIni, "darkhelmet", "ludicrousspeed");
        assertNotNull(t);
        Holder.setAuth(true);
        DialogInfo i = iServer.findDialog(t, "test38.xml");
        assertNotNull(i);
        assertEquals(0,i.getSecurity().getButtonAccess().size());
        assertEquals(0,i.getSecurity().getButtonReadOnly().size());
        assertEquals(2,i.getSecurity().getFieldAccess().size());
        assertEquals(0,i.getSecurity().getFieldReadOnly().size());
    }
    
    @Test
    public void test3() {
        String t = iSec.authenticateToken(realmIni, "darkhelmet", "ludicrousspeed");
        assertNotNull(t);
        DialogInfo i = iServer.findDialog(t, "test39.xml");
        assertNotNull(i);
        assertEquals(0,i.getSecurity().getButtonAccess().size());
        assertEquals(0,i.getSecurity().getButtonReadOnly().size());
        // both are accessible
        assertEquals(2,i.getSecurity().getFieldAccess().size());
        assertEquals(0,i.getSecurity().getFieldReadOnly().size());
        assertTrue(i.getSecurity().getFieldAccess().contains("globenum"));
        assertEquals(DialSecurityInfo.AccessType.ACCESS,i.getSecurity().fieldAccess("globenum"));
        assertEquals(DialSecurityInfo.AccessType.ACCESS,i.getSecurity().fieldAccess("glob"));
        iSec.logout(t);
        t = iSec.authenticateToken(realmIni, "lonestarr", "vespa");
        assertNotNull(t);
        i = iServer.findDialog(t, "test39.xml");
        assertNotNull(i);
        assertEquals(0,i.getSecurity().getButtonAccess().size());
        assertEquals(0,i.getSecurity().getButtonReadOnly().size());
        // only one is accessible
        assertEquals(1,i.getSecurity().getFieldAccess().size());
        assertEquals(DialSecurityInfo.AccessType.NOACCESS,i.getSecurity().fieldAccess("globenum"));
        assertEquals(DialSecurityInfo.AccessType.ACCESS,i.getSecurity().fieldAccess("glob"));
        assertEquals(0,i.getSecurity().getFieldReadOnly().size());
        // glob only
        assertTrue(i.getSecurity().getFieldAccess().contains("glob"));
        iSec.logout(t);        
    }    
    
    @Test
    public void test4() {
        String t = iSec.authenticateToken(realmIni, "darkhelmet", "ludicrousspeed");
        assertNotNull(t);
        DialogInfo i = iServer.findDialog(t, "test40.xml");
        assertNotNull(i);
        assertEquals(2,i.getSecurity().getButtonAccess().size());
        assertEquals(0,i.getSecurity().getButtonReadOnly().size());
        // both are accessible
        assertEquals(3,i.getSecurity().getFieldAccess().size());
        assertEquals(0,i.getSecurity().getFieldReadOnly().size());        
        iSec.logout(t);
        
        t = iSec.authenticateToken(realmIni, "lonestarr", "vespa");
        assertNotNull(t);
        i = iServer.findDialog(t, "test40.xml");
        assertNotNull(i);
        assertEquals(1,i.getSecurity().getButtonAccess().size());
        assertTrue(i.getSecurity().getButtonAccess().contains("ID1"));
        assertEquals(DialSecurityInfo.AccessType.ACCESS,i.getSecurity().buttonAccess("ID1"));
        // one read only
        assertEquals(1,i.getSecurity().getButtonReadOnly().size());
        assertTrue(i.getSecurity().getButtonReadOnly().contains("ID"));
        assertEquals(DialSecurityInfo.AccessType.READONLY,i.getSecurity().buttonAccess("ID"));

        // both are accessible
        assertEquals(3,i.getSecurity().getFieldAccess().size());
        assertEquals(0,i.getSecurity().getFieldReadOnly().size());        
        iSec.logout(t);
        
        t = iSec.authenticateToken(realmIni, "guest", "guest");
        assertNotNull(t);
        i = iServer.findDialog(t, "test40.xml");
        assertNotNull(i);
        // only on accessible
        assertEquals(1,i.getSecurity().getButtonAccess().size());
        // 0 read only
        assertEquals(0,i.getSecurity().getButtonReadOnly().size());
        // both are accessible
        assertEquals(3,i.getSecurity().getFieldAccess().size());
        assertEquals(0,i.getSecurity().getFieldReadOnly().size());        
        iSec.logout(t);
        
    }

}
