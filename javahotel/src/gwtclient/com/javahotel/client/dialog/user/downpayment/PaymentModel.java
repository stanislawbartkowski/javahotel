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
package com.javahotel.client.dialog.user.downpayment;

import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.idialog.GetIEditFactory;
import com.javahotel.client.ifield.ILineField;
import com.javahotel.client.mvc.crudtable.controler.CrudTableControlerParam;
import com.javahotel.client.mvc.crudtable.controler.ICrudTableControler;
import com.javahotel.client.mvc.dictdata.model.IAdvancePaymentModel;
import com.javahotel.client.mvc.table.view.ITableSignalClicked;
import com.javahotel.client.mvc.tablecrud.controler.TableDictCrudControlerFactory;
import com.javahotel.common.command.RType;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class PaymentModel implements IAdvancePaymentModel {

    @SuppressWarnings("unused")
	private final IResLocator rI;
    private final ILineField dFrom;
    private final ILineField dTo;
    private final ICrudTableControler iTable;

    PaymentModel(IResLocator rI, ITableSignalClicked iT) {
        this.rI = rI;
        dFrom = GetIEditFactory.getTextCalendard(rI);
        dTo = GetIEditFactory.getTextCalendard(rI);
        CrudTableControlerParam pa = new CrudTableControlerParam();
        pa.setSc(iT);
        iTable = TableDictCrudControlerFactory.getCrud(rI,
                new DictData(RType.DownPayments), this, pa);
    }

    /**
     * @return the dFrom
     */
    public ILineField getDFrom() {
        return dFrom;
    }

    /**
     * @return the dTo
     */
    public ILineField getDTo() {
        return dTo;
    }

    /**
     * @return the iTable
     */
    public ICrudTableControler getITable() {
        return iTable;
    }
}
