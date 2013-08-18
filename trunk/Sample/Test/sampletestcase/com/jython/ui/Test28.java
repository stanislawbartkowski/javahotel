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
import com.jythonui.shared.TabPanel;
import com.jythonui.shared.TabPanelElem;

public class Test28 extends TestHelper {

    @Test
    public void test1() { 
        DialogFormat d = findDialog("test57.xml");
        assertNotNull(d);
        assertTrue(d.isHtmlPanel());
        assertEquals("Hello",d.getHtmlPanel());
        assertEquals(0,d.getTabList().size());
    }

    @Test
    public void test2() { 
        DialogFormat d = findDialog("test58.xml");
        assertNotNull(d);
        assertEquals(1,d.getTabList().size());
        TabPanel ta = d.getTabList().get(0);
        assertEquals(1,ta.gettList().size());
        for (TabPanelElem t : ta.gettList()) {
            System.out.println(t.getId());
            System.out.println(t.getDisplayName());
        }
    }
}