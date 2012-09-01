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
package com.mygwt.client.impl.edit;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.Empty;
import com.gwtmodel.table.IClickYesNo;
import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.InvalidateFormContainer;
import com.gwtmodel.table.InvalidateMess;
import com.gwtmodel.table.WChoosedLine;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonFactory;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.common.PersistTypeEnum;
import com.gwtmodel.table.controler.DataListParam;
import com.gwtmodel.table.controler.DisplayListControlerParam;
import com.gwtmodel.table.controler.TableDataControlerFactory;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.factories.IDataPersistListAction;
import com.gwtmodel.table.factories.IFormTitleFactory;
import com.gwtmodel.table.factories.IGetViewControllerFactory;
import com.gwtmodel.table.factories.IHeaderListContainer;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.listdataview.ButtonCheckLostFocusSignal;
import com.gwtmodel.table.listdataview.DataIntegerSignal;
import com.gwtmodel.table.listdataview.DataIntegerVDataSignal;
import com.gwtmodel.table.listdataview.EditRowActionSignal;
import com.gwtmodel.table.listdataview.EditRowErrorSignal;
import com.gwtmodel.table.listdataview.EditRowsSignal;
import com.gwtmodel.table.listdataview.FinishEditRowSignal;
import com.gwtmodel.table.listdataview.StartNextRowSignal;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.slotmodel.CustomStringSlot;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.slotmodel.SlU;
import com.gwtmodel.table.view.ValidateUtil;
import com.gwtmodel.table.view.table.ChangeEditableRowsParam;
import com.gwtmodel.table.view.util.AbstractDataModel;
import com.gwtmodel.table.view.util.OkDialog;
import com.gwtmodel.table.view.util.YesNoDialog;
import com.mygwt.client.RemoteService;
import com.mygwt.client.testEntryPoint;
import com.mygwt.common.data.ToEditRecord;

/**
 * @author hotel
 * 
 */
public class EditTest implements testEntryPoint.IGetWidget {

    /** IDataType used all the time. */
    private final IDataType dType = Empty.getDataType();

    private final VerticalPanel vp = new VerticalPanel();

    private final String CHANGEEDIT = "BUTTON_CHANGEEDIT";
    private final String SAVEEDIT = "BUTTON_SAVEEDIT";

    /** Listener for setting widget. */
    private class GetWidget implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            Widget w = slContext.getGwtWidget().getGWidget();
            // clear : can be launched twice and more
            vp.clear();
            vp.add(w);
        }

    }

    private class DataModel extends AbstractDataModel {

        @Override
        public IVModelData construct(IDataType dType) {
            return new ItemVData(new ToEditRecord());
        }
    }

    private DataListParam getParam() {

        // create factories
        IDataPersistListAction iPersist = new ItemListPersistAction(dType);
        IHeaderListContainer heList = new HeaderList(dType);
        IDataModelFactory dataFactory = new DataModel();
        IFormTitleFactory formFactory = new IFormTitleFactory() {

            @Override
            public String getFormTitle(ICallContext iContext) {
                return "Item data";
            }

        };

        IGetViewControllerFactory fControler = new GetViewController(
                dataFactory);

        return new DataListParam(dType, iPersist, heList, dataFactory,
                formFactory, fControler, null);
    }

    private class ChangeToEdit implements ISlotListener {

        private final ISlotable iSlo;

        ChangeToEdit(ISlotable iSlo) {
            this.iSlo = iSlo;
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            List<IVField> eList = new ArrayList<IVField>();
            eList.add(ItemVData.fID);
            eList.add(ItemVData.fDATE);
            eList.add(ItemVData.fMARK);
            eList.add(ItemVData.fNUMB);
            eList.add(ItemVData.fNAME);
            EditRowsSignal si = new EditRowsSignal(
                    ChangeEditableRowsParam.ALLROWS, true,
                    ChangeEditableRowsParam.ModifMode.ADDCHANGEDELETEMODE,
                    eList);
            iSlo.getSlContainer().publish(
                    EditRowsSignal.constructEditRowSignal(dType), si);
        }

    }

    private class RowAction implements ISlotListener {

        private final ISlotable i;

        RowAction(ISlotable i) {
            this.i = i;
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            final EditRowActionSignal si = (EditRowActionSignal) slContext
                    .getCustom();
            String s = "Add new row";
            switch (si.getE()) {
            case MODIF:
                s = "Modif this row ";
                break;
            case REMOVE:
                s = "Remove this row";
                break;
            default:
                break;
            }
            s = si.getRownum() + " " + s + " ?";
            // OkDialog o = new OkDialog(s);
            // o.show(si.getW());
            IClickYesNo yes = new IClickYesNo() {

                @Override
                public void click(boolean yes) {
                    if (!yes) {
                        return;
                    }
                    if (si.getE() != PersistTypeEnum.ADD) {
                        CustomStringSlot sl = DataIntegerSignal
                                .constructSlotGetVSignal(dType);
                        i.getSlContainer().publish(sl,
                                new DataIntegerSignal(si.getRownum()));
                        return;

                    }
                    IClickYesNo nyes = new IClickYesNo() {

                        @Override
                        public void click(boolean yes) {
                            CustomStringSlot sl = DataIntegerVDataSignal
                                    .constructSlotAddRowSignal(dType);
                            ItemVData v = new ItemVData();
                            DataIntegerVDataSignal sig = new DataIntegerVDataSignal(
                                    si.getRownum(), v, !yes);
                            i.getSlContainer().publish(sl, sig);
                        }
                    };
                    YesNoDialog askafter = new YesNoDialog(
                            "Add new row before (yes) or after (no) the selected row ?",
                            nyes);
                    askafter.show(si.getW());
                }
            };
            YesNoDialog yesD = new YesNoDialog(s, yes);
            yesD.show(si.getW());
        }

    }

    private class NextRow implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            final StartNextRowSignal si = (StartNextRowSignal) slContext
                    .getCustom();
            // OkDialog o = new OkDialog("Go to line = "
            // + si.getValue().getChoosedLine());
            // o.show(si.getValue().getwSize());
        }

    }

    private class QuestionCaptureChange implements ISlotListener {

        private final ISlotable i;

        QuestionCaptureChange(ISlotable i) {
            this.i = i;
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            final FinishEditRowSignal si = (FinishEditRowSignal) slContext
                    .getCustom();
            final WChoosedLine lostW = si.getValue();
            IClickYesNo yes = new IClickYesNo() {

                @Override
                public void click(boolean yes) {
                    if (!yes) {
                        si.setDoNotChange();
                    }
                    CustomStringSlot sl = FinishEditRowSignal
                            .constructSlotFinishEditRowReturnSignal(dType);
                    i.getSlContainer().publish(sl, si);

                }
            };
            YesNoDialog yesD = new YesNoDialog(lostW.getChoosedLine() + " "
                    + "Do you want to move to the next line ?", yes);
            yesD.show(lostW.getwSize());
        }

    }

    private class CaptureChange implements ISlotListener {

        private final ISlotable i;

        CaptureChange(ISlotable i) {
            this.i = i;
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            final FinishEditRowSignal si = (FinishEditRowSignal) slContext
                    .getCustom();
            IVModelData v = SlU.getVDataByI(dType, i, si.getValue()
                    .getChoosedLine());
            List<IVField> eList = new ArrayList<IVField>();
            eList.add(ItemVData.fNAME);
            eList.add(ItemVData.fNUMB);
            List<InvalidateMess> emptyL = ValidateUtil.checkEmpty(v, eList);
            if (emptyL != null) {
                si.setDoNotChange();
            }
            CustomStringSlot sl = FinishEditRowSignal
                    .constructSlotFinishEditRowReturnSignal(dType);
            i.getSlContainer().publish(sl, si);
            if (emptyL != null) {
                sl = EditRowErrorSignal.constructSlotLineErrorSignal(dType);
                EditRowErrorSignal sig = new EditRowErrorSignal(si.getValue()
                        .getChoosedLine(), new InvalidateFormContainer(emptyL));
                i.getSlContainer().publish(sl, sig);
            }

        }

    }

    private class EndOfSave implements AsyncCallback<Void> {

        private final Widget w;

        EndOfSave(Widget w) {
            this.w = w;
        }

        @Override
        public void onFailure(Throwable caught) {
            OkDialog o = new OkDialog("Failure");
            o.show(w);
        }

        @Override
        public void onSuccess(Void result) {
            OkDialog o = new OkDialog("Ok, saved");
            o.show(w);
        }

    }

    private class SaveEdit implements ISlotListener {

        private final ISlotable i;

        SaveEdit(ISlotable i) {
            this.i = i;
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            final Widget w = slContext.getGwtWidget().getGWidget();
            IClickYesNo yes = new IClickYesNo() {

                @Override
                public void click(boolean yes) {
                    if (!yes) {
                        return;
                    }
                    List<ToEditRecord> rList = new ArrayList<ToEditRecord>();
                    IDataListType dList = SlU.getIDataListType(dType, i);
                    for (IVModelData v : dList.getList()) {
                        ItemVData va = (ItemVData) v;
                        rList.add(va.getRe());
                    }
                    RemoteService.getA().saveEditItemList(rList,
                            new EndOfSave(w));
                }
            };
            YesNoDialog yesD = new YesNoDialog("Persist current data ?", yes);
            yesD.show(w);
        }

    }

    @Override
    public Widget getW() {
        TableDataControlerFactory tFactory = GwtGiniInjector.getI()
                .getTableDataControlerFactory();
        CellId panelId = new CellId(0);
        ControlButtonFactory bFactory = GwtGiniInjector.getI()
                .getControlButtonFactory();
        List<ControlButtonDesc> cList = bFactory.constructCrudListButtons();
        ControlButtonDesc bChange = new ControlButtonDesc("Change to edit",
                CHANGEEDIT);
        ControlButtonDesc bSave = new ControlButtonDesc("Save changes",
                SAVEEDIT);
        cList.add(bChange);
        cList.add(bSave);
        ListOfControlDesc cButton = new ListOfControlDesc(cList);
        DisplayListControlerParam dList = tFactory.constructParam(cButton,
                panelId, getParam(), null, true);

        ISlotable i = tFactory.constructDataControler(dList);
        SlU.registerWidgetListener0(dType, i, new GetWidget());
        i.getSlContainer().registerSubscriber(dType,
                new ClickButtonType(CHANGEEDIT), new ChangeToEdit(i));
        i.getSlContainer().registerSubscriber(dType,
                new ClickButtonType(SAVEEDIT), new SaveEdit(i));

        i.getSlContainer().registerSubscriber(
                EditRowActionSignal.constructSlotEditActionSignal(dType),
                new RowAction(i));
        i.getSlContainer().registerSubscriber(
                FinishEditRowSignal.constructSlotFinishEditRowSignal(dType),
                new CaptureChange(i));
        i.getSlContainer().registerSubscriber(
                StartNextRowSignal.constructSlotStartNextRowSignal(dType),
                new NextRow());
        i.startPublish(null);
        CustomStringSlot sl = ButtonCheckLostFocusSignal
                .constructSlotButtonCheckFocusSignal(dType);
        ButtonCheckLostFocusSignal bSignal = new ButtonCheckLostFocusSignal(
                new ClickButtonType(SAVEEDIT));
        i.getSlContainer().publish(sl, bSignal);
        return vp;
    }

}
