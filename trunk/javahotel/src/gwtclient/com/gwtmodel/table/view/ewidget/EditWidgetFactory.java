/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.view.ewidget;

import com.google.inject.Inject;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGetDataList;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.injector.TableFactoriesContainer;
import com.gwtmodel.table.rdef.IFormLineView;

public class EditWidgetFactory {

    private final TableFactoriesContainer tFactories;

    @Inject
    public EditWidgetFactory(TableFactoriesContainer tFactories) {
        this.tFactories = tFactories;
    }

    public RadioBoxString constructRadioBoxString(IGetDataList iGet,
            final boolean enable) {
        return new RadioBoxString(tFactories, iGet, enable);
    }

    public IFormLineView contructCalculatorNumber() {
        return new NumberCalculator(tFactories);
    }

    public IFormLineView constructListValuesBox(IDataType dType,
            final IVField fie) {
        GetValueLB lB = new GetValueLB(tFactories);
        AddBoxValues.addValues(dType, fie, lB);
        return lB;
    }

    public IFormLineView constructListValuesHelp(IDataType dType,
            final IVField fie) {
        return new ListFieldWithHelp(tFactories, dType, fie);
    }

    public IFormLineView constructListBoxValuesHelp(IDataType dType,
            final IVField fie) {
        GetValueLB lB = new ListBoxWithHelp(tFactories, dType, fie);
        AddBoxValues.addValues(dType, fie, lB);
        return lB;
    }

}
