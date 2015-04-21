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
package com.gwtmodel.table.factories;

public interface ITableAbstractFactories {

	void registerDataModelFactory(IDataModelFactory iDataModelFactory);

	void registerDataValidateActionFactory(
			IDataValidateActionFactory iDataValidateFactory);

	void registerFormTitleFactory(IFormTitleFactory iFormDefFactory);

	void registerHeaderListFactory(IHeaderListFactory iheFactory);

	void registerPersistFactory(IPersistFactoryAction iPersistFactory);

	void registerGetViewControllerFactory(IGetViewControllerFactory iFactory);

	void registerGetCustomValues(IGetCustomValues iGetCustomValues);

	void registerDataFormConstructorAbstractFactory(
			IDataFormConstructorAbstractFactory cFactory);

	void registerJavaMailActionFactory(IJavaMailActionFactory mAction);

	void registerDataCrudModifButtonActionFactory(
			IDataCrudModifButtonActionFactory crudModifButtonAction);

	void registerWebPanelResources(IWebPanelResources wPanel);

	void registerDataStoreChanges(IDataStoreChanges iChanges);
}
