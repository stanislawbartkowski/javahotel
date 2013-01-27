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

import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.ListFormat;
import com.jythonui.shared.ListOfRows;
import com.jythonui.shared.RowContent;
import com.jythonui.shared.TypedefDescr;
import com.jythonui.shared.TypesDescr;

public class Test12 extends TestHelper {

    @Test
    public void test1() {
        DialogFormat d = iServer.findDialog("test25.xml");
        assertNotNull(d);
        assertNotNull(d.getTypeList());
        assertEquals(1, d.getTypeList().size());
        assertEquals(1, d.getTypeList().get(0).getTypeList().size());
        boolean ok = false;
        for (TypesDescr t : d.getTypeList()) {
            for (TypedefDescr ty : t.getTypeList()) {
                System.out.println(ty.getId());
                if (ty.eqId("datasource")) ok = true;
            }
        }
        assertTrue(ok);
    }

    @Test
    public void test2() {
        DialogFormat d = iServer.findDialog("test26.xml");
        assertNotNull(d);
        assertNotNull(d.getTypeList());
        assertEquals(1, d.getTypeList().size());
        assertEquals(1, d.getTypeList().get(0).getTypeList().size());
        DialogVariables v = new DialogVariables();
        iServer.runAction(v, "test26.xml", ICommonConsts.BEFORE);
        ListOfRows ro = v.getEnumList().get("tenum");
        assertNotNull(ro);
        assertEquals(100,ro.getRowList().size());
        for (RowContent r : ro.getRowList()) {
           String s = r.getRow(0).getValueS();
           System.out.println(s);
           String name = r.getRow(1).getValueS();
           System.out.println(name);
           assertNotNull(s);
           assertNotNull(name);
        }
        assertEquals(0,v.getRowList().size());
    }
    
    @Test
    public void test3() {
        DialogFormat d = iServer.findDialog("test27.xml");
        assertNotNull(d);
        ListFormat li = DialogFormat.findE(d.getListList(), "list");
        assertNotNull(li);
        assertNotNull(li.getStandButt());
        System.out.println(li.getStandButt());
    }

}
