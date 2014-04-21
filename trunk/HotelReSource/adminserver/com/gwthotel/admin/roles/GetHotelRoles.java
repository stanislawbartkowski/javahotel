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
package com.gwthotel.admin.roles;

import javax.inject.Inject;
import javax.inject.Named;

import com.gwthotel.admin.IGetHotelRoles;
import com.gwthotel.admin.Role;
import com.gwthotel.admin.xmlhelper.ReadXMLHelper;
import com.gwthotel.shared.IHotelConsts;
import com.jython.ui.shared.resource.IReadResourceFactory;
import com.jythonui.server.getmess.IGetLogMess;

public class GetHotelRoles extends ReadXMLHelper<Role> implements
        IGetHotelRoles {

    private static final String ROLES = "roles//roles.xml";
    private static final String ROLESTAG = "roles";
    private static final String ROLETAG = "role";

    @Inject
    public GetHotelRoles(@Named(IHotelConsts.MESSNAMED) IGetLogMess lMess,
            IReadResourceFactory iFactory) {
        super(lMess, new String[] { ROLES, ROLESTAG, ROLETAG }, new String[] {
                IHotelConsts.NAME, IHotelConsts.DESCRIPTION }, iFactory);
    }

    @Override
    protected Role constructT() {
        return new Role();
    }

}
