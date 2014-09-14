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
package com.gwthotel.admintest.suite;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.jythonui.server.security.token.ICustomSecurity;
import com.jythonui.shared.DialogVariables;

public class Test35 extends TestHelper {

    @Test
    public void test1() {
        setUserPassword();
        ICustomSecurity cu = getSec(HOTEL);
        String token = iSec.authenticateToken(realM, "user", "secret", cu);
        assertNotNull(token);
        DialogVariables v = new DialogVariables();
        runAction(token, v, "dialog9.xml", "createxml");
        assertOK(v);
        
        v = new DialogVariables();
        runAction(token, v, "dialog9.xml", "listxml");
        assertOK(v);
        
        v = new DialogVariables();
        runAction(token, v, "dialog9.xml", "testfailed");
        assertOK(v);
    }
    
}