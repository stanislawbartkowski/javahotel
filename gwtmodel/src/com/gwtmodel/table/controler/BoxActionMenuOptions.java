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
package com.gwtmodel.table.controler;

import java.util.HashMap;
import java.util.Map;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.slotmodel.CustomStringDataTypeSlot;
import com.gwtmodel.table.slotmodel.SlotType;
import com.gwtmodel.table.slotmodel.SlotTypeFactory;

/**
 * @author hotel This class gives the possibility of modifying behavior of
 *         BoxItem
 */
public class BoxActionMenuOptions {

    private static final String REMOVE_FORM_ACTION = "BoxActionMenuOptions_REMOVE_FORM_ACTION";
    private static final String ASK_BEFORE_REMOVE_ACTION = "BoxActionMenuOptions_AKS_BEFORE_REMOVE";

    public static final String REDIRECT_RESIGN = "REDIRECT_RESIGN";

    private final IDataType dType;

    private final SlotTypeFactory slTypeFactory;
    private final Map<String, SlotType> rMap = new HashMap<String, SlotType>();

    public BoxActionMenuOptions(IDataType dType) {
        assert dType != null : LogT.getT().cannotBeNull();
        this.dType = dType;
        slTypeFactory = GwtGiniInjector.getI().getSlotTypeFactory();
        setSlotType(REDIRECT_RESIGN, constructRemoveFormDialogSlotType());
    }

    public SlotType constructRemoveFormDialogSlotType() {
        return slTypeFactory.construct(new CustomStringDataTypeSlot(
                REMOVE_FORM_ACTION, dType));
    }

    public SlotType constructResignButtonSlotType() {
        return slTypeFactory.construct(new ClickButtonType(
                ClickButtonType.StandClickEnum.RESIGN));
    }

    public SlotType constructAskBeforeRemoveSlotType() {
        return slTypeFactory.construct(new CustomStringDataTypeSlot(
                ASK_BEFORE_REMOVE_ACTION, dType));
    }

    public void setSlotType(String key, SlotType sl) {
        rMap.put(key, sl);
    }

    public SlotType getSlotType(String key) {
        return rMap.get(key);
    }

}
