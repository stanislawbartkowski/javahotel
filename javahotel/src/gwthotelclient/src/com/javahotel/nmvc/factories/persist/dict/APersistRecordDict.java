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

package com.javahotel.nmvc.factories.persist.dict;

import com.gwtmodel.table.PersistTypeEnum;
import com.gwtmodel.table.view.callback.CommonCallBack;
import com.javahotel.client.GWTGetService;
import com.javahotel.client.IResLocator;
import com.javahotel.client.types.DataUtil;
import com.javahotel.client.types.HModelData;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.DictionaryP;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
abstract class APersistRecordDict implements IPersistRecord {

    protected final IResLocator rI;
    private final DictType d;
    private final boolean validate;

    abstract protected void persistDict(final DictType d, final DictionaryP dP,
            final CommonCallBack<ReturnPersist> b);

    private class CallB extends CommonCallBack<ReturnPersist> {

        private final IPersistResult res;
        private final PersistTypeEnum action;
        private final AbstractTo a;

        CallB(final IPersistResult res, PersistTypeEnum action, AbstractTo a) {
            this.res = res;
            this.action = action;
            this.a = a;
        }

        @Override
        public void onMySuccess(ReturnPersist pe) {
            rI.getR().invalidateCacheList();
            CallSuccess.callI(res, action, a, pe);
        }
    }

    APersistRecordDict(final IResLocator rI, final DictType d, boolean validate) {
        this.rI = rI;
        this.d = d;
        this.validate = validate;
    }

    @Override
    public void persist(PersistTypeEnum action, HModelData ho,
            IPersistResult ires) {
        ipersist(action, ho.getA(), ires);
    }

    protected void ipersist(PersistTypeEnum action, AbstractTo a,
            IPersistResult ires) {
        DictionaryP di = (DictionaryP) a;
        String ho = rI.getR().getpRoles().getHotel();
        di.setHotel(ho);
        CommonCallBack<ReturnPersist> ca = new CallB(ires, action, di);
        if (validate) {
            GWTGetService.getService().validateDictPersist(
                    DataUtil.persistTo(action), d, di, ca);
            return;
        }
        switch (action) {
        case ADD:
        case MODIF:
            persistDict(d, di, ca);
            break;
        case REMOVE:
            GWTGetService.getService().removeDict(d, di, ca);
            break;
        default:
            assert false : action + " invalid action code";
            break;
        }
    }
}
