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
package com.javahotel.client.mvc.table.model;

import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.IField;
import java.util.ArrayList;

/**
 *
 * @author hotel
 */
public interface ITableModel {

    int rowNum();

    int colNum();

    ArrayList<ColTitle> colList();

    String getField(int row, IField f);

    IField getCol(int col);

    String getHeader();

    AbstractTo getRow(int row);

    void setList(ArrayList<? extends AbstractTo> tList);

    ArrayList<? extends AbstractTo> getList();

    boolean isTableDefined();
}
