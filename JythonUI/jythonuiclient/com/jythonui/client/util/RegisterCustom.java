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

import com.gwtmodel.table.IVField;
import com.gwtmodel.table.factories.IGetCustomValues;
import com.gwtmodel.table.factories.ITableAbstractFactories;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.jythonui.shared.ClientProp;
import com.jythonui.shared.CustomMessages;

public class RegisterCustom {

    private RegisterCustom() {

    }

    private static class CustomVal implements IGetCustomValues {

        private final ClientProp prop;

        CustomVal(ClientProp prop) {
            this.prop = prop;
        }

        @Override
        public IVField getSymForCombo() {
            return null;
        }

        @Override
        public String getCustomValue(String key) {
            return null;
        }

        @Override
        public boolean compareComboByInt() {
            return false;
        }

        @Override
        public boolean addEmptyAsDefault() {
            return false;
        }

        @Override
        public String getStandMessage(String key) {
            return prop.getCustomM().getAttr(key);
        }

        void setCustMess(CustomMessages cust) {
            prop.setCustomM(cust);
        }

    }

    public static void registerCustom(ClientProp prop) {
        if (prop.getCustomM() == null)
            return;
        ITableAbstractFactories tFactories = GwtGiniInjector.getI()
                .getITableAbstractFactories();
        tFactories.registerGetCustomValues(new CustomVal(prop));
    }

    public static void registerCustom(CustomMessages cust) {
        if (cust == null)
            return;
        IGetCustomValues c = GwtGiniInjector.getI()
                .getTableFactoriesContainer().getGetCustomValuesNotDefault();
        if (c == null)
            return;
        CustomVal cu = (CustomVal) c;
        cu.setCustMess(cust);
    }

}
