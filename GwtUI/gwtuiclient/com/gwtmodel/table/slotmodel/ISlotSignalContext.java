/*
 * Copyright 2016 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.slotmodel;

import com.gwtmodel.table.*;
import com.gwtmodel.table.common.PersistTypeEnum;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.tabledef.VListHeaderContainer;

public interface ISlotSignalContext {

    SlotType getSlType();

    IFormLineView getChangedValue();

    IValidateError getValidateError();

    IGWidget getGwtWidget();

    IDataListType getDataList();

    WSize getWSize();

    IVModelData getVData();

    PersistTypeEnum getPersistType();

    VListHeaderContainer getListHeader();

    IVField getVField();

    FormLineContainer getEditContainer();

    String getStringButton();

    IGWidget getHtmlWidget();

    ICustomObject getCustom();

    IOkModelData getIOkModelData();

}
