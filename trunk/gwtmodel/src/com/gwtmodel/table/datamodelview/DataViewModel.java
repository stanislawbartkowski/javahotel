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

import com.gwtmodel.table.ICustomObject;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.InvalidateFormContainer;
import com.gwtmodel.table.PersistTypeEnum;
import com.gwtmodel.table.SynchronizeList;
import com.gwtmodel.table.common.ISignal;
import com.gwtmodel.table.factories.IDataFormConstructorAbstractFactory;
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
import com.gwtmodel.table.slotmodel.ISlotCallerListener;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.SlU;
import com.gwtmodel.table.view.form.GwtFormViewFactory;
import com.gwtmodel.table.view.form.IGwtFormView;

class DataViewModel extends AbstractSlotContainer implements IDataViewModel {

    private final FormLineContainer fContainer;
    private final IGwtFormView gView;
    private final IDataModelFactory dFactory;
    private final ICallContext iContext;

    private final SyPublish syP;

    private class SyPublish extends SynchronizeList {

        CellId cellId;

        SyPublish() {
            super(2);
        }

        @Override
        protected void doTask() {
            IGWidget w = getHtmlWidget(cellId);
            if (w == null) {
                publish(dType, cellId, gView);
                return;
            }
            gView.fillHtml(w);
        }
    }

    private class ClearAction implements ISlotListener {

        public void signal(ISlotSignalContext slContext) {
            for (FormField fie : fContainer.getfList()) {
                fie.getELine().setValObj(null);
            }
        }
    }

    private void changeMode(PersistTypeEnum persistTypeEnum, FormField fie) {
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
        case SHOWONLY:
            vie.setReadOnly(true);
            break;
        }
    }

    private class ChangeMode implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            PersistTypeEnum persistTypeEnum = slContext.getPersistType();
            for (FormField fie : fContainer.getfList()) {
                changeMode(persistTypeEnum, fie);
            }
        }
    }

    private class DrawModel implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            PersistTypeEnum persistTypeEnum = slContext.getPersistType();
            IVModelData mData = slContext.getVData();
            fromDataToView(mData, persistTypeEnum);
        }
    }

    private class GetterModel implements ISlotCallerListener {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            LogT.getLS().info(LogT.getT().GetterModelDataViewModel());
            IVModelData mData = slContext.getVData();
            fromViewToData(mData);
            return slContext;
        }
    }

    private class GetterContainer implements ISlotCallerListener {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            LogT.getLS().info(LogT.getT().GetterContainerDataViewModel());
            return construct(dType, fContainer);
        }
    }

    private class GetterWidget implements ISlotCallerListener {

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

    private class InvalidateMess implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            InvalidateFormContainer errContainer = (InvalidateFormContainer) slContext
                    .getValidateError();
            gView.showInvalidate(errContainer);
        }
    }

    /**
     * Listener for all changes in the form
     * 
     * @author hotel
     * 
     */
    private class FormChangeListener implements IFormChangeListener {

        private final IVField fie;

        public FormChangeListener(IVField fie) {
            this.fie = fie;
        }

        @Override
        public void onChange(IFormLineView i, boolean afterFocus) {
            // propagate change (regardless if something is listening or not)
            SlU.publishValueChange(dType, DataViewModel.this, fie, i,
                    afterFocus);
        }
    }

    private class SetHtmlId implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            ICustomObject i = slContext.getCustom();
            SignalSetHtmlId setI = (SignalSetHtmlId) i;
            gView.setHtmlId(setI.getId(), setI.getgWidget());
        }
    }

    private class CustomChangeMode implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            ICustomObject i = slContext.getCustom();
            SignalChangeMode cMode = (SignalChangeMode) i;
            for (IVField fie : cMode.getvLi()) {
                FormField f = fContainer.findFormField(fie);
                changeMode(cMode.getPersistTypeEnum(), f);
            }
        }

    }

    DataViewModel(GwtFormViewFactory gFactory, IDataType dType,
            FormLineContainer fContainer, IDataModelFactory dFactory,
            IDataFormConstructorAbstractFactory abFactory) {
        syP = new SyPublish();
        this.fContainer = fContainer;
        this.dType = dType;
        this.iContext = GwtGiniInjector.getI().getCallContext();
        iContext.setdType(dType);
        // suspicious 'this' in constructor, but as designed
        iContext.setiSlo(this);
        IDataFormConstructorAbstractFactory.CType ccType;
        if (abFactory == null) {
            ccType = iContext.getC().getDataFormConstructorAbstractFactory()
                    .construct(iContext);
        } else {
            ccType = abFactory.construct(iContext);
        }
        ISignal iSig = new ISignal() {

            @Override
            public void signal() {
                syP.signalDone();
            }

        };
        gView = gFactory.construct(iContext, fContainer, ccType, iSig);
        if (dFactory == null) {
            this.dFactory = iContext.getC().getDataModelFactory();
        } else {
            this.dFactory = dFactory;

        }
        registerSubscriber(dType, DataActionEnum.ChangeViewFormToInvalidAction,
                new InvalidateMess());
        registerSubscriber(dType, DataActionEnum.ChangeViewFormModeAction,
                new ChangeMode());
        registerSubscriber(dType, DataActionEnum.DrawViewFormAction,
                new DrawModel());
        registerSubscriber(dType, DataActionEnum.ClearViewFormAction,
                new ClearAction());
        registerSubscriber(SignalSetHtmlId.constructSlot(dType),
                new SetHtmlId());
        registerSubscriber(SignalChangeMode.constructSlot(dType),
                new CustomChangeMode());
        registerCaller(dType, GetActionEnum.GetViewModelEdited,
                new GetterModel());
        registerCaller(dType, GetActionEnum.GetFormFieldWidget,
                new GetterWidget());
        registerCaller(dType, GetActionEnum.GetEditContainer,
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
        syP.cellId = cellId;
        syP.signalDone();
        // IGWidget w = getHtmlWidget(cellId);
        // if (w == null) {
        // publish(dType, cellId, gView);
        // return;
        // }
        // gView.fillHtml(w);
    }
}
