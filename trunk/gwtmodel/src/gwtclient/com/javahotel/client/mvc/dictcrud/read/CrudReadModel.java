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
package com.javahotel.client.mvc.dictcrud.read;

import java.util.List;

import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.mvc.crud.controler.ICrudReadModel;
import com.javahotel.client.mvc.crud.controler.ITableModelSignalRead;
import com.javahotel.client.mvc.table.model.ITableModel;
import com.javahotel.client.rdata.RData;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.toobject.AbstractTo;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class CrudReadModel implements ICrudReadModel {

    private final IResLocator rI;
    private final DictData da;

    CrudReadModel(final IResLocator rI, final DictData da) {
        this.rI = rI;
        this.da = da;
    }

    class DrawCol implements RData.IVectorList {

        private final ITableModelSignalRead signal;
        private final ITableModel model;

        DrawCol(final ITableModelSignalRead signal,
                final ITableModel model) {
            this.signal = signal;
            this.model = model;
        }

        public void doVList(final List<? extends AbstractTo> val) {
            model.setList(val);
            signal.successRead();
        }
    }

    public void readModel(ITableModel model, ITableModelSignalRead signal) {
        CommandParam co = null;
        if (da.getD() != null) {
            co = rI.getR().getHotelCommandParam();
            co.setDict(da.getD());
        }
        rI.getR().getList(da.getRt(), co, new DrawCol(signal, model));
    }
}
