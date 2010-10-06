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

package com.javahotel.client.mvc.crudtable.controler;

import com.google.inject.Inject;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.mvc.table.model.ITableModel;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class CrudTableControlerFactory {

    private final IResLocator rI;
    
    @Inject
    public CrudTableControlerFactory(final IResLocator rI) {
        this.rI = rI;

    }

    public ICrudTableControler getCrudTable(final DictData da, final ITableModel mo,
            final CrudTableControlerParam pa) {
        return new CrudTableControler(rI, da, mo, pa);
    }
}
