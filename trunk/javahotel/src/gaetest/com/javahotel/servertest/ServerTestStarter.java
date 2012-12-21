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
package com.javahotel.servertest;

import java.util.ArrayList;
import java.util.List;

//import org.sortedunderbelly.appengineunit.harness.junit4.JUnit4Config;
//import org.sortedunderbelly.appengineunit.spi.EmailTestRunListener;
//import org.sortedunderbelly.appengineunit.spi.TestRun;
//import org.sortedunderbelly.appengineunit.spi.TestRunListener;
//
//public class ServerTestStarter extends JUnit4Config {
//
//    class MyTest implements TestRun {
//
//        public Iterable<String> getTestIds(long runId) {
//            List<String> li = new ArrayList<String>();
//            // li.add("org.testcode.mytest4.MyTestCase1");
//            // li.add("org.testcode.mytest4.MyTestCase2");
//            li.add("com.javahotel.test.TestSuite2");
//            return li;
//        }
//
//    }
//
//    public TestRun newTestRun() {
//        return new MyTest();
//    }
//
//    public TestRunListener getTestRunListener() {
//        List<String> to = new ArrayList<String>();
//        to.add("stanislawbartkowski@gmail.com");
//        return new EmailTestRunListener(to, new ArrayList<String>(),
//                new ArrayList<String>(), "ciacho");
//    }
//
//}
