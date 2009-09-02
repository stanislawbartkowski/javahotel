/*
 * Copyright 2009 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.dialog.user.booking;

import com.javahotel.client.IResLocator;
import com.javahotel.client.ifield.IChangeListener;
import com.javahotel.client.ifield.ILineField;
import com.javahotel.client.mvc.auxabstract.ABillsCustomer;
import com.javahotel.client.mvc.auxabstract.BillsCustomer;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.dictcrud.controler.IBeforeViewSignal;
import com.javahotel.client.mvc.dictcrud.controler.IModifRecordDef;
import com.javahotel.client.mvc.dictcrud.controler.RecordAuxParam;
import com.javahotel.client.mvc.record.model.RecordField;
import com.javahotel.client.rdata.RData;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.util.StringU;
import java.util.ArrayList;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class ListenerCustomer {

    private final IResLocator rI;

    ListenerCustomer(IResLocator rI) {
        this.rI = rI;
    }

    private class BackCC implements RData.IOneList {

        private final RecordModel a;
        @SuppressWarnings("unused")
        private final ArrayList<RecordField> dict;

        BackCC(RecordModel a, ArrayList<RecordField> dict) {
            this.a = a;
            this.dict = dict;
        }

        public void doOne(AbstractTo val) {
            CustomerP cust = (CustomerP) val;
            ABillsCustomer aB = (ABillsCustomer) a.getA();
            BillsCustomer bb = (BillsCustomer) aB.getO();
            bb.getCust().copyFrom(cust);
        }
    }

    private class ChangeC implements IChangeListener {

        private final RecordModel a;
        private final ArrayList<RecordField> dict;

        ChangeC(RecordModel a, ArrayList<RecordField> dict) {
            this.a = a;
            this.dict = dict;
        }

        public void onChange(ILineField sender) {
            ILineField eL = (ILineField) sender;
            String name = eL.getVal();
            if (StringU.isEmpty(name)) {
                return;
            }
            CommandParam pa = rI.getR().getHotelDictName(
                    DictType.CustomerList, name);
            rI.getR().getOne(RType.ListDict, pa, new BackCC(a, dict));
        }
    }

    private class Modif implements IModifRecordDef {

        private RecordModel a;

        public void modifRecordDef(ArrayList<RecordField> dict) {
            for (RecordField re : dict) {
                if (re.getFie() == DictionaryP.F.name) {
                    ILineField eL = re.getELine();
                    eL.setChangeListener(new ChangeC(a, dict));
                }
            }
        }
    }

    private class BeforeV implements IBeforeViewSignal {

        private final Modif mo;

        BeforeV(Modif mo) {
            this.mo = mo;
        }

        public void signal(RecordModel a) {
            mo.a = a;
        }
    }

    void modifAux(RecordAuxParam aux) {
        Modif mod = new Modif();
        BeforeV bef = new BeforeV(mod);
        aux.setModifD(mod);
        aux.setBSignal(bef);
    }
}
