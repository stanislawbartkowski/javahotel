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
package com.gwthotel.adminejb;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import com.gwthotel.admin.IHotelAdmin;
import com.gwthotel.admin.ITestHotelAdmin;
import com.gwthotel.shared.IHotelConsts;
import com.jythonui.server.defa.GuiceInterceptor;

@Stateless
@EJB(name = IHotelConsts.TESTHOTELADMINEJBJNDI, beanInterface = ITestHotelAdmin.class)
@Remote
@Interceptors(value = { GuiceInterceptor.class })
public class TestHotelAdminEJB extends AbstractHotelAdminEJB implements
        ITestHotelAdmin {

    @Inject
    public void injectHotelAdmin(
            @Named(IHotelConsts.TESTHOTELADMIN) IHotelAdmin injectedHotel) {
        iHotel = injectedHotel;
    }
}
