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

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.jythonui.shared.DialogVariables;

public class Test44 extends TestHelper {

    @Test
    public void test1() {
        String t = authenticateToken(realmIni, "guest", "guest");
        assertNotNull(t);
        DialogVariables v = new DialogVariables();
        runAction(t, v,"test85.xml", "testmap"); 
        v = new DialogVariables();
        runAction(t, v,"test85.xml", "testlistmap");         
    }
    
    @Test
    public void test2() {
        String t = authenticateToken(realmIni, "guest", "guest");
        assertNotNull(t);
        DialogVariables v = new DialogVariables();
        runAction(t, v,"test85.xml", "testxmllist"); 
    }

}
