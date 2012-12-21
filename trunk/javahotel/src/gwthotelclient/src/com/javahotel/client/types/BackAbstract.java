/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.types;

import com.gwtmodel.table.injector.LogT;
import com.javahotel.client.IResLocator;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.rdata.RData;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.types.LId;

/**
 * @author hotel
 * 
 */
public class BackAbstract<T extends AbstractTo> {

    public interface IRunAction<T> {
        void action(T t);
    }

    private class R implements RData.IOneList<T> {

        private final IRunAction<T> i;

        R(IRunAction<T> i) {
            this.i = i;
        }

        public void doOne(T a) {
            assert a != null : LogT.getT().cannotBeNull();
            i.action(a);
        }
    }

    public void readAbstract(DictType d, String name, IRunAction<T> i) {
        IResLocator rI = HInjector.getI().getI();
        CommandParam p = rI.getR().getHotelDictName(d, name);
        rI.getR().getOne(RType.ListDict, p, new R(i));

    }

    public void readAbstract(DictType d, LId id, IRunAction<T> i) {
        IResLocator rI = HInjector.getI().getI();
        CommandParam p = rI.getR().getHotelDictId(d, id);
        rI.getR().getOne(RType.ListDict, p, new R(i));

    }

}
