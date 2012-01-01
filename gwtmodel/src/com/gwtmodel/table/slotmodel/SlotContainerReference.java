/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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

import java.util.ArrayList;
import java.util.List;

/**
 * @author hotel
 * Contains reference to slContainer and list if ISlotable pointint to it
 */
class SlotContainerReference {
    
    /** SlotListContainer. */
    private final SlotListContainer slContainer;
    /** List of ISlotable pointing to this reference. */
    private final List<ISlotable> sList = new ArrayList<ISlotable>();
    
    /**
     * Constructor
     * @param iSlo ISlotable pointing to this reference
     * @param slContainer SlotListContainer itself
     */
    SlotContainerReference(ISlotable iSlo,SlotListContainer slContainer) {
        this.slContainer = slContainer; 
        sList.add(iSlo);
    }

    /**
     * @return the slContainer
     */
    SlotListContainer getSlContainer() {
        return slContainer;
    }
    
    /**
     * Replace all ISlotable grouped in sRef with this reference
     * @param sRef Reference to be replaced
     */
    void replace(SlotContainerReference sRef) {
        for (ISlotable i : sRef.sList) {
            i.setSlotContainerReference(this);
        }
        sList.addAll(sRef.sList);
    }

}
