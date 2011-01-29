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
package com.javahotel.client.mvc.contrpanel.model;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class ContrButton {

    private final String imageHtml;
    private final String contrName;
    private final int actionId;
    private final boolean textimage;

    public ContrButton(final String imageHtml, final String contrName,
            final int actionId) {
        this.imageHtml = imageHtml;
        this.contrName = contrName;
        this.actionId = actionId;
        textimage = false;
    }

    public ContrButton(final String imageHtml, final String contrName,
            final int actionId, boolean textimage) {
        this.imageHtml = imageHtml;
        this.contrName = contrName;
        this.actionId = actionId;
        this.textimage = textimage;
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
    public int getActionId() {
        return actionId;
    }
}
