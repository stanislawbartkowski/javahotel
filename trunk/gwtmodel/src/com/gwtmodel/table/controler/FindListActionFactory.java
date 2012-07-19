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
package com.gwtmodel.table.controler;

import com.gwtmodel.table.*;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.common.ISignal;
import com.gwtmodel.table.controlbuttonview.IControlButtonView;
import com.gwtmodel.table.controler.DataListActionItemFactory.DrawForm;
import com.gwtmodel.table.controler.DataListActionItemFactory.ResignAction;
import com.gwtmodel.table.datamodelview.DataViewModelFactory;
import com.gwtmodel.table.datamodelview.IDataViewModel;
import com.gwtmodel.table.injector.*;
import com.gwtmodel.table.panelview.IPanelView;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.rdef.ITouchListener;
import com.gwtmodel.table.slotmediator.ISlotMediator;
import com.gwtmodel.table.slotmodel.*;
import com.gwtmodel.table.view.ewidget.EditWidgetFactory;
import com.gwtmodel.table.view.table.VListHeaderContainer;
import com.gwtmodel.table.view.table.VListHeaderDesc;
import com.gwtmodel.table.view.util.CreateReadOnlyI;
import com.gwtmodel.table.view.util.OkDialog;
import com.gwtmodel.table.view.util.YesNoDialog;
import com.gwtmodel.table.view.webpanel.IWebPanel;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author perseus
 */
class FindListActionFactory {

    private final GetHeader gHeader = new GetHeader();
    private final TablesFactories tFactories;
    private final IDataType ddType;
    private final IDataType eType;
    private final EditWidgetFactory eFactory;
    private final DataViewModelFactory vFactory;
    private final DataListParam listParam;
    private List<FormField> liFilter = null;
    private List<FormField> liFind = null;

    FindListActionFactory(TablesFactories tFactories, IDataType dType,
            DataListParam listParam) {
        this.tFactories = tFactories;
        this.ddType = dType;
        eType = Empty.getDataType();
        eFactory = GwtGiniInjector.getI().getEditWidgetFactory();
        vFactory = tFactories.getdViewFactory();
        this.listParam = listParam;
    }

    private class GetHeader implements ISlotListener {

        private VListHeaderContainer listHeader = null;

        @Override
        public void signal(ISlotSignalContext slContext) {
            listHeader = slContext.getListHeader();
        }
    }

    private class RemoveFilter implements ISlotListener {

        private final DrawForm dForm;
        private final ISlotable publishSlo;
        private final IDataType publishdType;

        RemoveFilter(DrawForm dForm, ISlotable publishSlo,
                IDataType publishdType) {
            this.dForm = dForm;
            this.publishSlo = publishSlo;
            this.publishdType = publishdType;
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            publishSlo.getSlContainer().publish(publishdType,
                    DataActionEnum.DrawListRemoveFilter);
            dForm.hide();
        }
    }

    private class NotFoundSignal implements ISlotListener {

        private IGWidget w;

        @Override
        public void signal(ISlotSignalContext slContext) {
            OkDialog ok = new OkDialog(MM.getL().NotFound(), null, null);
            if (w == null) {
                Utils.errAlert("signal", LogT.getT().notFoundSignalNotNull());
            }
            ok.show(w.getGWidget());
        }

        /**
         * @param w
         *            the w to set
         */
        public void setW(IGWidget w) {
            this.w = w;
            if (w == null) {
                Utils.errAlert("setW", LogT.getT().notFoundSignalNotNull());
            }
        }
    }

    private class ClearParam implements ISlotListener {

        private final ISlotMediator slMediator;
        private final List<FormField> liF;
        private final List<VListHeaderDesc> li;
        private final NotFoundSignal nF;

        ClearParam(ISlotMediator slMediator, List<FormField> liF,
                List<VListHeaderDesc> li, NotFoundSignal nF) {
            this.slMediator = slMediator;
            this.liF = liF;
            this.li = li;
            this.nF = nF;
        }

        public void signal(ISlotSignalContext slContext) {
            FData fa = returnNotEmpty(slMediator, liF, li, slContext, nF);
            if (fa == null) {
                return;
            }
            IClickYesNo yes = new IClickYesNo() {

                public void click(boolean yes) {
                    if (yes) {
                        slMediator.getSlContainer().publish(eType,
                                DataActionEnum.ClearViewFormAction);
                        clearBoolean(liF);
                    }
                }
            };
            YesNoDialog yesDialog = new YesNoDialog(MM.getL()
                    .ClearParametersQuestion(), yes);
            yesDialog.show(nF.w.getGWidget());
        }
    }

    private FData returnNotEmpty(ISlotMediator slMediator, List<FormField> liF,
            List<VListHeaderDesc> li, ISlotSignalContext slContext,
            NotFoundSignal nF) {
        FData fa = new FData(liF, li);
        slMediator.getSlContainer().getGetterIVModelData(eType,
                GetActionEnum.GetViewModelEdited, fa);
        IGWidget w = slContext.getGwtWidget();
        nF.setW(w);
        if (fa.isEmpty()) {
            OkDialog ok = new OkDialog(MM.getL().NothingEntered(), null, null);
            ok.show(w.getGWidget());
            return null;
        }
        return fa;
    }

    private class SetFilter implements ISlotListener {

        private final ISlotMediator slMediator;
        private final List<FormField> liF;
        private final DrawForm dForm;
        private final List<VListHeaderDesc> li;
        private final ISlotable publishSlo;
        private final IDataType publishdType;
        private final DataActionEnum a;
        private final boolean hide;
        private final NotFoundSignal nF;

        SetFilter(ISlotMediator slMediator, List<FormField> liF,
                DrawForm dForm, List<VListHeaderDesc> li, ISlotable publishSlo,
                IDataType publishdType, DataActionEnum a, boolean hide,
                NotFoundSignal nF) {
            this.slMediator = slMediator;
            this.liF = liF;
            this.dForm = dForm;
            this.li = li;
            this.publishSlo = publishSlo;
            this.publishdType = publishdType;
            this.a = a;
            this.hide = hide;
            this.nF = nF;
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            FData fa = returnNotEmpty(slMediator, liF, li, slContext, nF);
            if (fa != null) {
                publishSlo.getSlContainer().publish(publishdType, a,
                        fa.constructIOk());
            } else {
                return; // do not close the dialog if empty
            }
            if (hide) {
                dForm.hide();
            }
        }
    }

    private void clearBoolean(List<FormField> li) {
        for (FormField f : li) {
            IVField fie = f.getFie();
            FField fe = (FField) fie;
            if (fe.isCheckField()) {
                continue;
            }
            if (fie.getType().getType() == FieldDataType.T.BOOLEAN) {
                Boolean val = new Boolean(!f.isRange());
                f.getELine().setValObj(val);
            }
        }

    }

    private void modifCheck(IVModelData v, IVField from, IVField to,
            IFormLineView icheck) {
        boolean nullFrom = FUtils.isNullValue(v, from);
        boolean nullTo = FUtils.isNullValue(v, to);
        if (nullFrom || !nullTo) {
            icheck.setReadOnly(true);
            return;
        }
        icheck.setReadOnly(false);
    }

    private class Touch implements ITouchListener {

        private final IVField from;
        private final IVField to;
        private final IFormLineView icheck;
        private final List<FormField> liF;

        @Override
        public void onTouch() {
            FormLineContainer fContainer = new FormLineContainer(liF);
            IVModelData v = CreateReadOnlyI.contructReadonlyVModel(fContainer);
            modifCheck(v, from, to, icheck);
        }

        /**
         * @param from
         * @param to
         * @param icheck
         * @param liF
         */
        Touch(IVField from, IVField to, IFormLineView icheck,
                List<FormField> liF) {
            this.from = from;
            this.to = to;
            this.icheck = icheck;
            this.liF = liF;
        }

    }

    private List<FormField> constructForm(List<VListHeaderDesc> li) {

        List<FormField> liF = new ArrayList<FormField>();
        // cannot use Set - use List instead
        List<IVField> vLi = new ArrayList<IVField>();
        for (VListHeaderDesc he : li) {
            boolean found = false;
            for (IVField v : vLi) {
                if (v.eq(he.getFie())) {
                    found = true;
                    break;
                }
            }
            if (found) {
                continue;
            }
            IVField fe = he.getFie();
            vLi.add(fe);
            IVField from = new FField(fe, true, he, false);
            IVField to = new FField(fe, false, he, false);
            IVField check = new FField(fe, false, he, true);
            IFormLineView icheck = null;
            if (!fe.getType().isBoolean()) {
                icheck = eFactory.constructCheckField(check, MM.getL()
                        .EqualSign());
                icheck.setValObj(new Boolean(true));
                liF.add(new FormField(null, icheck, check, from));
            }
            IFormLineView ifrom = eFactory.constructEditWidget(from);
            IFormLineView ito = eFactory.constructEditWidget(to);
            liF.add(new FormField(he.getHeaderString(), ifrom));
            ITouchListener t = null;
            if (icheck != null) {
                liF.add(new FormField(null, icheck, check, from));
                t = new Touch(from, to, icheck, liF);
                ifrom.setOnTouch(t);
                ito.setOnTouch(t);
            }
            liF.add(new FormField(null, ito, to, from));
            // important: after adding (not before)
            if (t != null) {
                t.onTouch();
            }

        }
        clearBoolean(liF);
        return liF;
    }

    private class ActionFind implements ISlotListener {

        private final ClickButtonType.StandClickEnum action;
        private ISlotMediator slMediator;
        private final ISlotable publishSlo;
        private final IDataType publishdType;
        private final NotFoundSignal nF;

        private boolean isFilter() {
            return action == ClickButtonType.StandClickEnum.FILTRLIST;
        }

        ActionFind(ClickButtonType.StandClickEnum action, ISlotable publishSlo,
                IDataType publishdType, NotFoundSignal nF) {
            this.action = action;
            this.publishSlo = publishSlo;
            this.publishdType = publishdType;
            this.nF = nF;
            this.slMediator = null;
        }

        private class Re implements ISignal {

            @Override
            public void signal() {
                publishSlo.getSlContainer().removeSubscriber(publishdType,
                        DataActionEnum.NotFoundSignal);
                slMediator = null;
            }
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            if (gHeader.listHeader == null) {
                return;
            }
            if (!isFilter() && slMediator != null) {
                return;
            }
            final ISignal remF = new Re();
            IGWidget wi = slContext.getGwtWidget();
            WSize wSize = new WSize(wi.getGWidget());
            List<VListHeaderDesc> li = gHeader.listHeader.getVisHeList();
            List<FormField> liF;
            boolean modal;

            if (isFilter()) {
                if (liFilter == null) {
                    liFilter = constructForm(li);
                }
                liF = liFilter;
                modal = true;
            } else {
                if (liFind == null) {
                    liFind = constructForm(li);
                }
                liF = liFind;
                modal = false;
            }

            FormLineContainer fContainer = new FormLineContainer(liF);
            // 2012/03/30 : parameter added
            IDataViewModel daView = vFactory.construct(eType, fContainer,
                    listParam.getDataFactory());
            ListOfControlDesc bControl;
            if (isFilter()) {
                bControl = tFactories.getControlButtonFactory()
                        .constructFilterButton();
            } else {
                bControl = tFactories.getControlButtonFactory()
                        .constructFindButton();
            }

            IControlButtonView bView = tFactories.getbViewFactory().construct(
                    eType, bControl);
            CellId panelId = new CellId(1);
            IPanelView pView = tFactories.getpViewFactory().construct(eType,
                    panelId);
            CellId controlId = pView.addCellPanel(1, 0);
            CellId cellTableId = pView.addCellPanel(0, 0);
            pView.createView();
            slMediator = tFactories.getSlotMediatorFactory().construct();
            slMediator.registerSlotContainer(panelId, pView);
            slMediator.registerSlotContainer(cellTableId, daView);
            slMediator.registerSlotContainer(controlId, bView);

            ICallContext iCall = GwtGiniInjector.getI().getCallContext();
            iCall.setdType(ddType);
            iCall.setiSlo(publishSlo);
            iCall.setPersistTypeEnum(slContext.getPersistType());

            String title = listParam.getFormFactory().getFormTitle(iCall);
            ISignal o = new ISignal() {

                @Override
                public void signal() {
                    remF.signal();
                }
            };
            final DrawForm dForm = new DrawForm(wSize, title, action, modal, o,
                    null);
            ISlotListener clearS = new ClearParam(slMediator, liF, li, nF);
            slMediator.getSlContainer().registerSubscriber(eType, panelId,
                    dForm);
            ResignAction aRes = new ResignAction(dForm, remF, null);
            slMediator.getSlContainer().registerSubscriber(eType,
                    ClickButtonType.StandClickEnum.RESIGN, aRes);

            slMediator.getSlContainer().registerSubscriber(
                    eType,
                    ClickButtonType.StandClickEnum.SETFILTER,
                    new SetFilter(slMediator, liF, dForm, li, publishSlo,
                            publishdType, DataActionEnum.DrawListSetFilter,
                            true, nF));
            slMediator.getSlContainer()
                    .registerSubscriber(
                            eType,
                            ClickButtonType.StandClickEnum.FINDNOW,
                            new SetFilter(slMediator, liF, dForm, li,
                                    publishSlo, publishdType,
                                    DataActionEnum.FindRowList, false, nF));
            slMediator.getSlContainer().registerSubscriber(
                    eType,
                    ClickButtonType.StandClickEnum.FINDFROMBEGINNING,
                    new SetFilter(slMediator, liF, dForm, li, publishSlo,
                            publishdType, DataActionEnum.FindRowBeginningList,
                            false, nF));
            slMediator.getSlContainer().registerSubscriber(
                    eType,
                    ClickButtonType.StandClickEnum.FINDNEXT,
                    new SetFilter(slMediator, liF, dForm, li, publishSlo,
                            publishdType, DataActionEnum.FindRowNextList,
                            false, nF));
            slMediator.getSlContainer().registerSubscriber(eType,
                    ClickButtonType.StandClickEnum.REMOVEFILTER,
                    new RemoveFilter(dForm, publishSlo, publishdType));
            slMediator.getSlContainer().registerSubscriber(eType,
                    ClickButtonType.StandClickEnum.CLEARFILTER, clearS);
            slMediator.getSlContainer().registerSubscriber(eType,
                    ClickButtonType.StandClickEnum.CLEARFIND, clearS);
            if (!isFilter()) {
                publishSlo.getSlContainer().registerSubscriber(publishdType,
                        DataActionEnum.NotFoundSignal, nF);
            }

            slMediator.startPublish(panelId);
            if (!isFilter()) {
                IWebPanel wPanel = GwtGiniInjector.getI().getWebPanel();
                if (wPanel != null) {
                    ISignal iSig = new ISignal() {

                        @Override
                        public void signal() {
                            dForm.hide();
                        }
                    };
                    wPanel.setCentreHideSignal(iSig);
                }
            }
        }
    }

    ISlotListener constructActionFind(ClickButtonType.StandClickEnum eNum,
            ISlotable publishSlo, IDataType publishdType) {
        return new ActionFind(eNum, publishSlo, publishdType,
                new NotFoundSignal());
    }

    ISlotListener constructActionHeader() {
        return gHeader;
    }
}
