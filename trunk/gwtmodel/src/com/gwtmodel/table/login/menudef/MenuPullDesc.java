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
package com.gwtmodel.table.login.menudef;

import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import java.util.List;

/**
 *
 * @author perseus
 */
public class MenuPullDesc {

    private final List<MenuPullDesc> bList;
    private final ControlButtonDesc butt;
    private final String displayName;

    public MenuPullDesc(String displayName, List<MenuPullDesc> bList) {
        this.displayName = displayName;
        this.bList = bList;
        this.butt = null;
    }

    public MenuPullDesc(ControlButtonDesc butt) {
        this.butt = butt;
        this.displayName = null;
        this.bList = null;
    }

    /**
     * @return the bList
     */
    public List<MenuPullDesc> getbList() {
        return bList;
    }

    /**
     * @return the butt
     */
    public ControlButtonDesc getButt() {
        return butt;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    public boolean isHeader() {
        return butt == null;
    }
}
