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
package com.javahotel.view.gwt.record.view;

import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.IResLocator;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.dict.validator.errmess.DictErrorMessage;
import com.javahotel.client.mvc.dict.validator.errmess.InvalidateMess;
import com.javahotel.client.mvc.record.model.IRecordDef;
import com.javahotel.client.mvc.record.model.RecordField;
import com.javahotel.client.mvc.record.view.IRecordView;
import com.javahotel.client.mvc.record.view.helper.ExtractFields;
import com.javahotel.client.mvc.record.view.helper.RecordChangeMode;
import com.javahotel.client.mvc.validator.IErrorMessage;
import com.javahotel.common.toobject.IField;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
abstract class AbstractRecordView implements IRecordView {

    protected final IRecordDef model;
    protected final IResLocator rI;
    protected final ELineStore eStore = new ELineStore();

    AbstractRecordView(IResLocator rI, IRecordDef model) {
        this.rI = rI;
        this.model = model;
    }

    public IRecordDef getModel() {
        return model;
    }

    public void setPos(Widget w) {
    }

    public void show() {
    }

    public void hide() {
    }

    public String getString(IField f) {
        return ExtractFields.getString(model, f);
    }

    public void extractFields(final RecordModel a) {

        ExtractFields.extractFields(model, a);
    }

    public void setFields(final RecordModel a) {
        ExtractFields.setFields(model, a);
    }

    public void changeMode(int actionMode) {
        RecordChangeMode.changeMode(model, actionMode);
    }

    public void setString(IField f, String val) {
        ExtractFields.setString(model, f, val);
    }

    protected void setListener() {
        for (RecordField e : model.getFields()) {
            e.getELine().setKLi(new DefaultListener(eStore));
        }

    }

    public void showInvalidate(IErrorMessage me) {
        DictErrorMessage dictM = (DictErrorMessage) me;
        List<InvalidateMess> col = dictM.getErrmess();

        for (InvalidateMess m : col) {
            IField f = m.getFie();
            for (RecordField re : model.getFields()) {
                if (re.getFie().equals(f)) {
                    eStore.setEMess(re, m);
                }
            }
        }
    }
}
