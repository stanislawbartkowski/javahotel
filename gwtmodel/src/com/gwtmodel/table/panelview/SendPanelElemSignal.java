/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.panelview;

import com.gwtmodel.table.ICustomObject;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.slotmodel.CustomStringDataTypeSlot;
import com.gwtmodel.table.slotmodel.CustomStringSlot;

/**
 *
 * @author perseus
 */
public class SendPanelElemSignal implements ICustomObject {

    private final String htmlId;
    private final IGWidget gwtWidget;

    SendPanelElemSignal(String htmlId, IGWidget gwtWidget) {
        this.htmlId = htmlId;
        this.gwtWidget = gwtWidget;
    }

    /**
     * @return the htmlId
     */
    public String getHtmlId() {
        return htmlId;
    }

    /**
     * @return the gwtWidget
     */
    public IGWidget getGwtWidget() {
        return gwtWidget;
    }
    private static final String SIGNAL_ID = SendPanelElemSignal.class.getName() + "PANEL_ELEM_SEND_HTMLID";

    public static CustomStringSlot constructSlotSendPanelElem(
            IDataType dType) {
        return new CustomStringDataTypeSlot(SIGNAL_ID, dType);
    }
}
