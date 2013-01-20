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

import com.gwtmodel.table.IVField;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.tabledef.VListHeaderContainer;
import com.gwtmodel.table.tabledef.VListHeaderDesc;
import com.jythonui.client.M;
import com.jythonui.client.dialog.VField;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.ListFormat;

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

    public static FormLineContainer construct(DialogFormat d) {
        return construct(d.getFieldList());
    }

    public static FormLineContainer construct(List<FieldItem> iList) {
        List<FormField> fList = new ArrayList<FormField>();
        for (FieldItem f : iList) {
            String name = getDisplayName(f);
            IVField vf = VField.construct(f);
            IFormLineView v = EWidgetFactory.construct(vf);
            if (f.isHidden()) {
                v.setHidden(true);
            }
            if (f.isReadOnly()) {
                v.setReadOnly(true);
            }
            FormField fie = new FormField(name, v, vf, f.isReadOnlyChange(),
                    f.isReadOnlyAdd());
            fList.add(fie);
        }
        return new FormLineContainer(fList);
    }

    public static VListHeaderContainer constructColumns(ListFormat l) {
        List<VListHeaderDesc> heList = new ArrayList<VListHeaderDesc>();
        for (FieldItem f : l.getColumns()) {
            IVField vf = VField.construct(f);
            VListHeaderDesc v = new VListHeaderDesc(getDisplayName(f), vf,
                    f.isHidden());
            heList.add(v);
        }
        String lName = l.getDisplayName();
        return new VListHeaderContainer(heList, lName);
    }

}
