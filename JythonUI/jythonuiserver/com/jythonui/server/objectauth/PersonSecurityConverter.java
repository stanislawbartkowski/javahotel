/*
 * Copyright 2016 stanislawbartkowski@gmail.com 
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
package com.jythonui.server.objectauth;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.jython.serversecurity.AppInstanceId;
import com.jython.serversecurity.IOObjectAdmin;
import com.jython.serversecurity.OObject;
import com.jython.serversecurity.OObjectRoles;
import com.jython.serversecurity.cache.IGetInstanceOObjectIdCache;
import com.jythonui.server.IConsts;
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.security.ISecurityConvert;
import com.jythonui.server.security.token.ICustomSecurity;
import com.jythonui.shared.CustomSecurity;

public class PersonSecurityConverter implements ISecurityConvert {

    private final IOObjectAdmin iAdmin;
    private final IGetInstanceOObjectIdCache iGetI;

    @Inject
    public PersonSecurityConverter(IOObjectAdmin iAdmin,
            IGetInstanceOObjectIdCache iGetI) {
        this.iAdmin = iAdmin;
        this.iGetI = iGetI;
    }

    private void check(String instanceId) {
        AppInstanceId i = iGetI.getInstance(instanceId, "");
        List<OObject> aList = iAdmin.getListOfObjects(i);
        for (OObject o : aList)
            if (o.getName().equals(ISharedConsts.SINGLEOBJECTHOLDER))
                return;
        OObject oo = new OObject();
        oo.setName(ISharedConsts.SINGLEOBJECTHOLDER);
        List<OObjectRoles> roles = new ArrayList<OObjectRoles>();
        iAdmin.addOrModifObject(i, oo, roles);
    }

    @Override
    public ICustomSecurity construct(CustomSecurity sou) {
        ObjectCustom ho = new ObjectCustom();
        ho.setObjectName(ISharedConsts.SINGLEOBJECTHOLDER);
        String instanceId = sou.getAttr(IConsts.INSTANCEID);
        ho.setInstanceId(instanceId);
        check(instanceId);
        return ho;
    }

}
