/*
 *  Copyright 2010 stanislawbartkowski@gmail.com
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package com.gwtmodel.table.datelist;

import com.gwtmodel.table.DataListTypeFactory;
import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hotel
 */
public class DatePeriodListFactory {

    public IDatePeriodList construct(String title, IDatePeriodFactory eFactory,
            ISlotSignaller setGwt) {
        return new DatePeriodList(title, eFactory, setGwt);
    }

    public IDataListType construct(List<AbstractDatePeriodE> li) {
        List<IVModelData> a = new ArrayList<IVModelData>();
        a.addAll(li);
        return DataListTypeFactory.construct(a);
    }
}
