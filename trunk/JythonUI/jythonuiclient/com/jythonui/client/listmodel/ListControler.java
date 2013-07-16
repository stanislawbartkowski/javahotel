/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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
package com.jythonui.client.listmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;
import com.gwtmodel.table.AVModelData;
import com.gwtmodel.table.ICustomObject;
import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IOkModelData;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.SynchronizeList;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.WChoosedLine;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonFactory;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.common.CUtil;
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
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.listdataview.ChangeFieldEditSignal;
import com.gwtmodel.table.listdataview.DataIntegerSignal;
import com.gwtmodel.table.listdataview.EditRowActionSignal;
import com.gwtmodel.table.listdataview.EditRowsSignal;
import com.gwtmodel.table.listdataview.ReadChunkSignal;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ClickButtonType.StandClickEnum;
import com.gwtmodel.table.slotmodel.CustomStringSlot;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.slotmodel.SlU;
import com.gwtmodel.table.slotmodel.SlotType;
import com.gwtmodel.table.tabledef.VListHeaderContainer;
import com.gwtmodel.table.view.callback.CommonCallBack;
import com.gwtmodel.table.view.table.ChangeEditableRowsParam;
import com.gwtmodel.table.view.util.AbstractDataModel;
import com.jythonui.client.M;
import com.jythonui.client.dialog.ICreateBackActionFactory;
import com.jythonui.client.dialog.IPerformClickAction;
import com.jythonui.client.util.CreateForm;
import com.jythonui.client.util.CreateSearchVar;
import com.jythonui.client.util.PerformVariableAction;
import com.jythonui.client.util.PerformVariableAction.VisitList;
import com.jythonui.client.util.PerformVariableAction.VisitList.IGetFooter;
import com.jythonui.client.variables.IVariablesContainer;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.FieldValue;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.ListFormat;
import com.jythonui.shared.ListOfRows;

/**
 * @author hotel
 * 
 */
class ListControler {

    private ListControler() {

    }

    private static class DataModel extends AbstractDataModel {

        private final RowListDataManager rM;

        DataModel(RowListDataManager rM) {
            this.rM = rM;
        }

        @Override
        public IVModelData construct(IDataType dType) {
            return rM.contructE(dType);
        }
    }

    private static String getFormHeader(RowListDataManager rM, IDataType da) {
        ListFormat fo = rM.getFormat(da);
        String tName = null;
        if (fo.getfElem() != null) {
            tName = fo.getfElem().getDisplayName();
        }
        if (tName == null) {
            tName = fo.getDisplayName();
        }
        return tName;
    }

    private static class DataListPersistAction extends AbstractSlotContainer
            implements IDataPersistListAction {

        private final RowListDataManager rM;
        private final IVariablesContainer iCon;
        private final ICreateBackActionFactory bFactory;

        private int lastRowNum;
        private PersistTypeEnum lastPersistAction;

        private class FooterV extends AVModelData {

            private final List<IGetFooter> fList;

            FooterV(List<IGetFooter> fList) {
                this.fList = fList;
            }

            @Override
            public Object getF(IVField fie) {
                String s = fie.getId();
                for (IGetFooter f : fList) {
                    if (f.getFie().equals(s)) {
                        return f.getV().getValue();
                    }
                }
                return null;
            }

            @Override
            public void setF(IVField fie, Object o) {
            }

            @Override
            public boolean isValid(IVField fie) {
                return false;
            }

            @Override
            public List<IVField> getF() {
                return null;
            }

        }

        private void drawFooter(List<IGetFooter> fList) {
            IVModelData vFooter = new FooterV(fList);
            getSlContainer().publish(dType, DataActionEnum.DrawFooterAction,
                    vFooter);
        }

        private void changeToEdit(VisitList.EditListMode eMode) {
            SlotType sl = EditRowsSignal.constructEditRowSignal(dType);
            EditRowsSignal sig = new EditRowsSignal(
                    ChangeEditableRowsParam.ALLROWS, true, eMode.getMode(),
                    eMode.geteList());
            getSlContainer().publish(sl, sig);
        }

        private class DrawFooter implements ISlotListener {

            @Override
            public void signal(ISlotSignalContext slContext) {
                ICustomObject i = slContext.getCustom();
                DrawFooterSignal sig = (DrawFooterSignal) i;
                drawFooter(sig.getValue());
            }
        }

        private class ChangeToEdit implements ISlotListener {

            @Override
            public void signal(ISlotSignalContext slContext) {
                ICustomObject i = slContext.getCustom();
                ChangeToEditSignal sig = (ChangeToEditSignal) i;
                changeToEdit(sig.getValue());

            }

        }

        private class Synch extends SynchronizeList {

            ListOfRows lOfRows;

            Synch() {
                super(2);
            }

            @Override
            protected void doTask() {
                ListFormat fo = rM.getFormat(dType);
                if (lOfRows == null) {
                    // Utils.errAlert(M.M().NoInfoOnList(fo.getId()));
                    return;
                }
                if (fo.isChunked()) {
                    int size = lOfRows.getSize();
                    CustomStringSlot slot = DataIntegerSignal
                            .constructSlotSetTableSize(dType);
                    DataIntegerSignal sig = new DataIntegerSignal(size);
                    getSlContainer().publish(slot, sig);
                } else {
                    IDataListType dList = ListUtils.constructList(dType, rM,
                            lOfRows);
                    getSlContainer().publish(dType,
                            DataActionEnum.ListReadSuccessSignal, dList);
                }
            }

        }

        private final Synch sy = new Synch();

        private class xActionList extends CommonCallBack<DialogVariables> {

            @Override
            public void onMySuccess(DialogVariables arg) {
                PerformVariableAction.VisitList vis = new PerformVariableAction.VisitList() {

                    @Override
                    public void accept(IDataType da, ListOfRows lRows) {
                        if (da.eq(dType)) {
                            sy.lOfRows = lRows;
                            sy.signalDone();
                        }
                    }

                    @Override
                    public void acceptTypes(String typeName, ListOfRows lRows) {
                        // do nothing, not expected here
                    }

                    @Override
                    public void acceptFooter(IDataType da,
                            List<IGetFooter> fList) {
                        drawFooter(fList);
                    }

                    @Override
                    public void acceptEditListMode(IDataType da, EditListMode e) {
                        changeToEdit(e);
                    }

                };
                PerformVariableAction.perform(null, null, arg, iCon, rM, vis,
                        null);

            }

        }

        private class ReadList implements ISlotListener {

            @Override
            public void signal(ISlotSignalContext slContext) {
                if (sy.signalledAlready()) {
                    DialogVariables v = iCon.getVariables();
                    ListFormat li = rM.getFormat(dType);
                    IOkModelData iOk = SlU.getOkModelData(dType,
                            DataListPersistAction.this);
                    CreateSearchVar.addSearchVar(v, li, iOk);
                    ListUtils.executeCrudAction(v, li, rM.getDialogName(),
                            ICommonConsts.CRUD_READLIST,
                            bFactory.construct(li.getId(), null));

                } else {
                    sy.signalDone();
                }
            }

        }

        private class FormBeforeCompleted implements ISlotListener {

            @Override
            public void signal(ISlotSignalContext slContext) {
                ICustomObject i = slContext.getCustom();
                FormBeforeCompletedSignal sig = (FormBeforeCompletedSignal) i;
                sy.lOfRows = sig.getValue();
                sy.signalDone();
            }

        }

        private class GetListSize implements ISlotListener {

            @Override
            public void signal(ISlotSignalContext slContext) {
                IOkModelData iOk = slContext.getIOkModelData();
                DialogVariables v = iCon.getVariables();
                ListFormat li = rM.getFormat(dType);
                CreateSearchVar.addSearchVar(v, li, iOk);
                ListUtils.executeCrudAction(v, li, rM.getDialogName(),
                        ICommonConsts.JLIST_GETSIZE,
                        bFactory.construct(li.getId(), null));
            }

        }

        private class EditRowAction implements ISlotListener {

            @Override
            public void signal(ISlotSignalContext slContext) {
                ICustomObject i = slContext.getCustom();
                EditRowActionSignal e = (EditRowActionSignal) i;
                int k = 0;
            }

        }

        private class ChangeColumnEdit implements ISlotListener {

            class KeyTable {
                int row;
                IVField v;

                public @Override
                int hashCode() {
                    return v.hashCode() + row;
                }

                public @Override
                boolean equals(Object o) {
                    KeyTable c = (KeyTable) o;
                    return c.row == row && v.eq(c.v);
                }
            }

            private final Map<KeyTable, Optional<Object>> valsBefore = new HashMap<KeyTable, Optional<Object>>();

            @Override
            public void signal(ISlotSignalContext slContext) {
                ICustomObject i = slContext.getCustom();
                ChangeFieldEditSignal c = (ChangeFieldEditSignal) i;
                ListFormat li = rM.getFormat(dType);
                IVField vv = c.getV();
                FieldItem col = li.getColumn(vv.getId());
                assert col != null : LogT.getT().cannotBeNull();
                IVModelData iD = SlU.getVDataByI(dType,
                        DataListPersistAction.this, c.getValue());
                Object o = iD.getF(vv);
                KeyTable key = new KeyTable();
                key.row = c.getValue();
                key.v = vv;
                Optional<Object> beforeD = null;
                if (c.isBefore()) {
                    key.v = vv;
                    valsBefore.put(key, Optional.of(o));
                } else {
                    beforeD = valsBefore.get(key);
                    valsBefore.remove(key);
                }
                if (c.isBefore() && !col.isSignalBefore())
                    return;
                if (!c.isBefore() && !col.isSignalChange())
                    return;

                Object prevO = null;
                if (!c.isBefore()) {
                    if (beforeD == null) {
                        String mess = M.M().BeforeValueNotFound(c.getValue(),
                                vv.getId());
                        Utils.errAlert(LogT.getT().InternalError(), mess);
                        return;
                    }
                    prevO = beforeD.orNull();
                }
                DialogVariables v = iCon.getVariables();
                v.setValueB(ICommonConsts.JCHANGESIGNALBEFORE, c.isBefore());
                v.setValueS(ICommonConsts.SIGNALCHANGEFIELD, vv.getId());
                FieldValue prev = new FieldValue();
                prev.setValue(col.getFieldType(), prevO, col.getAfterDot());
                v.setValue(ICommonConsts.JVALBEFORE, prev);
                ListUtils.executeCrudAction(v, li, rM.getDialogName(),
                        ICommonConsts.SIGNALCOLUMNCHANGE,
                        bFactory.construct(li.getId(), c.getW()));
            }

        }

        DataListPersistAction(IDataType d, RowListDataManager rM,
                IVariablesContainer iCon, ICreateBackActionFactory bFactory) {
            this.dType = d;
            this.rM = rM;
            this.bFactory = bFactory;
            this.iCon = iCon;
            registerSubscriber(dType, DataActionEnum.ReadListAction,
                    new ReadList());
            registerSubscriber(FormBeforeCompletedSignal.constructSignal(d),
                    new FormBeforeCompleted());
            registerSubscriber(dType, DataActionEnum.GetListSize,
                    new GetListSize());
            registerSubscriber(DrawFooterSignal.constructSignal(d),
                    new DrawFooter());
            registerSubscriber(ChangeToEditSignal.constructSignal(d),
                    new ChangeToEdit());
            registerSubscriber(
                    ChangeFieldEditSignal.constructSlotChangeEditSignal(d),
                    new ChangeColumnEdit());
            registerSubscriber(
                    EditRowActionSignal.constructSlotEditActionSignal(d),
                    new EditRowAction());

        }

    }

    private static class HeaderList extends AbstractSlotContainer implements
            IHeaderListContainer {

        private final RowListDataManager rM;

        HeaderList(IDataType dType, RowListDataManager rM) {
            this.rM = rM;
            this.dType = dType;
        }

        @Override
        public void startPublish(CellId cellId) {
            ListFormat fo = rM.getFormat(dType);
            VListHeaderContainer vHeader = CreateForm.constructColumns(rM
                    .getDialogInfo().getSecurity(), fo);
            publish(dType, vHeader);
        }

    }

    private static DataListParam getParam(final RowListDataManager rM,
            final IDataType da, IVariablesContainer iCon,
            ICreateBackActionFactory bFactory) {

        // create factories
        IDataPersistListAction iPersist = new DataListPersistAction(da, rM,
                iCon, bFactory);
        IHeaderListContainer heList = new HeaderList(da, rM);
        IDataModelFactory dataFactory = new DataModel(rM);
        IFormTitleFactory formFactory = new IFormTitleFactory() {

            @Override
            public String getFormTitle(ICallContext iContext) {
                return getFormHeader(rM, da);
            }

        };

        IGetViewControllerFactory fControler = new GetViewController(rM,
                dataFactory, iCon);

        return new DataListParam(da, iPersist, heList, dataFactory,
                formFactory, fControler, null);
    }

    private static class ActionClicked implements ISlotListener {

        private final IPerformClickAction iClick;
        private final IVariablesContainer iCon;

        ActionClicked(IPerformClickAction iClick, IVariablesContainer iCon) {
            this.iClick = iClick;
            this.iCon = iCon;
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            ICustomObject i = slContext.getCustom();
            WChoosedLine wC = (WChoosedLine) i;

            IVField fie = wC.getvField();
            WSize w = wC.getwSize();
            iClick.click(fie.getId(), w);
        }

    }

    private static class ReadInChunk implements ISlotListener {

        private final IDataType dType;
        private final IVariablesContainer iCon;
        private final RowListDataManager rM;

        ReadInChunk(IDataType dType, IVariablesContainer iCon,
                RowListDataManager rM) {
            this.dType = dType;
            this.iCon = iCon;
            this.rM = rM;
        }

        private class ReadRowsChunk extends CommonCallBack<DialogVariables> {

            private final ReadChunkSignal r;

            ReadRowsChunk(ReadChunkSignal r) {
                this.r = r;
            }

            @Override
            public void onMySuccess(DialogVariables arg) {
                PerformVariableAction.VisitList vis = new PerformVariableAction.VisitList() {

                    @Override
                    public void accept(IDataType da, ListOfRows lRows) {
                        if (da.eq(dType)) {
                            IDataListType dList = ListUtils.constructList(
                                    dType, rM, lRows);
                            r.signalRowsRead(dList.getList());
                        }
                    }

                    @Override
                    public void acceptTypes(String typeName, ListOfRows lRows) {
                        // do nothing, not expected here
                    }

                    @Override
                    public void acceptFooter(IDataType da,
                            List<IGetFooter> fList) {
                        // TODO: do something

                    }

                    @Override
                    public void acceptEditListMode(IDataType da, EditListMode e) {
                        // TODO Auto-generated method stub

                    }
                };
                PerformVariableAction.perform(null, null, arg, iCon, rM, vis,
                        null);

            }
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            ICustomObject i = slContext.getCustom();
            ReadChunkSignal r = (ReadChunkSignal) i;
            int start = r.getStart();
            int size = r.getSize();
            DialogVariables v = iCon.getVariables();
            v.setValueL(ICommonConsts.JLIST_READCHUNKSTART, start);
            v.setValueL(ICommonConsts.JLIST_READCHUNKLENGTH, size);
            IVField fSort = r.getfSort();
            if (fSort == null)
                v.setValueS(ICommonConsts.JLIST_SORTLIST, null);

            else
                v.setValueS(ICommonConsts.JLIST_SORTLIST, fSort.getId());
            v.setValueB(ICommonConsts.JLIST_SORTASC, r.isAsc());
            ListFormat li = rM.getFormat(dType);
            CreateSearchVar.addSearchVar(v, li, r.getOkData());
            ListUtils.executeCrudAction(v, li, rM.getDialogName(),
                    ICommonConsts.JLIST_READCHUNK, new ReadRowsChunk(r));
        }

    }

    static ISlotable contruct(RowListDataManager rM, IDataType da,
            CellId panelId, IVariablesContainer iCon,
            IPerformClickAction iClick, ICreateBackActionFactory backFactory) {
        TableDataControlerFactory tFactory = GwtGiniInjector.getI()
                .getTableDataControlerFactory();
        ControlButtonFactory buFactory = GwtGiniInjector.getI()
                .getControlButtonFactory();

        ControlButtonFactory bFactory = GwtGiniInjector.getI()
                .getControlButtonFactory();
        ListFormat li = rM.getFormat(da);
        List<ControlButtonDesc> crudList = bFactory.constructCrudListButtons();
        List<ControlButtonDesc> cList;
        if (!CUtil.EmptyS(li.getStandButt())) {
            String[] liButton = li.getStandButt().split(",");
            cList = new ArrayList<ControlButtonDesc>();
            for (String s : liButton) {
                StandClickEnum bu = null;
                if (s.equals(ICommonConsts.BUTT_ADD)) {
                    bu = StandClickEnum.ADDITEM;
                }
                if (s.equals(ICommonConsts.BUTT_REMOVE)) {
                    bu = StandClickEnum.REMOVEITEM;
                }
                if (s.equals(ICommonConsts.BUTT_MODIF)) {
                    bu = StandClickEnum.MODIFITEM;
                }
                if (s.equals(ICommonConsts.BUTT_SHOW)) {
                    bu = StandClickEnum.SHOWITEM;
                }
                if (s.equals(ICommonConsts.BUTT_TOOLS)) {
                    bu = StandClickEnum.TABLEDEFAULTMENU;
                }
                if (s.equals(ICommonConsts.BUTT_FILTER)) {
                    bu = StandClickEnum.FILTRLIST;
                }
                if (s.equals(ICommonConsts.BUTT_FIND)) {
                    bu = StandClickEnum.FIND;
                }
                if (bu == null) {
                    continue;
                }
                ControlButtonDesc b = buFactory.constructButt(bu);
                cList.add(b);
            }
        } else {
            cList = crudList;
        }
        ListOfControlDesc cButton = new ListOfControlDesc(cList);
        DisplayListControlerParam dList = tFactory.constructParam(cButton,
                panelId, getParam(rM, da, iCon, backFactory), null, false,
                li.isChunked());
        ISlotable i = tFactory.constructDataControler(dList);
        CustomStringSlot sl = ReadChunkSignal.constructReadChunkSignal(da);
        i.getSlContainer()
                .registerSubscriber(sl, new ReadInChunk(da, iCon, rM));
        i.getSlContainer().registerSubscriber(da,
                DataActionEnum.TableCellClicked,
                new ActionClicked(iClick, iCon));
        return i;
    }

}
