/*
 * Copyright 2009 stanislawbartkowski@gmail.com
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
package com.javahotel.test;


import com.javahotel.remoteinterfaces.PasswordT;
import com.javahotel.remoteinterfaces.ISecurity;
import com.javahotel.remoteinterfaces.SessionT;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class TestSuite1 {

    private ISecurity sec;
    private SessionT se;
    private final String SESSION = "AAAAAA";

    private void setUpG() {
        sec = TestUtil.getSe();
        TestUtil.propStart(se, sec);
    }

    public TestSuite1() {
    }
        
    private void login() {
        se = sec.loginadminSession(SESSION, "admin", new PasswordT("rybka"));        
    }

    @Before
    public void setUp() {
        setUpG();
    }

    @Test
    public void Test1() throws Exception {
        System.out.println("Basic Test");
        login();
    }
    
    @Test
    public void Test2() throws Exception {
        System.out.println("Negative step");
        try {
          se = sec.loginadminSession(SESSION, "admin", new PasswordT("pipkaa"));        
          assertTrue(false);
        }
        catch (Exception e) {
            // success
        }
    }
    
    @Test
    public void Test3() throws Exception {
        System.out.println("Security Test");
        login();
        assertTrue(sec.isAdminSession(se));
        assertTrue(sec.isValidSession(se));
        sec.logoutSession(se);
        assertFalse(sec.isValidSession(se));
    }
}
