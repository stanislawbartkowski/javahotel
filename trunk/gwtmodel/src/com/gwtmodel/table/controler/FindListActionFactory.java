/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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

import com.gwtmodel.table.Empty;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.ISignal;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.controlbuttonview.IControlButtonView;
import com.gwtmodel.table.controler.DataListActionItemFactory.DrawForm;
import com.gwtmodel.table.controler.DataListActionItemFactory.ResignAction;
import com.gwtmodel.table.datamodelview.DataViewModelFactory;
import com.gwtmodel.table.datamodelview.IDataViewModel;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.MM;
import com.gwtmodel.table.injector.TablesFactories;
import com.gwtmodel.table.panelview.IPanelView;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.slotmediator.ISlotMediator;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.view.ewidget.EditWidgetFactory;
import com.gwtmodel.table.view.table.VListHeaderContainer;
import com.gwtmodel.table.view.table.VListHeaderDesc;
import com.gwtmodel.table.view.util.OkDialog;
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

    FindListActionFactory(TablesFactories tFactories, IDataType dType, DataListParam listParam) {
        this.tFactories = tFactories;
        this.ddType = dType;
        eType = Empty.getDataType();
        eFactory = GwtGiniInjector.getI().getEditWidgetFactory();
        vFactory = tFactories.getdViewFactory();
        this.listParam = listParam;
    }

    private class GetHeader implements ISlotSignaller {

        VListHeaderContainer listHeader = null;

        @Override
        public void signal(ISlotSignalContext slContext) {
            listHeader = slContext.getListHeader();
        }
    }

    private class RemoveFilter implements ISlotSignaller {

        private final DrawForm dForm;
        private final ISlotable publishSlo;
        private final IDataType publishdType;

        RemoveFilter(DrawForm dForm, ISlotable publishSlo, IDataType publishdType) {
            this.dForm = dForm;
            this.publishSlo = publishSlo;
            this.publishdType = publishdType;
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            publishSlo.getSlContainer().publish(DataActionEnum.DrawListRemoveFilter,
                    publishdType);
            dForm.hide();
        }
    }

    private class NotFoundSignal implements ISlotSignaller {

        private IGWidget w;

        @Override
        public void signal(ISlotSignalContext slContext) {
            OkDialog ok = new OkDialog(MM.getL().NotFound(), null, null);
            ok.show(new WSize(w.getGWidget()));
        }
    }

    private class SetFilter implements ISlotSignaller {

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
            FData fa = new FData(liF, li);
            slMediator.getSlContainer().getGetterIVModelData(
                    GetActionEnum.GetViewModelEdited, eType, fa);
            IGWidget w = slContext.getGwtWidget();
            nF.w = w;
            if (fa.isEmpty()) {
                OkDialog ok = new OkDialog(MM.getL().NothingEntered(), null, null);
                ok.show(new WSize(w.getGWidget()));
                return;
            }
            publishSlo.getSlContainer().publish(a,
                    publishdType, fa.constructIOk());
            if (hide) {
                dForm.hide();
            }
        }
    }

    private List<FormField> constructForm(List<VListHeaderDesc> li) {

        List<FormField> liF = new ArrayList<FormField>();
        for (VListHeaderDesc he : li) {
            IVField from = new FField(he.getFie(), true, he);
            IVField to = new FField(he.getFie(), false, he);
            IFormLineView ifrom = eFactory.constructEditWidget(from);
            IFormLineView ito = eFactory.constructEditWidget(to);
            liF.add(new FormField(he.getHeaderString(), ifrom));
            liF.add(new FormField(he.getHeaderString(), ito, to, from));
        }
        return liF;
    }

    private class ActionFind implements ISlotSignaller {

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
                publishSlo.getSlContainer().removeSubscriber(DataActionEnum.NotFoundSignal,
                        publishdType);
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
            ISignal remF = new Re();
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
            IDataViewModel daView = vFactory.construct(eType, fContainer);
            ListOfControlDesc bControl;
            if (isFilter()) {
                bControl = tFactories.getControlButtonFactory().constructFilterButton();
            } else {
                bControl = tFactories.getControlButtonFactory().constructFindButton();
            }

            IControlButtonView bView = tFactories.getbViewFactory().construct(eType,
                    bControl);
            CellId panelId = new CellId(1);
            IPanelView pView = tFactories.getpViewFactory().construct(eType, panelId);
            CellId controlId = pView.addCellPanel(1, 0);
            CellId cellTableId = pView.addCellPanel(0, 0);
            pView.createView();
            slMediator = tFactories.getSlotMediatorFactory().construct();
            slMediator.registerSlotContainer(panelId, pView);
            slMediator.registerSlotContainer(cellTableId, daView);
            slMediator.registerSlotContainer(controlId, bView);

            String title = listParam.getFormFactory().getFormTitle(ddType);
            final DrawForm dForm = new DrawForm(wSize, title, action, modal);
            slMediator.getSlContainer().registerSubscriber(eType, panelId, dForm);
            ResignAction aRes = new ResignAction(dForm, remF);
            slMediator.getSlContainer().registerSubscriber(
                    ClickButtonType.StandClickEnum.RESIGN, aRes);

            slMediator.getSlContainer().registerSubscriber(
                    ClickButtonType.StandClickEnum.SETFILTER,
                    new SetFilter(slMediator, liF, dForm, li,
                    publishSlo, publishdType, DataActionEnum.DrawListSetFilter, true, nF));
            slMediator.getSlContainer().registerSubscriber(
                    ClickButtonType.StandClickEnum.FINDNOW,
                    new SetFilter(slMediator, liF, dForm, li,
                    publishSlo, publishdType, DataActionEnum.FindRowList, false, nF));
            slMediator.getSlContainer().registerSubscriber(
                    ClickButtonType.StandClickEnum.FINDFROMBEGINNING,
                    new SetFilter(slMediator, liF, dForm, li,
                    publishSlo, publishdType, DataActionEnum.FindRowBeginningList, false, nF));
            slMediator.getSlContainer().registerSubscriber(
                    ClickButtonType.StandClickEnum.FINDNEXT,
                    new SetFilter(slMediator, liF, dForm, li,
                    publishSlo, publishdType, DataActionEnum.FindRowNextList, false, nF));
            slMediator.getSlContainer().registerSubscriber(
                    ClickButtonType.StandClickEnum.REMOVEFILTER,
                    new RemoveFilter(dForm, publishSlo, publishdType));
            publishSlo.getSlContainer().registerSubscriber(DataActionEnum.NotFoundSignal,
                    publishdType, nF);

            slMediator.startPublish(panelId);
            if (!isFilter()) {
                IWebPanel wPanel = GwtGiniInjector.getI().getWebPanel();
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

    ISlotSignaller constructActionFind(ClickButtonType.StandClickEnum eNum,
            ISlotable publishSlo, IDataType publishdType) {
        return new ActionFind(eNum, publishSlo, publishdType, new NotFoundSignal());
    }

    ISlotSignaller constructActionHeader() {
        return gHeader;
    }
}
