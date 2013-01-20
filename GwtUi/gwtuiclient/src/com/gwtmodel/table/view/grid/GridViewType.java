/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.view.grid;

public class GridViewType {

    public enum GridType {
        DECIMAL, BOOLEAN;
    }

    private final GridType gType;

    private final boolean horizontal;

    private final boolean rowBeginning;

    private final boolean colHeaders;

    public GridType getgType() {
        return gType;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public boolean isRowBeginning() {
        return rowBeginning;
    }

    public boolean isColHeaders() {
        return colHeaders;
    }

    public GridViewType(GridType gType, boolean horizontal,
            boolean rowBeginning, boolean colHeaders) {
        this.gType = gType;
        this.horizontal = horizontal;
        this.rowBeginning = rowBeginning;
        this.colHeaders = colHeaders;
    }

}
