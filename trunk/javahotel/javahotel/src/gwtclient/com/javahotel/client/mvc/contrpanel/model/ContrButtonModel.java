/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.mvc.contrpanel.model;

import com.javahotel.client.mvc.contrpanel.view.IControlClick;
import com.javahotel.client.IResLocator;
import java.util.ArrayList;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class ContrButtonModel implements IContrPanel {

    private final IResLocator rI;
    private final ArrayList<ContrButton> cList;
    private IControlClick iClick = null;

    ContrButtonModel(final IResLocator rI, final ArrayList<ContrButton> cList) {
        this.rI = rI;
        this.cList = cList;
    }

    public ArrayList<ContrButton> getContr() {
        return cList;
    }

    public IControlClick getIClick() {
        return iClick;
    }

    public void setIClick(IControlClick iClick) {
        this.iClick = iClick;
    }
}
