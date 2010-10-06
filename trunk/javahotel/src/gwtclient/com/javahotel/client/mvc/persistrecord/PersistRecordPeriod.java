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
package com.javahotel.client.mvc.persistrecord;

import com.javahotel.client.mvc.auxabstract.PeOfferNumTo;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.persistrecord.PersistNumUtil.IGenNextLp;
import com.javahotel.client.mvc.dictdata.model.ISeasonOffModel;
import com.javahotel.client.mvc.table.model.ITableModel;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class PersistRecordPeriod implements IPersistRecord {

    private class GetNext implements IGenNextLp {

        private final ISeasonOffModel of;

        GetNext(ISeasonOffModel of) {
            this.of = of;
        }

        public Integer getNext() {
            IGenNextLp m1 = new PersistNumUtil.GenNextLp(
                    of.getTable1().getTableView().getModel());
            IGenNextLp m2 = new PersistNumUtil.GenNextLp(
                    of.getTable2().getTableView().getModel());
            Integer i1 = m1.getNext();
            Integer i2 = m2.getNext();
            if (i1.compareTo(i2) == -1) {
                return i2;
            }
            return i1;
        }
    }

    public void persist(int action, RecordModel a, IPersistResult ires) {
        PeOfferNumTo pe = (PeOfferNumTo) a.getA();
        ISeasonOffModel of = (ISeasonOffModel) a.getAuxData1();

        ITableModel mo = (ITableModel) a.getAuxData();
        PersistNumUtil.persist(action, mo, new GetNext(of), pe);
        CallSuccess.callI(ires, action, pe,null);
    }
}