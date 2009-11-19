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
package com.javahotel.client.mvc.persistrecord;

import java.util.ArrayList;
import java.util.List;

import com.javahotel.client.IResLocator;
import com.javahotel.client.mvc.checkmodel.ICheckDictModel;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.rdata.RData.IVectorList;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.DictionaryP;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
abstract class AbstractPersistRecordDict extends PersistRecordDict {

    private final DictType serv;

    AbstractPersistRecordDict(final IResLocator rI, DictType d, DictType serv) {
        super(rI, d);
        this.serv = serv;
    }

    protected abstract List<? extends DictionaryP> createNew();

    protected abstract void setRes(RecordModel mo,
            List<? extends DictionaryP> col);

    private class SetD implements IVectorList {

        private final IPersistResult ires;
        private final List<String> dic;
        private final int action;
        private final RecordModel mo;

        SetD(final IPersistResult ires,
                final List<String> dic, final int action,
                final RecordModel mo) {
            this.ires = ires;
            this.dic = dic;
            this.action = action;
            this.mo = mo;
        }

        public void doVList(final List<? extends AbstractTo> val) {
            List<DictionaryP> dcol = (List<DictionaryP>) createNew();
            for (String s : dic) {
                for (AbstractTo a : val) {
                    DictionaryP se = (DictionaryP) a;
                    if (se.getName().equals(s)) {
                        dcol.add(se);
                    }
                }
            }
            setRes(mo, dcol);
            ipersist(action, mo, ires);
        }
    }

    @Override
    public void persist(int action, RecordModel mo, IPersistResult ires) {
        ICheckDictModel i = (ICheckDictModel) mo.getAuxData();
        CommandParam p = rI.getR().getHotelCommandParam();
        p.setDict(serv);
        List<String> dic = i.getValues();
        rI.getR().getList(RType.ListDict, p, new SetD(ires, dic, action, mo));
    }
}
