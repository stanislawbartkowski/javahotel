/*
 * Copyright 2009 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.panelcommand;

import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.mvc.auxabstract.LoginRecord;
import com.javahotel.client.mvc.crud.controler.ICrudControler;
import com.javahotel.client.mvc.dictcrud.controler.DictCrudControlerFactory;
import com.javahotel.client.mvc.dictcrud.controler.RecordAuxParam;
import com.javahotel.client.mvc.table.model.ITableConverter;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.PersonP;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class HotelPersonCommand extends AbstractPanelCommand {

    private final IResLocator rI;
    private final RType r;
    private ICrudControler iCrud;

    private static class PersonConv implements ITableConverter {

        public AbstractTo convertA(AbstractTo a) {
            LoginRecord lo = new LoginRecord();
            PersonP pe = (PersonP) a;
            lo.setLogin(pe.getName());
            return lo;
        }
    }

    HotelPersonCommand(final IResLocator rI, final RType r) {
        this.rI = rI;
        this.r = r;
    }

    public void beforeDrawAction(ISetGwtWidget iSet) {
        RecordAuxParam aux = new RecordAuxParam();
        if (r == RType.AllPersons) {
            aux.setIConv(new PersonConv());
        }
        iCrud = DictCrudControlerFactory.getCrud(rI, new DictData(r),
                aux, null);
        iSet.setGwtWidget(iCrud.getMWidget());
    }

    public void drawAction() {
        iCrud.drawTable();
    }
}

