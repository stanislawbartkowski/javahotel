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
package com.javahotel.client.mvc.record.view;

import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.dialog.IMvcShowHide;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.record.model.IRecordDef;
import com.javahotel.client.mvc.validator.IErrorMessage;
import com.javahotel.common.toobject.IField;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public interface IRecordView extends IMvcShowHide {

    IAuxRecordPanel getAuxV();

    IRecordDef getModel();

    void showInvalidate(IErrorMessage col);

    String getString(IField f);

    void setString(IField f, String val);

    void extractFields(RecordModel mo);

    void setFields(RecordModel mo);

    void changeMode(int actionMode);

    void setPos(Widget w);


}
