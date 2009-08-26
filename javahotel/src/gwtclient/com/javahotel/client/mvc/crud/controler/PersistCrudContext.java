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
package com.javahotel.client.mvc.crud.controler;

import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class PersistCrudContext {

    private boolean stayDialog;
    private Widget wDialog;

    PersistCrudContext() {
        stayDialog = false;
        wDialog = null;
    }

    /**
     * @return the stayDialog
     */
    public boolean isStayDialog() {
        return stayDialog;
    }

    /**
     * @param stayDialog the stayDialog to set
     */
    public void setStayDialog(boolean stayDialog) {
        this.stayDialog = stayDialog;
    }

    /**
     * @return the wDialog
     */
    public Widget getWDialog() {
        return wDialog;
    }

    /**
     * @param wDialog the wDialog to set
     */
    public void setWDialog(Widget wDialog) {
        this.wDialog = wDialog;
    }
}
