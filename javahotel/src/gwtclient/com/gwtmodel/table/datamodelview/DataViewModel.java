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

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.InvalidateFormContainer;
import com.gwtmodel.table.PersistTypeEnum;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.injector.TableFactoriesContainer;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotCaller;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.view.form.GwtFormViewFactory;
import com.gwtmodel.table.view.form.IGwtFormView;

class DataViewModel extends AbstractSlotContainer implements IDataViewModel {

    private final FormLineContainer fContainer;
    private final IGwtFormView gView;
    private final IDataModelFactory dFactory;

    private class ChangeMode implements ISlotSignaller {

        public void signal(ISlotSignalContext slContext) {
            PersistTypeEnum persistTypeEnum = slContext.getPersistType();
            for (FormField fie : fContainer.getfList()) {
                IFormLineView vie = fie.getELine();
                switch (persistTypeEnum) {
                case ADD:
                    break;
                case MODIF:
                    if (fie.isReadOnlyIfModif()) {
                        vie.setReadOnly(true);
                    }
                    break;
                case REMOVE:
                    vie.setReadOnly(true);
                    break;
                }
            }
        }

    }

    private class DrawModel implements ISlotSignaller {

        public void signal(ISlotSignalContext slContext) {
            IVModelData mData = slContext.getVData();
            fromDataToView(mData);
        }

    }

    private class GetterModel implements ISlotCaller {

        public ISlotSignalContext call(ISlotSignalContext slContext) {
            IVModelData mData = slContext.getVData();
            fromViewToData(mData);
            return slContext;
        }

    }

    private class InvalidateMess implements ISlotSignaller {

        public void signal(ISlotSignalContext slContext) {
            InvalidateFormContainer errContainer = (InvalidateFormContainer) slContext
                    .getValidateError();
            gView.showInvalidate(errContainer);
        }

    }

    DataViewModel(GwtFormViewFactory gFactory, IDataType dType,
            FormLineContainer fContainer, TableFactoriesContainer cFactories) {
        this.fContainer = fContainer;
        gView = gFactory.construct(fContainer);
        dFactory = cFactories.getDataModelFactory();
        registerSubscriber(DataActionEnum.ChangeViewFormToInvalidAction, dType,
                new InvalidateMess());
        registerSubscriber(DataActionEnum.ChangeViewFormModeAction, dType,
                new ChangeMode());
        registerSubscriber(DataActionEnum.DrawViewFormAction, dType,
                new DrawModel());
        registerCaller(GetActionEnum.GetViewModelEdited, dType,
                new GetterModel());
    }

    private void fromViewToData(IVModelData aTo) {
        // for (FormField d : fContainer.getfList()) {
        // String s = d.getELine().getVal();
        // aTo.setS(d.getFie(), s);
        // }
        dFactory.fromViewToData(fContainer, aTo);
    }

    private void fromDataToView(IVModelData aFrom) {
        // for (FormField d : fContainer.getfList()) {
        // String s = aFrom.getS(d.getFie());
        // d.getELine().setVal(s);
        // }
        dFactory.fromDataToView(aFrom, fContainer);
    }

    public void startPublish(int cellId) {
        publish(cellId, gView);
    }

}
