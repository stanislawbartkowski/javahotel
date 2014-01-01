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
package com.jythonui.client.util;

import java.util.HashSet;
import java.util.Set;

import com.gwtmodel.table.IOkModelData;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.injector.LogT;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.FieldValue;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.ListFormat;

public class CreateSearchVar {

    private CreateSearchVar() {

    }

    private static void setV(DialogVariables v, IVField fie, String prefix,
            FieldItem fItem, Object o) {
        FieldValue val = new FieldValue();
        val.setValue(fItem.getFieldType(), o, fItem.getAfterDot());
        v.setValue(prefix + fie.getId(), val);
    }

    public static void addSearchVar(DialogVariables v, ListFormat li,
            IOkModelData iOk) {
        boolean added = false;
        String searchV = ICommonConsts.JFILTR_SEARCH;
        Set<String> sSet = new HashSet<String>();
        if (iOk != null) {
            for (IOkModelData.ValidationData va : iOk.getValList()) {
                IVField fie = va.getF();
                sSet.add(fie.getId());
                FieldItem fItem = DialogFormat.findE(li.getColumns(),
                        fie.getId());
                assert fItem != null : LogT.getT().cannotBeNull();
                if (!added) {
                    v.setValueB(searchV, true);
                    added = true;
                }
                setV(v, fie, ICommonConsts.JSEARCH_FROM, fItem, va.getValFrom());
                setV(v, fie, ICommonConsts.JSEARCH_TO, fItem, va.getValTo());
                v.setValueB(ICommonConsts.JSEARCH_EQ + fie.getId(),
                        va.isCheck());
                v.setValueB(ICommonConsts.JSEARCH_SET + fie.getId(), true);
            }
        }
        if (!added) {
            v.setValueB(searchV, false);
        }
        for (FieldItem f : li.getColumns()) {
            if (!sSet.contains(f.getId())) {
                v.setValueB(ICommonConsts.JSEARCH_SET + f.getId(), false);
            }
        }
    }

}
