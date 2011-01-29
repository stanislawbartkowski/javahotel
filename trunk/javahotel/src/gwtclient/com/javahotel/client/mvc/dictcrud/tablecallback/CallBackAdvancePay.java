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
package com.javahotel.client.mvc.dictcrud.tablecallback;

import com.javahotel.client.IResLocator;
import com.javahotel.client.mvc.auxabstract.AdvancePaymentCustomer;
import com.javahotel.client.mvc.table.model.ITableModel;
import com.javahotel.client.mvc.table.view.ITableCallBackSetField;
import com.javahotel.client.mvc.table.view.ITableSetField;
import com.javahotel.client.rdata.RData.IOneList;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.IField;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class CallBackAdvancePay implements ITableCallBackSetField {

    private final IResLocator rI;

    CallBackAdvancePay(IResLocator rI) {
        this.rI = rI;
    }

    private class CallBackCustOne implements IOneList<CustomerP> {

        private final ITableModel a;
        private final int row;
        private final IField f;
        private final ITableSetField iSet;

        CallBackCustOne(final ITableModel a, int row, final IField f,
                final ITableSetField iSet) {
            this.a = a;
            this.f = f;
            this.row = row;
            this.iSet = iSet;
        }

        public void doOne(CustomerP val) {
            AdvancePaymentCustomer pa = (AdvancePaymentCustomer) a.getRow(row);
            pa.setCust(val);
            String s = a.getField(row, f);
            iSet.setField(s);
        }
    }

    public void CallSetField(ITableModel a, int row, IField f,
            ITableSetField iSet) {
        AdvancePaymentCustomer pa = (AdvancePaymentCustomer) a.getRow(row);
        CommandParam par = rI.getR().getHotelDictId(DictType.CustomerList,
                pa.getCustomerId());
        rI.getR().getOne(RType.ListDict, par,
                new CallBackCustOne(a, row, f, iSet));
    }
}
