/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.InvalidateMess;
import com.gwtmodel.table.MutableInteger;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.VModelData;
import com.gwtmodel.table.common.TT;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.CustomStringSlot;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.jythonui.client.M;
import com.jythonui.client.dialog.ICreateBackActionFactory;
import com.jythonui.client.dialog.IPerformClickAction;
import com.jythonui.client.dialog.VField;
import com.jythonui.client.util.IConstructCustomDataType;
import com.jythonui.client.util.JUtils;
import com.jythonui.client.util.PerformVariableAction.VisitList;
import com.jythonui.client.util.PerformVariableAction.VisitList.IGetFooter;
import com.jythonui.client.util.RowVModelData;
import com.jythonui.client.util.VerifyJError;
import com.jythonui.client.variables.ISetGetVar;
import com.jythonui.client.variables.IVariablesContainer;
import com.jythonui.shared.DialogInfo;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.FieldValue;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.ListFormat;
import com.jythonui.shared.ListOfRows;
import com.jythonui.shared.RowIndex;

/**
 * @author hotel
 * 
 */
public class RowListDataManager implements ISetGetVar {

    private final Map<IDataType, String> listMap = new HashMap<IDataType, String>();
    private final Map<IDataType, ListFormat> lMap = new HashMap<IDataType, ListFormat>();
    private final Map<IDataType, RowIndex> rMap = new HashMap<IDataType, RowIndex>();
    private final DialogInfo dialogInfo;
    private final ISlotable iSlo;
    private final IConstructCustomDataType tConstruct;

    public RowListDataManager(DialogInfo dialogInfo, ISlotable iSlo,IConstructCustomDataType tConstruct) {
        this.dialogInfo = dialogInfo;
        this.iSlo = iSlo;
        this.tConstruct = tConstruct;
    }

    /**
     * @return the dialogName
     */
    DialogInfo getDialogInfo() {
        return dialogInfo;
    }

    String getDialogName() {
        return dialogInfo.getDialog().getId();
    }

    public void addList(IDataType di, String lId, ListFormat fo) {
        listMap.put(di, lId);
        lMap.put(di, fo);
        rMap.put(di, new RowIndex(fo.getColumns()));
    }

    public ListFormat getFormat(IDataType da) {
        return lMap.get(da);
    }

    public void publishBeforeForm(IDataType d, ListOfRows l) {
        FormBeforeCompletedSignal signal = new FormBeforeCompletedSignal(l);
        CustomStringSlot slot = FormBeforeCompletedSignal.constructSignal(d);
        iSlo.getSlContainer().publish(slot, signal);
    }

    public void publishBeforeFooter(IDataType d, List<IGetFooter> value) {
        DrawFooterSignal signal = new DrawFooterSignal(value);
        CustomStringSlot slot = DrawFooterSignal.constructSignal(d);
        iSlo.getSlContainer().publish(slot, signal);
    }

    public void publishBeforeListEdit(IDataType d, VisitList.EditListMode eModel) {
        ChangeToEditSignal signal = new ChangeToEditSignal(eModel);
        CustomStringSlot slot = ChangeToEditSignal.constructSignal(d);
        iSlo.getSlContainer().publish(slot, signal);
    }

    public List<IDataType> getList() {
        Iterator<IDataType> i = lMap.keySet().iterator();
        List<IDataType> l = new ArrayList<IDataType>();
        while (i.hasNext()) {
            l.add(i.next());
        }
        return l;
    }

    public String getLId(IDataType f) {
        return listMap.get(f);
    }

    RowIndex getR(IDataType d) {
        return rMap.get(d);
    }

    public ISlotable constructListControler(IDataType da, CellId panelId,
            IVariablesContainer iCon, IPerformClickAction iAction,
            ICreateBackActionFactory bFactory,IPerformClickAction custAction) {
        return ListControler.contruct(this, da, panelId, iCon, iAction,
                bFactory,custAction);
    }

    IVModelData contructE(IDataType da) {
        return new RowVModelData(rMap.get(da));
    }

    @Override
    public void addToVar(DialogVariables var, String buttonId) {
        for (IDataType dType : getList()) {
            AddVarList signal = new AddVarList(var, buttonId);
            CustomStringSlot sl = AddVarList.constructSignal(dType);
            iSlo.getSlContainer().publish(sl, signal);
        }

    }

    @Override
    public void readVar(DialogVariables var) {
        for (IDataType dType : getList()) {
            String listid = listMap.get(dType);

            String okKey = ICommonConsts.JEDITROW_OK + listid;
            FieldValue valOK = var.getValue(okKey);
            if (valOK != null) {
                if (valOK.getType() != TT.BOOLEAN) {
                    String mess = M.M().FooterSetValueShouldBeBoolean(
                            dialogInfo.getDialog().getId(), okKey);
                    Utils.errAlertB(mess);
                    continue;
                }
                AfterRowOk signal = new AfterRowOk(valOK.getValueB());
                CustomStringSlot sl = AfterRowOk.constructSignal(dType);
                iSlo.getSlContainer().publish(sl, signal);
            }

            String jKey = ICommonConsts.JEDITROWYESACTION + listid;
            FieldValue val = var.getValue(jKey);
            final RowVModelData vData = new RowVModelData(rMap.get(dType));
            final VModelData lData = new VModelData();
            final MutableInteger mu = new MutableInteger(0);
            JUtils.IFieldVisit iVisit = new JUtils.IFieldVisit() {

                @Override
                public void setField(VField v, FieldValue val) {
                    if (!vData.isValid(v))
                        return;
                    mu.inc();
                    vData.setF(v, val.getValue());
                    lData.setF(v, val.getValue());
                }
            };

            JUtils.VisitVariable(var, listid,iVisit);
            if (val != null) {
                if (val.getType() != TT.BOOLEAN) {
                    String mess = M.M().FooterSetValueShouldBeBoolean(
                            dialogInfo.getDialog().getId(), jKey);
                    Utils.errAlertB(mess);
                    continue;
                }
                if (!val.getValueB())
                    continue;
                RowActionOk signal = new RowActionOk(vData);
                CustomStringSlot sl = RowActionOk.constructSignal(dType);
                iSlo.getSlContainer().publish(sl, signal);
            } else if (mu.intValue() > 0) {
                SetNewValues signal = new SetNewValues(lData);
                CustomStringSlot sl = SetNewValues.constructSignal(dType);
                iSlo.getSlContainer().publish(sl, signal);
            }

            final ListFormat fo = getFormat(dType);
            VerifyJError.IOkFieldName iOk = new VerifyJError.IOkFieldName() {

                @Override
                public boolean okField(String s) {
                    FieldItem i = fo.getColumn(s);
                    if (i == null)
                        return false;
                    return i.isColumnEditable();
                }
            };
            List<InvalidateMess> err = VerifyJError.constructErrors(var, iOk);
            SendErrorsInfo signal = new SendErrorsInfo(err);
            CustomStringSlot sl = SendErrorsInfo.constructSignal(dType);
            iSlo.getSlContainer().publish(sl, signal);
        }

    }

    public void sendEnum(String customT, IDataListType dList) {

        SendEnumToList eList = new SendEnumToList(customT, dList);

        for (IDataType d : getList()) {
            CustomStringSlot sl = SendEnumToList.constructSignal(d);
            iSlo.getSlContainer().publish(sl, eList);
        }

    }

    public IConstructCustomDataType gettConstruct() {
        return tConstruct;
    }
    
    

}
