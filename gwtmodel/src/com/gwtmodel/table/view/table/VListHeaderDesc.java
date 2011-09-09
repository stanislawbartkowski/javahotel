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
package com.gwtmodel.table.view.table;

import com.gwtmodel.table.IGHeader;
import com.gwtmodel.table.IVField;

public class VListHeaderDesc {

    private final String headerString;
    private final IVField fie;
    private final boolean hidden;
    private final String buttonAction;
    private final IGHeader gHeader;
    private final boolean editable;

    public VListHeaderDesc(IGHeader gHeader, IVField fie) {
        this.headerString = null;
        this.fie = fie;
        this.hidden = false;
        this.buttonAction = null;
        this.gHeader = gHeader;
        this.editable = false;
    }

    /**
     * @return the editable
     */
    boolean isEditable() {
        return editable;
    }

    /**
     * @return the gCell
     */
    public IGHeader getgHeader() {
        return gHeader;
    }

    public VListHeaderDesc(String headerString, IVField fie) {
        this(headerString, fie, false, null, false);
    }

    public VListHeaderDesc(IVField fie, VListHeaderDesc v) {
        this(v.getHeaderString(), fie, v.isHidden(), v.getButtonAction(), false);
    }

    public VListHeaderDesc(String headerString, IVField fie, boolean hidden,
            String buttonAction, boolean editable) {
        this.headerString = headerString;
        this.fie = fie;
        this.hidden = hidden;
        this.buttonAction = buttonAction;
        this.gHeader = null;
        this.editable = editable;
    }

    public VListHeaderDesc(String headerString, IVField fie, boolean hidden) {
        this(headerString, fie, hidden, null, false);
    }

    public String getHeaderString() {
        return headerString;
    }

    public IVField getFie() {
        return fie;
    }

    /**
     * @return the hidden
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * @return the buttonAction
     */
    public String getButtonAction() {
        return buttonAction;
    }
}
