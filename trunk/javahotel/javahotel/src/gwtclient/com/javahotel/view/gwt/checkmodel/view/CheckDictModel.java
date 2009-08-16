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
package com.javahotel.view.gwt.checkmodel.view;

import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.mvc.checkmodel.*;
import com.javahotel.client.IResLocator;
import com.javahotel.common.command.DictType;
import java.util.Collection;
import com.javahotel.client.idialog.ELineDialogMulChoice;
import com.javahotel.common.command.CommandParam;
import java.util.ArrayList;
import com.javahotel.client.idialog.GetIEditFactory;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class CheckDictModel implements ICheckDictModel {

    private final IResLocator rI;
    private final DictType d;
    private final ELineDialogMulChoice serv;

    CheckDictModel(final IResLocator rI, final DictType d) {
        this.rI = rI;
        this.d = d;
        CommandParam p = rI.getR().getHotelCommandParam();
        p.setDict(d);
        serv = GetIEditFactory.getMChoice(rI, p, false, false);
    }

    public void setValues(final Collection<String> val) {
        Collection<String> v = val;
        if (v == null) {
            v = new ArrayList<String>();
        }
        serv.setValues(v);
    }

    public Collection<String> getValues() {
        return serv.getValues();
    }

    public void setEnable(boolean enable) {
        serv.setReadOnly(!enable);
    }


    public void refresh() {
        serv.refresh();

    }

    public IMvcWidget getMWidget() {
        return serv.getMWidget();
    }
}
