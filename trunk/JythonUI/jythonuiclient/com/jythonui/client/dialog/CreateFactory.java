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

import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.factories.IDataFormConstructorAbstractFactory;
import com.gwtmodel.table.factories.IDataPersistAction;
import com.gwtmodel.table.factories.IDataPersistListAction;
import com.gwtmodel.table.factories.IFormTitleFactory;
import com.gwtmodel.table.factories.IPersistFactoryAction;
import com.gwtmodel.table.factories.ITableAbstractFactories;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.tabledef.VListHeaderContainer;
import com.gwtmodel.table.view.callback.CommonCallBack;
import com.jythonui.client.util.CreateForm;
import com.jythonui.client.util.JUtils;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.ListFormat;
import com.jythonui.shared.ListOfRows;
import com.jythonui.shared.RowIndex;
import com.jythonui.shared.TypedefDescr;

public class CreateFactory {

    private CreateFactory() {
    }

    static class FormFactory implements IDataFormConstructorAbstractFactory {

        public CType construct(ICallContext iContext) {
            return new IDataFormConstructorAbstractFactory.CType();
        }
    }

    static class PersistListFactory extends AbstractSlotContainer implements
            IDataPersistListAction {

        private class ReadList implements ISlotListener {

            private class ReadC extends CommonCallBack<DialogVariables> {

                @Override
                public void onMySuccess(DialogVariables arg) {
                    DataType d = (DataType) dType;
                    String id = d.getId();
                    String tType = FieldItem.getCustomT(id);
                    ListOfRows liR = arg.getEnumList().get(tType);
                    TypedefDescr te = d.getD().getD().findCustomType(tType);
                    RowIndex rI = new RowIndex(te.getListOfColumns());
                    IVField comboId = VField.construct(te.getComboId());
                    IVField displayId = null;
                    if (!CUtil.EmptyS(te.getDisplayName())) {
                        displayId = VField.construct(te.getDisplayName());
                    }
                    IDataListType dList = JUtils.constructList(rI, liR,
                            comboId, displayId);
                    getSlContainer().publish(dType,
                            DataActionEnum.ListReadSuccessSignal, dList);
                    ListFormat l = new ListFormat();
                    l.getColumns().addAll(te.getListOfColumns());
                    l.setAttr(ICommonConsts.DISPLAYNAME, te.getDisplayName());
                    VListHeaderContainer vHeader = CreateForm.constructColumns(d.getD().getInfo().getSecurity(),l);                    
                    publish(dType, vHeader);
                }

            }

            @Override
            public void signal(ISlotSignalContext slContext) {
                DataType d = (DataType) dType;
                String id = d.getId();
                d.getD().executeAction(id, new ReadC());
            }

        }

        PersistListFactory(IDataType dType) {
            this.dType = dType;
            this.getSlContainer().registerSubscriber(dType,
                    DataActionEnum.ReadListAction, new ReadList());
        }

    }

    static class PersistDataAction implements IPersistFactoryAction {

        @Override
        public IDataPersistAction construct(IDataType dType) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public IDataPersistListAction constructL(IDataType dType) {
            return new PersistListFactory(dType);
        }

    }

    static class FormTitleFactory implements IFormTitleFactory {

        @Override
        public String getFormTitle(ICallContext iContext) {
            return "";
        }

    }

    static public void create() {
        // IDataPersistListAction
        ITableAbstractFactories tFactories = GwtGiniInjector.getI()
                .getITableAbstractFactories();
        tFactories
                .registerDataFormConstructorAbstractFactory(new FormFactory());
        tFactories.registerPersistFactory(new PersistDataAction());
        tFactories.registerFormTitleFactory(new FormTitleFactory());
    }

}
