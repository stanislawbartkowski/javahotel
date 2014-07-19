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
package com.jython.ui.server.gae.security.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import javax.inject.Inject;
import javax.inject.Named;

import com.googlecode.objectify.LoadResult;
import com.googlecode.objectify.ObjectifyService;
import com.jython.serversecurity.AppInstanceId;
import com.jython.serversecurity.cache.OObjectId;
import com.jython.serversecurity.instance.IAppInstanceOObject;
import com.jython.ui.server.gae.security.entities.EInstance;
import com.jython.ui.server.gae.security.entities.EObject;
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.UtilHelper;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;

public class ObjectInstanceImpl extends UtilHelper implements
        IAppInstanceOObject {

    static {
        ObjectifyService.register(EInstance.class);
    }

    private final IGetLogMess lMess;

    @Inject
    public ObjectInstanceImpl(
            @Named(ISharedConsts.JYTHONMESSSERVER) IGetLogMess lMess) {
        this.lMess = lMess;
    }

    @Override
    public AppInstanceId getInstanceId(String instanceName, String userName) {
        LoadResult<EInstance> p = ofy().load().type(EInstance.class)
                .filter("name ==", instanceName).first();
        EInstance e = null;
        if (p.now() == null) {
            // test if default instances
            if (instanceName.equals(ISharedConsts.INSTANCETEST)
                    || instanceName.equals(ISharedConsts.INSTANCEDEFAULT)) {
                e = new EInstance();
                e.setName(instanceName);
                ofy().save().entity((EInstance) e).now();
            } else {
                errorMess(lMess, IErrorCode.ERRORCODE92,
                        ILogMess.INSTANCENOTFOUND, null, instanceName);
            }
        } else
            e = p.now();
        AppInstanceId i = new AppInstanceId();
        i.setInstanceName(instanceName);
        i.setId(e.getId());
        i.setPerson(userName);
        return i;
    }

    @Override
    public OObjectId getOObjectId(AppInstanceId instanceId, String hotelName,
            String userName) {
        EInstance ei = EntUtil.findI(lMess, instanceId);
        LoadResult<EObject> p = ofy().load().type(EObject.class).ancestor(ei)
                .filter("name ==", hotelName).first();
        if (p.now() == null) {
            errorMess(lMess, IErrorCode.ERRORCODE93,
                    ILogMess.OBJECTCANNOTBEFOUND, null, hotelName);
        }
        OObjectId ho = new OObjectId();
        ho.setObject(hotelName);
        ho.setId(p.now().getId());
        ho.setInstanceId(instanceId);
        ho.setUserName(userName);
        return ho;
    }

}
