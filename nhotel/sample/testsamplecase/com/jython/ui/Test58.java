/*
 * Copyright 2017 stanislawbartkowski@gmail.com  
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

import com.jythonui.shared.DialogVariables;

public class Test58 extends TestHelper {
    
    @Test
    public void test1() {
        DialogVariables v = new DialogVariables();
        runAction(v, "test103.xml", "test1");
        assertOK(v);        
    }

    @Test
    public void test2() {
        DialogVariables v = new DialogVariables();
        runAction(v, "test103.xml", "test2");
        assertOK(v);        
    }

    @Test
    public void test3() {
        DialogVariables v = new DialogVariables();
        runAction(v, "test103.xml", "test3");
        assertOK(v);        
    }

    @Test
    public void test4() {
        DialogVariables v = new DialogVariables();
        runAction(v, "test103.xml", "test4");
        assertOK(v);        
    }

    @Test
    public void test5() {
        DialogVariables v = new DialogVariables();
        runAction(v, "test103.xml", "test5");
        assertOK(v);        
    }

}
