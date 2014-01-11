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
package com.jythonui.server.defa;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.gwthotel.shared.IHotelConsts;

public class GuiceInterceptor {

    @EJB
    private GuiceInjectorHolder injectorHolder;

    // TODO: investigate later
    // I do not understand it fully but it seems necessary to inject
    // EntityManager here just now although it is not used directly.
    // 
    // It is necessary for later invocation of EntityManagerFactoryProvider
    // If this injection is done before then the later invocation of
    // EntityManagerFactoryProvider creates shared copy of EntityManager
    // managed by Container.
    // Otherwise it creates it's own copy and transactional processing does not
    // work
    // Additional problem: GuiceService : Singleton.class or not Singleton.class
    @PersistenceContext(unitName = IHotelConsts.HOTELPERSISTENCEPROVIDER)
    private EntityManager em;

    @AroundInvoke
    public Object injectByGuice(InvocationContext context) throws Exception {

        injectorHolder.getInjector().injectMembers(context.getTarget());
        return context.proceed();
    }
}