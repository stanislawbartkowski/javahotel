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
package com.javahotel.client.mvc.table.model;

import java.util.List;

import com.google.inject.Inject;
import com.javahotel.client.IResLocator;
import com.javahotel.common.toobject.AbstractTo;

/**
 *
 * @author hotel
 */
public class TableModelFactory {

    private final IResLocator rI;
    
    @Inject
    public TableModelFactory(IResLocator rI) {
        this.rI = rI;
    }

    public ITableModel getModel(final List<ColTitle> cTitle, final AbstractTo.IFieldToS iToS,
            final String header) {
        return new TableList(rI, cTitle, iToS, header,null,null);
    }

    public ITableModel getModel(final List<ColTitle> cTitle, final AbstractTo.IFieldToS iToS,
            final String header,ITableFilter iF,ITableConverter iConv) {
        return new TableList(rI, cTitle, iToS, header,iF,iConv);
    }
}
