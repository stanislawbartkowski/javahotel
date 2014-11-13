/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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
package com.jythonui.client.listmodel;

import com.gwtmodel.table.slotmodel.ISlotable;
import com.jythonui.client.interfaces.IRowListDataManagerFactory;
import com.jythonui.client.util.IConstructCustomDataType;
import com.jythonui.shared.DialogInfo;

public class RowListDataManagerFactory implements IRowListDataManagerFactory {

    @Override
    public IRowListDataManager construct(DialogInfo dialogInfo, ISlotable iSlo,
            IConstructCustomDataType tConstruct) {
        return new RowListDataManager(dialogInfo, iSlo, tConstruct);
    }

}
