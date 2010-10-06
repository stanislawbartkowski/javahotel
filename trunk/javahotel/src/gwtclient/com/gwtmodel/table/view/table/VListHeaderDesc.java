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
package com.gwtmodel.table.view.table;

import com.gwtmodel.table.IVField;

public class VListHeaderDesc {

    private final String headerString;
    private final IVField fie;
    private final boolean hidden;
    private final ColumnDataType colType;

    public VListHeaderDesc(String headerString, IVField fie, ColumnDataType colType) {
        this.headerString = headerString;
        this.fie = fie;
        this.colType = colType;
        this.hidden = false;
    }

    public VListHeaderDesc(String headerString, IVField fie, ColumnDataType colType, boolean hidden) {
        this.headerString = headerString;
        this.fie = fie;
        this.colType = colType;
        this.hidden = hidden;
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
     * @return the colType
     */
    public ColumnDataType getColType() {
        return colType;
    }
}
