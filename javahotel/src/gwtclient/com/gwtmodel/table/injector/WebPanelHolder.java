/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
import com.gwtmodel.table.factories.IFormTitleFactory;
import com.gwtmodel.table.factories.IGetCustomValues;
import com.gwtmodel.table.factories.IGetViewControllerFactory;
import com.gwtmodel.table.factories.IHeaderListFactory;
import com.gwtmodel.table.factories.IPersistFactoryAction;
import com.gwtmodel.table.factories.ITableAbstractFactories;
import com.gwtmodel.table.factories.ITableCustomFactories;
import com.gwtmodel.table.view.webpanel.IWebPanel;

public class WebPanelHolder {

    private static class TableFactoriesContainer implements ITableAbstractFactories, ITableCustomFactories {

        private IFormTitleFactory iFormDefFactory;
        private IPersistFactoryAction iPersistFactory;
        private IHeaderListFactory iheFactory;
        private IDataModelFactory iDataModelFactory;
        private IDataValidateActionFactory iDataValidateFactory;
        private IGetViewControllerFactory iViewFactory;
        private IGetCustomValues iGetCustomValues;
        private IDataFormConstructorAbstractFactory formFactory;

        private TableFactoriesContainer() {
        }

        @Override
        public IFormTitleFactory getFormDefFactory() {
            return iFormDefFactory;
        }

        @Override
        public IPersistFactoryAction getPersistFactoryAction() {
            return iPersistFactory;
        }

        @Override
        public IHeaderListFactory getHeaderListFactory() {
            return iheFactory;
        }

        @Override
        public void registerFormDefFactory(IFormTitleFactory iFormDefFactory) {
            this.iFormDefFactory = iFormDefFactory;

        }

        @Override
        public void registerPersistFactory(IPersistFactoryAction iPersistFactory) {
            this.iPersistFactory = iPersistFactory;
        }

        @Override
        public void registerHeaderListFactory(IHeaderListFactory iheFactory) {
            this.iheFactory = iheFactory;
        }

        @Override
        public void registerDataModelFactory(IDataModelFactory iDataModelFactory) {
            this.iDataModelFactory = iDataModelFactory;
        }

        @Override
        public IDataModelFactory getDataModelFactory() {
            return iDataModelFactory;
        }

        @Override
        public IDataValidateActionFactory getDataValidateFactory() {
            return iDataValidateFactory;
        }

        @Override
        public void registerDataValidateActionFactory(
                IDataValidateActionFactory iDataValidateFactory) {
            this.iDataValidateFactory = iDataValidateFactory;
        }

        @Override
        public void registerGetViewControllerFactory(
                IGetViewControllerFactory iViewFactory) {
            this.iViewFactory = iViewFactory;
        }

        @Override
        public IGetViewControllerFactory getGetViewControllerFactory() {
            return iViewFactory;
        }

        @Override
        public void registerGetCustomValues(IGetCustomValues iGetCustomValues) {
            this.iGetCustomValues = iGetCustomValues;
        }

        @Override
        public IGetCustomValues getGetCustomValues() {
            return iGetCustomValues;
        }

        @Override
        public void registerDataFormConstructorAbstractFactory(
                IDataFormConstructorAbstractFactory cFactory) {
            formFactory = cFactory;
        }

        @Override
        public IDataFormConstructorAbstractFactory getDataFormConstructorAbstractFactory() {
            return formFactory;
        }
    }
    private static IWebPanel iWeb;
    private static boolean googletable;
    private static final TableFactoriesContainer factoriesContainer;

    static {
        factoriesContainer = new TableFactoriesContainer();
    }

    /**
     * @return the googletable
     */
    public static boolean isGoogletable() {
        return googletable;
    }

    /**
     * @param aGoogletable the googletable to set
     */
    public static void setGoogletable(boolean aGoogletable) {
        googletable = aGoogletable;
    }

    /**
     * @return the factoriesContainer
     */
    static ITableCustomFactories getFactoriesContainer() {
        return factoriesContainer;
    }

    static ITableAbstractFactories getAbstractFactoriesContainer() {
        return factoriesContainer;
    }

    private WebPanelHolder() {
    }

    public static void setWebPanel(IWebPanel i) {
        iWeb = i;
    }

    static IWebPanel getWebPanel() {
        return iWeb;
    }
}
