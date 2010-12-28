/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.view.util;

import com.gwtmodel.table.FUtils;
import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormLineView;

public class FormUtil {

    private FormUtil() {
    }

    public static FormField findI(final List<FormField> fList, IVField f) {
        for (FormField re : fList) {
            if (re.getFie().eq(f)) {
                return re;
            }
        }
        return null;
    }

    public static void copyFromDataToView(IVModelData aFrom,
            FormLineContainer fContainer) {
        for (FormField d : fContainer.getfList()) {
            Object o = FUtils.getValue(aFrom, d.getFie());
            d.getELine().setValObj(o);
        }
    }

    public static void copyFromViewToData(FormLineContainer fContainer,
            IVModelData aTo) {
        for (FormField d : fContainer.getfList()) {
            IVField vFie = d.getFie();
            IFormLineView vView = d.getELine();
            aTo.setF(vFie, vView.getValObj());
        }
    }

//    public static void copyFromViewToDataS(FormLineContainer fContainer,
//            IVModelData aTo) {
//        for (FormField d : fContainer.getfList()) {
//            IVField vFie = d.getFie();
//            IFormLineView vView = d.getELine();
//            aTo.setF(vFie, vView.getVal());
//        }
//    }
    public static List<IVField> getVList(FormLineContainer fContainer) {
        List<IVField> fList = new ArrayList<IVField>();
        for (FormField d : fContainer.getfList()) {
            IVField vFie = d.getFie();
            fList.add(vFie);
        }
        return fList;
    }

    public static void copyData(IVModelData aFrom, IVModelData aTo) {
        for (IVField v : aFrom.getF()) {
            Object val = aFrom.getF(v);
            aTo.setF(v, val);
        }
    }
//    public static void copyDataS(IVModelData aFrom, IVModelData aTo) {
//        for (IVField v : aFrom.getF()) {
//            String val = aFrom.getS(v);
//            aTo.setF(v, val);
//        }
//    }
}
