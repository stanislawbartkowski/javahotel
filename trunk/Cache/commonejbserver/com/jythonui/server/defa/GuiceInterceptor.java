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

import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.gwthotel.adminejb.guice.EMHolder;
import com.gwthotel.shared.IHotelConsts;

public class GuiceInterceptor {

    @EJB
    private GuiceInjectorHolder injectorHolder;

    static final private Logger log = Logger.getLogger(GuiceInterceptor.class
            .getName());

    @PersistenceContext(unitName = IHotelConsts.PERSISTENCE_UNIT_NAME)
    public void setPersistence(EntityManager em) {
//        log.info("EntityManager injected");
        EMHolder.setEm(em);
    }

    // private EntityManager em;

    @AroundInvoke
    public Object injectByGuice(InvocationContext context) throws Exception {

        injectorHolder.getInjector().injectMembers(context.getTarget());
        return context.proceed();
    }
}