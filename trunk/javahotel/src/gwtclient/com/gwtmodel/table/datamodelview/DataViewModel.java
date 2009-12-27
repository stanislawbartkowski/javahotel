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
package com.gwtmodel.table.datamodelview;

import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.view.form.GwtFormViewFactory;
import com.gwtmodel.table.view.form.IGwtFormView;

class DataViewModel extends AbstractSlotContainer implements IDataViewModel {

    private final FormLineContainer fContainer;
    private final IGwtFormView gView;

    DataViewModel(GwtFormViewFactory gFactory,int cellId, FormLineContainer fContainer) {
        this.fContainer = fContainer;
        gView = gFactory.construct(fContainer);
        createCallBackWidget(cellId);        
    }

    public void fromViewToData(IVModelData aTo) {
        for (FormField d : fContainer.getfList()) {
            String s = d.getELine().getVal();
            aTo.setS(d.getFie(), s);
        }
    }

    public void fromDataToView(IVModelData aFrom) {
        for (FormField d : fContainer.getfList()) {
            String s = aFrom.getS(d.getFie());
            d.getELine().setVal(s);
        }
    }

    public void startPublish() {
        publishCallBack(gView);
    }

}
