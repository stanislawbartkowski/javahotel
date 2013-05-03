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

import org.junit.Test;

import com.jythonui.server.holder.Holder;

import static org.junit.Assert.assertEquals;

public class Test19 extends TestHelper {

    @Test
    public void test1() {
        String mess = appMess.getMessN("MESS1");
        System.out.println(mess);
        assertEquals("app message", mess);
        Holder.SetLocale("xx");
        mess = appMess.getMessN("MESS1");
        System.out.println(mess);
        assertEquals("app message", mess);
        Holder.SetLocale("pl");
        mess = appMess.getMessN("MESS1");
        System.out.println(mess);
        assertEquals("app message", mess);
        mess = appMess.getMessN("MESSPL");
        System.out.println(mess);
        assertEquals("pl message", mess);
        System.out.println(Holder.getLocale());
        assertEquals("pl",Holder.getLocale());
    }

}
