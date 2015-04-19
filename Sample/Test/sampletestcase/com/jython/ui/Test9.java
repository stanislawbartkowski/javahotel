/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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

import java.util.List;

import org.junit.Test;

import com.jythonui.server.registry.IStorageRegistry;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;

/**
 * @author hotel
 * 
 */
public class Test9 extends TestHelper {

    @Test
    public void test1() {
        DialogFormat d = findDialog("test19.xml");
        assertNotNull(d);

        DialogVariables v = new DialogVariables();
        runAction(v, "test19.xml", "before");
        String val = v.getValueS("AAAA");
        assertEquals("aaaa", val);
    }
    
    @Test
    public void test2() {
        IStorageRegistry iR = iReg.construct("PXXXX");
        for (int i =0; i<10; i++) {
            String val = "Name " + i;
            iR.putEntry(""+i, val.getBytes());
        }
        List<String> keyS = iR.getKeys();
        int i = 0;
        for (String s : keyS) {
            System.out.println(s);
            assertEquals(""+i,s);
            i++;
        }
        for (String s : keyS) {
            iR.removeEntry(s);
        }        
        keyS = iR.getKeys();
        assertTrue(keyS.isEmpty());
        
    }

}
