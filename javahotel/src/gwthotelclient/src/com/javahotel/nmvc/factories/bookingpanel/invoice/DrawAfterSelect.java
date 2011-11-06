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
package com.javahotel.nmvc.factories.bookingpanel.invoice;

import java.util.Map;
import java.util.Map.Entry;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.datamodelview.IDataViewModel;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.SlU;
import com.javahotel.client.types.DataUtil;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.IField;

/**
 * @author hotel
 * 
 */
class DrawAfterSelect {

    private final IDataViewModel iView;
    private final IDataType iViewType;
    private final MapStringField mapS;
    private final Map<IField, String> bList;
    private final IDataType dType;

    private class DrawHotelAfterSelect implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            AbstractTo cust = DataUtil.getData(slContext);
            FormLineContainer formL = SlU
                    .getFormLineContainer(iViewType, iView);

            for (Entry<IField, String> e : bList.entrySet()) {
                Object o = cust.getF(e.getKey());
                IVField fie = mapS.get(e.getValue());
                IFormLineView view = formL.findLineView(fie);
                view.setValObj(o);
            }

        }

    }

    ISlotListener constructDrawListener() {
        return new DrawHotelAfterSelect();
    }

    DrawAfterSelect(IDataType dType, Map<IField, String> bList,
            MapStringField mapS, IDataViewModel iView, IDataType iViewType) {
        this.dType = dType;
        this.iView = iView;
        this.iViewType = iViewType;
        this.mapS = mapS;
        this.bList = bList;
    }

}
