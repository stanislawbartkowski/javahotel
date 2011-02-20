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
package com.javahotel.nmvc.factories.booking;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.factories.IFormDefFactory;
import com.gwtmodel.table.factories.IFormTitleFactory;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.javahotel.nmvc.factories.FormDefFactory;

class BookElemDataFactory implements IFormTitleFactory, IFormDefFactory {

    private final FormDefFactory fFactory;
    
    BookElemDataFactory(FormDefFactory fFactory) {
        this.fFactory = fFactory;
    }

    @Override
    public FormLineContainer construct(IDataType dType) {
        FormLineContainer fContainer = fFactory.construct(dType);
        return fContainer;
    }

    @Override
    public String getFormTitle(IDataType dType) {
        return "Rezerwacja";
    }
}
