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
package com.javahotel.client.mvc.controller.onearecord;

import com.javahotel.client.mvc.controller.onerecordmodif.IOneRecordModifWidget;
import com.javahotel.client.mvc.dictdata.model.IOneRecordModel;
import com.javahotel.client.mvc.contrpanel.view.IControlClick;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.table.model.ITableFilter;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.client.mvc.validator.IErrorMessage;
import com.javahotel.client.mvc.validator.IRecordValidator;
import com.javahotel.client.mvc.crud.controler.ICrudRecordFactory;


/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public interface IOneARecord extends IOneRecordModel {

    IControlClick getClick(ITableFilter tFilter);

    IOneRecordModifWidget getMWidget();

    void setMWidget(IOneRecordModifWidget mWidget);

    RecordModel getRModel();

    void setFields(AbstractTo cp);

    AbstractTo getExtractFields();

    void ExtractFields();

    void changeMode(int actionMode);

    void showInvalidate(IErrorMessage col);

    IRecordValidator getValidator();

    void setModifWidgetStatus(boolean modif);

    void setNewWidgetStatus(boolean modif);
    
    ICrudRecordFactory getFactory();

}
