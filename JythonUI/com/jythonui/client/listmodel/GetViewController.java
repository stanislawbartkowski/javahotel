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

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.common.PersistTypeEnum;
import com.gwtmodel.table.composecontroller.ComposeControllerFactory;
import com.gwtmodel.table.composecontroller.ComposeControllerType;
import com.gwtmodel.table.composecontroller.IComposeController;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.factories.IDataPersistAction;
import com.gwtmodel.table.factories.IDataValidateAction;
import com.gwtmodel.table.factories.IGetViewControllerFactory;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.SlU;
import com.gwtmodel.table.view.callback.CommonCallBack;
import com.jythonui.client.M;
import com.jythonui.client.dialog.DialogContainer;
import com.jythonui.client.util.ExecuteAction;
import com.jythonui.client.util.VerifyEmpty;
import com.jythonui.client.util.VerifyJError;
import com.jythonui.client.variables.IVariablesContainer;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.ListFormat;

/**
 * @author hotel
 * 
 */
class GetViewController implements IGetViewControllerFactory {

    private final ComposeControllerFactory fFactory;
    private final IDataModelFactory dFactory;
    private final RowListDataManager rM;
    private final IVariablesContainer iCon;

    private class ValidateAction extends AbstractSlotContainer implements
            IDataValidateAction {

        private class ValidateEmpty implements ISlotListener {

            @Override
            public void signal(ISlotSignalContext slContext) {
                IVModelData v = rM.contructE(dType);
                v = getGetterIVModelData(dType,
                        GetActionEnum.GetViewModelEdited, v);
                ListFormat li = rM.getFormat(dType);
                if (VerifyEmpty.isEmpty(dType, v, ValidateAction.this,
                        li.getColumns())) {
                    return;
                }
                SlU.publishDataAction(dType, ValidateAction.this, slContext,
                        DataActionEnum.PersistDataAction);
            }
        }

        ValidateAction(IDataType da) {
            this.dType = da;
            getSlContainer().registerSubscriber(da,
                    DataActionEnum.ValidateComposeFormAction,
                    new ValidateEmpty());

        }

    }

    private class ItemDataPersistAction extends AbstractSlotContainer implements
            IDataPersistAction {

        private final DialogContainer dC;

        private class JBack extends CommonCallBack<DialogVariables> {

            private final PersistTypeEnum e;

            JBack(PersistTypeEnum e) {
                this.e = e;
            }

            @Override
            public void onMySuccess(DialogVariables arg) {
                if (VerifyJError
                        .isError(dType, arg, ItemDataPersistAction.this)) {
                    return;
                }
                publish(dType, DataActionEnum.PersistDataSuccessSignal, e);
            }
        }

        private class PersistData implements ISlotListener {

            @Override
            public void signal(ISlotSignalContext slContext) {
                PersistTypeEnum e = slContext.getPersistType();
                IVariablesContainer iCon = dC.getiCon();
                DialogVariables v = iCon.getVariables();
                String eCrud = null;
                switch (e) {
                case ADD:
                    eCrud = ICommonConsts.CRUD_ADD;
                    break;
                case MODIF:
                    eCrud = ICommonConsts.CRUD_CHANGE;
                    break;
                case REMOVE:
                    eCrud = ICommonConsts.CRUD_REMOVE;
                    break;
                }
                // v.setValueS(ICommonConsts.JCRUD_ACTION, eCrud);
                // ListFormat li = rM.getFormat(dType);
                // v.setValueS(ICommonConsts.JLIST_NAME, li.getId());
                // ExecuteAction.action(v, li.getfElem().getId(),
                // ICommonConsts.CRUDACTION, new JBack(e));
                ListFormat li = rM.getFormat(dType);
                ListUtils.executeCrudAction(v, li, li.getfElem().getId(),
                        eCrud, new JBack(e));
            }

        }

        ItemDataPersistAction(IDataType da, DialogContainer dC) {
            this.dType = da;
            this.dC = dC;
            getSlContainer().registerSubscriber(dType,
                    DataActionEnum.PersistDataAction, new PersistData());

        }

    }

    @Override
    public IComposeController construct(ICallContext iContext) {
        IDataType da = iContext.getDType();
        ListFormat li = rM.getFormat(da);
        if (li.getElemFormat() == null) {
            Utils.errAlert(M.M().ListDoesNotHaveELem(
                    li.getId() + " " + li.getDisplayName(),
                    ICommonConsts.ELEMFORMAT));
            return null;
        }
        IComposeController i = fFactory.construct(da, dFactory);
        DialogContainer sLo = new DialogContainer(da, li.getfElem(), iCon);
        ComposeControllerType cType = new ComposeControllerType(sLo, da, 0, 0);
        i.registerControler(cType);

        IDataValidateAction iValidate = new ValidateAction(da);
        cType = new ComposeControllerType(iValidate, da);
        i.registerControler(cType);

        IDataPersistAction iPersist = new ItemDataPersistAction(da, sLo);

        cType = new ComposeControllerType(iPersist, da);
        i.registerControler(cType);

        return i;
    }

    public GetViewController(RowListDataManager rM, IDataModelFactory dFactory,
            IVariablesContainer iCon) {
        fFactory = GwtGiniInjector.getI().getComposeControllerFactory();
        this.dFactory = dFactory;
        this.rM = rM;
        this.iCon = iCon;
    }

}
