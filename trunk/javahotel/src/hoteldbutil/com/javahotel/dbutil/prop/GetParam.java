/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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
package com.javahotel.dbutil.prop;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author defekt
 */
public class GetParam {

    private GetParam() {
    }

    public static String getContextString(final String name) throws Exception {
//        try {
//            // "java.naming.factory.initial"=>"org.apache.naming.java.javaURLContextFactory"
//            // "java.naming.factory.url.pkgs"=>"org.apache.naming"
//            Properties prop = new Properties();
//            prop.put("java.naming.factory.initial", "org.apache.naming.java.javaURLContextFactory");
//            prop.put("java.naming.factory.url.pkgs", "org.apache.naming");
//            Context initContext = new InitialContext(prop);
//            Context envContext = (Context) initContext.lookup("java:/comp/env");
////            Context envContext = initContext;
//            NamingEnumeration li = initContext.list("/");
//
//            while (li.hasMore()) {
//                Object o = li.nextElement();
//            }
//            Object o = envContext.lookup("/" + name);
////            Object o = (Context) initContext.lookup(name);
//            String s = (String) o;
//            return s;
//        } catch (NamingException ex) {
//            throw ex;
//        }
    	return null;
    }

    public static boolean getContextBoolean(final String name, boolean throwe, boolean defa) throws Exception {
//        try {
//            Context initContext = new InitialContext();
//            Context envContext = (Context) initContext.lookup("java:/comp/env");
//            Object o = envContext.lookup(name);
//            Boolean b = (Boolean) o;
//            return b.booleanValue();
//        } catch (NamingException ex) {
//            if (throwe) {
//                throw ex;
//            }
//            return defa;
//        }
    	return false;
    }
}
