/*
 * Copyright 2016 stanislawbartkowski@gmail.com 
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

import java.util.List;

import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.rdef.FormLineContainer;

/**
 * @author hotel
 * 
 */
public class CreateReadOnly {

    private CreateReadOnly() {
    }

    private static class FormLineDataView implements IVModelData {

        private final FormLineContainer fo;

        FormLineDataView(FormLineContainer fo) {
            this.fo = fo;
        }

        public Object getF(IVField fie) {
            return fo.findLineView(fie).getValObj();
        }

        public void setF(IVField fie, Object o) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public boolean isValid(IVField fie) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public List<IVField> getF() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Object getCustomData() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void setCustomData(Object o) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    };

    public static IVModelData contructReadonlyVModel(FormLineContainer fo) {
        return new FormLineDataView(fo);
    }

}
