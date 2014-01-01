/*
 *  Copyright 2014 stanislawbartkowski@gmail.com
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
package com.gwtmodel.table.slotmodel;

import com.gwtmodel.table.IDataType;

/**
 * 
 * @author hotel
 */
public class TemplateContainerSlotable<T extends ISlotable> implements
        ISlotable {

    protected T iSlot;
    protected IDataType dType;

    @Override
    public SlotListContainer getSlContainer() {
        return iSlot.getSlContainer();
    }

    @Override
    public void startPublish(CellId cellId) {
        iSlot.startPublish(cellId);
    }

    @Override
    public void setSlContainer(ISlotable iSlo) {
        iSlot.setSlContainer(iSlo);

    }

    @Override
    public void setSlotContainerReference(SlotContainerReference sReference) {
        iSlot.setSlotContainerReference(sReference);
    }

    @Override
    public SlotContainerReference getSlotContainerReference() {
        return iSlot.getSlotContainerReference();
    }

}
