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
package com.jythonui.client.util;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGetDataList;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.editc.IRequestForGWidget;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.MM;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.tabledef.VListHeaderContainer;
import com.gwtmodel.table.tabledef.VListHeaderDesc;
import com.gwtmodel.table.view.ewidget.EditWidgetFactory;
import com.jythonui.client.M;
import com.jythonui.client.dialog.VField;
import com.jythonui.shared.ButtonItem;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.ListFormat;
import com.jythonui.shared.TypedefDescr;

/**
 * @author hotel
 * 
 */
public class CreateForm {

    private CreateForm() {
    }

    private static String getDisplayName(FieldItem f) {
        String name = f.getDisplayName();
        if (name == null) {
            name = M.J().DefaStringName();
        }
        return name;
    }

    public static FormLineContainer construct(DialogFormat d,
            IGetDataList iGet, EnumTypesList eList, IRequestForGWidget iHelper,
            IConstructCustomDataType fType) {
        List<FieldItem> iList = d.getFieldList();
        EditWidgetFactory eFactory = GwtGiniInjector.getI()
                .getEditWidgetFactory();
        List<FormField> fList = new ArrayList<FormField>();
        for (FieldItem f : iList) {
            IVField vf = VField.construct(f);
            IFormLineView v;
            if (!CUtil.EmptyS(f.getCustom())) {
                TypedefDescr te = d.findCustomType(f.getCustom());
                if (te == null) {
                    Utils.errAlert(f.getCustom(),
                            M.M().CannotFindCustomType(f.getTypeName()));
                }
                if (te.isComboType()) {
                    eList.add(vf, f.getCustom());
                    v = eFactory.constructListValuesCombo(vf, iGet,
                            !f.isNotEmpty());
                } else {
                    IDataType dType = fType.construct(f.getTypeName());
                    v = eFactory.constructHelperList(vf, dType,
                            f.isHelperRefresh());
                }
            } else {
                if (f.isPassword()) {
                    v = eFactory.constructPasswordField(vf);
                } else if (f.isHelper() || f.isTextArea() || f.isRichText()) {
                    v = eFactory.constructTextField(vf, null,
                            f.isHelper() ? iHelper : null, f.isTextArea(),
                            f.isRichText(), f.isHelperRefresh());
                } else {
                    v = eFactory.constructEditWidget(vf);
                }

            }
            if (f.isHidden()) {
                v.setHidden(true);
            }
            if (f.isReadOnly()) {
                v.setReadOnly(true);
            }

            String name = null;
            IVField fRange = null;
            if (!CUtil.EmptyS(f.getFrom())) {
                if (CUtil.EmptyS(f.getDisplayName())) {
                    name = MM.getL().BetweenFieldsRange();
                }
                FieldItem ff = d.findFieldItem(f.getFrom());
                if (ff == null) {
                    Utils.errAlert(M.M().CannotFindFromField(
                            ICommonConsts.FROM, f.getFrom()));
                } else
                    fRange = VField.construct(ff);
            }
            if (name == null)
                name = getDisplayName(f);

            FormField fie = new FormField(name, v, vf, fRange,
                    f.isReadOnlyChange(), f.isReadOnlyAdd());
            fList.add(fie);
        }
        return new FormLineContainer(fList);
    }

    public static VListHeaderContainer constructColumns(ListFormat l) {
        List<VListHeaderDesc> heList = new ArrayList<VListHeaderDesc>();
        for (FieldItem f : l.getColumns()) {
            IVField vf = VField.construct(f);
            VListHeaderDesc.ColAlign al = null;
            if (CUtil.EqNS(f.getAlign(), ICommonConsts.ALIGNL)) {
                al = VListHeaderDesc.ColAlign.LEFT;
            }
            if (CUtil.EqNS(f.getAlign(), ICommonConsts.ALIGNR)) {
                al = VListHeaderDesc.ColAlign.RIGHT;
            }
            if (CUtil.EqNS(f.getAlign(), ICommonConsts.ALIGNC)) {
                al = VListHeaderDesc.ColAlign.CENTER;
            }

            VListHeaderDesc v = new VListHeaderDesc(getDisplayName(f), vf,
                    f.isHidden(), f.getActionId(), false, al, f.getWidth());
            heList.add(v);
        }
        String lName = l.getDisplayName();

        return new VListHeaderContainer(heList, lName, l.getPageSize(), null,
                l.getWidth(), null, null);
    }

    private static ControlButtonDesc constructButton(ButtonItem b) {

        String id = b.getId();
        String dName = b.getDisplayName();
        return new ControlButtonDesc(dName, id);
    }

    public static List<ControlButtonDesc> constructBList(List<ButtonItem> iList) {
        List<ControlButtonDesc> bList = new ArrayList<ControlButtonDesc>();
        for (ButtonItem b : iList) {
            bList.add(constructButton(b));
        }
        return bList;
    }

}