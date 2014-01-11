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
import static org.junit.Assert.*;

public class Test33 extends TestHelper {

    @Test
    public void test1() {
        String plmess = appMess.getCustomMess().getAttr("numbergreater");
        System.out.println(plmess);
        assertNotNull(plmess);
        putLocale("en");
        String enmess = appMess.getCustomMess().getAttr("numbergreater");
        System.out.println(enmess);
        assertNotNull(enmess);
        assertNotEquals(plmess,enmess);
    }

}
