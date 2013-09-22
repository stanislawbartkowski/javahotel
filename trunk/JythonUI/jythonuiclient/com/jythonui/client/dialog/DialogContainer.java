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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtmodel.table.IClickYesNo;
import com.gwtmodel.table.ICommand;
import com.gwtmodel.table.ICustomObject;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.IGetDataList;
import com.gwtmodel.table.IGetDataListCallBack;
import com.gwtmodel.table.IMapEntry;
import com.gwtmodel.table.ISetGWidget;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.VModelData;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.controlbuttonview.ControlButtonViewFactory;
import com.gwtmodel.table.controlbuttonview.IControlButtonView;
import com.gwtmodel.table.datamodelview.DataViewModelFactory;
import com.gwtmodel.table.datamodelview.IDataViewModel;
import com.gwtmodel.table.editc.IRequestForGWidget;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.listdataview.ButtonCheckLostFocusSignal;
import com.gwtmodel.table.panelview.IPanelView;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.slotmodel.AbstractSlotMediatorContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.slotmodel.CustomObjectValue;
import com.gwtmodel.table.slotmodel.CustomStringSlot;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotCustom;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.slotmodel.SlU;
import com.gwtmodel.table.tabpanelview.BeforeTabChange;
import com.gwtmodel.table.tabpanelview.ITabPanelView;
import com.gwtmodel.table.tabpanelview.TabPanelViewFactory;
import com.gwtmodel.table.view.callback.CommonCallBack;
import com.gwtmodel.table.view.util.AbstractDataModel;
import com.gwtmodel.table.view.util.YesNoDialog;
import com.gwtmodel.table.view.webpanel.IWebPanel;
import com.jythonui.client.M;
import com.jythonui.client.dialog.datepanel.DateLineManager;
import com.jythonui.client.dialog.datepanel.RefreshData;
import com.jythonui.client.listmodel.RowListDataManager;
import com.jythonui.client.util.CreateForm;
import com.jythonui.client.util.EnumTypesList;
import com.jythonui.client.util.ExecuteAction;
import com.jythonui.client.util.IConstructCustomDataType;
import com.jythonui.client.util.IExecuteAfterModalDialog;
import com.jythonui.client.util.ISendCloseAction;
import com.jythonui.client.util.IYesNoAction;
import com.jythonui.client.util.JUtils;
import com.jythonui.client.util.PerformVariableAction;
import com.jythonui.client.util.RegisterCustom;
import com.jythonui.client.util.ValidateForm;
import com.jythonui.client.util.VerifyJError;
import com.jythonui.client.variables.IBackAction;
import com.jythonui.client.variables.ISetGetVar;
import com.jythonui.client.variables.IVariablesContainer;
import com.jythonui.client.variables.VariableContainerFactory;
import com.jythonui.shared.ButtonItem;
import com.jythonui.shared.CheckList;
import com.jythonui.shared.DateLine;
import com.jythonui.shared.DateLineVariables;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogInfo;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.FieldValue;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.ListFormat;
import com.jythonui.shared.ListOfRows;
import com.jythonui.shared.MapDialogVariable;
import com.jythonui.shared.TabPanel;
import com.jythonui.shared.TabPanelElem;

/**
 * @author hotel
 * 
 */
public class DialogContainer extends AbstractSlotMediatorContainer {

    private final RowListDataManager liManager;
    private final DialogInfo info;
    private final DialogFormat d;
    private final IVariablesContainer iCon;
    private final ISendCloseAction iClose;
    private final DialogVariables addV;
    // not safe !
    private final FormGridManager gManager;
    private final DateLineManager dManager;

    private final IExecuteAfterModalDialog iEx;

    private final Map<String, IDataType> dLineType = new HashMap<String, IDataType>();

    private final String startVal;

    public DialogContainer(IDataType dType, DialogInfo info,
            IVariablesContainer pCon, ISendCloseAction iClose,
            DialogVariables addV, IExecuteAfterModalDialog iEx, String startVal) {
        this.info = info;
        this.d = info.getDialog();
        this.dType = dType;
        this.iEx = iEx;
        this.startVal = startVal;
        liManager = new RowListDataManager(info, slMediator, new DTypeFactory());
        // not safe, reference is escaping
        dManager = new DateLineManager(this);
        if (pCon == null) {
            if (M.getVar() == null) {
                iCon = VariableContainerFactory.construct();
                M.setVar(iCon);
            } else {
                iCon = VariableContainerFactory.clone(M.getVar());
            }
        } else {
            // clone
            this.iCon = VariableContainerFactory.clone(pCon);
        }
        this.iClose = iClose;
        this.addV = addV;
        gManager = new FormGridManager(this, dType);
        RegisterCustom.registerCustom(info.getCustMess());

    }

    private class CButton implements ISlotListener {

        private final IPerformClickAction clickAction;

        CButton(IPerformClickAction clickAction) {
            this.clickAction = clickAction;
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            IGWidget w = slContext.getGwtWidget();
            clickAction.click(slContext.getSlType().getButtonClick()
                    .getCustomButt(), new WSize(w));
        }

    }

    private ISlotListener constructCButton(List<ButtonItem> bList) {
        return new CButton(new ActionButton(bList));
    }

    private class ListClick implements IPerformClickAction {

        private final IDataType dList;

        ListClick(IDataType dType) {
            this.dList = dType;
        }

        @Override
        public void click(String id, WSize w) {
            ListFormat fo = liManager.getFormat(dList);
            FieldItem fie = DialogFormat.findE(fo.getColumns(), id);
            assert fie != null : LogT.getT().cannotBeNull();
            String actionId = fie.getActionId();
            assert actionId != null : LogT.getT().cannotBeNull();
            ExecuteAction.action(iCon, d.getId(), actionId, new BackClass(id,
                    false, w, null));
        }

    }

    private class GetEnumList implements IGetDataList {

        private final EnumTypesList eList;

        GetEnumList(EnumTypesList eList) {
            this.eList = eList;
        }

        @Override
        public void call(IVField v, IGetDataListCallBack iCallBack) {
            eList.add(v, iCallBack);
        }

    }

    private class ChangeField implements ISlotListener {

        @SuppressWarnings("unchecked")
        @Override
        public void signal(ISlotSignalContext slContext) {
            IFormLineView i = slContext.getChangedValue();
            IVField fie = i.getV();
            ICustomObject cu = slContext.getCustom();
            CustomObjectValue<Boolean> coB = (CustomObjectValue<Boolean>) cu;
            String fieldid = fie.getId();
            FieldItem fItem = d.findFieldItem(fieldid);
            if (fItem == null) {
                return;
            }
            if (!fItem.isSignalChange()) {
                return;
            }
            DialogVariables v = iCon.getVariables(ICommonConsts.SIGNALCHANGE);
            v.setValueS(ICommonConsts.SIGNALCHANGEFIELD, fieldid);
            FieldValue val = new FieldValue();
            val.setValue(coB.getValue());
            v.setValue(ICommonConsts.SIGNALAFTERFOCUS, val);
            ExecuteAction
                    .action(v, d.getId(), ICommonConsts.SIGNALCHANGE,
                            new BackClass(null, false,
                                    new WSize(i.getGWidget()), null));
        }

    }

    private class GetHelperWidget implements ISlotListener {

        private final ISetGWidget setW;

        GetHelperWidget(ISetGWidget setW) {
            this.setW = setW;
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            IGWidget w = slContext.getGwtWidget();
            setW.setW(w);
        }

    }

    private class HelperW implements IRequestForGWidget {

        private class Close implements ISendCloseAction {

            private final ICommand close;

            Close(ICommand close) {
                this.close = close;
            }

            @Override
            public void closeAction(String resString) {
                close.execute();
            }

        }

        @Override
        public void run(IVField v, WSize w, ISetGWidget setW, ICommand close) {
            String fieldid = v.getId();
            FieldItem fItem = d.findFieldItem(fieldid);
            String dialog = fItem.getAttr(ICommonConsts.HELPER);
            DialogVariables var = new DialogVariables();
            var.setValueS(ICommonConsts.SIGNALCHANGEFIELD, fieldid);
            new RunAction().getHelperDialog(dialog, new GetHelperWidget(setW),
                    iCon, new Close(close), var);
        }

    }

    private class DTypeFactory implements IConstructCustomDataType {

        @Override
        public IDataType construct(String customType) {
            return DataType.construct(customType, DialogContainer.this);
        }

    }

    private class DateLineClick implements IPerformClickAction {

        @SuppressWarnings("unused")
        private final String listId;

        DateLineClick(String listId) {
            this.listId = listId;
        }

        @Override
        public void click(String actionId, WSize w) {
            if (runAction(actionId, w, d.getActionList()))
                return;
            ExecuteAction.action(iCon, d.getId(), actionId, new BackClass(
                    actionId, false, w, null));

        }

    }

    private class BAction implements IBackAction {

        @Override
        public void refreshDateLine(String dId) {
            IDataType dType = dLineType.get(dId);
            if (dType == null)
                return;
            ISlotCustom sl = RefreshData.constructRequestForRefreshData(dType);
            getSlContainer().publish(sl);
        }

    }

    private class DialogVariablesGetSet implements ISetGetVar {

        @Override
        public void addToVar(DialogVariables v, String buttonId) {
            IVModelData vData = new VModelData();
            vData = getSlContainer().getGetterIVModelData(dType,
                    GetActionEnum.GetViewModelEdited, vData);
            JUtils.setVariables(v, vData);
            if (addV != null) {
                for (String fie : addV.getFields()) {
                    FieldValue val = addV.getValue(fie);
                    v.setValue(fie, val);
                }
            }
            // set cookies
            List<IMapEntry> cList = Utils.getCookies();
            for (IMapEntry i : cList) {
                v.setValueS(ICommonConsts.JCOOKIE + i.getKey(), i.getValue());
            }
        }

        @Override
        public void readVar(final DialogVariables var) {
            JUtils.IFieldVisit iVisit = new JUtils.IFieldVisit() {

                @Override
                public void setField(VField v, FieldValue val) {
                    IFormLineView i = SlU.getVWidget(dType, slMediator, v);
                    if (i == null) {
                        return;
                    }
                    i.setValObj(val.getValue());
                }
            };
            JUtils.VisitVariable(var, null, iVisit);
        }
    }

    private class BackFactory implements ICreateBackActionFactory {

        @Override
        public CommonCallBack<DialogVariables> construct(String id, WSize w,
                MapDialogVariable addV) {
            return new BackClass(id, false, w, null, addV);
        }

    }

    private class PViewData {
        final IPanelView pView;
        boolean emptyView = true;
        int pLine = 0;
        final List<ITabPanelView> pList = new ArrayList<ITabPanelView>();
        final List<CheckTab> cList = new ArrayList<CheckTab>();

        private class CheckTab {
            final TabPanel tab;
            final TabPanelElem elem;
            boolean exist;

            CheckTab(TabPanel tab, TabPanelElem elem) {
                this.tab = tab;
                this.elem = elem;
                exist = false;
            }

        }

        PViewData(CellId cId) {
            pView = pViewFactory.construct(dType, cId);
            TabPanelViewFactory pFactory = GwtGiniInjector.getI()
                    .getTabPanelViewFactory();
            for (int i = 0; i < d.getTabList().size(); i++)
                pList.add(pFactory.construct(dType, cId, d.getTabList().get(i)
                        .getId()));
            // fill cList
            for (TabPanel tab : d.getTabList())
                for (TabPanelElem e : tab.gettList())
                    cList.add(new CheckTab(tab, e));
        }

        CellId addElemC(String s, IDataType tType) {
            CellId dId = null;
            for (int i = 0; i < d.getTabList().size(); i++)
                for (int k = 0; k < d.getTabList().get(i).gettList().size(); k++) {
                    TabPanelElem e = d.getTabList().get(i).gettList().get(k);
                    if (e.getId().equals(s)) {
                        ITabPanelView iP = pList.get(i);
                        dId = iP.addPanel(k, e.getDisplayName(), null);
                        // check out in the cList table
                        for (CheckTab c : cList) {
                            // important : compare references
                            if (c.tab == d.getTabList().get(i) && c.elem == e)
                                c.exist = true;
                        }
                    }
                }
            if (dId == null)
                dId = pView.addCellPanel(tType, pLine++, 0);

            emptyView = false;
            return dId;
        }

        void addElem(String s, IDataType tType, ISlotable sl) {
            slMediator.registerSlotContainer(addElemC(s, tType), sl);
        }

        void createView(CellId cId) {
            // verify that all tab are filled
            for (CheckTab c : cList) {
                if (!c.exist) {
                    String mess = M.M().TabNotFilled(c.tab.getId(),
                            c.elem.getId());
                    Utils.errAlertB(mess);
                }
            }
            if (!emptyView) {
                for (ITabPanelView i : pList) {
                    i.createView();
                    CellId dId = pView.addCellPanel(dType, pLine++, 0);
                    slMediator.registerSlotContainer(dId, i);
                }
                if (d.isHtmlPanel())
                    pView.createView(d.getHtmlPanel());
                else
                    pView.createView();
                slMediator.registerSlotContainer(cId, pView);
            }
        }

    }

    private class BeforeChangeTab implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            BeforeTabChange t = (BeforeTabChange) slContext.getCustom();
            int tabno = t.getValue();
            String tabId = t.getTabId();
            TabPanel dPanel = DialogFormat.findE(d.getTabList(), tabId);
            TabPanelElem tElem = dPanel.gettList().get(tabno);
            if (!tElem.isBeforeChangeTabbSignal())
                return;
            DialogVariables v = iCon
                    .getVariables(ICommonConsts.BEFORECHANGETAB);
            v.setValueS(ICommonConsts.TABID, tElem.getId());
            v.setValueS(ICommonConsts.TABPANELID, dPanel.getId());
            ExecuteAction.action(v, d.getId(), ICommonConsts.BEFORECHANGETAB,
                    new BackClass(ICommonConsts.BEFORECHANGETAB, false, null,
                            null));
        }

    }

    @Override
    public void startPublish(CellId cId) {

        if (d.isClearCentre() || d.isClearLeft()) {
            IWebPanel i = GwtGiniInjector.getI().getWebPanel();
            if (d.isClearCentre())
                i.setDCenter(null);
            if (d.isClearLeft())
                i.setWest(null);
        }

        // Java script code
        if (d.isJsCode())
            Utils.callJs(d.getJsCode());

        PViewData pView = new PViewData(cId);

        M.getLeftMenu().createLeftButton(
                constructCButton(d.getLeftButtonList()), info.getSecurity(),
                d.getLeftButtonList(), LeftMenu.MenuType.LEFTPANEL);
        M.getLeftMenu().createLeftButton(constructCButton(d.getUpMenuList()),
                info.getSecurity(), d.getUpMenuList(),
                LeftMenu.MenuType.UPPANELMENU);
        M.getLeftMenu().createLeftButton(
                constructCButton(d.getLeftStackList()), info.getSecurity(),
                d.getLeftStackList(), LeftMenu.MenuType.LEFTSTACK);
        EnumTypesList eList = new EnumTypesList(d, liManager);
        if (!d.getFieldList().isEmpty()) {
            FormLineContainer fContainer = CreateForm.construct(info,
                    new GetEnumList(eList), eList, new HelperW(),
                    new DTypeFactory());

            DataViewModelFactory daFactory = GwtGiniInjector.getI()
                    .getDataViewModelFactory();

            IDataModelFactory dFactory = new DataModel();

            IDataViewModel daModel = daFactory.construct(dType, fContainer,
                    dFactory);
            // important: before not after next instruction
            pView.addElem(ICommonConsts.FORM, dType, daModel);
            iCon.copyCurrentVariablesToForm(slMediator, dType);
            SlU.registerChangeFormSubscriber(dType, slMediator, (IVField) null,
                    new ChangeField());
        }
        List<ControlButtonDesc> bList = null;
        if (!d.getButtonList().isEmpty()) {
            bList = CreateForm.constructBList(info.getSecurity(),
                    d.getButtonList());
            ListOfControlDesc deList = new ListOfControlDesc(bList);
            ControlButtonViewFactory bFactory = GwtGiniInjector.getI()
                    .getControlButtonViewFactory();
            IControlButtonView bView = bFactory.construct(dType, deList);
            slMediator.getSlContainer().registerSubscriber(dType,
                    ClickButtonType.StandClickEnum.ALL,
                    constructCButton(d.getButtonList()));
            pView.addElem(ICommonConsts.BUTTONS, dType, bView);
        }
        if (!d.getDatelineList().isEmpty())
            for (DateLine dl : d.getDatelineList()) {
                IDataType dLType = DataType.construct(dl.getId(), this);
                CellId panelId = pView.addElemC(dl.getId(), dType);
                ISlotable i = dManager.contructSlotable(dType, dLType, dl,
                        panelId, new DateLineClick(dl.getId()));
                dLineType.put(dl.getId(), dLType);
                slMediator.registerSlotContainer(panelId, i);
            }
        if (!d.getListList().isEmpty())
            for (ListFormat f : d.getListList()) {
                String id = f.getId();
                IDataType da = DataType.construct(id, this);
                liManager.addList(da, id, f);
                CellId panelId = pView.addElemC(id, dType);
                ISlotable i = liManager.constructListControler(da, panelId,
                        iCon, new ListClick(da), new BackFactory());
                slMediator.registerSlotContainer(panelId, i);
                slMediator.getSlContainer().registerSubscriber(dType,
                        ClickButtonType.StandClickEnum.ALL,
                        constructCButton(d.getLeftButtonList()));
            }
        if (!d.getCheckList().isEmpty())
            for (CheckList c : d.getCheckList()) {
                String id = c.getId();
                IDataType dat = DataType.construct(id, this);
                gManager.addDataType(id, dat);
                CellId panelId = pView.addElemC(id, dType);
                slMediator.registerSlotContainer(panelId,
                        gManager.constructSlotable(id));
            }

        iCon.addFormVariables(new BAction(), new DialogVariablesGetSet(),
                liManager, gManager, dManager);

        pView.createView(cId);
        slMediator.startPublish(cId);
        if (d.isBefore()) {
            executeAction(ICommonConsts.BEFORE, new BackClass(null, true, null,
                    eList));
        } else {
            // display empty list
            for (IDataType da : liManager.getList()) {
                liManager.publishBeforeForm(da, null);
            }
        }
        CustomStringSlot sig = SendDialogFormSignal.constructSignal(dType);
        slMediator.getSlContainer().publish(sig, new SendDialogFormSignal(d));
        sig = BeforeTabChange.constructBeforeTabChangeSignal(dType);
        slMediator.getSlContainer().registerSubscriber(sig,
                new BeforeChangeTab());

        //
        if (bList != null) {
            for (IDataType da : liManager.getList()) {
                ListFormat li = liManager.getFormat(da);
                String buttList = li.getListButtonsValidate();
                if (!CUtil.EmptyS(buttList)) {
                    String vList[] = buttList.split(",");
                    for (String b : vList) {
                        for (ControlButtonDesc bu : bList) {
                            if (CUtil.EqNS(bu.getActionId().getCustomButt(), b)) {
                                CustomStringSlot sl = ButtonCheckLostFocusSignal
                                        .constructSlotButtonCheckFocusSignal(da);
                                ButtonCheckLostFocusSignal sign = new ButtonCheckLostFocusSignal(
                                        bu.getActionId(), dType);
                                slMediator.getSlContainer().publish(sl, sign);
                            }
                        }
                    }
                }
            }
        }
    }

    private class HandleYesNoDialog implements IYesNoAction {

        private final MapDialogVariable addV;
        private final ButtonItem bId;

        HandleYesNoDialog(MapDialogVariable addV, ButtonItem bId) {
            this.addV = addV;
            this.bId = bId;
        }

        @Override
        public void answer(String content, String title, final String param1,
                final WSize w) {
            IClickYesNo i = new IClickYesNo() {

                @Override
                public void click(boolean yes) {
                    DialogVariables v = iCon.getVariables(param1);
                    v.setValueB(ICommonConsts.JYESANSWER, yes);
                    v.copyVariables(addV);
                    // M.JR().runAction(v, d.getId(), param1,
                    // new BackClass(param1, false, w, null));
                    // 2013/04/14
                    String action = param1;
                    if (CUtil.EmptyS(param1) && bId != null)
                        action = bId.getId();
                    ExecuteAction.action(v, d.getId(), action, new BackClass(
                            param1, false, w, null));
                }

            };

            YesNoDialog yes = new YesNoDialog(content, title, i);
            yes.show(w);
        }
    }

    private class CloseDialog implements ISendCloseAction {

        private final String id;

        CloseDialog(String id) {
            this.id = id;
        }

        @Override
        public void closeAction(String resString) {
            SendCloseSignal sig = new SendCloseSignal(id);
            slMediator.getSlContainer().publish(
                    SendCloseSignal.constructSignal(dType), sig);
            if (iClose != null)
                iClose.closeAction(resString);
            // else {
            // IWebPanel i = GwtGiniInjector.getI().getWebPanel();
            // i.setDCenter(null);
            // }
            if (iEx != null) {
                iEx.setResultButton(id, resString);
            }
        }

    }

    private class AfterModal implements IExecuteAfterModalDialog {

        private String afterAction = null;

        @Override
        public void setAction(String afterAction) {
            this.afterAction = afterAction;

        }

        @Override
        public void setResultButton(String buttonid, String resVal) {
            if (CUtil.EmptyS(afterAction))
                return;
            DialogVariables v = iCon.getVariables(afterAction);
            v.setValueS(ICommonConsts.JBUTTONRES, buttonid);
            v.setValueS(ICommonConsts.JBUTTONDIALOGRES, resVal);
            ExecuteAction.action(v, d.getId(), afterAction, new BackClass(
                    afterAction, false, null, null));
        }

    }

    private boolean runAction(String id, WSize w, List<ButtonItem> bList) {
        ButtonItem bItem = DialogFormat.findE(bList, id);
        // it can be call from several places
        // so filter out not relevant
        if (bItem != null) {
            if (bItem.isValidateAction()) {
                if (!ValidateForm.validateV(dType, DialogContainer.this, d,
                        DataActionEnum.ChangeViewFormToInvalidAction))
                    return true;
            }
            if (bItem.isAction()) {
                String action = bItem.getAction();
                String param = bItem.getActionParam();
                String param1 = bItem.getAttr(ICommonConsts.ACTIONPARAM1);
                String param2 = bItem.getAttr(ICommonConsts.ACTIONPARAM2);
                PerformVariableAction.performAction(new HandleYesNoDialog(
                        new MapDialogVariable(), bItem), new CloseDialog(id),
                        action, param, param1, param2, w, iCon,
                        new AfterModal(), null);
                return true;
            }
            ExecuteAction.action(iCon, d.getId(), id, new BackClass(id, false,
                    w, null));
            return true;
        }
        return false;
        // 2012-04-03 : in order to add action from list
        // 2012-04-14 : cannot be at that place
        // not relevant should be weeded out
        // ExecuteAction.action(iCon, d.getId(), id, new BackClass(id, false, w,
        // null));
    }

    private class ActionButton implements IPerformClickAction {

        private final List<ButtonItem> bList;

        ActionButton(List<ButtonItem> bList) {
            this.bList = bList;
        }

        @Override
        public void click(String id, WSize w) {
            // can be null for standard buttons
            // do nothing, will be passed forward
            if (id == null) {
                return;
            }
            runAction(id, w, bList);
        }
    }

    private class BackClass extends CommonCallBack<DialogVariables> {

        private final String id;
        private final boolean before;
        private final WSize w;
        private final EnumTypesList eList;
        private final MapDialogVariable addV;

        BackClass(String id, boolean before, WSize w, EnumTypesList eList,
                MapDialogVariable addV) {
            this.id = id;
            this.before = before;
            this.w = w;
            this.eList = eList;
            this.addV = addV;
        }

        BackClass(String id, boolean before, WSize w, EnumTypesList eList) {
            this(id, before, w, eList, new MapDialogVariable());
        }

        @Override
        public void onMySuccess(final DialogVariables arg) {
            PerformVariableAction.VisitList vis = new PerformVariableAction.VisitList() {

                @Override
                public void accept(IDataType da, ListOfRows lRows) {
                    // at the beginning send null list to have list header
                    // displayed
                    if (!before && lRows == null) {
                        return;
                    }
                    liManager.publishBeforeForm(da, lRows);
                }

                @Override
                public void acceptTypes(String typeName, ListOfRows lRows) {
                    eList.add(typeName, lRows);
                }

                @Override
                public void acceptFooter(IDataType da, List<IGetFooter> fList) {
                    liManager.publishBeforeFooter(da, fList);
                }

                @Override
                public void acceptEditListMode(IDataType da, EditListMode e) {
                    liManager.publishBeforeListEdit(da, e);
                }

            };
            PerformVariableAction.perform(new HandleYesNoDialog(addV, null),
                    new CloseDialog(id), arg, iCon, liManager, vis, w,
                    new AfterModal());
            if (!arg.getCheckVariables().isEmpty()) {
                gManager.addLinesAndColumns(id, arg);
            }
            JUtils.IVisitor statusC = new JUtils.IVisitor() {

                @Override
                public void action(String fie, String field) {
                    IWebPanel.InfoType tType = null;
                    try {
                        tType = IWebPanel.InfoType.valueOf(fie);
                    } catch (IllegalArgumentException e) {
                        String mess = M.M().NotValidStatusTextType(field, fie);
                        Utils.errAlert(mess, e);
                    }
                    String textF = ICommonConsts.JSTATUSTEXT + fie;
                    String info = null;
                    if (arg.getValue(textF) != null)
                        info = arg.getValueS(textF);
                    IWebPanel i = GwtGiniInjector.getI().getWebPanel();
                    i.setPaneText(tType, info);
                }

            };
            JUtils.visitListOfFields(arg, ICommonConsts.JSTATUSSET, statusC);

            JUtils.IVisitor visC = new JUtils.IVisitor() {

                @Override
                public void action(String fie, String field) {
                    String[] li = fie.split("_");
                    if (li.length != 2) {
                        String mess = M.M().InproperFormatCheckSet(field,
                                ICommonConsts.JSETATTRCHECK);
                        Utils.errAlertB(mess);
                    }
                    String checkid = li[0];
                    String action = li[1];
                    CheckList cList = d.findCheckList(checkid);
                    if (cList == null) {
                        String mess = M.M().CannotFindCheckList(field, checkid);
                        Utils.errAlertB(mess);
                    }
                    if (!action.equals(ICommonConsts.READONLY)) {
                        String mess = M.M().CheckListActionNotExpected(field,
                                action);
                        Utils.errAlertB(mess);
                    }
                    String valS = ICommonConsts.JVALATTRCHECK + checkid + "_"
                            + action;
                    FieldValue val = arg.getValue(valS);
                    gManager.modifAttr(checkid, action, val);

                }
            };
            JUtils.visitListOfFields(arg, ICommonConsts.JSETATTRCHECK, visC);

            // refresh dateline
            for (String id : arg.getDatelineVariables().keySet()) {
                IDataType dType = dLineType.get(id);
                if (dType == null) {
                    Utils.errAlert(M.M().DataLineNotDefined(id,
                            ICommonConsts.DATELINE, d.getId()));
                    continue;
                }
                DateLineVariables var = arg.getDatelineVariables().get(id);
                CustomStringSlot sl = RefreshData.constructRefreshData(dType);
                RefreshData da = new RefreshData(var);
                getSlContainer().publish(sl, da);
            }
            // dateline request for refresh
            JUtils.IVisitor visDL = new JUtils.IVisitor() {

                @Override
                public void action(String fie, String field) {
                    List<IBackAction> bList = iCon.getList();
                    for (IBackAction b : bList) {
                        b.refreshDateLine(fie);
                    }

                }
            };
            JUtils.visitListOfFields(arg, ICommonConsts.JREFRESHDATELINE, visDL);
            VerifyJError.isError(DialogContainer.this, dType, arg,
                    DialogContainer.this);
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

    void executeAction(String actionId, AsyncCallback<DialogVariables> callback) {
        DialogVariables v = iCon.getVariables(actionId);
        // null should be sent also
        v.setValueS(ICommonConsts.JBUTTONDIALOGSTART, startVal);
        ExecuteAction.action(v, d.getId(), actionId, callback);
    }

    DialogFormat getD() {
        return d;
    }

    public DialogInfo getInfo() {
        return info;
    }

    public boolean okCheckListError(DialogVariables v) {
        return gManager.okGridErrors(v);
    }

}
