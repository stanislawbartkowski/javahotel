/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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
    private final String displayName;
    private final ClickButtonType actionId;
    private final boolean textimage;
    private final boolean enabled;

    public ControlButtonDesc(final String imageHtml, final String displayName,
            final ClickButtonType actionId, boolean enabled) {
        this.imageHtml = imageHtml;
        this.displayName = displayName;
        this.actionId = actionId;
        textimage = false;
        this.enabled = enabled;
    }

    public ControlButtonDesc(final String imageHtml, final String displayName,
            final ClickButtonType actionId) {
        this(imageHtml, displayName, actionId, true);
    }

    public ControlButtonDesc(final String contrName,
            final ClickButtonType actionId) {
        this(null, contrName, actionId, true);
    }

    public ControlButtonDesc(final String contrName,
            final ClickButtonType actionId, boolean enabled) {
        this(null, contrName, actionId, enabled);
    }

    public ControlButtonDesc(final String contrName, String actionId) {
        this(null, contrName, new ClickButtonType(actionId), true);
    }

    public ControlButtonDesc(final String contrName, String actionId,
            boolean enabled) {
        this(null, contrName, new ClickButtonType(actionId), enabled);
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
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @return the actionId
     */
    public ClickButtonType getActionId() {
        return actionId;
    }

    /**
     * @return the htmlElementName
     */
    public boolean isMenuTitle() {
        return actionId.isMenuTitle();
    }

    public boolean isEnabled() {
        return enabled;
    }

}
