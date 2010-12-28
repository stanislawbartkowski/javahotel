/*
 * Copyright 2009 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.stackmenu.model;

import com.javahotel.client.panelcommand.IPanelCommand;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class StackButtonElem {

    private final String bName;
    private final IPanelCommand bClick;
    private final String iName;

    public StackButtonElem(String bName, IPanelCommand bCLick) {
        this.bName = bName;
        this.bClick = bCLick;
        this.iName = null;
    }

    /**
     * @return the bName
     */
    public String getBName() {
        return bName;
    }

    /**
     * @return the bClick
     */
    public IPanelCommand getBClick() {
        return bClick;
    }

    /**
     * @return the iName
     */
    public String getIName() {
        return iName;
    }
}
