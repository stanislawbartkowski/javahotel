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

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import com.google.inject.Inject;

import javax.interceptor.Interceptors;

import com.jython.ui.shared.ISharedConsts;
import com.jythonui.server.defa.GuiceInterceptor;
import com.jythonui.server.storage.blob.IBlobHandler;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@EJB(name = ISharedConsts.COMMONBEANBLOBJNDI, beanInterface = IBlobHandler.class)
@Remote
@Interceptors(value = { GuiceInterceptor.class })
public class StorageBlobRegistryEJB extends AbstractStorageBlobRegistry
        implements IBlobHandler {

    @Inject
    public void injectJpaRegistryEJB(IBlobHandler iBlob) {
        this.iBlob = iBlob;
    }

}
