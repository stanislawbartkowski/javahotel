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
package com.javahotel.nmvc.ewidget;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.DataListTypeFactory;
import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IGetDataList;
import com.gwtmodel.table.IGetDataListCallBack;
import com.gwtmodel.table.IVModelData;
import com.javahotel.client.IResLocator;
import com.javahotel.client.rdata.RData;
import com.javahotel.client.types.VField;
import com.javahotel.client.types.VModelDataFactory;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.IField;

class GetCommandList implements IGetDataList {

    private final IResLocator rI;
    private final RType r;
    private final CommandParam p;
    private final IField f;

    private static class R implements RData.IVectorList {

        private final IField f;
        private final IGetDataListCallBack iCallBack;

        R(final IField f, IGetDataListCallBack iCallBack) {
            this.f = f;
            this.iCallBack = iCallBack;
        }

        @Override
        public void doVList(final List<? extends AbstractTo> val) {
            List<IVModelData> dList = new ArrayList<IVModelData>();
            for (AbstractTo a : val) {
                dList.add(VModelDataFactory.construct(a));
            }
            IDataListType dataList = DataListTypeFactory.construct(dList,
                    new VField(f));
            iCallBack.set(dataList);
        }
    }

    GetCommandList(final IResLocator rI, final RType r, final CommandParam p,
            final IField f) {
        this.rI = rI;
        this.r = r;
        this.p = p;
        this.f = f;
    }

    @Override
    public void call(IGetDataListCallBack iCallBack) {
        rI.getR().getList(r, p, new R(f, iCallBack));
    }

}
