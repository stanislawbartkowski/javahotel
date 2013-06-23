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
package com.jythonui.shared;

import java.io.Serializable;

/**
 * @author hotel
 * 
 */
public class RowContent implements Serializable {

    private static final long serialVersionUID = 1L;
    private FieldValue[] row;

    public void setRowSize(int size) {
        row = new FieldValue[size];
    }

    public void addRow(FieldValue onerow) {
        FieldValue[] nRow = new FieldValue[row.length + 1];
        for (int i = 0; i < row.length; i++) {
            nRow[i] = row[i];
        }
        nRow[row.length] = onerow;
        row = nRow;
    }

    public int getLength() {
        return row.length;
    }

    public FieldValue getRow(int i) {
        return row[i];
    }

    public void setRow(int i, FieldValue onerow) {
        row[i] = onerow;
    }

}
