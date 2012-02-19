/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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
package com.javahotel.nmvc.factories.persist;

import com.gwtmodel.table.PersistTypeEnum;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.types.BackAbstract;
import com.javahotel.client.types.BackAbstract.IRunAction;
import com.javahotel.client.types.DataType;
import com.javahotel.client.types.HModelData;
import com.javahotel.client.types.VModelDataFactory;
import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.nmvc.factories.booking.util.BookingCustInfo;
import com.javahotel.nmvc.factories.persist.dict.IHotelPersistFactory;
import com.javahotel.nmvc.factories.persist.dict.IPersistRecord;
import com.javahotel.nmvc.factories.persist.dict.IPersistResult;
import com.javahotel.types.LId;

/**
 * @author hotel
 * 
 */
public class PersistCustomer {

    private final IHotelPersistFactory pFactory;

    public PersistCustomer() {
        pFactory = HInjector.getI().getHotelPersistFactory();
    }

    public interface ISetCustomerId {
        void setC(LId custId);
    }

    private class PersistP implements IPersistResult {

        private final ISetCustomerId sId;

        PersistP(ISetCustomerId sId) {
            this.sId = sId;
        }

        @Override
        public void success(PersistResultContext re) {
            sId.setC(re.getRet().getId());
        }
    }

    public void persistCustomer(PersistTypeEnum action, BookingCustInfo bCust,
            final ISetCustomerId sId) {
        if (!bCust.isChangeCust()) {
            // customer data is not changed
            if (bCust.getCust().getId() != null) {
                // if LId available set immediately and return
                sId.setC(bCust.getCust().getId());
            } else {
                // if not then read using 'name' attribute
                IRunAction<CustomerP> i = new IRunAction<CustomerP>() {

                    @Override
                    public void action(CustomerP t) {
                        sId.setC(t.getId());
                    }

                };
                new BackAbstract<CustomerP>().readAbstract(
                        DictType.CustomerList, bCust.getCust().getName(), i);
            }

            return;
        }
        IPersistRecord re = pFactory.construct(new DataType(
                DictType.CustomerList), false);
        HModelData custH = VModelDataFactory.construct(bCust.getCust());
        re.persist(action, custH, new PersistP(sId));
    }

}
