/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.IGetSetVField;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;

/**
 * Utility class for copying data between view and model
 * 
 * @author hotel
 * 
 */

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
        copyFromDataToView(aFrom, fContainer.getvList());
    }

    /**
     * Copies values from model to view
     * 
     * @param aFrom
     *            IVModelData to copy from
     * @param vList
     *            Destination view
     */
    public static void copyFromDataToView(IVModelData aFrom,
            List<IGetSetVField> vList) {
        for (IGetSetVField v : vList) {
            if (aFrom.isValid(v.getV())) {
              Object o = FUtils.getValue(aFrom, v.getV());
              v.setValObj(o);
            }
        }
    }

    /**
     * Copies values from view to model data
     * 
     * @param fContainer
     *            Source view
     * @param aTo
     *            Destination model data
     */
    public static void copyFromViewToData(FormLineContainer fContainer,
            IVModelData aTo) {
        copyFromViewToModel(fContainer.getvList(), aTo);
    }

    public static void copyFromViewToModel(List<IGetSetVField> vList,
            IVModelData aTo) {
        for (IGetSetVField v : vList) {
            IVField vFie = v.getV();
            if (aTo.isValid(vFie)) {
                aTo.setF(vFie, v.getValObj());
            }
        }
    }

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
}
