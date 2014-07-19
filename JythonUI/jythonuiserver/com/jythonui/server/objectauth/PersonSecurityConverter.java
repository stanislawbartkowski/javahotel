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
package com.jythonui.server.objectauth;

import com.jythonui.server.IConsts;
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.security.ISecurityConvert;
import com.jythonui.server.security.token.ICustomSecurity;
import com.jythonui.shared.CustomSecurity;

public class PersonSecurityConverter implements ISecurityConvert {

    @Override
    public ICustomSecurity construct(CustomSecurity sou) {
        ObjectCustom ho = new ObjectCustom();
        ho.setObjectName(ISharedConsts.SINGLEOBJECTHOLDER);
        String instanceId = sou.getAttr(IConsts.INSTANCEID);
        ho.setInstanceId(instanceId);
        return ho;
    }

}
