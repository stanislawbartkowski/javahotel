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

import java.util.List;

import com.google.inject.Inject;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.injector.TablesFactories;
import com.gwtmodel.table.persist.IDataPersistAction;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.view.table.VListHeaderDesc;

public class TableDataControlerFactory {

    private final TablesFactories tFactories;

    @Inject
    public TableDataControlerFactory(TablesFactories tFactories) {
        this.tFactories = tFactories;
    }

    public IDataControler constructDataControler(IDataType dType, int panelId,
            int cellIdFirst, List<VListHeaderDesc> heList,
            ListOfControlDesc cList, IDataPersistAction persistA) {
        return new DisplayListControler(tFactories, dType, panelId,
                cellIdFirst, heList, cList, persistA, new DataListCrudControler(dType));
    }

}
