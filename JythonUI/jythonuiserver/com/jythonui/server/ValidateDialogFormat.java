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
package com.jythonui.server;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gwtmodel.table.common.CUtil;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.ElemDescription;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.JythonUIFatal;
import com.jythonui.shared.ListFormat;

/**
 * @author hotel
 * 
 */
class ValidateDialogFormat {

    static final private Logger log = Logger
            .getLogger(ValidateDialogFormat.class.getName());

    static private void error(String mess) {
        log.log(Level.SEVERE, mess);
        throw new JythonUIFatal(mess);
    }

    private ValidateDialogFormat() {
    }

    private static void idNotNull(String tag, ElemDescription e) {
        if (CUtil.EmptyS(e.getId())) {
            error(tag + " cannot be empty");
        }
    }

    private static <T extends ElemDescription> void validateL(String lTag,
            String eTag, List<T> eList) {
        Set<String> sId = new HashSet<String>();
        for (ElemDescription e : eList) {
            idNotNull(lTag + " " + eTag, e);
            String id = e.getId();
            if (sId.contains(id)) {
                error(lTag + " " + eTag + " : duplicated " + ICommonConsts.ID
                        + " value duplicated:" + id);
            }
            sId.add(id);
        }
    }

    static void validate(DialogFormat d) {
        if (d.getLeftButtonList() != null) {
            validateL(ICommonConsts.LEFTMENU, ICommonConsts.BUTTON,
                    d.getLeftButtonList());
        }
        if (d.getFieldList() != null) {
            validateL(ICommonConsts.FORM, ICommonConsts.FIELD, d.getFieldList());
        }
        if (d.getListList() != null) {
            for (ListFormat l : d.getListList()) {
                idNotNull(ICommonConsts.LIST, l);
                validateL(ICommonConsts.COLUMNS, ICommonConsts.COLUMN,
                        l.getColumns());
            }
        }
        if ((d.getFieldList() != null) && (d.getListList() != null)) {
            Set<String> sId = new HashSet<String>();
            for (FieldItem f : d.getFieldList()) {
                sId.add(f.getId());
            }
            for (ListFormat l : d.getListList()) {
                String id = l.getId();
                if (sId.contains(id)) {
                    error(ICommonConsts.LIST
                            + "duplicated itentifier like one of "
                            + ICommonConsts.FIELD + " duplicated value : " + id);
                }
                sId.add(id);
            }
        }
    }

}
