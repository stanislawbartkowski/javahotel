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
package com.gwthotel.admin.jpa;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.persistence.EntityManagerFactory;

import com.gwthotel.admin.IAppInstanceHotel;
import com.gwthotel.shared.IHotelConsts;
import com.jythonui.server.getmess.IGetLogMess;

public class HotelAppInstanceProvider implements Provider<IAppInstanceHotel> {

    @Inject
    private EntityManagerFactory eFactory;

    @Inject
    @Named(IHotelConsts.MESSNAMED)
    private IGetLogMess lMess;

    @Override
    public IAppInstanceHotel get() {
        return new HotelAdminInstance(eFactory, lMess);
    }

}
