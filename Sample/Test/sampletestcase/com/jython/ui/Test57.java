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

import static org.junit.Assert.*;

import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

import com.jythonui.server.holder.Holder;

public class Test57 extends TestHelper {

    @Before
    public void setUp() {
        super.setUp();
        URL url = TestHelper.class.getClassLoader().getResource("addpath");
        System.out.println(url);
        M.setAddPath(url.getFile());
    }

    @Test
    public void test1() {
        URL u = Holder.getFindResource().getFirstURL("bundle",
                "addmessages.properties");
        System.out.println(u);
        int i = u.getFile().indexOf(M.getAddPath());
        System.out.println("" + i);
        assertEquals(0, i);
    }

    @Test
    public void test2() {
        Map<String, String> ma = iGet.getResourceMap(iResServer.getResource(),
                true, "bundle", "addmessages");
        for (Entry<String, String> e : ma.entrySet()) {
            System.out.println(e.getKey() + " " + e.getValue());
        }
        assertEquals("hello", ma.get("hello"));
        assertNull(ma.get("hey"));
    }

    @Test
    public void test3() {
        iCache.invalidate();
        Map<String, String> ma = iGet.getResourceMap(iResServer.getResource(),
                false, "bundle", "addmessages");
        for (Entry<String, String> e : ma.entrySet()) {
            System.out.println(e.getKey() + " " + e.getValue());
        }
        assertEquals("Hejka", ma.get("hey"));
        assertEquals("My name is Bond", ma.get("myname"));

    }

}
