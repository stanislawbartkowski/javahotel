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
package com.gwthotel.admintest.suite;

import org.junit.Test;

import com.jythonui.shared.DialogVariables;

public class Test45 extends TestHelper {
    
    @Test
    public void test1() {
        String resename = createRes(5);
        addGuest(resename);
        DialogVariables v = new DialogVariables();
        v.setValueS("resename", resename);
        scriptTest("dialog45.xml", "test1", v);        
    }

    @Test
    public void test2() {
        String resename = createRes(15);
        addGuest(resename);
        DialogVariables v = new DialogVariables();
        v.setValueS("resename", resename);
        scriptTest("dialog45.xml", "test2", v);        
    }
    
    @Test
    public void test3() {
        String resename = createRes(10);
        addGuest(resename);
        DialogVariables v = new DialogVariables();
        v.setValueS("resename", resename);
        scriptTest("dialog45.xml", "test3", v);        
    }

    @Test
    public void test4() {
        String resename = createResV(10,"free");
        addGuest(resename);
        DialogVariables v = new DialogVariables();
        v.setValueS("resename", resename);
        scriptTest("dialog45.xml", "test4", v);        
    }

    @Test
    public void test5() {
        scriptTest("dialog45.xml", "test5");        
    }

}
