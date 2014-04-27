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
package com.jythonui.server.ejb;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import com.google.inject.Inject;
import com.jython.serversecurity.AppInstanceId;
import com.jython.serversecurity.OObjectId;
import com.jython.serversecurity.instance.IAppInstanceOObject;
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.defa.GuiceInterceptor;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@EJB(name = ISharedConsts.COMMONAPPINSTANCEJNDI, beanInterface = IAppInstanceOObject.class)
@Remote
@Interceptors(value = { GuiceInterceptor.class })
public class AppInstanceOObjectEJB implements IAppInstanceOObject {

    private IAppInstanceOObject iApp;

    @Inject
    public void inject(IAppInstanceOObject iApp) {
        this.iApp = iApp;
    }

    @Override
    public AppInstanceId getInstanceId(String instanceName, String userName) {
        return iApp.getInstanceId(instanceName, userName);
    }

    @Override
    public OObjectId getOObjectId(AppInstanceId instanceId, String objectName,
            String userName) {
        return iApp.getOObjectId(instanceId, objectName, userName);
    }

}
