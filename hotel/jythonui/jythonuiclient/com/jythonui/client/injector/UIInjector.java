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
package com.jythonui.client.injector;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.gwtmodel.table.factories.IDataStoreChanges;
import com.gwtmodel.table.factories.ILaunchPropertyDialogColumn;
import com.gwtmodel.table.factories.ITableAbstractFactories;
import com.gwtmodel.table.smessage.IGetStandardMessage;
import com.jythonui.client.IJythonUIClient;
import com.jythonui.client.enumtypes.IEnumTypesFactory;
import com.jythonui.client.interfaces.IChartManagerFactory;
import com.jythonui.client.interfaces.IDateLineManagerFactory;
import com.jythonui.client.interfaces.IDialogContainerFactory;
import com.jythonui.client.interfaces.IExecuteBackAction;
import com.jythonui.client.interfaces.IExecuteJS;
import com.jythonui.client.interfaces.IFormGridManagerFactory;
import com.jythonui.client.interfaces.IGenCookieName;
import com.jythonui.client.interfaces.IGetDialogFormat;
import com.jythonui.client.interfaces.ILoginPage;
import com.jythonui.client.interfaces.IRegisterCustom;
import com.jythonui.client.interfaces.IRowListDataManagerFactory;
import com.jythonui.client.interfaces.IVariableContainerFactory;
import com.jythonui.client.interfaces.IWebPanelResourcesFactory;
import com.jythonui.client.start.IJythonClientStart;
import com.jythonui.shared.RequestContext;

@GinModules(UIInjectModule.class)
public interface UIInjector extends Ginjector {

	IExecuteJS getExecuteJS();

	IDialogContainerFactory getDialogContainterFactory();

	IDateLineManagerFactory getDateLineManagerFactory();

	IExecuteBackAction getExecuteBackAction();

	IFormGridManagerFactory getFormGridManagerFactory();

	IRowListDataManagerFactory getRowListDataManagerFactory();

	RequestContext getRequestContext();

	ILoginPage getLoginPage();

	IJythonUIClient getJythonUIClient();

	IWebPanelResourcesFactory getWebPanelResourcesFactory();

	IJythonClientStart getJythonClientStart();

	ITableAbstractFactories getTFactories();

	IGetStandardMessage getGetStandardMessage();

	IChartManagerFactory getChartManagerFactory();

	IEnumTypesFactory getEnumTypesFactory();

	IVariableContainerFactory getVariableContainerFactory();

	IGetDialogFormat getDialogFormatHandler();

	IRegisterCustom getRegisterCustom();

	IDataStoreChanges getDataStoreChanges();

	IGenCookieName getGenCookieName();

	ILaunchPropertyDialogColumn getLaunchPropertyDialog();
}
