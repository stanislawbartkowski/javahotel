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

import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.datalisttype.DataListTypeFactory;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldValue;
import com.jythonui.shared.ListOfRows;
import com.jythonui.shared.RowContent;
import com.jythonui.shared.RowIndex;

/**
 * @author hotel
 * 
 */
public class JUtils {

    private JUtils() {
    }

    public interface IVisitor {
        void action(String fie, String field);
    };

    public static void visitListOfFields(DialogVariables var, String prefix,
            IVisitor i) {
        for (String key : var.getFields())
            if (key.startsWith(prefix)) {
                String fie = key.substring(prefix.length());
                i.action(fie, key);
            }
    }

    public static IDataListType constructList(RowIndex rI, ListOfRows rL,
            IVField comboField, IVField displayFie) {
        DataListTypeFactory lFactory = GwtGiniInjector.getI()
                .getDataListTypeFactory();

        List<IVModelData> rList = new ArrayList<IVModelData>();
        if (rL != null)
            for (RowContent t : rL.getRowList()) {
                RowVModelData r = new RowVModelData(rI, t);
                rList.add(r);
            }
        return lFactory.construct(rList, comboField, displayFie);
    }
    
    public static void setVariables(DialogVariables v, IVModelData vData) {
        if (vData == null) {
            return;
        }
        for (IVField fie : vData.getF()) {
            Object o = vData.getF(fie);
            // pass empty as null (None)
            FieldValue fVal = new FieldValue();
            fVal.setValue(fie.getType().getType(), o, fie.getType()
                    .getAfterdot());
            v.setValue(fie.getId(), fVal);
        }
    }


}
