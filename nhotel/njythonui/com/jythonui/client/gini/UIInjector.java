/*
 * Copyright 2017 stanislawbartkowski@gmail.com  
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
package com.jythonui.client.gini;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.jythonui.client.dialog.IReadDialog;
import com.jythonui.client.smessage.IGetDisplayMess;
import com.jythonui.client.smessage.IGetStandardMessage;
import com.jythonui.client.start.IJythonClientStart;
import com.jythonui.shared.RequestContext;
import com.polymerui.client.binder.ICreateBinderWidget;

@GinModules(UIInjectModule.class)
public interface UIInjector extends Ginjector {

	IJythonClientStart getJythonClientStart();

	RequestContext getRequestContext();
	
	IReadDialog getReadDialog();
	
	IGetDisplayMess getGetDisplayMess();
	
	IGetStandardMessage getGetStandardMessage();
	
	ICreateBinderWidget getCreateBinderWidget();

}
