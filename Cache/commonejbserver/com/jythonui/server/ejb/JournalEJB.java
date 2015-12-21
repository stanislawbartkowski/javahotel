/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.defa.GuiceInterceptor;
import com.jythonui.server.ejb.crud.AbstractCrudEJB;
import com.jythonui.server.journal.IJournal;
import com.jythonui.server.journal.JournalRecord;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@EJB(name = ISharedConsts.COMMONNOJOURNALJNDI, beanInterface = IJournal.class)
@Remote
@Interceptors(value = { GuiceInterceptor.class })
public class JournalEJB extends AbstractCrudEJB<JournalRecord> implements IJournal {

	@Inject
	public void injectHotelServices(IJournal injectedServices) {
		service = injectedServices;
	}

}
