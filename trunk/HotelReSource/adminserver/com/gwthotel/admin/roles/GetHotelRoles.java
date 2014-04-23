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

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.gwthotel.admin.IGetHotelRoles;
import com.gwthotel.admin.Role;
import com.gwthotel.shared.IHotelConsts;
import com.gwtmodel.table.map.XMap;
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.xml.IXMLHelper;
import com.jythonui.server.xml.IXMapFactory;

public class GetHotelRoles implements IGetHotelRoles {

    private static final String ROLES = "roles//roles.xml";
    private static final String ROLESTAG = "roles";
    private static final String ROLETAG = "role";

    private final IXMLHelper xHelper;

    @Inject
    public GetHotelRoles(
            @Named(ISharedConsts.XMLHELPERCACHED) IXMLHelper xHelper) {
        // super(lMess, new String[] { ROLES, ROLESTAG, ROLETAG }, new String[]
        // {
        // IHotelConsts.NAME, IHotelConsts.DESCRIPTION }, iFactory);
        this.xHelper = xHelper;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Role> getList() {
        IXMapFactory xFactory = new IXMapFactory() {

            @Override
            public XMap construct() {
                return new Role();
            }
        };
        return (List<Role>) xHelper.getList(new String[] { ROLES, ROLESTAG,
                ROLETAG }, new String[] { IHotelConsts.NAME,
                IHotelConsts.DESCRIPTION }, xFactory);

    }

}
