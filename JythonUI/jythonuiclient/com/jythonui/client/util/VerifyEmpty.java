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
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.InvalidateFormContainer;
import com.gwtmodel.table.InvalidateMess;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.view.ValidateUtil;
import com.jythonui.client.dialog.VField;
import com.jythonui.shared.FieldItem;

/**
 * @author hotel
 * 
 */
class VerifyEmpty {

    private VerifyEmpty() {

    }

    static boolean isEmpty(IDataType dType, IVModelData vData,
            ISlotable iSlo, List<FieldItem> fList, DataActionEnum errSig) {
        List<IVField> eList = new ArrayList<IVField>();
        for (FieldItem i : fList)
            if (i.isNotEmpty()) {
                IVField fie = VField.construct(i);
                eList.add(fie);
            }
        List<InvalidateMess> err = ValidateUtil.checkEmpty(vData, eList);
        if (err != null) {
            iSlo.getSlContainer().publish(dType, errSig,
                    new InvalidateFormContainer(err));
            return true;
        }
        return false;
    }

}
