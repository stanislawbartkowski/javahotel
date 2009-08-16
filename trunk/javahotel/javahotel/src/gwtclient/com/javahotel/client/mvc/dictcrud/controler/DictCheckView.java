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
package com.javahotel.client.mvc.dictcrud.controler;

import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.common.command.DictType;
import com.javahotel.client.dialog.IPersistAction;
import com.javahotel.client.mvc.checkmodel.CheckDictModelFactory;
import com.javahotel.client.mvc.checkmodel.ICheckDictModel;
import com.javahotel.client.mvc.crud.controler.RecordModel;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class DictCheckView extends AbstractAuxRecordPanel {

    private final IResLocator rI;
    private final ICheckDictModel mo;

    IBeforeViewSignal getBefore() {
        return new IBeforeViewSignal() {

            public void signal(RecordModel a) {
                mo.refresh();
            }
        };
    }

    DictCheckView(final IResLocator rI, final DictType d) {
        this.rI = rI;
        mo = CheckDictModelFactory.getModel(rI, d);
    }

    ICheckDictModel getAuxO() {
        return mo;
    }

    @Override
    public void changeMode(int actionMode) {
        boolean enable = true;
        if (actionMode == IPersistAction.DELACTION) {
            enable = false;
        }
        mo.setEnable(enable);
    }

    public IMvcWidget getMWidget() {
        return mo.getMWidget();
    }
}
