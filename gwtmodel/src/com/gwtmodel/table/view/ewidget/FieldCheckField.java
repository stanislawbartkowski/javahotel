/*
 * Copyright 2011 stanislawbartkowski@gmail.com
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

import com.google.gwt.user.client.ui.CheckBox;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.factories.ITableCustomFactories;

/**
 *
 * @author perseus
 */
class FieldCheckField extends AbstractField {

    private final CheckBox ch;

    FieldCheckField(ITableCustomFactories tFactories, IVField v) {
        super(tFactories,v);
        ch = new CheckBox();
        ch.setChecked(true);
        initWidget(ch);
//        setMouse();
    }

    @Override
    public void setValObj(Object o) {
        Boolean b = (Boolean) o;
        if (b != null) {
            ch.setChecked(b.booleanValue());
        }

    }

    @Override
    public Object getValObj() {
        Boolean b = new Boolean(ch.isChecked());
        return b;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        ch.setEnabled(!readOnly);
    }
}
