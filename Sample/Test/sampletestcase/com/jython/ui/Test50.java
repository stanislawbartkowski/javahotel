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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.jythonui.server.security.token.ICustomSecurity;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;

public class Test50 extends TestHelper {
    
    protected final String realM = "classpath:resources/shiro/user.ini";

    @Before
    public void setUp() {
        super.setUp();
        iPerson.clearAll(getI());
    }

    @Test
    public void test1() {
        DialogFormat d = findDialog("test95.xml");
        assertNotNull(d);
        DialogVariables v = new DialogVariables();
        runAction(v, "test95.xml", "setperson");
        runAction(v, "test95.xml", "checkperson");
        runAction(v, "test95.xml", "setpassword");
        ICustomSecurity cu = getPersonSec();
        String token = iSec.authenticateToken(realM, "user", "secret", cu);
        assertNotNull(token);
        assertTrue(iSec.isAuthorized(token, "sec.u('user')"));
        assertTrue(iSec.isAuthorized(token, "sec.r('role2')"));
        assertFalse(iSec.isAuthorized(token, "sec.r('xxxxxxxxxx')"));
    }

}
