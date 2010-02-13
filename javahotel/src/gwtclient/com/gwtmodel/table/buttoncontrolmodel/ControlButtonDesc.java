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
package com.gwtmodel.table.buttoncontrolmodel;

import com.gwtmodel.table.slotmodel.ClickButtonType;

public class ControlButtonDesc {

    private final String imageHtml;
    private final String contrName;
    private final ClickButtonType actionId;
    private final boolean textimage;

    public ControlButtonDesc(final String imageHtml, final String contrName,
            final ClickButtonType actionId) {
        this.imageHtml = imageHtml;
        this.contrName = contrName;
        this.actionId = actionId;
        textimage = false;
    }

    public boolean isTextimage() {
        return textimage;
    }

    /**
     * @return the imageHtml
     */
    public String getImageHtml() {
        return imageHtml;
    }

    /**
     * @return the contrName
     */
    public String getContrName() {
        return contrName;
    }

    /**
     * @return the actionId
     */
    public ClickButtonType getActionId() {
        return actionId;
    }
}
