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

import java.util.List;

import com.gwtmodel.table.ICustomObject;
import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.SynchronizeList;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonFactory;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
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
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.tabledef.VListHeaderContainer;
import com.gwtmodel.table.view.callback.CommonCallBack;
import com.gwtmodel.table.view.util.AbstractDataModel;
import com.jythonui.client.util.CreateForm;
import com.jythonui.client.util.PerformVariableAction;
import com.jythonui.client.variables.IVariablesContainer;
import com.jythonui.shared.DialogVariables;
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

        private class Synch extends SynchronizeList {

            ListOfRows lOfRows;

            Synch() {
                super(2);
            }

            @Override
            protected void doTask() {
                IDataListType dList = ListUtils.constructList(dType, rM,
                        lOfRows);
                getSlContainer().publish(dType,
                        DataActionEnum.ListReadSuccessSignal, dList);
            }

        }

        private final Synch sy = new Synch();

        private class ReadListAgain extends CommonCallBack<DialogVariables> {

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
                };
                PerformVariableAction.perform(arg, iCon, rM, vis);

            }

        }

        private class ReadList implements ISlotListener {

            @Override
            public void signal(ISlotSignalContext slContext) {
                if (sy.signalledAlready()) {
                    DialogVariables v = iCon.getVariables();
                    ListFormat li = rM.getFormat(dType);
                    ListUtils.executeCrudAction(v, li, rM.getDialogName(),
                            ICommonConsts.CRUD_READLIST, new ReadListAgain());

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

        DataListPersistAction(IDataType d, RowListDataManager rM,
                IVariablesContainer iCon) {
            this.dType = d;
            this.rM = rM;
            this.iCon = iCon;
            registerSubscriber(dType, DataActionEnum.ReadListAction,
                    new ReadList());
            registerSubscriber(FormBeforeCompletedSignal.constructSignal(d),
                    new FormBeforeCompleted());
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
            VListHeaderContainer vHeader = CreateForm.constructColumns(fo);
            publish(dType, vHeader);
        }

    }

    private static DataListParam getParam(final RowListDataManager rM,
            final IDataType da, IVariablesContainer iCon) {

        // create factories
        IDataPersistListAction iPersist = new DataListPersistAction(da, rM,
                iCon);
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

    static ISlotable contruct(RowListDataManager rM, IDataType da,
            CellId panelId, IVariablesContainer iCon) {
        TableDataControlerFactory tFactory = GwtGiniInjector.getI()
                .getTableDataControlerFactory();

        ControlButtonFactory bFactory = GwtGiniInjector.getI()
                .getControlButtonFactory();
        List<ControlButtonDesc> cList = bFactory.constructCrudListButtons();
        ListOfControlDesc cButton = new ListOfControlDesc(cList);
        DisplayListControlerParam dList = tFactory.constructParam(cButton,
                panelId, getParam(rM, da, iCon), null, false);
        ISlotable i = tFactory.constructDataControler(dList);
        return i;
    }

}
