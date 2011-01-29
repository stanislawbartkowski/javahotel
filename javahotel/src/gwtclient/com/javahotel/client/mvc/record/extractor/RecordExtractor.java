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
package com.javahotel.client.mvc.record.extractor;

import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.mvc.auxabstract.LoginRecord;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.record.view.IAuxRecordPanel;
import com.javahotel.client.mvc.record.view.IRecordView;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.PersonP;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class RecordExtractor implements IRecordExtractor {

    private final DictData da;
    private final IResLocator rI;

    RecordExtractor(final IResLocator rI, final DictData da) {
        this.da = da;
        this.rI = rI;
    }

    public void toA(RecordModel a, IRecordView view) {
        view.extractFields(a);
        IAuxRecordPanel aV = view.getAuxV();
        if (aV != null) {
            aV.extractFields(a);
        }
    }

    public void toView(IRecordView view, RecordModel a) {
        IAuxRecordPanel aV = view.getAuxV();
        if ((da.getRt() == RType.AllPersons) && a.getA() instanceof PersonP) {
            PersonP pe = (PersonP) a.getA();
            String name = pe.getName();
            view.setString(LoginRecord.F.login,name);
        } else {
          view.setFields(a);
        }
        if (aV != null) {
            aV.setFields(a);
        }
    }
}
