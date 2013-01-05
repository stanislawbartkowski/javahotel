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
package com.javahotel.javatest.gaetest;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.apphosting.api.ApiProxy;

public class LocalServiceTestEnvironment {


    private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
    private final LocalServiceTestHelper helper1 =
        new LocalServiceTestHelper(new LocalMemcacheServiceTestConfig());
    
    public void beforeTest() {
        helper.setUp();
//        helper1.setUp();
        ApiProxy.setEnvironmentForCurrentThread(new TestEnvironment());
//        ApiProxy.setDelegate(new ApiProxyLocalImpl(new File(".")) {
//        });
    }

    public void afterTest() {
        // not strictly necessary to null these out but there's no harm either
//        helper.tearDown();
        ApiProxy.setDelegate(null);
        ApiProxy.setEnvironmentForCurrentThread(null);
//        helper.tearDown();
  //      helper1.tearDown();
    }
}