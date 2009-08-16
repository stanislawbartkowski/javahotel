package com.javahotel.test;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
