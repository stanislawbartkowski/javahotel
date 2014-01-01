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
package com.gwtmodel.table.slotmediator;

import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ISlotable;
import java.util.ArrayList;
import java.util.List;

class SlotMediator extends AbstractSlotContainer implements ISlotMediator {

    private final List<C> slList = new ArrayList<C>();

    private void addSlotContainer(CellId cellId, ISlotable iSlo) {
        slList.add(new C(cellId, iSlo));
    }

    private class C {

        private final CellId cellId;
        private final ISlotable iSlo;

        C(CellId cellId, ISlotable iSlo) {
            this.cellId = cellId;
            this.iSlo = iSlo;
        }
    }

    @Override
    public void registerSlotContainer(CellId cellId, ISlotable iSlo) {
        addSlotContainer(cellId, iSlo);
        iSlo.setSlContainer(this);
    }

    @Override
    public void startPublish(CellId nullId) {
        for (C c : slList) {
            c.iSlo.startPublish(c.cellId);
        }
    }

    @Override
    public void registerSlotContainer(ISlotable iSlo) {
        registerSlotContainer(null, iSlo);
    }

    @Override
    public void registerSlotContainer(int cellId, ISlotable iSlo) {
        registerSlotContainer(new CellId(cellId), iSlo);
    }
}
