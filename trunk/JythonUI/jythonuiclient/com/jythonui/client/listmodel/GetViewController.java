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

import com.gwtmodel.table.IClickYesNo;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.WSize;
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
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.SlU;
import com.gwtmodel.table.view.callback.CommonCallBack;
import com.gwtmodel.table.view.callback.ICommonCallBackFactory;
import com.gwtmodel.table.view.util.YesNoDialog;
import com.jythonui.client.M;
import com.jythonui.client.dialog.DialogContainer;
import com.jythonui.client.util.ExecuteAction;
import com.jythonui.client.util.ISendCloseAction;
import com.jythonui.client.util.IYesNoAction;
import com.jythonui.client.util.PerformVariableAction;
import com.jythonui.client.util.ValidateForm;
import com.jythonui.client.util.VerifyJError;
import com.jythonui.client.variables.IVariablesContainer;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogInfo;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.ListFormat;
import com.jythonui.shared.ListOfRows;
import com.jythonui.shared.SecurityInfo;

/**
 * @author hotel
 * 
 */
class GetViewController implements IGetViewControllerFactory {

    private final ComposeControllerFactory fFactory;
    private final IDataModelFactory dFactory;
    private final RowListDataManager rM;
    private final IVariablesContainer iCon;

    private String getCrudId(PersistTypeEnum e) {
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
        case SHOWONLY:
            eCrud = ICommonConsts.CRUD_SHOW;
            break;
        default:
            assert false : LogT.getT().notExpected();
            break;
        }
        return eCrud;
    }

    private class ValidateAction extends AbstractSlotContainer implements
            IDataValidateAction {

        private class ValidateEmpty implements ISlotListener {

            @Override
            public void signal(ISlotSignalContext slContext) {
                ListFormat li = rM.getFormat(dType);
                if (!ValidateForm.validateV(dType, ValidateAction.this,
                        li.getfElem(), DataActionEnum.InvalidSignal))
                    return;
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

    private interface executeCrud {

        void action(boolean afterConfirm, CommonCallBack<DialogVariables> back);

    }

    private class ItemDataPersistAction extends AbstractSlotContainer implements
            IDataPersistAction {

        private final DialogContainer dC;

        private class CommandCrud implements executeCrud {

            private final DialogVariables v;
            private final ListFormat li;
            private final String eCrud;

            CommandCrud(DialogVariables v, ListFormat li, String eCrud) {
                this.v = v;
                this.li = li;
                this.eCrud = eCrud;
            }

            @Override
            public void action(boolean afterConfirm,
                    CommonCallBack<DialogVariables> back) {
                v.setValueB(ICommonConsts.JCRUD_AFTERCONF, afterConfirm);
                ListUtils.addListName(v, li);
                ExecuteAction.action(v, li.getfElem().getId(), eCrud, back);

                // ListUtils.executeCrudAction(v, li, li.getfElem().getId(),
                // eCrud, back);
            }

        }

        private class JBack extends CommonCallBack<DialogVariables> {

            private final PersistTypeEnum e;
            private final WSize w;
            private final executeCrud exe;
            private final ICommonCallBackFactory<DialogVariables> bFactory;

            private class CloseD implements ISendCloseAction {

                @Override
                public void closeAction(String resString) {
                    publish(dType, DataActionEnum.PersistDataSuccessSignal, e);
                }
            }

            private class YesNo implements IYesNoAction {

                @Override
                public void answer(String content, String title, String param1,
                        WSize ww) {
                    IClickYesNo i = new IClickYesNo() {

                        @Override
                        public void click(boolean yes) {
                            if (yes)
                                exe.action(true, bFactory.construct());
                        }
                    };
                    YesNoDialog yesD = new YesNoDialog(content, title, i);
                    yesD.show(w);
                }
            }

            private class Vis implements PerformVariableAction.VisitList {

                @Override
                public void accept(IDataType da, ListOfRows lRows) {
                }

                @Override
                public void acceptTypes(String typeName, ListOfRows lRows) {
                }

                @Override
                public void acceptFooter(IDataType da, List<IGetFooter> fList) {
                    // TODO: Auto-generated method stub

                }

                @Override
                public void acceptEditListMode(IDataType da, EditListMode e) {
                    // TODO Auto-generated method stub

                }

            }

            JBack(PersistTypeEnum e, WSize w, executeCrud exe,
                    ICommonCallBackFactory<DialogVariables> bFactory) {
                this.e = e;
                this.w = w;
                this.exe = exe;
                this.bFactory = bFactory;
            }

            @Override
            public void onMySuccess(DialogVariables arg) {
                if (VerifyJError.isError(dC, dType, arg,
                        ItemDataPersistAction.this)) {
                    return;
                }
                PerformVariableAction.perform(new YesNo(), new CloseD(), arg,
                        iCon, rM, new Vis(), w, null);
            }
        }

        private class PersistData implements ISlotListener {

            @Override
            public void signal(ISlotSignalContext slContext) {
                final PersistTypeEnum e = slContext.getPersistType();
                final WSize w = new WSize(slContext.getGwtWidget());
                IVariablesContainer iCon = dC.getiCon();
                DialogVariables v = iCon.getVariables(e.name());
                String eCrud = getCrudId(e);
                ListFormat li = rM.getFormat(dType);
                final executeCrud exe = new CommandCrud(v, li, eCrud);

                ICommonCallBackFactory<DialogVariables> bFact = new ICommonCallBackFactory<DialogVariables>() {

                    @Override
                    public CommonCallBack<DialogVariables> construct() {
                        return new JBack(e, w, exe, this);
                    }

                };
                exe.action(false, bFact.construct());
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

        DialogVariables addV = new DialogVariables();
        String eCrud = getCrudId(iContext.getPersistTypeEnum());
        addV.setValueS(ICommonConsts.JCRUD_DIALOG, eCrud);

        DialogInfo dInfo = rM.getDialogInfo();
        DialogFormat dElem = li.getfElem();
        SecurityInfo elemSec = new SecurityInfo(dInfo.getSecurity().getlSecur()
                .get(li.getId()));
        DialogInfo elemInfo = new DialogInfo(dElem, elemSec, null);
        DialogContainer sLo = new DialogContainer(da, elemInfo, iCon, null,
                addV, null, null);
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
