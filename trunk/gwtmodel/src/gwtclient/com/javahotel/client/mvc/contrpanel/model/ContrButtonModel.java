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
package com.javahotel.client.mvc.contrpanel.model;

import java.util.List;

import com.javahotel.client.IResLocator;
import com.javahotel.client.mvc.contrpanel.view.IControlClick;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class ContrButtonModel implements IContrPanel {

    private final IResLocator rI;
    private final List<ContrButton> cList;
    private IControlClick iClick = null;

    ContrButtonModel(final IResLocator rI, final List<ContrButton> cList) {
        this.rI = rI;
        this.cList = cList;
    }

    public List<ContrButton> getContr() {
        return cList;
    }

    public IControlClick getIClick() {
        return iClick;
    }

    public void setIClick(IControlClick iClick) {
        this.iClick = iClick;
    }
}
