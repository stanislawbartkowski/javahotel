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

package com.javahotel.client.mvc.persistrecord;

import com.gwtmodel.table.view.callback.CommonCallBack;
import com.javahotel.client.GWTGetService;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.IPersistAction;
import com.javahotel.client.mvc.crud.controler.RecordModel;
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

    abstract protected void persistDict(final DictType d, final DictionaryP dP,
            final CommonCallBack b);

    private class CallB extends CommonCallBack<ReturnPersist> {

        private final IPersistResult res;
        private final int action;
        private final AbstractTo a;

        CallB(final IPersistResult res, int action, AbstractTo a) {
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

    APersistRecordDict(final IResLocator rI, final DictType d) {
        this.rI = rI;
        this.d = d;
    }

    public void persist(int action, RecordModel a, IPersistResult ires) {
        ipersist(action, a, ires);
    }

    protected void ipersist(int action, RecordModel a, IPersistResult ires) {
        DictionaryP di = (DictionaryP) a.getA();
        String ho = rI.getR().getHotel();
        di.setHotel(ho);
        switch (action) {
        case IPersistAction.ADDACION:
        case IPersistAction.MODIFACTION:
            persistDict(d, di, new CallB(ires, action, di));
            break;
        case IPersistAction.DELACTION:
            GWTGetService.getService().removeDict(d, di,
                    new CallB(ires, action, di));
            break;
        default:
            assert false : action + " invalid action code";
            break;
        }
    }
}
