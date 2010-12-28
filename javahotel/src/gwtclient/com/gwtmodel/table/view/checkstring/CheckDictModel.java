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
package com.gwtmodel.table.view.checkstring;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IGetDataList;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.view.ewidget.EditWidgetFactory;
import com.gwtmodel.table.view.ewidget.RadioBoxString;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class CheckDictModel implements ICheckDictModel {

    private final RadioBoxString serv;

    CheckDictModel(EditWidgetFactory eFactory, IVField v, IGetDataList iGet) {
        serv = eFactory.constructRadioBoxString(v, iGet, true);
    }

    @Override
    public void setValues(final List<String> val) {
        List<String> v = val;
        if (v == null) {
            v = new ArrayList<String>();
        }
        serv.setValues(v);
    }

    @Override
    public List<String> getValues() {
        return serv.getValues();
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        serv.setReadOnly(readOnly);
    }

    @Override
    public void refresh() {
        serv.refresh();
    }

    @Override
    public Widget getGWidget() {
        return serv.getGWidget();
    }
}
