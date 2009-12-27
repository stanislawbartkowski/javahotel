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
package com.gwtmodel.table.controler;

import com.google.inject.Inject;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonFactory;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.injector.TableFactoriesContainer;
import com.gwtmodel.table.injector.TablesFactories;

public class TableDataControlerFactory {

    private final TablesFactories tFactories;
    private final TableFactoriesContainer fContainer;
    private final ControlButtonFactory cButtonFactory;

    @Inject
    public TableDataControlerFactory(TablesFactories tFactories,
            TableFactoriesContainer fContainer,
            ControlButtonFactory cButtonFactory) {
        this.tFactories = tFactories;
        this.fContainer = fContainer;
        this.cButtonFactory = cButtonFactory;
    }

    public IDataControler constructDataControler(IDataType dType, int panelId,
            int cellIdFirst) {
        ListOfControlDesc cList = cButtonFactory.constructCrudList();
        return new DisplayListControler(tFactories, fContainer, dType, panelId,
                cellIdFirst, cList, new DataListCrudControler(tFactories,
                        fContainer, dType));
    }

}
