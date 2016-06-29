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
package com.jythonui.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtmodel.table.binder.BinderWidget;
import com.jythonui.shared.ClientProp;
import com.jythonui.shared.DialogInfo;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.CustomSecurity;
import com.jythonui.shared.RequestContext;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface JythonServiceAsync {

	void getDialogFormat(RequestContext context, String name, AsyncCallback<DialogInfo> callback);

	void runAction(RequestContext context, DialogVariables v, String name, String actionId,
			AsyncCallback<DialogVariables> callback);

	void getClientRes(RequestContext context, AsyncCallback<ClientProp> callback);

	void login(String shiroRealm, String user, String password, CustomSecurity iCustom, AsyncCallback<String> callback);

	void withoutlogin(CustomSecurity iCustom, AsyncCallback<String> callback);

	void logout(RequestContext context, AsyncCallback<Void> callback);

	void readBinderWidget(String fileName, AsyncCallback<BinderWidget> callback);
}
