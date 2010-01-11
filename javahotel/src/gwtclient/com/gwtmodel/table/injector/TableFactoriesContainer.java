/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.injector;

import com.gwtmodel.table.factories.IDataFormConstructorAbstractFactory;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.factories.IDataValidateActionFactory;
import com.gwtmodel.table.factories.IFormDefFactory;
import com.gwtmodel.table.factories.IGetCustomValues;
import com.gwtmodel.table.factories.IGetViewControllerFactory;
import com.gwtmodel.table.factories.IHeaderListFactory;
import com.gwtmodel.table.factories.IPersistFactoryAction;
import com.gwtmodel.table.factories.ITableAbstractFactories;

public class TableFactoriesContainer implements ITableAbstractFactories {

    private IFormDefFactory iFormDefFactory;
    private IPersistFactoryAction iPersistFactory;
    private IHeaderListFactory iheFactory;
    private IDataModelFactory iDataModelFactory;
    private IDataValidateActionFactory iDataValidateFactory;
    private IGetViewControllerFactory iViewFactory;
    private IGetCustomValues iGetCustomValues;
    private IDataFormConstructorAbstractFactory formFactory;

    public IFormDefFactory getFormDefFactory() {
        return iFormDefFactory;
    }

    public IPersistFactoryAction getPersistFactoryAction() {
        return iPersistFactory;
    }

    public IHeaderListFactory getHeaderListFactory() {
        return iheFactory;
    }

    public void registerFormDefFactory(IFormDefFactory iFormDefFactory) {
        this.iFormDefFactory = iFormDefFactory;

    }

    public void registerPersistFactory(IPersistFactoryAction iPersistFactory) {
        this.iPersistFactory = iPersistFactory;
    }

    public void registerHeaderListFactory(IHeaderListFactory iheFactory) {
        this.iheFactory = iheFactory;
    }

    public void registerDataModelFactory(IDataModelFactory iDataModelFactory) {
        this.iDataModelFactory = iDataModelFactory;
    }

    public IDataModelFactory getDataModelFactory() {
        return iDataModelFactory;
    }

    public IDataValidateActionFactory getDataValidateFactory() {
        return iDataValidateFactory;
    }

    public void registerDataValidateActionFactory(
            IDataValidateActionFactory iDataValidateFactory) {
        this.iDataValidateFactory = iDataValidateFactory;
    }

    public void registerGetViewControllerFactory(
            IGetViewControllerFactory iViewFactory) {
        this.iViewFactory = iViewFactory;
    }

    public IGetViewControllerFactory getGetViewControllerFactory() {
        return iViewFactory;
    }

    public void registerGetCustomValues(IGetCustomValues iGetCustomValues) {
        this.iGetCustomValues = iGetCustomValues;
    }

    public IGetCustomValues getGetCustomValues() {
        return iGetCustomValues;
    }

    public void registerDataFormConstructorAbstractFactory(
            IDataFormConstructorAbstractFactory cFactory) {
        formFactory = cFactory;
    }

    public IDataFormConstructorAbstractFactory getDataFormConstructorAbstractFactory() {
        return formFactory;
    }

}
