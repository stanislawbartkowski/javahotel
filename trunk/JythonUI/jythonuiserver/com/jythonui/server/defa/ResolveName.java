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

import javax.inject.Inject;
import javax.inject.Named;

import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.IMessConsts;
import com.jythonui.server.IConsts;
import com.jythonui.server.IResolveName;
import com.jythonui.server.resbundle.IAppMess;

public class ResolveName implements IResolveName {

    private final IAppMess appMess;

    @Inject
    public ResolveName(@Named(IConsts.APPMESS) IAppMess appMess) {
        this.appMess = appMess;
    }

    @Override
    public String resolveName(String id) {
        if (CUtil.EmptyS(id))
            return id;
        if (id.charAt(0) == IMessConsts.STANDCH)
            return appMess.getCustomMess().getAttr(id.substring(1));
        return id;
    }

}
