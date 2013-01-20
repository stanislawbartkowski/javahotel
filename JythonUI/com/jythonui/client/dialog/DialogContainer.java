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
package com.jythonui.client.dialog;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.VModelData;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.datamodelview.DataViewModelFactory;
import com.gwtmodel.table.datamodelview.IDataViewModel;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.panelview.IPanelView;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.slotmodel.AbstractSlotMediatorContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.view.callback.CommonCallBack;
import com.gwtmodel.table.view.callback.ICommonCallBackFactory;
import com.gwtmodel.table.view.util.AbstractDataModel;
import com.jythonui.client.M;
import com.jythonui.client.listmodel.RowListDataManager;
import com.jythonui.client.util.CreateForm;
import com.jythonui.client.util.ExecuteAction;
import com.jythonui.client.util.PerformVariableAction;
import com.jythonui.client.variables.IVariablesContainer;
import com.jythonui.client.variables.VariableContainerFactory;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.ListFormat;
import com.jythonui.shared.ListOfRows;

/**
 * @author hotel
 * 
 */
public class DialogContainer extends AbstractSlotMediatorContainer {

    private final RowListDataManager liManager;
    private DialogFormat d;
    private final IVariablesContainer iCon;

    public DialogContainer(IDataType dType, DialogFormat d,
            IVariablesContainer pCon) {
        this.d = d;
        this.dType = dType;
        liManager = new RowListDataManager(d.getId());
        if (pCon == null) {
            if (M.getVar() == null) {
                iCon = VariableContainerFactory.construct();
                M.setVar(iCon);
            } else {
                iCon = VariableContainerFactory.clone(M.getVar());
            }
        } else {
            this.iCon = pCon;
        }
    }

    @Override
    public void startPublish(CellId cId) {
        ICommonCallBackFactory<DialogVariables> backFactory = new ICommonCallBackFactory<DialogVariables>() {

            @Override
            public CommonCallBack<DialogVariables> construct() {
                return new BackClass();
            }
        };

        M.getLeftMenu().createLeftButton(iCon, backFactory, d.getId(),
                d.getLeftButtonList());
        IPanelView pView = pViewFactory.construct(dType, cId);
        boolean emptyView = true;
        int pLine = 0;
        if (d.getFieldList() != null) {
            FormLineContainer fContainer = CreateForm.construct(d);

            DataViewModelFactory daFactory = GwtGiniInjector.getI()
                    .getDataViewModelFactory();

            IDataModelFactory dFactory = new DataModel();

            IDataViewModel daModel = daFactory.construct(dType, fContainer,
                    dFactory);
            CellId dId = pView.addCellPanel(dType, pLine, 0);
            slMediator.registerSlotContainer(dId, daModel);
            iCon.addFormVariables(slMediator, dType);
            emptyView = false;
            pLine = 1;
        }
        if (d.getListList() != null)
            for (ListFormat f : d.getListList()) {
                String id = f.getId();
                IDataType da = DataType.construct(id);
                liManager.addList(da, id, f);
                CellId panelId = pView.addCellPanel(dType, pLine++, 0);
                ISlotable i = liManager.constructListControler(da, panelId,
                        iCon);
                slMediator.registerSlotContainer(panelId, i);
                emptyView = false;
            }

        if (!emptyView) {
            pView.createView();
            slMediator.registerSlotContainer(cId, pView);
        }
        slMediator.startPublish(cId);
        if (d.isBefore()) {
            ExecuteAction.action(iCon, d.getId(), ICommonConsts.BEFORE,
                    new BackClass());
        }

    }

    private class BackClass extends CommonCallBack<DialogVariables> {

        @Override
        public void onMySuccess(DialogVariables arg) {
            // iCon.setVariablesToForm(arg);
            // // lists
            // for (IDataType da : liManager.getList()) {
            // String s = liManager.getLId(da);
            // ListOfRows lRows = arg.getList(s);
            // liManager.publishBeforeForm(slMediator, da, lRows);
            // }
            // String mainDialog = arg.getValueS(ICommonConsts.JMAINDIALOG);
            // if (!CUtil.EmptyS(mainDialog)) {
            // new RunAction().start(mainDialog);
            // }
            PerformVariableAction.VisitList vis = new PerformVariableAction.VisitList() {

                @Override
                public void accept(IDataType da, ListOfRows lRows) {
                    liManager.publishBeforeForm(slMediator, da, lRows);
                }
            };
            PerformVariableAction.perform(arg, iCon, liManager, vis);
        }

    }

    private class DataModel extends AbstractDataModel {

        @Override
        public IVModelData construct(IDataType dType) {
            return new VModelData();
        }
    }

    /**
     * @return the iCon
     */
    public IVariablesContainer getiCon() {
        return iCon;
    }

}
