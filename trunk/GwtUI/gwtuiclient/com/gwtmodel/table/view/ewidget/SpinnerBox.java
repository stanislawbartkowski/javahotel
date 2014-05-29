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
package com.gwtmodel.table.view.ewidget;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.factories.IGetCustomValues;

//        <label for="age">
//            I am   
//            <input type="number" name="age" id="age"
//                min="18" max="120" step="1" value="18">
//             years old
//        </label>

class SpinnerBox extends AbstractField {

    private final SpinnerInt t;

    private class CHandler implements ChangeHandler {

        @Override
        public void onChange(ChangeEvent event) {
            runOnChange(SpinnerBox.this, true);
        }
    }

    SpinnerBox(IGetCustomValues cValues, IVField v, String htmlName, int min,
            int max) {
        super(cValues, v, htmlName);
        t = new SpinnerInt(min, max);
        t.addChangeHandler(new CHandler());
        initWidget(t);
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        t.setReadOnly(readOnly);
    }

    @Override
    public Object getValObj() {
        String s = t.getValue();
        return FUtils.getValue(v, s);
    }

    @Override
    public void setValObj(Object o) {
        String s = FUtils.getValueOS(o, v);
        t.setValue(s);
    }

}
