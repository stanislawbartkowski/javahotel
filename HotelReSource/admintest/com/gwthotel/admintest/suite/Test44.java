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

public class Test44 extends TestHelper {

    @Test
    public void test1() {
        String sym = createRes(1);
        DialogVariables v = new DialogVariables();
        v.setValueS("resename", sym);
        scriptTest("dialog44.xml", "test1", v);
    }

    @Test
    public void test2() {
        String sym = createRes(4);
        addGuest(sym);
        DialogVariables v = new DialogVariables();
        v.setValueS("resename", sym);
        scriptTest("dialog44.xml", "test2", v);
    }

}
