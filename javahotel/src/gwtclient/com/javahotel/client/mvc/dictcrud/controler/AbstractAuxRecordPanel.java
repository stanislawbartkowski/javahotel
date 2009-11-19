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

import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.dialog.ISetGwtWidget;
import com.javahotel.client.mvc.contrpanel.view.IContrButtonView;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.record.view.IAuxRecordPanel;
import com.javahotel.client.mvc.record.view.ICreateViewContext;
import com.javahotel.client.mvc.validator.IErrorMessage;
import com.javahotel.client.mvc.validator.IRecordValidator;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
abstract public class AbstractAuxRecordPanel implements IAuxRecordPanel {

    protected IRecordContext iContx;

    public void changeMode(int actionMode) {
    }

    public IRecordValidator getValidator() {
        return null;
    }

    public void SetContextParam(IRecordContext iContx) {
        this.iContx = iContx;
    }

    public void showInvalidate(IErrorMessage col) {
    }

    public void show() {
    }

    public void hide() {
    }

    public void extractFields(RecordModel mo) {
    }

    public void setFields(RecordModel mo) {
    }

    public boolean getCustomView(ISetGwtWidget iSet, ICreateViewContext con,
            IContrButtonView i) {
        return false;
    }

}
