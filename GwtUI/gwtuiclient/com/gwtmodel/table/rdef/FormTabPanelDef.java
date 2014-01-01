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
package com.gwtmodel.table.rdef;

/**
 *
 * @author perseus
 */
public class FormTabPanelDef {

    private final String tabId;
    private final String tabTitle;
    private final String htmlDef;

    public FormTabPanelDef(String tabId, String tabTitle, String htmlDef) {
        this.tabId = tabId;
        this.tabTitle = tabTitle;
        this.htmlDef = htmlDef;
    }

    /**
     * @return the tabId
     */
    public String getTabId() {
        return tabId;
    }

    /**
     * @return the tabTitle
     */
    public String getTabTitle() {
        return tabTitle;
    }

    /**
     * @return the htmlDef
     */
    public String getHtmlDef() {
        return htmlDef;
    }
}
