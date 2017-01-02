/*
 * Copyright 2017 stanislawbartkowski@gmail.com  
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
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.slotmodel.CustomStringDataTypeSlot;
import com.gwtmodel.table.slotmodel.ISlotCustom;

/**
 * @author hotel
 * 
 */
public class SignalSetHtmlId implements ICustomObject {

    private final static String SIGNAL_SET = "SIGNAL_SET_HTML_ID";

    private final String id;

    private final IGWidget gWidget;

    /**
     * @param id
     * @param gWidget
     */
    public SignalSetHtmlId(String id, IGWidget gWidget) {
        super();
        this.id = id;
        this.gWidget = gWidget;
    }

    /**
     * @return the id
     */
    String getId() {
        return id;
    }

    /**
     * @return the gWidget
     */
    IGWidget getgWidget() {
        return gWidget;
    }

    public static ISlotCustom constructSlot(IDataType dType) {
        return new CustomStringDataTypeSlot(dType, SIGNAL_SET);
    }

}
