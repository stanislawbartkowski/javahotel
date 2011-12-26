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
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.slotmodel.SlU;
import com.javahotel.client.types.DataUtil;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.IField;

/**
 * @author hotel
 * 
 */
class DrawAfterSelect {

    private final ISlotable iView;
    private final IDataType iViewType;
    private final MapStringField mapS;
    private final Map<IField, String> bList;
    private final IDataType dType;
    private AbstractTo aObject;

    void copyFields(IVModelData from, AbstractTo dest) {
        for (Entry<IField, String> e : bList.entrySet()) {
            IVField fie = mapS.get(e.getValue());
            Object o = from.getF(fie);
            dest.setF(e.getKey(), o);
        }
    }

    void copyFields() {
        FormLineContainer formL = SlU.getFormLineContainer(iViewType, iView);

        for (Entry<IField, String> e : bList.entrySet()) {
            Object o = aObject.getF(e.getKey());
            IVField fie = mapS.get(e.getValue());
            IFormLineView view = formL.findLineView(fie);
            assert view != null : LogT.getT().cannotBeNull();
            view.setValObj(o);
        }
    }

    private class DrawDataAfterSelect implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            aObject = DataUtil.getData(slContext);
            copyFields();
        }
    }

    ISlotListener constructDrawListener() {
        return new DrawDataAfterSelect();
    }

    DrawAfterSelect(IDataType dType, Map<IField, String> bList,
            MapStringField mapS, ISlotable iView, IDataType iViewType) {
        this.dType = dType;
        this.iView = iView;
        this.iViewType = iViewType;
        this.mapS = mapS;
        this.bList = bList;
    }

    /**
     * @return the aObject
     */
    public AbstractTo getaObject() {
        return aObject;
    }

    /**
     * @param aObject
     *            the aObject to set
     */
    public void setaObject(AbstractTo aObject) {
        this.aObject = aObject;
    }

}
