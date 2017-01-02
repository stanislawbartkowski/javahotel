/*
 * Copyright 2017 stanislawbartkowski@gmail.com  
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
package com.jythonui.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hotel
 * 
 */
public class ListOfRows implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<RowContent> rowList = new ArrayList<RowContent>();

    private int size = -1;

    /**
     * @return the rowList
     */
    public List<RowContent> getRowList() {
        return rowList;
    }

    public void addRow(RowContent r) {
        rowList.add(r);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isEmpty() {
        return rowList.isEmpty();
    }

}
