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
package com.gwtmodel.table.datamodelview;

import java.util.List;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.InvalidateFormContainer;
import com.gwtmodel.table.PersistTypeEnum;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.injector.LogT;
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
    private final ICallContext iContext;

    private class ClearAction implements ISlotSignaller {

        public void signal(ISlotSignalContext slContext) {
            for (FormField fie : fContainer.getfList()) {
                fie.getELine().setValObj(null);
            }
        }
    }

    private class ChangeMode implements ISlotSignaller {

        @Override
        public void signal(ISlotSignalContext slContext) {
            PersistTypeEnum persistTypeEnum = slContext.getPersistType();
            for (FormField fie : fContainer.getfList()) {
                IFormLineView vie = fie.getELine();
                switch (persistTypeEnum) {
                case ADD:
                    if (fie.isReadOnlyIfAdd()) {
                        vie.setReadOnly(true);
                    } else {
                        vie.setReadOnly(false);
                    }
                    break;
                case MODIF:
                    if (fie.isReadOnlyIfModif()) {
                        vie.setReadOnly(true);
                    } else {
                        vie.setReadOnly(false);
                    }
                    break;
                case REMOVE:
                    vie.setReadOnly(true);
                    break;
                case SHOWONLY:
                    vie.setReadOnly(true);
                    break;
                }
            }
        }
    }

    private class DrawModel implements ISlotSignaller {

        @Override
        public void signal(ISlotSignalContext slContext) {
            PersistTypeEnum persistTypeEnum = slContext.getPersistType();
            IVModelData mData = slContext.getVData();
            fromDataToView(mData, persistTypeEnum);
        }
    }

    private class GetterModel implements ISlotCaller {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            LogT.getLS().info(LogT.getT().GetterModelDataViewModel());
            IVModelData mData = slContext.getVData();
            fromViewToData(mData);
            return slContext;
        }
    }

    private class GetterContainer implements ISlotCaller {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            LogT.getLS().info(LogT.getT().GetterContainerDataViewModel());
            return construct(dType, fContainer);
        }
    }

    private class GetterWidget implements ISlotCaller {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            LogT.getLS().info(LogT.getT().GetterWidgetDataViewModel());
            IVField v = slContext.getVField();
            List<FormField> l = fContainer.getfList();
            for (FormField f : l) {
                if (f.getFie().eq(v)) {
                    return construct(slContext.getSlType(), f.getELine());
                }
            }
            return null;
        }
    }

    private class InvalidateMess implements ISlotSignaller {

        @Override
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

        @Override
        public void onChange(IFormLineView i) {
            publish(dType, fie, i);
        }
    }

    DataViewModel(GwtFormViewFactory gFactory, IDataType dType,
            FormLineContainer fContainer, IDataModelFactory dFactory) {
        this.fContainer = fContainer;
        this.dType = dType;
        this.iContext = GwtGiniInjector.getI().getCallContext();
        iContext.setdType(dType);
        // suspicious 'this' in constructor, but as designed
        iContext.setiSlo(this);
        gView = gFactory.construct(iContext, fContainer, iContext.getC()
                .getDataFormConstructorAbstractFactory().construct(iContext));
        if (dFactory == null) {
            this.dFactory = iContext.getC().getDataModelFactory();
        } else {
            this.dFactory = dFactory;

        }
        registerSubscriber(DataActionEnum.ChangeViewFormToInvalidAction, dType,
                new InvalidateMess());
        registerSubscriber(DataActionEnum.ChangeViewFormModeAction, dType,
                new ChangeMode());
        registerSubscriber(DataActionEnum.DrawViewFormAction, dType,
                new DrawModel());
        registerSubscriber(DataActionEnum.ClearViewFormAction, dType,
                new ClearAction());
        registerCaller(GetActionEnum.GetViewModelEdited, dType,
                new GetterModel());
        registerCaller(GetActionEnum.GetFormFieldWidget, dType,
                new GetterWidget());
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

    private void fromDataToView(IVModelData aFrom,
            PersistTypeEnum persistTypeEnum) {
        dFactory.fromDataToView(dType, persistTypeEnum, aFrom, fContainer);
    }

    @Override
    public void startPublish(CellId cellId) {
        IGWidget w = getHtmlWidget(cellId);
        if (w == null) {
            publish(dType, cellId, gView);
            return;
        }
        gView.fillHtml(w);
    }
}
