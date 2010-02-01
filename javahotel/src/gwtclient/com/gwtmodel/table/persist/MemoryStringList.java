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
package com.gwtmodel.table.persist;

import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.controler.IDataControler;
import com.gwtmodel.table.controler.TableDataControlerFactory;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.slotmediator.ISlotMediator;
import com.gwtmodel.table.slotmediator.SlotMediatorFactory;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.slotmodel.SlotListContainer;

public class MemoryStringList implements ISlotable {
    
    private final TableDataControlerFactory tFactory;
    private final ISlotMediator slMediator;
    private IDataControler iDataControler;
    private final IDataListType lModel;
    private final IDataType vField;
        
    public MemoryStringList(IDataListType lModel) {
        tFactory = GwtGiniInjector.getI().getTableDataControlerFactory(); 
        slMediator = SlotMediatorFactory.construct();
        this.lModel = lModel;
        vField = new StringV();
    }

    public SlotListContainer getSlContainer() {
        return slMediator.getSlContainer();
    }

    public void startPublish(int cellId) {
        iDataControler = tFactory.constructDataControler(vField, cellId, cellId+1);
        slMediator.registerSlotContainer(-1,new MemoryListPersist(vField));
        slMediator.startPublish(cellId);        
    }

}
