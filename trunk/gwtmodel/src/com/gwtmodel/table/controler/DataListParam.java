/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.controler;

import com.gwtmodel.table.factories.IDataCrudModifButtonAction;
import com.gwtmodel.table.factories.IDataCrudModifButtonActionFactory;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.factories.IDataPersistAction;
import com.gwtmodel.table.factories.IFormTitleFactory;
import com.gwtmodel.table.factories.IGetViewControllerFactory;
import com.gwtmodel.table.factories.IHeaderListContainer;

public class DataListParam {

    private final IDataPersistAction persistA;
    private final IHeaderListContainer heList;
    private final IDataModelFactory dataFactory;
    private final IFormTitleFactory formFactory;
    private final IGetViewControllerFactory fControler;
    private final IDataCrudModifButtonActionFactory modifButtonFactory;

    public IFormTitleFactory getFormFactory() {
        return formFactory;
    }

    public DataListParam(IDataPersistAction persistA,
            IHeaderListContainer heList, IDataModelFactory dataFactory,
            IFormTitleFactory formFactory, IGetViewControllerFactory fControler,
            IDataCrudModifButtonActionFactory modifButtonFactory) {
        this.persistA = persistA;
        this.heList = heList;
        this.dataFactory = dataFactory;
        this.formFactory = formFactory;
        this.fControler = fControler;
        this.modifButtonFactory = modifButtonFactory;
    }

    public DataListParam(IDataPersistAction persistA,
            IHeaderListContainer heList, IDataModelFactory dataFactory,
            IFormTitleFactory formFactory, IGetViewControllerFactory fControler) {
        this(persistA, heList, dataFactory, formFactory, fControler, null);
    }

    public IGetViewControllerFactory getfControler() {
        return fControler;
    }

    public IDataModelFactory getDataFactory() {
        return dataFactory;
    }

    public IHeaderListContainer getHeList() {
        return heList;
    }

    public IDataPersistAction getPersistA() {
        return persistA;
    }

    /**
     * @return the modifButton
     */
    public IDataCrudModifButtonActionFactory getModifButtonFactory() {
        return modifButtonFactory;
    }
}
