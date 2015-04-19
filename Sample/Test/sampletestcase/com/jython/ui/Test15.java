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
package com.jython.ui;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.Test;

import static org.junit.Assert.*;

public class Test15 extends TestHelper {

    private static String[] dropSchema = { "DROP TABLE USERS",
            "DROP TABLE USER_ROLES", "DROP TABLE ROLES_PERMISSIONS" };

    private static String[] createSchema = {
            "CREATE TABLE USERS (USERNAME VARCHAR(128), PASSWORD VARCHAR(128))",
            "CREATE TABLE USER_ROLES (USERNAME VARCHAR(128), ROLE_NAME VARCHAR(128))",
            "CREATE TABLE ROLES_PERMISSIONS (ROLE_NAME VARCHAR(128), PERMISSION VARCHAR(128))" };

    private static String[] insertData = {
            "INSERT INTO USERS VALUES('root','secret')",
            "INSERT INTO USERS VALUES('presidentskroob','12345')",
            "INSERT INTO USERS VALUES('darkhelmet','ludicrousspeed')",
            "INSERT INTO USERS VALUES('lonestarr','vespa')",
            "INSERT INTO USER_ROLES VALUES('root','admin')",
            "INSERT INTO USER_ROLES VALUES('presidentskroob', 'president')",
            "INSERT INTO USER_ROLES VALUES('darkhelmet','darklord')",
            "INSERT INTO USER_ROLES VALUES('darkhelmet','schwartz')",
            "INSERT INTO USER_ROLES VALUES('lonestarr','goodguy')",
            "INSERT INTO USER_ROLES VALUES('lonestarr','schwartz')",
            "INSERT INTO ROLES_PERMISSIONS VALUES('admin','*')",
            "INSERT INTO ROLES_PERMISSIONS VALUES('schwartz','lightsaber:*')",
            // goodguy = winnebago:drive:eagle5
            "INSERT INTO ROLES_PERMISSIONS VALUES('goodguy','winnebago:drive:eagle5')" };

    private static void executeSQL(Connection con, String[] sql)
            throws SQLException {
        for (String s : sql) {
            con.createStatement().execute(s);
        }
    }

    private static void executeSQLE(Connection con, String[] sql) {
        for (String s : sql) {
            try {
                con.createStatement().execute(s);
            } catch (SQLException e) {
                continue;
            }
        }
    }

    private static void createDB() {
        EmbeddedDataSource ds = new EmbeddedDataSource();
        ds.setDatabaseName("/tmp/realm;create=true");
        ds.setUser("APP");
        ds.setPassword("APP");
        Connection con = null;
        try {
            con = ds.getConnection();
            executeSQLE(con, dropSchema);
            executeSQL(con, createSchema);
            executeSQL(con, insertData);
            con.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void runTest1(String realm) {
        String t = authenticateToken(realm, "aaa", "bbb");
        assertNull(t);
        t = authenticateToken(realm, "darkhelmet", "bbb");
        assertNull(t);
        t = authenticateToken(realm, "darkhelmet", "ludicrousspeed");
        assertNotNull(t);
        System.out.println(t);
        iSec.logout(t);
    }

    private void runTestNext(String realm) {
        String t = authenticateToken(realm, "darkhelmet", "ludicrousspeed");
        assertNotNull(t);
        boolean ok = iSec.isAuthorized(t, "ff:aaaa");
        assertFalse(ok);
        assertFalse(iSec.isAuthorized(t, "sec.u('wrongdoinguser')"));
        assertTrue(iSec.isAuthorized(t, "sec.u('darkhelmet')"));
        assertTrue(iSec.isAuthorized(t, "sec.u('imwrong') || sec.u('darkhelmet')"));
        assertFalse(iSec.isAuthorized("falsetoken", "u:darkhelmet"));
        iSec.logout(t);
        t = authenticateToken(realm, "lonestarr", "vespa");
        assertNotNull(t);
        assertTrue(iSec.isAuthorized(t, "sec.r('schwartz')"));
        System.out.println("May the Schwartz be with you!");

        assertTrue(iSec.isAuthorized(t, "sec.p('lightsaber:weild')"));
        System.out.println("You may use a lightsaber ring.  Use it wisely.");
        assertTrue(iSec.isAuthorized(t, "sec.p('winnebago:drive:eagle5')"));
        System.out
                .println("You are permitted to 'drive' the winnebago with license plate (id) 'eagle5'.  "
                        + "Here are the keys - have fun!");
        iSec.logout(t);
    }
    
    private void runTestNext1(String realm) {
        String t = authenticateToken(realm, "lonestarr", "vespa");
        assertNotNull(t);
        assertTrue(iSec.isAuthorized(t, "sec.r('schwartz')"));
        System.out.println("May the Schwartz be with you!");
        iSec.logout(t);
        // try again after logging out
        assertFalse(iSec.isAuthorized(t, "sec.r('schwartz')"));
        // False expected
    }

    @Test
    public void test1() {
        runTest1(realmIni);
    }

    @Test
    public void test2() {
        createDB();
        runTest1(derbyIni);
    }

    @Test
    public void test3() {
        runTestNext(realmIni);
    }
    
    @Test
    public void test4() {
        createDB();
        runTestNext(derbyIni);
    }
    
    @Test
    public void test5() {
        runTestNext1(realmIni);
    }
    
    @Test
    public void test6() {
        createDB();
        runTestNext1(derbyIni);
    }
    
    private void runTestNext2(String realm) {
        String t1 = authenticateToken(realm, "lonestarr", "vespa");
        assertNotNull(t1);    
        assertTrue(iSec.isAuthorized(t1, "sec.r('schwartz')"));
        // presidentskroob = 12345, president
        String t2 = authenticateToken(realm, "presidentskroob", "12345");
        assertNotNull(t2);   
        assertTrue(iSec.isAuthorized(t1, "sec.r('schwartz')"));
        assertFalse(iSec.isAuthorized(t2, "sec.r('schwartz')"));
    }
    
    @Test
    public void test7() {
        runTestNext2(realmIni);
    }
    
    @Test
    public void test8() {
        createDB();
        runTestNext2(derbyIni);
    }

    private void runTestNext3(String realm) {
        String t1 = authenticateToken(realm, "lonestarr", "vespa");
        assertNotNull(t1);    
        assertTrue(iSec.isAuthorized(t1, "!sec.r('admin')"));
        // negative
        assertTrue(iSec.isAuthorized(t1, "!sec.u('darkhelmet')"));
        assertFalse(iSec.isAuthorized(t1, "!sec.u('lonestarr')"));
        assertTrue(iSec.isAuthorized(t1, "sec.u('lonestarr')"));
        assertTrue(iSec.isAuthorized(t1, "sec.p('winnebago:drive:eagle5')"));
        assertFalse(iSec.isAuthorized(t1, "!sec.p('winnebago:drive:eagle5')"));

    }
    
    
    @Test
    public void test9() {
        runTestNext3(realmIni);
    }
    
    @Test
    public void test10() {
        createDB();
        runTestNext3(derbyIni);
    }


}
