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

import com.jythonui.server.registry.IStorageRegistry;
import com.jythonui.shared.DialogFormat;

import static org.junit.Assert.*;

public class Test35 extends TestHelper {

    private static final String RE1 = "Realm1";
    private static final String RE2 = "Realm2";

    private void beforeTest() {
        iBlob.clearAll(RE1);
        iBlob.clearAll(RE2);
    }

    @Test
    public void test1() {
        beforeTest();

        IStorageRegistry reg = iReg.construct(RE1);

        String hello = "Hello";
        reg.putEntry("hello", hello.getBytes());

        StringBuffer b = new StringBuffer();
        for (int i = 0; i < 1000; i++) {
            b.append(i);
        }

        iBlob.addBlob(RE1, "hello", b.toString().getBytes());

        byte[] bb = reg.getEntry("hello");
        assertNotNull(bb);
        String s = new String(bb);
        System.out.println(s);
        assertEquals("Hello", s);

        bb = iBlob.findBlob(RE1, "hello");
        assertNotNull(bb);
        assertEquals(2890, bb.length);

        iBlob.changeBlob(RE1, "hello", hello.getBytes());
        bb = iBlob.findBlob(RE1, "hello");
        assertNotNull(bb);
        s = new String(bb);
        System.out.println(s);
        assertEquals("Hello", s);

        iBlob.removeBlob(RE1, "hello");
        bb = iBlob.findBlob(RE1, "hello");
        assertNull(bb);
    }

    @Test
    public void test2() {
        beforeTest();
        try {
        DialogFormat d = findDialog("test71.xml");
        fail("Should fail here");
        } catch (Exception e) {
            // ok
        }

    }
    
    @Test
    public void test3() {
        beforeTest();
        DialogFormat d = findDialog("test72.xml");
        assertNotNull(d);

    }
}
