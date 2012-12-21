/*
 *  Copyright 2013 stanislawbartkowski@gmail.com
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
package com.gwtmodel.table.editc;

import com.gwtmodel.table.common.PersistTypeEnum;
import com.gwtmodel.table.slotmodel.ISlotable;

/**
 * 
 * @author hotel
 */
public interface IEditChooseRecordContainer extends ISlotable {
    
    /**
     * Set values for check box
     * @param newc Value for 'newc' checkbox
     * @param changec Value for 'changec' checkbox
     */
    void SetNewChange(boolean newc, boolean changec);

    /**
     * Getter for 'newc' checkbox
     * @return boolean value
     */
    boolean getNewCheck();

    /**
     * Getter for 'changec' checkbox
     * @return boolean value
     */
    boolean getChangeCheck();

    void ModifForm();

    void ChangeViewForm(PersistTypeEnum e);
}
