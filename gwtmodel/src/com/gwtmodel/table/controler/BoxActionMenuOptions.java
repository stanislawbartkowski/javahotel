/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.injector.MM;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.slotmodel.CustomStringDataTypeSlot;
import com.gwtmodel.table.slotmodel.SlotType;
import com.gwtmodel.table.slotmodel.SlotTypeFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hotel This class gives the possibility of modifying behavior of
 * BoxItem
 */
public class BoxActionMenuOptions {

    private static final String REMOVE_FORM_ACTION = "BoxActionMenuOptions_REMOVE_FORM_ACTION";
    public static final String REDIRECT_RESIGN = "REDIRECT_RESIGN";
    public static final String ASK_BEFORE_RESIGN = "ASK_BEFORE_RESING";
    public static final String ASK_BEFORE_PERSIST = "ASK_BEFORE_PERSIST";
    private final IDataType dType;
    private final static SlotTypeFactory slTypeFactory;

    static {
        slTypeFactory = GwtGiniInjector.getI().getSlotTypeFactory();
    }
    private final Map<String, SlotType> rMap = new HashMap<String, SlotType>();
    private final Map<String, String> sMap = new HashMap<String, String>();

    public BoxActionMenuOptions(IDataType dType) {
        assert dType != null : LogT.getT().cannotBeNull();
        this.dType = dType;
        setSlotType(REDIRECT_RESIGN, constructRemoveFormDialogSlotType());
    }

    public static SlotType constructSRemoveFormDialogSlotType(IDataType dType) {
        return slTypeFactory.construct(new CustomStringDataTypeSlot(
                REMOVE_FORM_ACTION, dType));
    }

    public SlotType constructRemoveFormDialogSlotType() {
        return constructSRemoveFormDialogSlotType(dType);
    }

    public SlotType constructResignButtonSlotType(IDataType dType) {
        return slTypeFactory.construct(dType, new ClickButtonType(
                ClickButtonType.StandClickEnum.RESIGN));
    }

    public void setSlotType(String key, SlotType sl) {
        rMap.put(key, sl);
    }

    SlotType getSlotType(String key) {
        return rMap.get(key);
    }

    public void setAskString(String key, String s) {
        sMap.put(key, s);
    }

    public void setAskStandardResign() {
        setAskString(BoxActionMenuOptions.ASK_BEFORE_RESIGN, MM.getL().YouResignStandard());
    }

    String getAskString(String key) {
        return sMap.get(key);
    }
}
