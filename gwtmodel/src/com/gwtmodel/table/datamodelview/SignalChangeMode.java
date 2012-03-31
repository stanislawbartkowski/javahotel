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
package com.gwtmodel.table.datamodelview;

import com.gwtmodel.table.ICustomObject;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.common.PersistTypeEnum;
import com.gwtmodel.table.slotmodel.CustomStringDataTypeSlot;
import com.gwtmodel.table.slotmodel.ISlotCustom;
import java.util.List;

/**
 * @author hotel
 * 
 */
public class SignalChangeMode implements ICustomObject {

    private final List<IVField> vLi;
    private final PersistTypeEnum persistTypeEnum;

    private static final String SIGNAL_CHANGE_MODE = "SIGNAL_CHANGE_MODE";

    public SignalChangeMode(List<IVField> vLi, PersistTypeEnum persistTypeEnum) {
        this.vLi = vLi;
        this.persistTypeEnum = persistTypeEnum;
    }

    /**
     * default visibility
     * 
     * @return the vLi
     */
    List<IVField> getvLi() {
        return vLi;
    }
    
    

    /**
     * default visibility
     * @return the persistTypeEnum
     */
    PersistTypeEnum getPersistTypeEnum() {
        return persistTypeEnum;
    }

    public static ISlotCustom constructSlot(IDataType dType) {
        return new CustomStringDataTypeSlot(SIGNAL_CHANGE_MODE, dType);
    }

}
