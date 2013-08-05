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
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.TT;
import com.gwtmodel.table.editc.IRequestForGWidget;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.MM;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.tabledef.IColumnImageSelect;
import com.gwtmodel.table.tabledef.VFooterDesc;
import com.gwtmodel.table.tabledef.VListHeaderContainer;
import com.gwtmodel.table.tabledef.VListHeaderDesc;
import com.gwtmodel.table.view.ewidget.EditWidgetFactory;
import com.jythonui.client.M;
import com.jythonui.client.dialog.VField;
import com.jythonui.shared.ButtonItem;
import com.jythonui.shared.DialSecurityInfo;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogInfo;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.ListFormat;
import com.jythonui.shared.SecurityInfo;
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

    public interface ISelectFactory {

        IColumnImageSelect construct(IVField v,FieldItem f);
    }

    public static FormLineContainer construct(DialogInfo dInfo,
            IGetDataList iGet, EnumTypesList eList, IRequestForGWidget iHelper,
            IConstructCustomDataType fType) {
        DialogFormat d = dInfo.getDialog();
        List<FieldItem> iList = d.getFieldList();
        EditWidgetFactory eFactory = GwtGiniInjector.getI()
                .getEditWidgetFactory();
        List<FormField> fList = new ArrayList<FormField>();
        for (FieldItem f : iList) {
            IVField vf = VField.construct(f);
            IFormLineView v;
            String htmlId = f.getHtmlId();
            if (!CUtil.EmptyS(f.getCustom())) {
                TypedefDescr te = d.findCustomType(f.getCustom());
                if (te == null) {
                    Utils.errAlert(f.getCustom(),
                            M.M().CannotFindCustomType(f.getTypeName()));
                }
                if (te.isComboType()) {
                    eList.add(vf, f.getCustom());
                    v = eFactory.constructListValuesCombo(vf, iGet,
                            !f.isNotEmpty(), htmlId);
                } else {
                    IDataType dType = fType.construct(f.getTypeName());
                    v = eFactory.constructHelperList(vf, dType,
                            f.isHelperRefresh(), htmlId);
                }
            } else {
                if (f.isPassword()) {
                    v = eFactory.constructPasswordField(vf, htmlId);
                } else if (f.isHelper() || f.isTextArea() || f.isRichText()) {
                    if (f.isHelper() && (f.getFieldType() != TT.STRING)
                            && f.getFieldType() != TT.DATE) {
                        String mess = M.M().HelperOnlyForStringType(f.getId());
                        Utils.errAlertB(mess);
                    }
                    if (f.getFieldType() == TT.STRING)
                        v = eFactory.constructTextField(vf, null,
                                f.isHelper() ? iHelper : null, f.isTextArea(),
                                f.isRichText(), f.isHelperRefresh(), htmlId);
                    else
                        v = eFactory.constructDateBoxCalendarWithHelper(vf,
                                iHelper, true, htmlId);
                } else {
                    v = eFactory.constructEditWidget(vf, htmlId);
                }

            }
            boolean modeSetAlready = false;
            if (dInfo.getSecurity().isFieldHidden(f)) {
                v.setHidden(true);
                modeSetAlready = true;
            }
            if (dInfo.getSecurity().isFieldReadOnly(f)) {
                v.setReadOnly(true);
                modeSetAlready = true;
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
                    f.isReadOnlyChange(), f.isReadOnlyAdd(), modeSetAlready);
            fList.add(fie);
        }
        return new FormLineContainer(fList);
    }

    public static class ColumnsDesc {
        public List<VListHeaderDesc> hList = new ArrayList<VListHeaderDesc>();
        public List<VFooterDesc> footList = new ArrayList<VFooterDesc>();
        public int colvisNo = 0;
    }

    public static ColumnsDesc constructColumns(List<FieldItem> fList,
            DialSecurityInfo lInfo, ISelectFactory sFactory) {
        ColumnsDesc desc = new ColumnsDesc();
        List<VListHeaderDesc> heList = desc.hList;
        for (FieldItem f : fList) {
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
            // TODO: can be null for combo, check it later
            IColumnImageSelect iHelper = null;
            if (f.isHelper() && sFactory != null) {
                iHelper = sFactory.construct(vf,f);
            }
            VListHeaderDesc v = new VListHeaderDesc(getDisplayName(f), vf,
                    lInfo == null ? f.isHidden() : lInfo.isFieldHidden(f),
                    f.getActionId(), f.isColumnEditable(), al, f.getWidth(),
                    null, null, iHelper, 0);
            heList.add(v);
            if (!f.isHidden())
                desc.colvisNo++;
            if (f.isFooter()) {
                if (desc.footList == null)
                    desc.footList = new ArrayList<VFooterDesc>();
                VFooterDesc foot = new VFooterDesc(vf, al, vf.getType());
                desc.footList.add(foot);
            }
        }
        return desc;
    }

    public static VListHeaderContainer constructColumns(SecurityInfo sInfo,
            ListFormat l, ISelectFactory sFactory) {
        DialSecurityInfo lInfo = sInfo.getListSecur().get(l.getId());
        ColumnsDesc desc = constructColumns(l.getColumns(), lInfo, sFactory);
        String lName = l.getDisplayName();
        return new VListHeaderContainer(desc.hList, lName, l.getPageSize(),
                null, l.getWidth(), null, desc.footList);
    }

    private static ControlButtonDesc constructButton(ButtonItem b,
            boolean enabled) {

        String id = b.getId();
        String dName = b.getDisplayName();
        return new ControlButtonDesc(dName, id, enabled);
    }

    public static List<ControlButtonDesc> constructBList(SecurityInfo sInfo,
            List<ButtonItem> iList) {
        List<ControlButtonDesc> bList = new ArrayList<ControlButtonDesc>();
        for (ButtonItem b : iList) {
            if (sInfo.isButtHidden(b))
                continue;
            bList.add(constructButton(b, !sInfo.isButtReadOnly(b)));
        }
        return bList;
    }

}
