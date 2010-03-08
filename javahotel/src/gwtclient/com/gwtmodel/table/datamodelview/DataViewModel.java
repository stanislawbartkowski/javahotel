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
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.InvalidateFormContainer;
import com.gwtmodel.table.PersistTypeEnum;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.factories.ITableCustomFactories;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormChangeListener;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
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
    private final IDataType dType;

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

    private class GetterContainer implements ISlotCaller {

        public ISlotSignalContext call(ISlotSignalContext slContext) {
            return construct(dType, fContainer);
        }

    }

    private class InvalidateMess implements ISlotSignaller {

        public void signal(ISlotSignalContext slContext) {
            InvalidateFormContainer errContainer = (InvalidateFormContainer) slContext
                    .getValidateError();
            gView.showInvalidate(errContainer);
        }

    }

    private class FormChangeListener implements IFormChangeListener {

        private final IVField fie;

        public FormChangeListener(IVField fie) {
            this.fie = fie;
        }

        public void onChange(IFormLineView i) {
            publish(dType, fie, i);
        }
    }

    DataViewModel(GwtFormViewFactory gFactory, IDataType dType,
            FormLineContainer fContainer, ITableCustomFactories cFactories,
            IDataModelFactory dFactory) {
        this.fContainer = fContainer;
        this.dType = dType;
        gView = gFactory.construct(fContainer, cFactories
                .getDataFormConstructorAbstractFactory().construct(dType));
        this.dFactory = dFactory;
        registerSubscriber(DataActionEnum.ChangeViewFormToInvalidAction, dType,
                new InvalidateMess());
        registerSubscriber(DataActionEnum.ChangeViewFormModeAction, dType,
                new ChangeMode());
        registerSubscriber(DataActionEnum.DrawViewFormAction, dType,
                new DrawModel());
        registerCaller(GetActionEnum.GetViewModelEdited, dType,
                new GetterModel());
        registerCaller(GetActionEnum.GetEditContainer, dType,
                new GetterContainer());
        for (FormField fie : fContainer.getfList()) {
            IFormLineView vie = fie.getELine();
            vie.addChangeListener(new FormChangeListener(fie.getFie()));
        }
    }

    private void fromViewToData(IVModelData aTo) {
        dFactory.fromViewToData(dType, fContainer, aTo);
    }

    private void fromDataToView(IVModelData aFrom) {
        dFactory.fromDataToView(dType, aFrom, fContainer);
    }

    @Override
    public void startPublish(CellId cellId) {
        publish(cellId, gView);
    }

}
