/*
 *  Copyright 2010 stanislawbartkowski@gmail.com
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package com.gwtmodel.table.injector;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.factories.ITableCustomFactories;
import com.gwtmodel.table.slotmodel.ISlotable;

/**
 *
 * @author hotel
 */
public interface ICallContext {

    IDataType getDType();

    ISlotable iSlo();

    TablesFactories getT();

    ITableCustomFactories getC();

    void setdType(IDataType dType);

    void setiSlo(ISlotable iSlo);

    ICallContext construct(IDataType dType);

    ICallContext construct(ISlotable iSlo, IDataType dType);

    ICallContext construct(ISlotable iSlo);

}
