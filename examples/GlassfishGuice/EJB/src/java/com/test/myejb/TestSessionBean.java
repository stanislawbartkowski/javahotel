/*
 *  Copyright 2013 stanislawbartkowski@gmail.com
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package com.test.myejb;

//import com.google.inject.Inject;
import com.test.myejb.guice.web.GuiceInterceptor;
import com.test.myejb.injected.InjectedService;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

/**
 *
 * @author hotel
 */
@Stateless
@Interceptors(value = { GuiceInterceptor.class })
@Local
public class TestSessionBean implements TestSessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method"
    @Inject
    private InjectedService iService;
    
    
    @Override
    public String getHello() {
        return "Hello, I'm your test session bean";
    }

    @Override
    public String getMessageFromInjected() {
        return iService.helloFromInjected();
    }


}
